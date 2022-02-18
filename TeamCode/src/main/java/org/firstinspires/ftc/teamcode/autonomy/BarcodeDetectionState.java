package org.firstinspires.ftc.teamcode.autonomy;

import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import org.firstinspires.ftc.teamcode.ComputerVision.BarcodeDetector;
import org.firstinspires.ftc.teamcode.Robot.Robot;

import java.util.concurrent.Future;

class BarcodeDetectionState extends State {
    private Future<Bitmap> image;

    BarcodeDetectionState(@NonNull Robot robot) {
        super(robot);
        robot.getTelemetry().addLine("Initialized barcode detection state");
    }

    @Override
    public State update() {
        if (image == null) {
            robot.getTelemetry().addLine("No image is being processed, retrieving new frame...");
            image = robot.camera.getImage();

            return this;
        }

        if (!image.isDone()) {
            return this;
        }

        try {
            robot.getTelemetry().addLine("Retrieved frame, detecting barcode...");

            return new BarcodeTestState(robot, BarcodeDetector.detect(image.get(), robot.getTelemetry()));
        } catch (Exception e) {
            robot.getTelemetry().addData("Unhandled exception", e);
            return new NoopState(robot);
        }
    }

    @Override
    public void stop() {
        super.stop();
        image.cancel(true);
    }
}
