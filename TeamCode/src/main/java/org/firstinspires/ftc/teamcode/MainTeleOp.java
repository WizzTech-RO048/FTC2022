
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Robot.*;
import org.firstinspires.ftc.teamcode.ComputerVision.*;

import java.util.concurrent.*;

@TeleOp(name = "MainTeleOp")
public class MainTeleOp extends OpMode {
	private Robot robot;
	private Controller controller1;
	private Controller controller2;

	private boolean isArmRaised = false;
	private ScheduledFuture<?> lastRotation = null, lastArmRaised = null, lastThrow = null;

	private double initialThrowServerPos = 0.1;
	private int currentArmPosition;

	TensorFlowObjectDetectionWebcam objectDetector;

	@Override
	public void init() {
		robot = new Robot(hardwareMap, telemetry, Executors.newScheduledThreadPool(1));

		// robot.runUsingEncoders();

		controller1 = new Controller(gamepad1);
		controller2 = new Controller(gamepad2);

		robot.arm.isArmRaised = false;
	}

	// ------------------------
	// - Stop button 
	// ------------------------
	@Override
	public void stop() {
		robot.wheels.stop();
		robot.arm.stopArm();
		robot.stopMotors();
		if (lastArmRaised != null) {
			lastArmRaised.cancel(true);
		}
		isArmRaised = robot.arm.isArmRaised;
		if (lastThrow != null) {
			lastThrow.cancel(true);
		}

		robot.duckServoOff();
		robot.stopIntake();
	}

	@Override
	public void loop() {
		controller1.update();
		controller2.update();

		// ------------------------
		// - Robot movement
		// ------------------------
		double y = -gamepad1.left_stick_y / 2.0;
		double x = gamepad1.right_stick_x / 2.0;
		double r = gamepad1.left_stick_x / 2.0;
		double h = controller2.leftStickY;

		double rightTrigger = controller1.rightTrigger;
		double leftTrigger = controller1.leftTrigger;

		if(Utils.isDone(lastRotation)) {
			if(isZero(x) && isZero(y) && isZero(r)) {
				robot.wheels.stop();
			}else {
				robot.wheels.move(x, y, r);
			}
		}

		// ------------------------
		// - Controlling the arm
		// ------------------------

		// TODO: find a good way to control the arm
		currentArmPosition = robot.arm.currentArmPosition();

		// position 0.0
		if(controller1.AOnce()){
			robot.arm.isArmRaised = !robot.arm.isArmRaised;
			lastArmRaised = robot.arm.moveArm(0.0);
		}

		// position 0.5
		if(controller1.BOnce()){
			robot.arm.isArmRaised = !robot.arm.isArmRaised;
			lastArmRaised = robot.arm.moveArm(0.5);
		}

		// position 1.0
		if(controller1.YOnce()){
			robot.arm.isArmRaised = !robot.arm.isArmRaised;
			lastArmRaised = robot.arm.moveArm(1.0);
		}

		// rotating the throwing servo
		if(controller1.dpadUpOnce() && currentArmPosition > 500){ robot.arm.rotateCage(1.0); }
		if(controller1.dpadDownOnce()){ robot.arm.rotateCage(0.1); }

		// ------------------------
		// - Intake system
		// ------------------------

		if(leftTrigger == 0.0){ robot.intake(rightTrigger); }
		if(rightTrigger == 0.0){ robot.intake(-leftTrigger); }

		// ------------------------
		// - Other features
		// ------------------------
		// emergency stop button
		if(controller1.XOnce()){ stop(); }

		// the duck servo
		if(controller1.dpadRightOnce()){ robot.duckServoOn(); }
		if(controller1.dpadLeftOnce()){ robot.duckServoOff(); }

		// ------------------------
		// - Printing stuff
		// ------------------------
		telemetry.addData("brakes status(on/off)", robot.arm.brakes);
		telemetry.addData("is arm raised?", robot.arm.isArmRaised);
		telemetry.addData("current arm position", currentArmPosition);
		robot.wheels.returnMotorsValues();
		robot.sensors.startColorSensor();

		// ------------------------
		// - Computer vision
		// ------------------------
		// objectDetector.initTfod();
	}

	private static boolean isZero(double value) {
		return Utils.inVicinity(value, 0, 0.01);
	}
}