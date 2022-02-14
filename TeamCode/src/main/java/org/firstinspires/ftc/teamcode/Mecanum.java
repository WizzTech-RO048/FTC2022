package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.teamcode.Robot.*;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import java.util.concurrent.Executors;

@TeleOp(name = "mecanum")
public class Mecanum extends OpMode {
	private Robot robot;
	private Controller controller1;

	private boolean arcadeMode = false;

	@Override
	public void init(){
		robot = new Robot(hardwareMap, telemetry, Executors.newScheduledThreadPool(1));

		controller1 = new Controller(gamepad1);
	}

	@Override
	public void loop(){
		controller1.update();
		if(controller1.AOnce()){
			arcadeMode = ! arcadeMode;
		}

		telemetry.addData("gyro calibrated?", robot.isGyroCallibrated() ? "YES" : "No");
		telemetry.addData("arcade mode", arcadeMode ? "YES": "NO");
		telemetry.update();
	}


}
