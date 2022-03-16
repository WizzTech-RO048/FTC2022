package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.*;
import org.firstinspires.ftc.teamcode.ComputerVision.BarcodeDetector;
import org.firstinspires.ftc.teamcode.ComputerVision.StartPosition;
import org.firstinspires.ftc.teamcode.States.State;

@Autonomous(name = "Default Autonomy (R1)")
@Disabled
public class Autonomy extends OpMode {
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