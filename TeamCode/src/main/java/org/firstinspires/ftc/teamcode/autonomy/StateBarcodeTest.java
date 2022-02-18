package org.firstinspires.ftc.teamcode.autonomy;

import androidx.annotation.NonNull;
import org.firstinspires.ftc.teamcode.ComputerVision.BarcodeDetector;
import org.firstinspires.ftc.teamcode.Robot.Robot;

class StateBarcodeTest extends State {
    StateBarcodeTest(@NonNull Robot robot, BarcodeDetector.Position detectedPosition) {
        super(robot);
        robot.getTelemetry().addData("Detected barcode position", detectedPosition).setRetained(true);
    }

    @Override
    public State update() {
        if (timePassed >= 5) {
            return new StateBarcodeDetect(robot);
        }

        return this;
    }
}
