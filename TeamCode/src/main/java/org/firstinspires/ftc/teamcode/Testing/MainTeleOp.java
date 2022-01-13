package org.firstinspires.ftc.teamcode.Testing;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "MainTeleOp")
public class MainTeleOp extends OpMode {
	private Robot robot;
	private Controller controller1;
	private Controller controller2;

	@Override
	public void init(){
		robot = new Robot(hardwareMap, telemetry);
		robot.runUsingEncoders();

		controller1 = new Controller(gamepad1);
		controller2 = new Controller(gamepad2);
	}

	private void updateRobot(){
		if(controller1.XOnce()){ robot.resetHeading(); }

		final double x = Math.pow(controller1.leftStickX, 3.0);
		final double y = Math.pow(controller1.leftStickY, 3.0);

		final double rotation = Math.pow(controller1.rightStickX, 3.0);
		final double direction = Math.atan2(x, y) - robot.getHeading();
		final double speed = Math.min(1.0, Math.sqrt(x * x + y * y));

		final double lf = speed * Math.sin(direction + Math.PI / 4.0) - rotation;
		final double lr = speed * Math.cos(direction + Math.PI / 4.0) + rotation;
		final double rf = speed * Math.cos(direction + Math.PI / 4.0) - rotation;
		final double rr = speed * Math.sin(direction + Math.PI / 4.0) + rotation;

		robot.setMotors(lf, lr, rf, rr, (controller1.rightTrigger != 0));
	}

	@Override
	public void loop(){
		controller1.update();
		controller2.update();
		robot.loop();

		if(controller1.Y()){
			robot.intake();
		}
		if(controller1.B()){
			robot.stopIntake();
		}

		updateRobot();
	}
}
