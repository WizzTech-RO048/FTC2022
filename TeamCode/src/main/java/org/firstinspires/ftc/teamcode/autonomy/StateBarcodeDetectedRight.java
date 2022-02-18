package org.firstinspires.ftc.teamcode.autonomy;

import androidx.annotation.NonNull;
import org.firstinspires.ftc.teamcode.Robot.Arm;
import org.firstinspires.ftc.teamcode.Robot.Robot;
import org.firstinspires.ftc.teamcode.Robot.Wheels;

class StateBarcodeDetectedRight extends State {
    private boolean hasMovedForward = false, hasMovedRight = false, hasRotatedInPosition = false, hasDroppedObject = false;

    StateBarcodeDetectedRight(@NonNull Robot robot) {
        super(robot);
    }

    @Override
    public State update() {
        if (!hasMovedForward) {
            hasMovedForward = true;
            return new StateMove(robot, 0.25, 0.3, Wheels.MoveDirection.BACKWARD, this);
        }

        if (!hasMovedRight) {
            hasMovedRight = true;
            return new StateMove(robot, 0.7, 0.4, Wheels.MoveDirection.LEFT, this);
        }

        if (!hasDroppedObject) {
            hasDroppedObject = true;
            return new StateDropObject(robot, Arm.Position.TOP, this);
        }

        if (!hasRotatedInPosition) {
            hasRotatedInPosition = true;
            return new StateRotate(robot, 180, this);
        }

        return new StateActionCarousel(robot);
    }
}
