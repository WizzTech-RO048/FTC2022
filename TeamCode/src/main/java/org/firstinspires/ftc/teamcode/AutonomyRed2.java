/*
* The tasks that need to be done for the RED 2 scenario:
*   -> move left
*   -> move forward
*   -> rotate 180
*   -> throw object
*   -> rotate 90 degrees counter clock wise
*   -> move right
*   -> move forward
* */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.*;
import org.firstinspires.ftc.teamcode.ComputerVision.BarcodeDetector;
import org.firstinspires.ftc.teamcode.ComputerVision.StartPosition;
import org.firstinspires.ftc.teamcode.autonomy.State;

@Autonomous(name = "Red 1")
@Disabled
public class AutonomyRed2 extends OpMode {
    private State state;

    @Override
    public void init() {
        state = State.initial(hardwareMap, telemetry, BarcodeDetector.Position.RIGHT, StartPosition.Position.RED2);
    }

    @Override
    public void stop() {
        state.setTime(time);
        state.stop();
    }

    @Override
    public void loop() {
        state.setTime(time);
        state = state.update();
    }
}