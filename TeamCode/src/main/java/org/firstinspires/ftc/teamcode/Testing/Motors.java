// this class is used to setup more easily the motors that will be used

package org.firstinspires.ftc.teamcode.Testing;

// the imports for the motors
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

// hardwareMap and telemetry imports
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Motors{
	// declaring the hardwareMap and telemetry variables
	private final HardwareMap hardwareMap;
	private final Telemetry telemetry;

	// declaring all the motor related variables
	private final DcMotor intakeMotor;
	private boolean turbo = false;

	public Motors(final HardwareMap _hardwareMap, final Telemetry _telemetry){
		hardwareMap = _hardwareMap;
		telemetry = _telemetry;
	}

	public void setMotorsMode(DcMotor.RunMode mode, DcMotor ... motors){
		for(DcMotor motor : motors){
			motor.setMode(mode);
			motor.setDirection(DcMotorSimple.Direction.FORWARD);
		}
	}

	public void runUsingEncoders(){ setMotorsMode(DcMotor.RunMode.RUN_USING_ENCODER, intakeMotor); }
	public void runWithoutEncoders(){ setMotorsMode(DcMotor.RunMode.RUN_WITHOUT_ENCODERS, intakeMotor); }

	void setTurbo(boolean turboValue){ turbo = tuboValue; }
	void stopMotors(DcMotor ... motors){
		for(DcMotor motor : motors){
			motor.setPower(0.0);
		}
	}
}