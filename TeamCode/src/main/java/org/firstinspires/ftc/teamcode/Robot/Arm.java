package org.firstinspires.ftc.teamcode.Robot;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.lang.annotation.Target;
import java.util.Objects;
import java.util.concurrent.*;

public class Arm {
	private final Telemetry telemetry;
	private final ScheduledExecutorService scheduler;

	private final DcMotorEx arm;
	private final Servo throwServo;

	private final int armRaisedPosition;
	public boolean isArmRaised = false;

	public int TargetPos;

	public boolean brakes = true;

	Arm(@NonNull final Parameters parameters){
		arm = Objects.requireNonNull(parameters.arm, "Scissors arm is not set");
		arm.setDirection(DcMotorSimple.Direction.REVERSE);
		arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

		throwServo = Objects.requireNonNull(parameters.throwServo, "throw servo is not set");
		throwServo.setDirection(Servo.Direction.FORWARD);

		scheduler = Objects.requireNonNull(parameters.scheduler, "scheduler is not set");
		telemetry = Objects.requireNonNull(parameters.telemetry, "Telemetry is not set");

		armRaisedPosition = parameters.armRaisedPosition;
	}

	// ------------------------
	// - Arm moving functions
	// ------------------------
	private ScheduledFuture<?> lastMove = null, lastThrow = null;
	public ScheduledFuture<?> moveArm(double positionPercentage){
		brakes = false;
		BrakeArm(brakes);
		if(!Utils.isDone(lastMove) && !lastMove.cancel(true)){
			telemetry.addLine("last move not done!");
			return null;
		}

		int targetPosition = (int) Math.floor(Utils.interpolate(0, armRaisedPosition, positionPercentage, 1));
		int initialPosition = arm.getCurrentPosition();

		TargetPos = targetPosition;

		if (armRaisedPosition == initialPosition) {
			brakes = true;
			BrakeArm(brakes);
			isArmRaised = true;
			return null;
		}

		arm.setTargetPosition(targetPosition);
		arm. setMode(DcMotor.RunMode.RUN_TO_POSITION);
		arm.setPower(targetPosition > initialPosition ? 1 : -1);

		lastMove = Utils.poll(scheduler, () -> !arm.isBusy(), () -> arm.setPower(0), 10, TimeUnit.MILLISECONDS);

		return lastMove;
	}

	public void stopArm(){
		brakes = true;
		BrakeArm(brakes);
		isArmRaised = false;
		arm.setPower(0.0);
	}

	public int currentArmPosition(){ return arm.getCurrentPosition(); }
	public void BrakeArm(boolean shouldUse) {
		DcMotorEx.ZeroPowerBehavior behavior = shouldUse ? DcMotorEx.ZeroPowerBehavior.BRAKE : DcMotorEx.ZeroPowerBehavior.FLOAT;
		arm.setZeroPowerBehavior(behavior);
	}

	// rotating the cage
	public void rotateCage(double position){ throwServo.setPosition(position); }

	static class Parameters{
		DcMotorEx arm;
		Servo throwServo;
		Telemetry telemetry;
		ScheduledExecutorService scheduler;
		int armRaisedPosition;
	}
}
