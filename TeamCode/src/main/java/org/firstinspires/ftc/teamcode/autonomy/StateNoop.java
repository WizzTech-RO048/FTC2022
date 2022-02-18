package org.firstinspires.ftc.teamcode.autonomy;

import androidx.annotation.NonNull;
import org.firstinspires.ftc.teamcode.Robot.Robot;

class StateNoop extends State {
    StateNoop(@NonNull Robot robot) {
        super(robot);
    }

    @Override
    public State update() {
        return this;
    }
}
