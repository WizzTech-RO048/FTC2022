package org.firstinspires.ftc.teamcode.Robot;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.*;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.concurrent.*;

public class Robot {
    private final DcMotor intakeMotor;

    private final Servo carouselServo;

    public final Arm arm;
    public final Wheels wheels;
    public final Camera camera;

    private final Telemetry telemetry;

    // private static final int SCISSORS_ARM_FINAL_POS = 12525;
    private static final int SCISSORS_ARM_FINAL_POS = 4300;

    public Robot(final HardwareMap hardwareMap, final Telemetry t, ScheduledExecutorService scheduler) {
        telemetry = t;

        carouselServo = hardwareMap.servo.get("duckWheel");

        intakeMotor = hardwareMap.dcMotor.get("intakeMotor");
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

        Arm.Parameters armParameters = new Arm.Parameters();
        armParameters.arm = hardwareMap.get(DcMotorEx.class, "armMotor");
        armParameters.throwServo = hardwareMap.servo.get("throwServo");
        armParameters.telemetry = telemetry;
        armParameters.scheduler = scheduler;
        armParameters.armRaisedPosition = SCISSORS_ARM_FINAL_POS;
        arm = new Arm(armParameters);

        camera = new Camera(hardwareMap, telemetry);
    }

    public void intake(double percentage) {
        intakeMotor.setPower(percentage);
    }

    public void stopIntake() {
        intakeMotor.setPower(0.0);
    }

    public void duckServoOn() {
        carouselServo.setPosition(-1.0);
    }

    public void duckServoOff() {
        carouselServo.setPosition(0.5);
    }

    public void stop() {
        wheels.stop();
        camera.stop();
        arm.stop();
        duckServoOff();
        stopIntake();
    }

    public Telemetry getTelemetry() {
        return telemetry;
    }
}
