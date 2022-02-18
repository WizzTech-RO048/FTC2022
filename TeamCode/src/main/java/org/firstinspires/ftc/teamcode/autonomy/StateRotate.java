package org.firstinspires.ftc.teamcode.autonomy;

import androidx.annotation.NonNull;
import org.firstinspires.ftc.teamcode.Robot.Robot;

import java.util.concurrent.ScheduledFuture;

class StateRotate extends State {
    private final ScheduledFuture<?> rotation;
    private final State previousState;

    StateRotate(@NonNull Robot robot, double degrees, @NonNull State previous) {
        super(robot);
        rotation = robot.wheels.rotateFor(degrees);
        previousState = previous;
    }

    @Override
    public State update() {
        if (rotation.isDone()) {
            return previousState;
        }

        return this;
    }

    @Override
    public void stop() {
        rotation.cancel(true);
        super.stop();
    }
}
