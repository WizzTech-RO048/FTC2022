package org.firstinspires.ftc.teamcode.autonomy;

import com.qualcomm.robotcore.eventloop.opmode.*;
import org.firstinspires.ftc.teamcode.ComputerVision.DetectieBarcode;
import org.firstinspires.ftc.teamcode.Robot.Robot;
import org.firstinspires.ftc.teamcode.state.State;

import java.util.concurrent.Executors;

@Autonomous(name = "BETA New Autonomy")
public class OpMode extends com.qualcomm.robotcore.eventloop.opmode.OpMode {
    private State state;

    @Override
    public void init() {
        state = new StateBarcodeDetect(new Robot(hardwareMap, telemetry, Executors.newScheduledThreadPool(1)));
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
