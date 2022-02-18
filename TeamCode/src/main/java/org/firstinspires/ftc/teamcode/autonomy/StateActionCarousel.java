package org.firstinspires.ftc.teamcode.autonomy;

import androidx.annotation.NonNull;
import org.firstinspires.ftc.teamcode.Robot.Robot;
import org.firstinspires.ftc.teamcode.Robot.Wheels;

public class StateActionCarousel extends State {
    private boolean hasMovedLaterallyToCarousel = false, hasMovedBackward = false;
    private double servoActionTime = 0;

    StateActionCarousel(@NonNull Robot robot) {
        super(robot);
    }

    @Override
    public State update() {
        if (!hasMovedLaterallyToCarousel) {
            hasMovedLaterallyToCarousel = true;
            return new StateMove(robot, 1.5, 0.5, Wheels.MoveDirection.LEFT, this);
        }

        if (!hasMovedBackward) {
            hasMovedBackward = true;
            return new StateMove(robot, 0.19, 0.3, Wheels.MoveDirection.BACKWARD, this);
        }

        if (servoActionTime == 0) {
            servoActionTime = time;
            robot.duckServoOn();
        }

        if (time - servoActionTime > 5) {
            robot.duckServoOff();
            return new StateException(robot, new Exception());
        }

        return this;
    }
}
