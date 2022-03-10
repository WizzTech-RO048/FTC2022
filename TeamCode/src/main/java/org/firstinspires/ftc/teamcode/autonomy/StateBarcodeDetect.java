package org.firstinspires.ftc.teamcode.autonomy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
//import org.firstinspires.ftc.teamcode.ComputerVision.BarcodeDetector;
import org.firstinspires.ftc.teamcode.ComputerVision.DetectieBarcode;
import org.firstinspires.ftc.teamcode.Robot.Arm;
import org.firstinspires.ftc.teamcode.Robot.Camera;
import org.firstinspires.ftc.teamcode.Robot.Robot;
import org.firstinspires.ftc.teamcode.Robot.Wheels;
import org.firstinspires.ftc.teamcode.state.State;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvPipeline;
import org.openftc.easyopencv.OpenCvWebcam;
import org.opencv.core.Mat;

final class StateBarcodeDetect extends RobotState {
    int pozitie_detectata=0;


    StateBarcodeDetect(@NonNull Robot robot) {
        super(robot);

    }

    @Override
    public State update(){

        if (pozitie_detectata != 0) {
            return nextState(pozitie_detectata);
        }

        robot.camera.applyPipeline(new DetectieBarcode.PipelineProcesare());

        return new StateException(robot, new Exception("eroare in StateBarcodeDetect, update()"));
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
