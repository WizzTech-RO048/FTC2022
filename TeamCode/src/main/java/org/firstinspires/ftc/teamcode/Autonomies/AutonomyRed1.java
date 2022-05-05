/*
 * The tasks that need to be done for the RED 1 scenario:
 *   -> move forward
 *   -> move right
 *   -> rotate 180
 *   -> throw object
 *   -> rotate 180
 *   -> move left
 *   -> move backward
 *   -> rotate carousel wheel
 *   -> move forward
 * */

package org.firstinspires.ftc.teamcode.Autonomies;

import com.qualcomm.robotcore.eventloop.opmode.*;
import org.firstinspires.ftc.teamcode.ComputerVision.BarcodeDetector;
import org.firstinspires.ftc.teamcode.ComputerVision.StartPosition;
import org.firstinspires.ftc.teamcode.States.State;

@Autonomous(name = "Red 1")
//@Disabled
public class AutonomyRed1 extends OpMode {
    private State state;

    @Override
    public void init() {
        state = State.initial(hardwareMap, telemetry, BarcodeDetector.Position.RIGHT, StartPosition.Position.RED1);
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