/*
 * coding to do list
 * - write code for the intake ramp
 * - implement the moving
 */

package org.firstinspires.ftc.teamcode.Robot;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.*;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.*;

import java.util.concurrent.*;
import java.util.stream.*;

public class Robot {
    private final DcMotor intakeMotor;

    private final DcMotor leftFront;
    private final DcMotor leftRear;
    private final DcMotor rightFront;
    private final DcMotor rightRear;

    private final Servo carouselServo;

    public final Arm arm;
    public final Wheels wheels;
    public final Sensors sensors;

    private final Telemetry telemetry;

    private BNO055IMU imu;
    private double headingOffset = 0.0;
    private Orientation angles;
    private Acceleration gravity;

    private boolean turbo = false;

    // private static final int SCISSORS_ARM_FINAL_POS = 12525;
    private static final int SCISSORS_ARM_FINAL_POS = 4300;

    public Robot(final HardwareMap hardwareMap, final Telemetry t, ScheduledExecutorService scheduler) {
        telemetry = t;

        leftFront = hardwareMap.dcMotor.get("lf");
        leftRear = hardwareMap.dcMotor.get("rf");
        rightFront = hardwareMap.dcMotor.get("lr");
        rightRear = hardwareMap.dcMotor.get("rr");
        intakeMotor = hardwareMap.dcMotor.get("intakeMotor");

        carouselServo = hardwareMap.servo.get("duckWheel");

        leftFront.setDirection(DcMotorSimple.Direction.FORWARD);
        leftRear.setDirection(DcMotorSimple.Direction.FORWARD);
        rightFront.setDirection(DcMotorSimple.Direction.FORWARD);
        rightRear.setDirection(DcMotorSimple.Direction.FORWARD);
        intakeMotor.setDirection(DcMotorSimple.Direction.FORWARD);

        BNO055IMU orientation = hardwareMap.get(BNO055IMU.class, "imu");
        orientation.initialize(new BNO055IMU.Parameters());
        orientation.startAccelerationIntegration(null, null, 1);

        Wheels.Parameters wheelsParams = new Wheels.Parameters();
        wheelsParams.hardwareMap = hardwareMap;
        wheelsParams.telemetry = telemetry;
        wheelsParams.orientationSensor = orientation;
        wheelsParams.scheduler = scheduler;
        wheelsParams.rpm = 435;
        wheelsParams.encoderResolution = 384.5;
        wheels = new Wheels(wheelsParams);

        Arm.Parameters armParameters = new Arm.Parameters();
        armParameters.arm = hardwareMap.get(DcMotorEx.class, "armMotor");
        armParameters.throwServo = hardwareMap.servo.get("throwServo");
        armParameters.telemetry = telemetry;
        armParameters.scheduler = scheduler;
        armParameters.armRaisedPosition = SCISSORS_ARM_FINAL_POS;
        arm = new Arm(armParameters);

        Sensors.Parameters sensorsParameters = new Sensors.Parameters();
        sensorsParameters.colorSensor = hardwareMap.get(ColorSensor.class, "colorSensor");
        sensorsParameters.scheduler = scheduler;
        sensorsParameters.telemetry = telemetry;
        sensors = new Sensors(sensorsParameters);
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

    // motor parameters
    public boolean isTurbo() {
        return turbo;
    }

    public void setTurbo(boolean value) {
        turbo = value;
    }

    public void stopMotors() {
        setMotors(0, 0, 0, 0);
    }

    // ------------------------
    // - Controlling the intake
    // ------------------------
    public void intake(double percentage) {
        intakeMotor.setPower(percentage);
    }

    public void stopIntake() {
        intakeMotor.setPower(0.0);
    }

    // ------------------------
    // - Carousel servo
    // ------------------------
    public void duckServoOn() {
        carouselServo.setPosition(-1.0);
    }

    public void duckServoOff() {
        carouselServo.setPosition(0.5);
    }
}
