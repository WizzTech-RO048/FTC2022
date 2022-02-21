package org.firstinspires.ftc.teamcode.Robot;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.*;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.*;

import java.util.*;

import java.util.concurrent.*;
import java.util.function.*;
import java.util.stream.IntStream;

import static java.util.concurrent.TimeUnit.*;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior;

public class Wheels {
    private static final List<String> HW_MOTOR_NAMES = Arrays.asList("lf", "lr", "rf", "rr");

    private final double encoderTicksPerSecond;
    private final List<DcMotorEx> engines = new ArrayList<>();

    private final BNO055IMU orientation;
    private final Telemetry telemetry;

    private final ScheduledExecutorService scheduler;

    private static DcMotorEx getEngine(HardwareMap map, String name) {
        DcMotorEx motor = map.get(DcMotorEx.class, name);
        motor.setMode(RunMode.STOP_AND_RESET_ENCODER);
        return motor;
    }

    Wheels(@NonNull final Parameters params) {
        telemetry = Objects.requireNonNull(params.telemetry, "Telemetry object was not set");
        orientation = Objects.requireNonNull(params.orientationSensor, "Orientation sensor was not set");
        scheduler = Objects.requireNonNull(params.scheduler, "Scheduler was not set");

        HardwareMap map = Objects.requireNonNull(params.hardwareMap, "Hardware map was not passed");

        HW_MOTOR_NAMES.forEach(name -> engines.add(getEngine(map, name)));

        if (params.encoderResolution != 0 && params.rpm != 0) {
            encoderTicksPerSecond = (params.rpm / 60) * params.encoderResolution;
            useEncoders(true);
        } else {
            encoderTicksPerSecond = 0;
            useEncoders(false);
        }

        useBrakes(true);
    }

    public void useEncoders(boolean shouldUse) {
        RunMode mode = shouldUse ? RunMode.RUN_USING_ENCODER : RunMode.RUN_WITHOUT_ENCODER;

        engines.forEach(engine -> engine.setMode(mode));
    }

    public void useBrakes(boolean shouldUse) {
        ZeroPowerBehavior behavior = shouldUse ? ZeroPowerBehavior.BRAKE : ZeroPowerBehavior.FLOAT;

        engines.forEach(engine -> engine.setZeroPowerBehavior(behavior));
    }

    /**
     * @param x The throttle on the horizontal axis of the robot.
     * @param y The throttle on the vertical axis of the robot.
     * @param r The rotation power around the robot's axis.
     */
    public void move(double x, double y, double r) {
        x = normalize(x);
        y = normalize(y);
        r = normalize(r);

        double[] input = {
                y + x + r, // left front
                y - x + r, // left rear
                y - x - r, // right front
                y + x - r  // right rear
        };

        double highest = Arrays.stream(input).map(Math::abs).reduce(1, Math::max);

        IntStream.range(0, input.length).forEach(i -> setPower(engines.get(i), input[i] / highest));
    }


    private void setPower(DcMotorEx engine, double power) {
        if (engine.getMode() == RunMode.RUN_USING_ENCODER) {
            engine.setVelocity(Math.round(power * encoderTicksPerSecond));
        } else {
            engine.setPower(power);
        }
    }

    private double getOrientation(boolean direction) {
        Orientation o = orientation.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);

