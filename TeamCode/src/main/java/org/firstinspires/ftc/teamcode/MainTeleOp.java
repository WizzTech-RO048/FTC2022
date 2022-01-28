/*
* Controls for the robot :
* -> gamepad1
* 	- right joystick -> movement (left, right, forward, backward)
* 	- left joystick (X axis) -> rotation of the robot
*
* -> gamepad2
* 	- Y - raise arm
* 	- X - start intake
* 	- B - stop all the actions (carousel spinning, intake)
* 	- A - throw the objects
* */

package org.firstinspires.ftc.teamcode;

// import com.acmerobotics.dashboard.FtcDashboard;
// import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
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

	private double initialThrowServerPos = 0.05;

	@Override
	public void init() {
		robot = new Robot(hardwareMap, telemetry, Executors.newScheduledThreadPool(1));

		robot.runUsingEncoders();

		controller1 = new Controller(gamepad1);
		controller2 = new Controller(gamepad2);

		robot.throwServo.setPosition(initialThrowServerPos);
	}

	// ------------------------
	// - Stop button 
	// ------------------------
	@Override
	public void stop() {
		robot.wheels.stop();
		robot.arm.stopArm();
		if (lastArmRaised != null) {
			lastArmRaised.cancel(true);
		}
		isArmRaised = false;
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
		double y = -1 * Math.pow(gamepad1.right_stick_y, 3.0);
		double x = Math.pow(gamepad1.right_stick_x, 3.0);
		double r = gamepad1.left_stick_x;

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
		if(controller1.rightBumberOnce()){
			isArmRaised = !isArmRaised;
			telemetry.addData("Is arm raised", isArmRaised);
			lastArmRaised = robot.arm.moveArm(1);
		} 
		// if(isArmRaised){ robot.arm.BrakeArm(true); }

		// rotating the throwing servo
		if(controller1.dpadDownOnce()){ robot.throwServo.setPosition(initialThrowServerPos); }
		if(controller1.dpadUpOnce()){ robot.throwServo.setPosition(1.0); }
		
		// ------------------------
		// - Other features
		// ------------------------
		if(controller1.AOnce()){ robot.duckServoOn(); }
		if(controller1.YOnce()){ robot.intake(); }
		if(controller1.BOnce()){ stop(); }

		// ------------------------
		// - Printing stuff
		// ------------------------
		telemetry.addData("is armed raised?", isArmRaised);
		telemetry.addData("target pos", robot.arm.TargetPos);
		telemetry.addData("current pos", robot.arm.CurrentPos);
		robot.arm.printArmPos();
	}

	private static boolean isZero(double value) {
		return Utils.inVicinity(value, 0, 0.01);
	}
}