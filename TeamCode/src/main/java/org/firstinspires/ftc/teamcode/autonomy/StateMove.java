package org.firstinspires.ftc.teamcode.autonomy;

//import android.sax.StartElementListener;

import androidx.annotation.NonNull;
import org.firstinspires.ftc.teamcode.Robot.*;

import java.util.concurrent.ScheduledFuture;

class StateMove extends State {
	private final ScheduledFuture<?> movement;
	private final State previousState;
	private final double timeout;

	StateMove(@NonNull Robot robot, double meters, double power, Wheels.MoveDirection direction, State previous) {
		super(robot);
		movement = robot.wheels.moveFor(meters, power, direction);
		previousState = previous;
		timeout = 0;
	}

	StateMove(@NonNull Robot robot, double meters, double power, Wheels.MoveDirection direction, State previous, double timeout) {
		super(robot);
		movement = robot.wheels.moveFor(meters, power, direction);
		previousState = previous;
		this.timeout = timeout;
	}

	@Override
	public State update() {
		if (movement.isDone()) {
			return previousState;
		}

		robot.getTelemetry().addData("Time", timePassed);
		robot.getTelemetry().update();

		if (timeout - timePassed < 0.5 && timeout != 0) {
			return previousState;
		}

		return this;
	}

	@Override
	public void stop() {
		movement.cancel(true);
		super.stop();
	}
}