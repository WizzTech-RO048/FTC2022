package org.firstinspires.ftc.teamcode.autonomy;

import androidx.annotation.NonNull;
import org.firstinspires.ftc.teamcode.Robot.*;
import org.firstinspires.ftc.teamcode.state.State;

import java.util.concurrent.ScheduledFuture;

final class ObjectDrop extends RobotComposableStateSource {
    private final ScheduledFuture<?> raise;

    private double throwStartTime = 0;

    ObjectDrop(@NonNull Robot robot, Arm.Position position) {
        super(robot);
        raise = robot.arm.raise(position);
    }

    @Override
    public boolean update() {
        if (!raise.isDone()) {
            return false;
        }

        if (throwStartTime == 0.0) {
            robot.arm.throwObjectFromBox();
            throwStartTime = time;
            return false;
        }

        if (time - throwStartTime < 1.5) {
            return false;
        }

        robot.arm.retractBox();
        robot.arm.raise(null);

        return true;
    }
}
