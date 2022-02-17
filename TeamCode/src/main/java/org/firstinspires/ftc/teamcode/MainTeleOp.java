
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
	private boolean servoRotated = false;
	private ScheduledFuture<?> lastRotation = null, lastArmRaised = null, lastThrow = null;

	private double initialThrowServerPos = 0.1;
	private int currentArmPosition;
	private double targetPosition;

	private int rbPressed = 0;
	private int dpadLeftPressed = 0;

	boolean turbo = false;

	@Override
	public void init() {
		robot = new Robot(hardwareMap, telemetry, Executors.newScheduledThreadPool(1));

		// robot.runUsingEncoders();

		controller1 = new Controller(gamepad1);
		controller2 = new Controller(gamepad2);

		robot.arm.rotateCage(initialThrowServerPos);

		servoRotated = false;
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

		turbo = false;

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
		double y = gamepad1.left_stick_x;
		double x = -gamepad1.right_stick_x;
		double r = -gamepad1.left_stick_y;

		if(x >= 0.7){ x = 0.7; }
		if(y >= 0.7){ y = 0.7; }
		if(r >= 0.7){ r = 0.7; }

		double rightTrigger = controller1.rightTrigger;
		double leftTrigger = controller1.leftTrigger;
		boolean leftBumber = controller1.leftBumber();

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
		currentArmPosition = robot.arm.currentArmPosition();

		if(controller1.AOnce()){ targetPosition = 0.0; }
		if(controller1.XOnce()){ targetPosition = 0.1; }
		if(controller1.BOnce()){ targetPosition = 0.3; }
		if(controller1.YOnce()){ targetPosition = 0.6; }

		if(controller1.dpadUpOnce() && targetPosition <= 1.0){
			targetPosition += 0.1;
		}
		if(controller1.dpadDownOnce() && targetPosition >= 0.0){
			targetPosition -= 0.1;
		}

		robot.arm.isArmRaised = isZero(targetPosition);

		if(targetPosition != 0.0 || targetPosition != 0.2) {
			lastArmRaised = robot.arm.moveArm(targetPosition);
		}
		// rotating the throwing servo
		if(controller1.rightBumberOnce()){
			rbPressed++;
			if(rbPressed % 2 == 1){
				servoRotated = true;
				robot.arm.rotateCage(1.0);
			} else{
				servoRotated = false;
				robot.arm.rotateCage(0.1);
			}
		}

		// ------------------------
		// -  Intake system
		// ------------------------
		if(leftTrigger == 0.0){ robot.intake(rightTrigger); }
		if(rightTrigger == 0.0){ robot.intake(-leftTrigger); }

		// ------------------------
		// - Other features
		// ------------------------
		if(controller1.startOnce()){ stop(); } // emergency stop button

		// the duck servo
		if(controller1.dpadLeftOnce()){
			dpadLeftPressed++;
			if(dpadLeftPressed % 2 == 0){
				robot.carouselServo.setPosition(0.8);
			} else{
				robot.carouselServo.setPosition(0.5);
			}
		}

		// ------------------------
		// - Printing stuff
		// ------------------------
		telemetry.addData("brakes status(on/off)", robot.arm.brakes);
		telemetry.addData("is arm raised?", robot.arm.isArmRaised);
		telemetry.addData("current arm position", currentArmPosition);
		robot.wheels.returnMotorsValues();
	}

	private static boolean isZero(double value) {
		return Utils.inVicinity(value, 0, 0.01);
	}
}