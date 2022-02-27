package org.firstinspires.ftc.teamcode.autonomy;

import androidx.annotation.NonNull;
import org.firstinspires.ftc.teamcode.Robot.Robot;
import org.firstinspires.ftc.teamcode.state.State;

abstract class RobotState extends State {
    protected final Robot robot;

    RobotState(@NonNull Robot robot) {
        this.robot = robot;
    }

    public abstract State update();

    @Override
    public void stop() {
        robot.stop();
    }
}
