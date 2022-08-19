package org.firstinspires.ftc.teamcode.States;

import androidx.annotation.NonNull;
import org.firstinspires.ftc.teamcode.Robot.Arm;
import org.firstinspires.ftc.teamcode.Robot.Robot;
import org.firstinspires.ftc.teamcode.Robot.Wheels;

class StateBarcodeDetectedRight extends State {
	private boolean hasMovedForward = false, hasMovedRight = false, hasRotatedInPosition = false, hasDroppedObject = false;
	private State state;

	StateBarcodeDetectedRight(@NonNull Robot robot) {
		super(robot);
	}

	@Override
	public State update() {
		// insert delay
		// state.waitForSeconds(seconds);

		// BLUE 1
//		try {
//			Thread.sleep(2);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//
//
		if (!hasMovedForward) {
			hasMovedForward = true;
			return new StateMove(robot, 0.72, 0.3, Wheels.MoveDirection.BACKWARD, this);
		}

		if (!hasRotatedInPosition) {
			hasRotatedInPosition = true;
			return new StateRotate(robot, 90, this);
		}

		if (!hasDroppedObject) {
			hasDroppedObject = true;
			return new StateDropObject(robot, Arm.Position.TOP, this);
		}

		// RED 1

//		if (!hasMovedForward) {
//			hasMovedForward = true;
//			return new StateMove(robot, 0.72, 0.3, Wheels.MoveDirection.BACKWARD, this);
//		}
//
//		if (!hasRotatedInPosition) {
//			hasRotatedInPosition = true;
//			return new StateRotate(robot, 90, this);
//		}
//
//		if (!hasDroppedObject) {
//			hasDroppedObject = true;
//			return new StateDropObject(robot, Arm.Position.TOP, this);
//		}


		return new StateActionCarousel(robot);
	}
}

/*
* BLUE 1
*
* 	try {
			Thread.sleep(2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}


		if (!hasMovedForward) {
			hasMovedForward = true;
			return new StateMove(robot, 0.72, 0.3, Wheels.MoveDirection.BACKWARD, this);
		}

		if (!hasRotatedInPosition) {
			hasRotatedInPosition = true;
			return new StateRotate(robot, -90, this);
		}

		if (!hasDroppedObject) {
			hasDroppedObject = true;
			return new StateDropObject(robot, Arm.Position.TOP, this);
	}
* */

/*
* RED 1
*
*
* */