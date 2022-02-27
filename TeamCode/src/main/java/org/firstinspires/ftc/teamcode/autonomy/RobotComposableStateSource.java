package org.firstinspires.ftc.teamcode.autonomy;

import androidx.annotation.NonNull;
import org.firstinspires.ftc.teamcode.Robot.Robot;
import org.firstinspires.ftc.teamcode.state.ComposableStateSource;

abstract class RobotComposableStateSource extends ComposableStateSource {
    protected final Robot robot;

    RobotComposableStateSource(@NonNull Robot robot) {
        this.robot = robot;
    }

    @Override
    public void stop() {
        robot.stop();
    }
}
