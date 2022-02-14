package org.firstinspires.ftc.teamcode.Robot;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.*;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import static java.util.concurrent.TimeUnit.*;

import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior;

public class Wheels {
	private static final String[] HW_MOTOR_NAMES = {
			"lf",
			"lr",
			"rf",
			"rr"
	};

	private final double encoderTicksPerSecond;
	private final List<DcMotorEx> engines;

	private final BNO055IMU orientation;
	private final Telemetry telemetry;

	private final ScheduledExecutorService scheduler;

	public int engine1;
	public int engine2;
	public int engine3;
	public int engine4;

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

		for (String name : HW_MOTOR_NAMES) {
			engines.add(getEngine(map, name));
		}

		// FIXME: right-left movement of the robot
		/**
		 * this works
		 *
		 * engines.get(0).setDirection(Direction.REVERSE);
		 * engines.get(1).setDirection(Direction.REVERSE);
		 * // engines.get(2).setDirection(Direction.REVERSE);
		 * engines.get(3).setDirection(Direction.REVERSE); // rr
		 *
		 * */

		// the good combo
		// engines.get(0).setDirection(Direction.REVERSE);
		// engines.get(1).setDirection(Direction.REVERSE);
		// engines.get(2).setDirection(Direction.REVERSE); // non comment
 		// engines.get(3).setDirection(Direction.REVERSE); // rr

		// engines.get(1).setDirection(Direction.REVERSE);
		engines.get(0).setDirection(Direction.FORWARD);
		engines.get(1).setDirection(Direction.FORWARD);
		engines.get(2).setDirection(Direction.FORWARD);
		// engines.get(3).setDirection(Direction.REVERSE);

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

	public void forEachEngine(EngineFunction fn) {
		int index = 0;
		for (DcMotorEx engine : engines) {
			fn.apply(engine, index);
			index++;
		}
	}

	// TODO: implement a feature to track robot's movement disntance
	public int returnMotorsValues(){
		AtomicInteger sum = new AtomicInteger();
		sum.set(0);
		forEachEngine((engine, _i) -> telemetry.addData(String.format("%s", _i), modulo(engine.getCurrentPosition())));
		forEachEngine((engine, _i) -> sum.addAndGet(modulo(engine.getCurrentPosition())));
		telemetry.addData("meanValue", sum.get() / 4);

		return sum.get() / 4;
	}

	public int modulo(int number){
		if(number > 0){ number = number; }
		else{ number = -number; }
		return number;
	}

	public void useEncoders(boolean shouldUse) {
		RunMode mode = shouldUse ? RunMode.RUN_USING_ENCODER : RunMode.RUN_WITHOUT_ENCODER;

		forEachEngine((engine, _i) -> engine.setMode(mode));
	}

	public void useBrakes(boolean shouldUse) {
		ZeroPowerBehavior behavior = shouldUse ? ZeroPowerBehavior.BRAKE : ZeroPowerBehavior.FLOAT;

		forEachEngine((engine, _i) -> engine.setZeroPowerBehavior(behavior));
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

		double highest = 0;

		for (double d : input) {
			double abs = Math.abs(d);
			if (abs > highest) {
				highest = abs;
			}
		}

		highest = Math.max(highest, 1);

		for (int i = 0; i < input.length; i++) {
			input[i] /= highest;
		}

		forEachEngine((engine, i) -> setPower(engine, input[i]));
	}


	public void forward(){
		move(0.0, 0.0, 1.0);
		engine1 = modulo(engines.get(0).getCurrentPosition());
		engine2 = modulo(engines.get(1).getCurrentPosition());
		engine3 = modulo(engines.get(2).getCurrentPosition());
		engine4 = modulo(engines.get(3).getCurrentPosition());

		if(engine1 > 2300 && engine2 > 2300 && engine3 > 2300 && engine4 > 2300){
			stopMotors();
		}

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

	private ScheduledFuture<?> lastRotation = null;

	private boolean isRotating() {
		return !Utils.isDone(lastRotation);
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

		if (isRotating() && !lastRotation.cancel(true)) {
			return null;
		}

		double initialPower = -Math.signum(degrees);
		boolean isPositiveDirection = initialPower < 0;

		lastRotation = Utils.poll(
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

		return lastRotation;
	}

	private void stopMotors() {
		forEachEngine((engine, _i) -> setPower(engine, 0.0));
	}

	/**
	 * Stop any movement or rotation of the robot.
	 */
	public void stop() {
		if (isRotating() && lastRotation.cancel(true)) {
			return;
		}

		stopMotors();
	}

	private static double normalize(double val) {
		return Utils.clamp(val, -1, 1);
	}

	private static double normalizeRotationPower(double initialPower, double degreesLeft) {
		if (degreesLeft > 60) {
			return initialPower;
		}

		return Utils.interpolate(initialPower, Math.signum(initialPower) * 0.05, (60 - degreesLeft) / 60, 1.1);
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

	@FunctionalInterface
	private interface EngineFunction {
		void apply(DcMotorEx engine, int index);
	}
}