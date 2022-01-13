/*
* This code is written for testing the movement of the robot.
*
* For this to work, the motors that are connected to the control hub,
* need to be connected to the next ports:
*     - lf - port 0
*     - lr - port 1
*     - rf - port 2
*     - rr - port 3
*
* Controls:
*     - the right joystick - movement of the robot
*     - the left joystick - rotation of the robot
*     - B - stops the robot
*     - right_bumber - turbo on
* */

package org.firstinspires.ftc.teamcode.Testing;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "MovementTest")
public class Movement extends OpMode {
	private Robot robot;

	private boolean turbo = false;

	@Override
	public void init(){
		robot = new Robot(hardwareMap, telemetry);

		robot.runUsingEncoders();

		telemetry.addData("WizzTech Robotics Team", "");
		telemetry.addData("---------------------------", "");
		telemetry.addData("testing session for the robot movement", "");
		telemetry.addData("---------------------------", "");
	}

	public void loop(){
		double x = gamepad2.left_stick_x;
		double y = gamepad2.left_stick_y;
		if(gamepad2.right_bumper){ turbo = true; }

		robot.moveRobot(x, y, gamepad2.right_stick_x, turbo);
		if(gamepad2.b){ robot.stopRobot(); }
	}
}