        double angle = o.secondAngle > 0 ? o.secondAngle : 360 + o.secondAngle;
        return direction ? angle : 360 - angle;
    }

    private ScheduledFuture<?> lastMovement = null;

    private boolean isMoving() {
        return !Utils.isDone(lastMovement);
    }

    public void moveForTime(double x, double y, int millis) {
        try {
            move(x, y, 0);
            Thread.sleep(millis);
        } catch (Exception e) {
            ;
        }
    }

    public void rotateForTime(int millis, double power) {
        try {
            move(0, normalizeRotationPower(power, 10), 0);
            Thread.sleep(millis);
        } catch (Exception e) {
            ;
        }
    }

    /**
     * Because of time related issues, this function is blocking. Am very sorry
     *
     * XOXO,
     * Mihai
     *
     * @param degrees
     * @param power
     */
    public void rotateFor2(double degrees, double power) {
        if (Utils.inVicinity(degrees, 0, 1e-4)) {
            return ;
        }

        double orientation = getOrientation(true);
        double finalOrientation = (orientation + degrees) % 360;
        double rotation = Math.abs(degrees);

        move(0, normalizeRotationPower(power, rotation), 0);

        while (Math.abs(getOrientation(true) - finalOrientation) > 5) {
            telemetry.addData("Orientation: ", getOrientation(true));
            telemetry.update();
        }

        stop();
    }

    public void rotateToOrientation(double orientation, double power) {
        rotateFor2(orientation - getOrientation(true), power);
    }

    /**
     * Rotates the robot for the given number of degrees.
     *
     * For positive values, the robot rotates counter-clockwise.
     * Provide negative values for clockwise rotation.
     *
     * Values are in degrees, not radians. The robot can rotate
     * multiple times around itself: for a value of 720, the robot
     * will rotate twice around its axis.
     *
     * @param degrees The number of degrees to rotate the robot.
     * @return A future that completes when the rotation is complete.
     * Canceling this future will cancel the rotation, with the robot
     * stopping any movement immediately.
     */
    public ScheduledFuture<?> rotateFor(double degrees) {
        if (Utils.inVicinity(degrees, 0, 1e-4)) {
            return null;
        }

        if (isMoving() && !lastMovement.cancel(true)) {
            return null;
        }

        // Positive degrees - clockwise movement - negative power
        // Negative degrees - counter-clockwise movement - positive power
        double initialPower = -Math.signum(degrees);
        boolean isPositiveDirection = initialPower < 0;

        lastMovement = Utils.poll(
                scheduler,
                new Supplier<Boolean>() {
                    private double rotation = Math.abs(degrees);
                    private double prevOrientation = getOrientation(isPositiveDirection);

                    @Override
                    public Boolean get() {
                        double currentOrientation = getOrientation(isPositiveDirection);
                        double delta = currentOrientation - prevOrientation;
                        prevOrientation = currentOrientation;
                        rotation -= delta >= 0 ? delta : 360 + delta;

                        if (rotation < 0.5) {
                            return true;
                        }

                        move(0, normalizeRotationPower(initialPower, rotation), 0);
                        return false;
                    }
                },
                this::stopMotors,
                5,
                MILLISECONDS
        );

        return lastMovement;
    }

    @FunctionalInterface
    private interface PositionGetter extends Function<Position, Double> {
    }

    @FunctionalInterface
    private interface PositionChecker extends BiFunction<Double, Double, Boolean> {
    }

    @FunctionalInterface
    private interface MovementPowerGetter extends Function<Double, MovementPower> {
    }


    public enum MoveDirection {
        FORWARD(p -> p.unit.toMm(p.x), (c, e) -> c >= e, power -> new MovementPower(0, 0, power)),
        BACKWARD(p -> p.unit.toMm(p.x), (c, e) -> c <= e, power -> new MovementPower(0, 0, -power)),
        LEFT(p -> p.unit.toMm(p.y), (c, e) -> c <= e, power -> new MovementPower(power, 0, 0)),
        RIGHT(p -> p.unit.toMm(p.y), (c, e) -> c <= e, power -> new MovementPower(-power, 0, 0));

        private final PositionGetter positionGetter;
        private final PositionChecker positionChecker;
        private final MovementPowerGetter movementPowerGetter;
        private final boolean isPositive;

        MoveDirection(PositionGetter getter, PositionChecker checker, MovementPowerGetter powerGetter) {
            positionGetter = getter;
            positionChecker = checker;
            movementPowerGetter = powerGetter;
            isPositive = positionChecker.apply(2.0, 1.0);
        }

        public boolean hasReachedEnd(double current, double end) {
            return positionChecker.apply(current, end);
        }

        public double getCoordinate(Position position) {
            return positionGetter.apply(position);
        }

        public double getMovement(double meters) {
            if (meters < 1e-3) {
                throw new IllegalArgumentException("movement distance must be greater than 1mm");
            }
            double movement = meters * 1000; // convert to mm
            if (isPositive) {
                return movement;
            }
            return -movement; // the robot moves backward
        }

        public MovementPower getPower(double initialPower, double current, double end) {
            if (initialPower < 1e-2) {
                throw new IllegalArgumentException("movement power must be greater or equal to 1%");
            }

            return movementPowerGetter.apply(initialPower);
        }


        @Override
        public String toString() {
            switch (this) {
                case FORWARD:
                    return "Forward";
                case BACKWARD:
                    return "Backward";
                case LEFT:
                    return "Left";
                case RIGHT:
                    return "Right";
                default:
                    return "None";
            }
        }
    }

    private static class MovementPower {
        public final double x, y, r;

        MovementPower(double x, double y, double r) {
            this.x = x;
            this.y = y;
            this.r = r;
        }

        @Override
        public String toString() {
            return "MovementPower{" +
                    "x=" + x +
                    ", y=" + y +
                    ", r=" + r +
                    '}';
        }
    }

    public ScheduledFuture<?> moveFor(double meters, double inputPower, @NonNull MoveDirection direction) {
        if (isMoving() && !lastMovement.cancel(true)) {
            return null;
        }

        orientation.startAccelerationIntegration(new Position(), new Velocity(), 1);

        double movement = direction.getMovement(meters);

        lastMovement = Utils.poll(
                scheduler,
                () -> {
                    double current = direction.getCoordinate(orientation.getPosition());
                    if (direction.hasReachedEnd(current, movement)) {
                        return true;
                    }

                    MovementPower power = direction.getPower(inputPower, current, movement);

                    move(power.x, power.y, power.r);
                    return false;
                },
                () -> {
                    stopMotors();
                    orientation.stopAccelerationIntegration();
                },
                1,
                MILLISECONDS
        );

        return lastMovement;
    }

    private void stopMotors() {
        engines.forEach(engine -> setPower(engine, 0.0));
    }

    /**
     * Stop any movement or rotation of the robot.
     */
    public void stop() {
        if (isMoving() && lastMovement.cancel(true)) {
            return;
        }

        stopMotors();
    }

    private static double normalize(double val) {
        return Utils.clamp(val, -1, 1);
    }

    public static double normalizeRotationPower(double initialPower, double degrees) {
        final double THRESHOLD = 60;
        if (degrees > THRESHOLD) {
            return initialPower;
        }

        return Utils.interpolate(initialPower, Math.signum(initialPower) * 0.05, (THRESHOLD - degrees) / THRESHOLD, 1.1);
    }

    public static class Parameters {
        /**
         * The robot's hardware configuration.
         */
        public HardwareMap hardwareMap = null;
        /**
         * The telemetry instance to send data to.
         */
        public Telemetry telemetry = null;
        /**
         * The orientation sensor instance used when rotating.
         */
        public BNO055IMU orientationSensor = null;
        /**
         * The executor service to be used for polling the orientation sensor when rotating.
         */
        public ScheduledExecutorService scheduler = null;

        /**
         * The PPR of the engine's encoder.
         */
        public double encoderResolution = 0;
        /**
         * The rotations per minute of the engine.
         */
        public double rpm = 0;
    }
}