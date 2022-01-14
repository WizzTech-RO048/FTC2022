package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.Teo.*;

import java.util.concurrent.*;


@TeleOp(name = "MainTeleOp")
public class MainTeleOp extends OpMode {
	private Robot robot;
	private Controller controller1;
	private Controller controller2;

	@Override
	public void init(){
		robot = new Robot(hardwareMap, telemetry, Executors.newScheduledThreadPool(1));
		robot.runUsingEncoders();

		controller1 = new Controller(gamepad1);
		controller2 = new Controller(gamepad2);
	}

	@Override
	public void stop(){
		robot.stop();
	}

	@Override
	public void loop(){
		robot.setTurbo(controller1.rightBumber());

		double x = gamepad1.right_stick_x;
		double y = gamepad1.right_trigger - gamepad1.left_trigger;

		telemetry.addData("rightStickX", gamepad1.right_stick_x);
		telemetry.addData("rightStickY", gamepad1.right_stick_y);

		robot.move(x, y);

		if(gamepad1.y){
			robot.intake();
		}
		if(gamepad1.b){
			robot.stopIntake();
			robot.duckServoOff();
		}

		if(gamepad1.a){
			robot.duckServoOn();
		}

	}

}
