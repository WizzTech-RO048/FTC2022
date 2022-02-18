package org.firstinspires.ftc.teamcode.autonomy;

import androidx.annotation.NonNull;
import org.firstinspires.ftc.teamcode.Robot.*;

import java.util.concurrent.ScheduledFuture;

class StateMove extends State {
    private final ScheduledFuture<?> movement;
    private final State previousState;

    StateMove(@NonNull Robot robot, double meters, double power, Wheels.MoveDirection direction, State previous) {
        super(robot);
        movement = robot.wheels.moveFor(meters, power, direction);
        previousState = previous;
    }

    @Override
    public State update() {
        if (movement.isDone()) {
            return previousState;
        }

        return this;
    }

    @Override
    public void stop() {
        movement.cancel(true);
        super.stop();
    }
}
