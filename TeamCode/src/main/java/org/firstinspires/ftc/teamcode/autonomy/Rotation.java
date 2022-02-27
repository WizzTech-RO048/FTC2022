package org.firstinspires.ftc.teamcode.autonomy;

import androidx.annotation.NonNull;
import org.firstinspires.ftc.teamcode.Robot.Robot;

import java.util.concurrent.ScheduledFuture;

final class Rotation extends RobotComposableStateSource {
    private final ScheduledFuture<?> rotation;

    Rotation(@NonNull Robot robot, double degrees) {
        super(robot);
        rotation = robot.wheels.rotateFor(degrees);
    }

    @Override
    public boolean update() {
        return rotation.isDone();
    }
}
