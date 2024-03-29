package org.firstinspires.ftc.teamcode.Robot;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.Objects;
import java.util.concurrent.*;

public class Arm {
    private final Telemetry telemetry;
    private final ScheduledExecutorService scheduler;

    private final DcMotorEx arm;
    private final Servo throwServo;

    private final int armRaisedPosition;

    Arm(@NonNull final Parameters parameters) {
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

    private ScheduledFuture<?> lastMove = null, lastThrow = null;

    public enum Position {
        BASE(0.1),
        MID(0.3),
        TOP(0.65);

        private final double position;

        Position(double position) {
            this.position = position;
        }
    }


    public ScheduledFuture<?> moveArm(double positionPercentage, double raise_power) {
        if (!Utils.isDone(lastMove) && !lastMove.cancel(true)) {
            return null;
        }

        int targetPosition = (int) Math.floor(Utils.interpolate(0, armRaisedPosition, positionPercentage, 1));
        int initialPosition = arm.getCurrentPosition();

        if (armRaisedPosition == initialPosition) {
			return null;
        }

        arm.setTargetPosition(targetPosition);
        arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        arm.setPower(targetPosition > initialPosition ? raise_power : -raise_power);

        lastMove = Utils.poll(scheduler, () -> !arm.isBusy(), () -> arm.setPower(0), 10, TimeUnit.MILLISECONDS);

        return lastMove;
    }

    public ScheduledFuture<?> raise(@Nullable Position position, double raise_power) {
        if (position != null) {
            return moveArm(position.position, raise_power);
        }

        return moveArm(0, raise_power);
    }

    public void stop() {
		arm.setPower(0.0);
    }

    // rotating the cage
	public void throwObject() {
		throwServo.setPosition(1);
	}

	public void retract() {
		throwServo.setPosition(0.3);
	}

    static class Parameters {
        DcMotorEx arm;
        Servo throwServo;
        Telemetry telemetry;
        ScheduledExecutorService scheduler;
        int armRaisedPosition;
    }
}
