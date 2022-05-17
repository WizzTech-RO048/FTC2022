package org.firstinspires.ftc.teamcode.autonomy;

import androidx.annotation.NonNull;
import org.firstinspires.ftc.teamcode.Robot.Robot;

import java.util.concurrent.ScheduledFuture;

class StateRotate extends State {
	// private final ScheduledFuture<?> rotation;
	private final State previousState;
	private double degrees;

	StateRotate(@NonNull Robot robot, double degrees, @NonNull State previous) {
		super(robot);
		robot.wheels.rotateFor2(degrees, 0.4);
		previousState = previous;
	}

	@Override
	public State update() {
//		if (rotation.isDone()) {
//			return previousState;
//		}

		// robot.wheels.rotateFor2(degrees, 0.2);

		return previousState;
	}

	@Override
	public void stop() {
		// rotation.cancel(true);
		super.stop();
	}
}