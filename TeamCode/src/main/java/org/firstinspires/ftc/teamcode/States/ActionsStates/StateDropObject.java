package org.firstinspires.ftc.teamcode.States.ActionsStates;

import androidx.annotation.NonNull;
import org.firstinspires.ftc.teamcode.Robot.*;
import org.firstinspires.ftc.teamcode.States.State;

import java.util.concurrent.ScheduledFuture;

public class StateDropObject extends State {
	private final ScheduledFuture<?> movement;
	private final State previousState;

	private double throwStartTime = 0;

	public StateDropObject(@NonNull Robot robot, Arm.Position position, State previous) {
		super(robot);
		movement = robot.arm.raise(position);
		previousState = previous;
	}

	@Override
	public State update() {
		if (!movement.isDone()) {
			return this;
		}

		if (throwStartTime == 0.0) {
			robot.arm.throwObjectFromBox();
			throwStartTime = time;
			return this;
		}

		if (time - throwStartTime < 1.5) {
			return this;
		}

		robot.arm.retractBox();
		robot.arm.raise(null);

		return previousState;
	}

	@Override
	public void stop() {
		super.stop();
	}
}