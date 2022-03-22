package org.firstinspires.ftc.teamcode.States.PositionsStates;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.Robot.*;
import org.firstinspires.ftc.teamcode.States.ActionsStates.StateActionCarousel;
import org.firstinspires.ftc.teamcode.States.ActionsStates.StateDropObject;
import org.firstinspires.ftc.teamcode.States.MovementStates.StateMove;
import org.firstinspires.ftc.teamcode.States.MovementStates.StateRotate;
import org.firstinspires.ftc.teamcode.States.State;

public class StateR1 extends State {
    Arm.Position armPosition;
    boolean movedForward = false, movedRight = false, rotate180 = false, objectThrowed = false, secondRotation = false, movedLeft = false, movedBackward = false, carouselWheelRotated = false, secondMoveForward = false;

    public StateR1(@NonNull Robot robot, Arm.Position positioned) {
        super(robot);
        armPosition = positioned;
    }
    @Override
    public State update() {
        if (!movedForward) {
            movedForward = true;
            return new StateMove(robot, 0.25, 0.3, Wheels.MoveDirection.FORWARD, this, 1);
        }

        if (!movedRight) {
            movedRight = true;
            return new StateMove(robot, 0.5, 0.4, Wheels.MoveDirection.RIGHT, this, 1);
        }

        if (!rotate180) {
            rotate180 = true;
            return new StateRotate(robot, 170, this);
        }

        if (!objectThrowed) {
            objectThrowed = true;
            return new StateDropObject(robot, armPosition, this);
        }

        if (!secondRotation) {
            secondRotation = true;
            return new StateRotate(robot, 170, this);
        }

        if (!movedLeft) {
            movedLeft = true;
            return new StateMove(robot, 0.5, 0.3, Wheels.MoveDirection.LEFT, this, 1);
        }

        if (!movedBackward) {
            movedBackward = true;
            return new StateMove(robot, 0.5, 0.4, Wheels.MoveDirection.BACKWARD, this, 1);
        }

        if (!carouselWheelRotated) {
            carouselWheelRotated = true;
            return new StateActionCarousel(robot);
        }

        if (!secondMoveForward) {
            secondMoveForward = true;
            return new StateMove(robot, 0.5, 0.4, Wheels.MoveDirection.FORWARD, this, 1);
        }

        return this;
    }

    @Override
    public void stop(){ super.stop(); }

}
