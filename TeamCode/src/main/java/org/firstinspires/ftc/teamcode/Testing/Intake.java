package org.firstinspires.ftc.teamcode.Testing;

// doing the basic imports for the teleop and opmode
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

// doing all the imports
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@TeleOp(name = "IntakeWheelTesting")
public class Intake extends OpMode{
	private final DcMotor intakeMotor;

	private final HardwareMap hardwareMap;
	private final Telemetry telemetry;

	private boolean turbo = false;

	public void init(){
		intakeMotor = hardwareMap.dcMotor.get("intakeMotor");

		intakeMotor.setDirection(DcMotorSimple.Direction.FORWARD);

		runWithoutEncoders();
	}

	@Override
	public void loop(){
		if(gamepad1.x){
			stopMotors(intakeMotor);
			intakeMotor.setPower(powerValue);
		}
		if(gamepad1.y){
			stopMotors(intakeMotor);
			intakeMotor.setPower(-1  * powerValue);
		}
		if(gamepad1.dpad_up){
			if(powerValue >= 0 && powerValue < 10){
				powerValue += 1;
			}
		}
		if(gamepad1.dpad_down){
			if(powerValue > 0 && powerValue <= 10){
				powerValue -= 1;
			}
		}
		telemetry.addData("power value:", powerValue);
	}

	public void setMotorsMode(DcMotorSimple.RunMode mode, DcMotors ... mode){
		for(DcMotor motor : motors){
			motor.setMode(mode);
		}
	}

	void runUsingEncoders(){ setMotorsMode(DcMotor.RunMode.RUN_USING_ENCODERS, intakeMotor); }
	void runWithoutEncoders(){ setMotorsMode(DcMotor.RunMode.RUN_WITHOUT_ENCODERS, intakeMotor); }

	void stopMotors(DcMotor ... motor){
		for(DcMotor motor : motors){
			motor.setPower(0.0);
		}
	}
}

