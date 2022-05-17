package org.firstinspires.ftc.teamcode.autonomy;

import androidx.annotation.NonNull;
import org.firstinspires.ftc.teamcode.Robot.Robot;
import org.firstinspires.ftc.teamcode.Robot.Wheels;

public class StateActionCarousel extends State {
	private boolean hasMovedLaterallyToCarousel = false, hasMovedBackward = false, hasParked = false, rotated = false;
	private double servoActionTime = 0;

	StateActionCarousel(@NonNull Robot robot) {
		super(robot);
	}

	@Override
	public State update() {

		// BLUE 1
//		if (!hasMovedLaterallyToCarousel) {
//			hasMovedLaterallyToCarousel = true;
//			return new StateMove(robot, 0.4, 0.4, Wheels.MoveDirection.FORWARD, this, 4);
//		}
//
//		if (!rotated) {
//			rotated = true;
//			return new StateRotate(robot, 180, this);
//		}
//
//
//		 robot.wheels.rotateToOrientation(180.0, 0.1); /// Srry for this too, didn't have time ot make a state
//
//		if (!hasMovedBackward) {
//			hasMovedBackward = true;
//			return new StateMove(robot, 0.6, 0.3, Wheels.MoveDirection.LEFT, this, 2.5);
//		}

		// RED 1
		if (!hasMovedLaterallyToCarousel) {
			hasMovedLaterallyToCarousel = true;
			return new StateMove(robot, 0.45, 0.4, Wheels.MoveDirection.FORWARD, this, 4);
		}

		if (!rotated) {
			rotated = true;
			return new StateRotate(robot, 270, this);
		}

//		if (!hasParked) {
//			hasParked = true;
//			return new StateMove(robot, 0.1, 0.4, Wheels.MoveDirection.BACKWARD, this, 4);
//		}


		if (servoActionTime == 0) {
			servoActionTime = time;
			robot.duckServoBlue();
		}

		if (time - servoActionTime > 5) {
			robot.duckServoOff();
			return new StateException(robot, new Exception());
		}


//		if(!hasParked && time - servoActionTime > 6) {
//			hasParked = true;
//			return new StateMove(robot, 0.2, 0.3, Wheels.MoveDirection.LEFT, this, 3);
//		}

		return this;

		// robot.wheels.rotateFor2(90, 0.3);
		// return new StateMove(robot, 5, 1, Wheels.MoveDirection.RIGHT, this, 5);
	}
}