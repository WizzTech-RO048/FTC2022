package org.firstinspires.ftc.teamcode.autonomy;

import androidx.annotation.NonNull;
import org.firstinspires.ftc.teamcode.Robot.Robot;

public class NoopState extends State {
    NoopState(@NonNull Robot robot) {
        super(robot);
    }

    @Override
    public State update() {
        return this;
    }
}
