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
import org.firstinspires.ftc.teamcode.Robot.*;

import java.util.concurrent.*;

@TeleOp(name = "MainTeleOp")
public class MainTeleOp extends OpMode {
	private Robot robot;

	private boolean isArmRaised = false;
	private ScheduledFuture<?> lastRotation = null, lastArmRaised = null, lastThrow = null;

	@Override
	public void init() {
		// telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
		robot = new Robot(hardwareMap, telemetry, Executors.newScheduledThreadPool(1));
		// We greatly increase the stop function timeout duration so the scissors' arm has time to lower.
		msStuckDetectStop = 15000;
	}

	@Override
	public void stop() {
		robot.wheels.stop();

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
		double y = gamepad1.right_stick_y;
		double x = gamepad1.right_stick_x;
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
		if(Utils.isDone(lastThrow) && gamepad2.a){
			lastThrow = robot.arm.throwObjects();
		}

		// other features
		if(gamepad2.a){ robot.duckServoOn(); }
		if(gamepad2.y){ robot.intake(); }
		if(gamepad2.b){ stop(); }
	}

	private static boolean isZero(double value) {
		return Utils.inVicinity(value, 0, 0.01);
	}
}