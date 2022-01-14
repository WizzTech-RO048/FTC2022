package org.firstinspires.ftc.teamcode;

// import com.acmerobotics.dashboard.FtcDashboard;
// import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.Teo.*;

import com.qualcomm.robotcore.hardware.Servo;

import java.util.concurrent.*;


@TeleOp(name = "MainTeoOp")
public class MainTeleOpTeo extends OpMode {
	private Robot robot;

	@Override
	public void init() {
		// telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
		robot = new Robot(hardwareMap, telemetry, Executors.newScheduledThreadPool(1));
		// We greatly increase the stop function timeout duration so the scissors' arm has time to lower.
		msStuckDetectStop = 15000;
	}

	private double angle = 0;
	private double lastAngleSet = 0, lastBowlSpeedSet = 0;
	private boolean isArmRaised = false;
	private double bowlSpeed = 0;
	private ScheduledFuture<?> lastRotation = null, lastScissorsArmRaise = null, lastCut = null;

	private double lastFlagsToggle = 0;

	@Override
	public void stop() {
		robot.wheels.stop();
		robot.duckServoOff();
		robot.stopIntake();

	}

	@Override
	public void loop() {
		double y = gamepad1.right_trigger - gamepad1.left_trigger;
		double x = gamepad1.right_stick_x;
		double r = gamepad1.left_stick_x;

		if (Utils.isDone(lastRotation)) {
			if (isZero(x) && isZero(y) && isZero(r)) {
				robot.wheels.stop();
			} else {
				robot.wheels.move(x, y, r);
			}
		}

		if(gamepad1.a){
			robot.duckServoOn();
		}

		if(gamepad1.y){
			robot.intake();
		}

		if(gamepad1.b){
			stop();
		}
	}

	private static boolean isZero(double value) {
		return Utils.inVicinity(value, 0, 0.01);
	}
}