package org.firstinspires.ftc.teamcode.Robot;

import androidx.annotation.NonNull;

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

	Arm(@NonNull final Parameters parameters){
		arm = Objects.requireNonNull(parameters.arm, "Scissors arm is not set");
		arm.setDirection(DcMotorSimple.Direction.FORWARD);
		arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

		throwServo = Objects.requireNonNull(parameters.throwServo, "throw servo is not set");
		throwServo.setDirection(Servo.Direction.REVERSE);

		scheduler = Objects.requireNonNull(parameters.scheduler, "scheduler is not set");
		telemetry = Objects.requireNonNull(parameters.telemetry, "Telemetry is not set");

		armRaisedPosition = parameters.armRaisedPosition;

	}

	public int TargetPos;
	public int CurrentPos;

	// ------------------------
	// - Arm moving functions
	// ------------------------
	private ScheduledFuture<?> lastMove = null, lastThrow = null;
	public ScheduledFuture<?> moveArm(double positionPercentage){
		if(!Utils.isDone(lastMove) && !lastMove.cancel(true)){
			telemetry.addLine("last move not done!");
			return null;
		}

		int targetPosition = (int) Math.floor(Utils.interpolate(0, armRaisedPosition, positionPercentage, 1));
		int initialPosition = arm.getCurrentPosition();

		TargetPos = targetPosition;

		if (armRaisedPosition == initialPosition) {
			return null;
		}

		arm.setTargetPosition(targetPosition);
		arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
		arm.setPower(targetPosition > initialPosition ? 0.2 : -0.2);

		lastMove = Utils.poll(scheduler, () -> !arm.isBusy(), () -> arm.setPower(0), 10, TimeUnit.MILLISECONDS);

		return lastMove;
	}


	public void BrakeArm(boolean shouldUse) {
		DcMotorEx.ZeroPowerBehavior behavior = shouldUse ? DcMotorEx.ZeroPowerBehavior.BRAKE : DcMotorEx.ZeroPowerBehavior.FLOAT;

		arm.setZeroPowerBehavior(behavior);
	}


	public void stopArm(){
		arm.setPower(0.0);
	}

	public ScheduledFuture<?> printArmPos(){
		telemetry.addData("current arm pos", arm.getCurrentPosition());
		telemetry.addData("target pos", TargetPos);
		lastMove = Utils.poll(scheduler, () -> !arm.isBusy(), () -> arm.setPower(0), 10, TimeUnit.MILLISECONDS);

		return lastMove;
	}

	// ------------------------
	// - Throw servo functions
	// ------------------------
	/*
	private static final double SERVO_POS = 0.3;
	public ScheduledFuture<?> throwObjects(){
		if(!Utils.isDone(lastThrow) && !lastThrow.cancel(true)){
			return null;
		}

		Servo servo = throwServo;
		servo.setPosition(SERVO_POS);

		lastThrow = scheduler.schedule(() -> servo.setPosition(0), 500, TimeUnit.MILLISECONDS);

		return lastThrow;
	}
	*/

	public void cancelCut(){
		if(Utils.isDone(lastThrow) || !lastThrow.cancel(true)){
			return ;
		}
	}

	static class Parameters{
		DcMotorEx arm;
		Servo throwServo;
		Telemetry telemetry;
		ScheduledExecutorService scheduler;
		int armRaisedPosition;
	}
}
