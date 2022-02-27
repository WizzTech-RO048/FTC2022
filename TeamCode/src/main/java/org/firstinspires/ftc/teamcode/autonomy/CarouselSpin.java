package org.firstinspires.ftc.teamcode.autonomy;

import androidx.annotation.NonNull;
import org.firstinspires.ftc.teamcode.Robot.Robot;

final class CarouselSpin extends RobotComposableStateSource {
    CarouselSpin(@NonNull Robot robot) {
        super(robot);
        robot.duckServoOn();
    }

    @Override
    public boolean update() {
        if (timePassed < 5) {
            return false;
        }

        robot.duckServoOff();

        return true;
    }
}
