package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Intake{
	private final DcMotor intakeMotor;

	private final HardwareMap hardwareMap;
	private final Telemetry telemetry;

	private boolean turbo = false;

	public Intake(final HardwareMap _hardwareMap, final Telemetry _telemetry){
		hardwareMap = _hardwareMap;
		telemetry = _telemetry;

		intakeMotor = hardwareMap.dcMotor.get("intakeMotor");

		intakeMotor.setDirection(DcMotorSimple.Direction.FORWARD);
	}

	public void runUsingEncoders(){ setMotorsMode(DcMotor.RunMode.RUN_USING_ENCODER, intakeMotor); }
	public void runWithoutEncoders(){ setMotorsMode(DcMotor.RunMode.RUN_WITHOUT_ENCODERS, intakeMotor); }

	public void setMotorsMode(DcMotor.RunMode mode, DcMotor ... motors){
		for(DcMotor motor : motors){
			motor.setMode(mode);
		}
	}

	public void setTurbo(boolean value){ turbo = value; }

	void goForward(double speedValue){ intakeMotor.setPower(speedValue); }
	void goBackward(double speedValue){ intakeMotor.setPower(speedValue); }
	void stopMoving(){ intakeMotor.setPower(0.0); }

}

