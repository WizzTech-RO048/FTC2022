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
    int pozitie_detectata=0;

    StateBarcodeDetect(@NonNull Robot robot) {
        super(robot);

    }

    @Override
    public State update() {

    //    if (mockPosition != null) {
    //        return nextState(mockPosition);
    //    }

     //   Mat image = new Mat();
     //   if (robot.camera.getImage(image)) {
     //       return nextState(BarcodeDetector.detect(image));
     //   }

        return new StateException(robot, new Exception("error"));
    }

    private State nextState(int pos) {
        switch (pos) {
            case 1:
            case 2:
            case 3:
                return State.compose(
                        () -> new Movement(robot, 0.25, 0.3, Wheels.MoveDirection.BACKWARD),
                        () -> new Movement(robot, 0.7, 0.4, Wheels.MoveDirection.LEFT),
                        () -> new ObjectDrop(robot, Arm.Position.TOP),
                        () -> new Rotation(robot, 180)
                ).build(() -> new StateActionCarousel(robot));
            default:
                return new StateException(robot, new RuntimeException("!Nu a fost detectata o pozitie"));
        }
    }
}
