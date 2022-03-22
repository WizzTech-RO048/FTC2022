package org.firstinspires.ftc.teamcode.States;

import androidx.annotation.NonNull;
import org.firstinspires.ftc.teamcode.Robot.Robot;
import org.firstinspires.ftc.teamcode.States.State;

public class StateException extends State {
    public StateException(@NonNull Robot robot, Exception e) {
        super(robot);
        robot.getTelemetry().addData("Unhandled exception", e);
    }

    @Override
    public State update() {
        return this;
    }
}