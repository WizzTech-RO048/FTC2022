package org.firstinspires.ftc.teamcode.autonomy;

import androidx.annotation.NonNull;
import org.firstinspires.ftc.teamcode.ComputerVision.DetectieBarcode;
import org.firstinspires.ftc.teamcode.Robot.Arm;
import org.firstinspires.ftc.teamcode.Robot.Camera;
import org.firstinspires.ftc.teamcode.Robot.Robot;
import org.firstinspires.ftc.teamcode.Robot.Wheels;
import org.firstinspires.ftc.teamcode.state.State;


final class StateBarcodeDetect extends RobotState {

    DetectieBarcode detector=new DetectieBarcode(robot.getTelemetry());
    int pozitie_detectata=detector.getLocation();

    StateBarcodeDetect(@NonNull Robot robot) {
        super(robot);
    }

    @Override
    public State update(){
        pozitie_detectata=detector.getLocation();

        if (pozitie_detectata != 0) {
            return nextStateDebug(pozitie_detectata);
        }

        robot.camera.applyPipeline(detector);

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


    private State nextStateDebug(int pos){
        switch (pos){
            case 1:
                robot.getTelemetry().addLine("Detected pos=1");
                robot.getTelemetry().update();
            case 2:
                robot.getTelemetry().addLine("Detected pos=2");
                robot.getTelemetry().update();
            case 3:
                robot.getTelemetry().addLine("Detected pos=3");
                robot.getTelemetry().update();
            default:
                return new StateException(robot, new RuntimeException("Nu a fost detectata o pozitie (pos=0)"));
        }
    }
}
