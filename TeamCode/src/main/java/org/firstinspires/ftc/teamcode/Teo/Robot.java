/*
* coding to do list
* - write code for the intake ramp
* - implement the moving
*/

package org.firstinspires.ftc.teamcode.Teo;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;

import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.*;

public class Robot {
	private final DcMotor intakeMotor;

	public final Wheels wheels;

	private final DcMotor leftFront;
	private final DcMotor leftRear;
	private final DcMotor rightFront;
	private final DcMotor rightRear;

	private final Servo duckWheelServo;

	private final Telemetry telemetry;

	private BNO055IMU imu;
	private double headingOffset = 0.0;
	private Orientation angles;
	private Acceleration gravity;

	private boolean turbo = false;



	public Robot(final HardwareMap hardwareMap, final Telemetry t, ScheduledExecutorService scheduler){
		telemetry = t;

		leftFront = hardwareMap.dcMotor.get("lf");
        leftRear = hardwareMap.dcMotor.get("rf");
		rightFront = hardwareMap.dcMotor.get("lr");
		rightRear = hardwareMap.dcMotor.get("rr");
	    intakeMotor = hardwareMap.dcMotor.get("intakeMotor");

		duckWheelServo = hardwareMap.servo.get("duckWheel");

		leftFront.setDirection(DcMotorSimple.Direction.FORWARD);
	    leftRear.setDirection(DcMotorSimple.Direction.FORWARD);
	    rightFront.setDirection(DcMotorSimple.Direction.FORWARD);
	    rightRear.setDirection(DcMotorSimple.Direction.FORWARD);
	    intakeMotor.setDirection(DcMotorSimple.Direction.FORWARD);

		BNO055IMU orientation = hardwareMap.get(BNO055IMU.class, "imu");
		orientation.initialize(new BNO055IMU.Parameters());

		Wheels.Parameters wheelsParams = new Wheels.Parameters();
		wheelsParams.hardwareMap = hardwareMap;
		wheelsParams.telemetry = telemetry;
		wheelsParams.orientationSensor = orientation;
		wheelsParams.scheduler = scheduler;
		wheelsParams.rpm = 435;
		wheelsParams.encoderResolution = 384.5;
		wheels = new Wheels(wheelsParams);


	}

	private void setMotorMode(DcMotor.RunMode mode, DcMotor... motors) {
		leftFront.setMode(mode);
	    leftRear.setMode(mode);
	    rightFront.setMode(mode);
	    rightRear.setMode(mode);
	    intakeMotor.setMode(mode);
	}

	public void runUsingEncoders() { setMotorMode(DcMotor.RunMode.RUN_USING_ENCODER); }
	public void runWithoutEncoders() { setMotorMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER); }

	public void intake(){ intakeMotor.setPower(1.0); }
	public void stopIntake(){ intakeMotor.setPower(0.0); }

	public void duckServoOn(){ duckWheelServo.setPosition(1.0); }
	public void duckServoOff(){ duckWheelServo.setPosition(0.5); }

	private double getAngularOrientation() { return imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.RADIANS).firstAngle; }
	public void resetHeading() { headingOffset = getAngularOrientation(); }

	public void rotate(double rotation) { setMotors(-rotation, -rotation, rotation, rotation); }

	public void move(double x, double y) {
		y = -y;

		final double orientation = getAngularOrientation();
		final double heading = (orientation - headingOffset) % (2.0 * Math.PI);
		headingOffset = orientation;

		final double direction = Math.atan2(x, y) - heading;
		final double speed = Math.min(1.0, Math.sqrt(x * x + y * y));
		final double factorSin = speed * Math.sin(direction + Math.PI / 4.0);
		final double factorCos = speed * Math.cos(direction + Math.PI / 4.0);

		telemetry.addData("Movement", "X: %f\nY: %f\nOrientation: %f\n", x, y, orientation);

		setMotors(factorSin, factorCos, factorCos, factorSin);
	}

	private void setMotors(double lf, double lr, double rf, double rr) {
		final double scale = Stream.of(1.0, lf, lr, rf, rr).mapToDouble(Math::abs).max().getAsDouble();

		leftFront.setPower(getPower(lf, scale, "front left"));
		leftRear.setPower(getPower(lr, scale, "rear left"));
		rightFront.setPower(getPower(rf, scale, "front right"));
		rightRear.setPower(getPower(rr, scale, "rear right"));
	}

	private double getPower(double rf, double scale, String engine) {
		telemetry.addData(String.format("Power in %s", engine), "initial: %f; turbo: %b; scale: %f", rf, turbo, scale);
		return rf / (isTurbo() ? 1.0 : 2.0) / scale;
	}

	public boolean isTurbo() {
		return turbo;
	}

	public void setTurbo(boolean value) {
		turbo = value;
	}

	public void stop() {
		setMotors(0, 0, 0, 0);
	}

}
