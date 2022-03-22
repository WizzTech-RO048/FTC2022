package org.firstinspires.ftc.teamcode.States;

import androidx.annotation.Nullable;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.ComputerVision.BarcodeDetector;
import org.firstinspires.ftc.teamcode.ComputerVision.StartPosition;
import org.firstinspires.ftc.teamcode.Robot.Arm;
import org.firstinspires.ftc.teamcode.Robot.Robot;
import org.firstinspires.ftc.teamcode.States.PositionsStates.*;

import java.util.Objects;
import java.util.concurrent.ScheduledExecutorService;

class StateInitial extends State {
    private final BarcodeDetector.Position mockPosition;
    private final StartPosition.Position startPosition;
    Arm.Position armPosition;

    StateInitial(HardwareMap hardwareMap, Telemetry telemetry, ScheduledExecutorService service, @Nullable BarcodeDetector.Position mockedPosition, @Nullable StartPosition.Position startedPosition) {
        super(new Robot(hardwareMap, telemetry, service));
        mockPosition = mockedPosition;
        startPosition = startedPosition;
    }

    @Override
    public State update() {
//		return new StateBarcodeDetect(robot, mockPosition);

        switch(mockPosition){
            case LEFT:
                armPosition = Arm.Position.BASE;
            case RIGHT:
                armPosition = Arm.Position.TOP;
            case MIDDLE:
                armPosition = Arm.Position.MID;
        }

        switch(Objects.requireNonNull(startPosition)){
            case RED1:
                return new StateR1(robot, armPosition);

            case RED2:
                return new StateR2(robot, armPosition);

            case BLUE1:
                return new StateB1(robot, armPosition);

            case BLUE2:
                return new StateB2(robot, armPosition);
        }

        return this;
    }
}