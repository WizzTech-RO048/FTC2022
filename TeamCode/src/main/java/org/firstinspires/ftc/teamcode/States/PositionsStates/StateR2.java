package org.firstinspires.ftc.teamcode.States.PositionsStates;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.Robot.*;
import org.firstinspires.ftc.teamcode.States.ActionsStates.StateDropObject;
import org.firstinspires.ftc.teamcode.States.MovementStates.StateMove;
import org.firstinspires.ftc.teamcode.States.MovementStates.StateRotate;
import org.firstinspires.ftc.teamcode.States.State;

public class StateR2 extends State {
    Arm.Position armPosition;
    boolean movedLeft = false, movedForward = false, rotated180 = false, thrownObject = false, rotated90 = false, movedRight = false, secondMoveForward = false;

    public StateR2(@NonNull Robot robot, Arm.Position postion) {
        super(robot);
    }

    @Override
    public State update() {
        if (!movedLeft) {
            movedLeft = true;
            return new StateMove(robot, 0.25, 0.3, Wheels.MoveDirection.LEFT, this, 1);
        }

        if (!movedForward) {
            movedForward = true;
            return new StateMove(robot, 0.25, 0.3, Wheels.MoveDirection.FORWARD, this, 1);
        }

        if (!rotated180) {
            rotated180 = true;
            return new StateRotate(robot, 170, this);
        }

        if (!thrownObject) {
            thrownObject = true;
            return new StateDropObject(robot, armPosition, this);
        }

        if (!rotated90) {
            rotated90 = true;
            return new StateRotate(robot, -80, this);
        }

        if (!movedRight) {
            movedRight = true;
            return new StateMove(robot, 0.25, 0.3, Wheels.MoveDirection.RIGHT, this, 1);
        }

        if (!secondMoveForward) {
            secondMoveForward = true;
            return new StateMove(robot, 0.25, 0.3, Wheels.MoveDirection.FORWARD, this, 1);
        }

        return this;
    }

    @Override
    public void stop(){ super.stop(); }

}
