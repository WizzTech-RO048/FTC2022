package org.firstinspires.ftc.teamcode.autonomy;

import androidx.annotation.NonNull;
import org.firstinspires.ftc.teamcode.Robot.*;
import org.firstinspires.ftc.teamcode.state.State;

import java.util.concurrent.ScheduledFuture;

final class Movement extends RobotComposableStateSource {
    private final ScheduledFuture<?> movement;

    Movement(@NonNull Robot robot, double meters, double power, Wheels.MoveDirection direction) {
        super(robot);
        movement = robot.wheels.moveFor(meters, power, direction);
    }

    @Override
    public boolean update() {
        return movement.isDone();
    }
}
