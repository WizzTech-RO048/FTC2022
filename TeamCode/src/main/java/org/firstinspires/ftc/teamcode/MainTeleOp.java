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
	private int k = 0;

	private double initialThrowServerPos = 0.0;

	@Override
	public void init() {
		// telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
		robot = new Robot(hardwareMap, telemetry, Executors.newScheduledThreadPool(1));
		// We greatly increase the stop function timeout duration so the scissors' arm has time to lower.
		msStuckDetectStop = 15000;

		controller1 = new Controller(gamepad1);
		controller2 = new Controller(gamepad2);

		robot.throwServo.setPosition(initialThrowServerPos);
		telemetry.addLine("throw servo disabled");

	}

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

		double y = -1 * Math.pow(gamepad1.right_stick_y, 3.0);
		double x = Math.pow(gamepad1.right_stick_x, 3.0);
		double r = gamepad1.left_stick_x;

		// movement of the robot
		if(Utils.isDone(lastRotation)) {
			if(isZero(x) && isZero(y) && isZero(r)) {
				robot.wheels.stop();
			}else {
				robot.wheels.move(x, y, r);
			}
		}

		// moving the arm
		if(Utils.isDone(lastArmRaised) && gamepad2.y){
			isArmRaised = !isArmRaised;
			telemetry.addData("Is arm raised ?", isArmRaised);
			lastArmRaised = robot.arm.moveArm(isArmRaised ? 1 : 0);
		}

		// servo throw
		// if(Utils.isDone(lastThrow) && gamepad2.a){
		// 	lastThrow = robot.arm.throwObjects();
		// }

		if(controller1.dpadDownOnce()){
			k+=1;
			// robot.throwServo.setPosition(0.0);
		}
		if(controller1.dpadUpOnce()){
			k-=1;
			// robot.throwServo.setPosition(1.0);
		}

		if(controller1.rightBumberOnce()){
			// the teo code
			// robot.arm.moveArm(k /10);
			robot.arm.stopArm();
			// robot.arm.moveUp(0.5);
		}

		if(controller1.leftBumberOnce()){
			robot.arm.stopArm();
			// robot.arm.moveDown(0.5);
		}

		robot.arm.printArmPos();

		// other features
		if(gamepad1.a){ robot.duckServoOn(); }
		if(gamepad1.y){ robot.intake(); }
		if(gamepad1.b){ stop(); }


		telemetry.addData("percentage", (double)(k / 10));
	}

	private static boolean isZero(double value) {
		return Utils.inVicinity(value, 0, 0.01);
	}


}