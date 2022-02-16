package org.firstinspires.ftc.teamcode.Robot;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.*;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import java.util.concurrent.*;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import static java.util.concurrent.TimeUnit.*;

import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior;

public class Wheels {
	private static final List<String> HW_MOTOR_NAMES = Arrays.asList("lf", "lr", "rf", "rr");

	private final double encoderTicksPerSecond;
	private final List<DcMotorEx> engines;

	private final BNO055IMU orientation;
	private final Telemetry telemetry;

	private final ScheduledExecutorService scheduler;

	private static DcMotorEx getEngine(HardwareMap map, String name) {
		DcMotorEx motor = map.get(DcMotorEx.class, name);
		motor.setMode(RunMode.STOP_AND_RESET_ENCODER);
		return motor;
	}

	Wheels(@NonNull final Parameters params) {
		this.telemetry = Objects.requireNonNull(params.telemetry, "Telemetry object was not set");
		this.orientation = Objects.requireNonNull(params.orientationSensor, "Orientation sensor was not set");
		this.scheduler = Objects.requireNonNull(params.scheduler, "Scheduler was not set");

		HardwareMap map = Objects.requireNonNull(params.hardwareMap, "Hardware map was not passed");
		ArrayList<DcMotorEx> engines = new ArrayList<>();

		HW_MOTOR_NAMES.forEach(name -> engines.add(getEngine(map, name)));

		engines.get(0).setDirection(Direction.FORWARD);
		engines.get(1).setDirection(Direction.FORWARD);
		engines.get(2).setDirection(Direction.FORWARD);

		this.engines = engines;

		if (params.encoderResolution != 0 && params.rpm != 0) {
			this.encoderTicksPerSecond = (params.rpm / 60) * params.encoderResolution;
			useEncoders(true);
		} else {
			this.encoderTicksPerSecond = 0;
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

		for (int i = 0; i < input.length; i++) {
			input[i] /= highest;
		}

		IntStream.range(0, input.length).forEach(i -> setPower(engines.get(i), input[i]));
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
		double angle = o.thirdAngle > 0 ? o.thirdAngle : 360 + o.thirdAngle;
		return direction ? angle : 360 - angle;
	}

	private ScheduledFuture<?> lastMovement = null;

	private boolean isMoving() {
		return !Utils.isDone(lastMovement);
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
	 *
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

	public ScheduledFuture<?> moveFor(double meters, double power) {
		if (Utils.inVicinity(meters, 0, 1e-4)) {
			return null;
		}

		if (isMoving() && !lastMovement.cancel(true)) {
			return null;
		}

		orientation.startAccelerationIntegration(new Position(), new Velocity(), 1);

		double movement = meters * 1000;
		Position initialPosition = orientation.getPosition();
		double initialX = initialPosition.unit.toMm(initialPosition.x);
		double finalX = initialX + movement;

		lastMovement = Utils.poll(
				scheduler,
				() -> {
					Position currentPosition = orientation.getPosition();
					double currentX = currentPosition.unit.toMm(currentPosition.x);
					double movementLeft = Math.abs(finalX - currentX);

					if ((finalX < 0 && currentX <= finalX) || (finalX >= 0 && currentX >= finalX)) {
						return true;
					}

					telemetry.clearAll();
					telemetry
							.addData("Current position", currentPosition).setRetained(true)
							.addData("Current X", currentX).setRetained(true)
							.addData("Final X", finalX).setRetained(true)
							.addData("Movement left", movementLeft).setRetained(true)
							.addData("Power", power).setRetained(true);
					telemetry.update();

					move(0, 0, power);
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

	private static double normalizeRotationPower(double initialPower, double degrees) {
		final double THRESHOLD = 60;
		if (degrees > THRESHOLD) {
			return initialPower;
		}

		return Utils.interpolate(initialPower, Math.signum(initialPower) * 0.05, (THRESHOLD - degrees) / THRESHOLD, 1.1);
	}

	static class Parameters {
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