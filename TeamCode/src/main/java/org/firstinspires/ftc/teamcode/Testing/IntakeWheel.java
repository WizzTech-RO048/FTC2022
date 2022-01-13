/*
* This code is written for testing the intake wheel.
*
* For this to work, the motor that is connected to the wheel,
* needs to be connected on the port number 0, on the Control
* Hub.
*
* Controls:
*     - Y - turn on intake wheel
*     - B - stop moving
*
* */

package org.firstinspires.ftc.teamcode.Testing;

// doing the basic imports for the opmode and teleopmode
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "IntakeWheelTesting")
public class IntakeWheel extends OpMode{
<<<<<<< HEAD
	// hardwareMap + telemetry
=======
	private Robot robot;
>>>>>>> 475e411d084e7fbd2c823c5e68aa15abe7a93f8b

	private boolean turbo = false;
	private int speedValue;

	@Override
	public void init(){
		// setting up the motor
		robot = new Robot(hardwareMap, telemetry);

		robot.runWithoutEncoders();
		telemetry.addData("WizzTech Robotics Team", "");
		telemetry.addData("---------------------------", "");
		telemetry.addData("testing session for the intake wheel", "");
		telemetry.addData("---------------------------", "");

	}

	public void loop(){
		double x = gamepad1.left_stick_x;
		double y = gamepad1.left_stick_y;

		if(gamepad1.y){
			robot.intake();
		}
		if(gamepad1.b){
			robot.stopIntake();
		}
		telemetry.update();
	}
<<<<<<< HEAD

	public void runUsingEncoders(){ setMotorsMode(DcMotor.RunMode.RUN_USING_ENCODER, intakeMotor); }
	public void runWithourEncoders(){ setMotorsMode(DcMotor.RunMode.RUN_WITHOUT_ENCODERS, intakeMotor); }

	public void setMotorsMode(DcMotor.RunMode mode, DcMotor ... motors){
		for(DcMotor motor : motors){
			motor.setMode(mode);
		}
	}

=======
>>>>>>> 475e411d084e7fbd2c823c5e68aa15abe7a93f8b
}