package org.firstinspires.ftc.teamcode.States.ActionsStates;

import androidx.annotation.NonNull;
import org.firstinspires.ftc.teamcode.Robot.Robot;
import org.firstinspires.ftc.teamcode.States.StateException;
import org.firstinspires.ftc.teamcode.States.State;

public class StateActionCarousel extends State {
	private boolean hasMovedLaterallyToCarousel = false, hasMovedBackward = false, hasParked = false;
	private double servoActionTime = 0;

	public StateActionCarousel(@NonNull Robot robot) {
		super(robot);
	}

	@Override
	public State update() {

		if (servoActionTime == 0) {
			servoActionTime = time;
			robot.duckServoOn();
		}

		if (time - servoActionTime > 5) {
			robot.duckServoOff();
			return new StateException(robot, new Exception());
		}

//		if(!hasParked && time - servoActionTime > 6) {
//			hasParked = true;
//			return new StateMove(robot, 0.13, 0.3, Wheels.MoveDirection.FORWARD, this, 3);
//		}

		return this;
	}
}