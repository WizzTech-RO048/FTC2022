package org.firstinspires.ftc.teamcode.autonomy;

import androidx.annotation.NonNull;
import org.firstinspires.ftc.teamcode.Robot.Robot;
import org.firstinspires.ftc.teamcode.state.State;

final class StateException extends State {
    StateException(@NonNull Robot robot, Exception e) {
        robot.getTelemetry().addData("Unhandled exception", e);
    }

    @Override
    public State update() {
        return this;
    }
}
