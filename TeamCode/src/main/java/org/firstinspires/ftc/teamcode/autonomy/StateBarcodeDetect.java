package org.firstinspires.ftc.teamcode.autonomy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import org.firstinspires.ftc.teamcode.ComputerVision.BarcodeDetector;
import org.firstinspires.ftc.teamcode.Robot.Arm;
import org.firstinspires.ftc.teamcode.Robot.Robot;
import org.firstinspires.ftc.teamcode.Robot.Wheels;
import org.firstinspires.ftc.teamcode.state.State;
import org.opencv.core.Mat;

final class StateBarcodeDetect extends RobotState {
    private final BarcodeDetector.Position mockPosition;

    StateBarcodeDetect(@NonNull Robot robot, @Nullable BarcodeDetector.Position mockPosition) {
        super(robot);

        this.mockPosition = mockPosition;
    }

    @Override
    public State update() {
        if (mockPosition != null) {
            return nextState(mockPosition);
        }

        Mat image = new Mat();
        if (robot.camera.getImage(image)) {
            return nextState(BarcodeDetector.detect(image));
        }

        return new StateException(robot, new Exception("Failed to get image from camera"));
    }

    private State nextState(BarcodeDetector.Position position) {
        switch (position) {
            case LEFT:
            case MIDDLE:
            case RIGHT:
                return State.compose(
                        () -> new Movement(robot, 0.25, 0.3, Wheels.MoveDirection.BACKWARD),
                        () -> new Movement(robot, 0.7, 0.4, Wheels.MoveDirection.LEFT),
                        () -> new ObjectDrop(robot, Arm.Position.TOP),
                        () -> new Rotation(robot, 180)
                ).build(() -> new StateActionCarousel(robot));
            default:
                return new StateException(robot, new RuntimeException("No barcode position detected"));
        }
    }
}
