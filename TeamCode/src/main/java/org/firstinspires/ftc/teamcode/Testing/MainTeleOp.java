package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "MainTeleOp")
public class MainTeleOp extends OpMode{
	private Intake intake;
	private int speedValue;
	private double[] speedValues;

	@Override
	public void init(){
		intake = new Intake(hardwareMap, telemetry);
		intake.runWithoutEncoders();
		speedValue = 0;

		speedValues = new double[] {0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0};
	}

	public void loop(){
		double x = gamepad1.left_stick_x;
		double y = gamepad1.left_stick_y;

		if(gamepad1.y){
			intake.goForward(speedValue);
		}
		if(gamepad1.x){
			intake.goBackward(speedValue);
		}
		if(gamepad1.b){
			intake.stopMoving();
		}
		if(gamepad1.dpad_up && speedValue < 10 && speedValue >= 0){
			speedValue = speedValue + 1;
			telemetry.addData("speedValue: ", speedValue);
		}
		if(gamepad1.dpad_down && speedValue <= 10 && speedValue > 0){
			speedValue = speedValue - 1;
			telemetry.addData("speedValue: ", speedValue);
		}
		telemetry.update();
	}
}