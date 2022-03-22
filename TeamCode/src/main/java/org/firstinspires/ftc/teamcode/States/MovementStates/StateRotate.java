package org.firstinspires.ftc.teamcode.States.MovementStates;

import androidx.annotation.NonNull;
import org.firstinspires.ftc.teamcode.Robot.Robot;
import org.firstinspires.ftc.teamcode.States.State;

import java.util.concurrent.ScheduledFuture;

public class StateRotate extends State {
	private final State previousState;
	double degrees;
	ScheduledFuture<?> rotation;

	public StateRotate(@NonNull Robot robot, double degrees2rotate, @NonNull State previous) {
		super(robot);
		robot.wheels.rotateFor2(degrees, 0.2);
		previousState = previous;
		degrees = degrees2rotate;
	}

	@Override
	public State update() {
		if (rotation.isDone()) {
			return previousState;
		}
		 robot.wheels.rotateFor2(degrees, 0.2);

		return previousState;
	}

	@Override
	public void stop() {
		 rotation.cancel(true);
		super.stop();
	}
}