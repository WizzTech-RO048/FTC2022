/*
* TODO LIST:
*  - turbo button
*
* */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Robot.*;

import java.util.concurrent.*;

@TeleOp(name = "MainTeleOp")
public class MainTeleOp extends OpMode {
	private Robot robot;
	private Controller controller1;
	private Controller controller2;

	private boolean isArmRaised = false;
	private ScheduledFuture<?> lastRotation = null, lastArmRaised = null, lastThrow = null;

	private double initialThrowServerPos = 0.1;

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
		if(controller1.AOnce()){
			robot.arm.isArmRaised = !robot.arm.isArmRaised;
			lastArmRaised = robot.arm.moveArm(0.0);
		}

		if(controller1.BOnce()){
			robot.arm.isArmRaised = !robot.arm.isArmRaised;
			lastArmRaised = robot.arm.moveArm(0.5);
		}

		if(controller1.YOnce()){
			robot.arm.isArmRaised = !robot.arm.isArmRaised;
			lastArmRaised = robot.arm.moveArm(1.0);
		}

		// if(rightTrigger != 0.0){
		// 	robot.arm.moveArm(rightTrigger);
	  	// }

		// robot.arm.BrakeArm(true);


		// rotating the throwing servo
		if(controller1.dpadDownOnce()){ robot.arm.rotateCage(1.0); }
		if(controller1.dpadUpOnce()){ robot.arm.rotateCage(0.1); }
		
		// ------------------------
		// - Other features
		// ------------------------
		// if(controller1.XOnce()){ robot.duckServoOn(); }
		if(controller1.XOnce()){ stop(); }
		robot.intake(rightTrigger);
		// if(controller1.BOnce()){ stop(); }

		// ------------------------
		// - Printing stuff
		// ------------------------
		telemetry.addData("brakes status(on/off)", robot.arm.brakes);
		telemetry.addData("is arm raised?", robot.arm.isArmRaised);
		robot.arm.printArmPos();

		robot.sensors.startColorSensor();
	}

	private static boolean isZero(double value) {
		return Utils.inVicinity(value, 0, 0.01);
	}
}