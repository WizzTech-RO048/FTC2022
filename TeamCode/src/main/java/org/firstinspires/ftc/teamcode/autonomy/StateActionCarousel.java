package org.firstinspires.ftc.teamcode.autonomy;

import androidx.annotation.NonNull;
import org.firstinspires.ftc.teamcode.Robot.Robot;
import org.firstinspires.ftc.teamcode.Robot.Wheels;
import org.firstinspires.ftc.teamcode.state.State;

final class StateActionCarousel extends RobotState {
    StateActionCarousel(@NonNull Robot robot) {
        super(robot);
    }

    @Override
    public State update() {
        return State.compose(
                () -> new Movement(robot, 1.5, 0.5, Wheels.MoveDirection.LEFT),
                () -> new Movement(robot, 0.19, 0.3, Wheels.MoveDirection.BACKWARD),
                () -> new CarouselSpin(robot)
        ).build(() -> new StateException(robot, new Exception()));
    }
}
