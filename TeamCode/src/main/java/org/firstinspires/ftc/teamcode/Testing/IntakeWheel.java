package org.firstinspires.ftc.teamcode.Testing;

// doing the basic imports for the opmode and teleopmode
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

// doing the imports for the robotic components
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;

@TeleOp(name = "IntakeWheel")
public class IntakeWheel extends OpMode{
	// hardwareMap + telemetry

	// motor variables
	private DcMotor intakeMotor;
	private boolean turbo = false;

	// moving variables
	private Intake intake;
	private int speedValue;
	private double[] speedValues;

	@Override
	public void init(){
		// setting up the motor
		intakeMotor = hardwareMap.dcMotor.get("intakeMotor");
		intakeMotor.setDirection(DcMotorSimple.Direction.FORWARD);

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

	public void runUsingEncoders(){ setMotorsMode(DcMotor.RunMode.RUN_USING_ENCODER, intakeMotor); }
	public void runWithourEncoders(){ setMotorsMode(DcMotor.RunMode.RUN_WITHOUT_ENCODERS, intakeMotor); }

	public void setMotorsMode(DcMotor.RunMode mode, DcMotor ... motors){
		for(DcMotor motor : motors){
			motor.setMode(mode);
		}
	}

}