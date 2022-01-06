package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "MainTeleOp")
public class MainTeleOp extends OpMode{
	private Intake intake;

	@Override
	public void init(){
		intake = new Intake(hardwareMap, telemetry);
		intake.runWithoutEncoders();
	}

	public void loop(){
		double x = gamepad1.left_stick_x;
		double y = gamepad1.left_stick_y;

		if(gamepad1.y){
			intake.goForward();
		}
		if(gamepad1.x){
			intake.goBackward();
		}
	}
}