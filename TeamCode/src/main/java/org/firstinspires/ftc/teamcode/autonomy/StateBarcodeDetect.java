package org.firstinspires.ftc.teamcode.autonomy;

import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import org.firstinspires.ftc.teamcode.ComputerVision.BarcodeDetector;
import org.firstinspires.ftc.teamcode.Robot.Robot;

import java.util.concurrent.Future;

class StateBarcodeDetect extends State {
    private final Future<Bitmap> image;
    private final BarcodeDetector.Position mockPosition;

    StateBarcodeDetect(@NonNull Robot robot, @Nullable BarcodeDetector.Position mockedPosition) {
        super(robot);

        if (mockedPosition != null) {
            mockPosition = mockedPosition;
            image = null;
        } else {
            mockPosition = null;
            image = robot.camera.getImage();
        }
    }

    @Override
    public State update() {
        if (mockPosition != null) {
            return nextState(mockPosition);
        }

        if (!image.isDone()) {
            return this;
        }

        try {
            return nextState(BarcodeDetector.detect(image.get()));
        } catch (Exception e) {
            return new StateException(robot, e);
        }
    }

    private State nextState(BarcodeDetector.Position position) {
        switch (position) {
            case LEFT:
            case MIDDLE:
            case RIGHT:
            default:
                return new StateException(robot, new RuntimeException("No barcode position detected"));
        }
    }

    @Override
    public void stop() {
        super.stop();
        image.cancel(true);
    }
}
