package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.*;
import org.firstinspires.ftc.teamcode.ComputerVision.BarcodeDetector;
import org.firstinspires.ftc.teamcode.ComputerVision.StartPosition;
import org.firstinspires.ftc.teamcode.States.State;
import org.opencv.core.Mat;

@Autonomous(name = "Default Autonomy (R1)")
@Disabled
public class Autonomy extends OpMode {
    private State state;
    private BarcodeDetector.Position detectedPosition;
    private Mat firstFrame = new Mat();

    @Override
    public void init() {
        // TODO: take a initial picture of the markers
        detectedPosition = BarcodeDetector.detect(firstFrame); // this is a little hack to not bring errors
        state = State.initial(hardwareMap, telemetry, detectedPosition, StartPosition.Position.RED1);
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