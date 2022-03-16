package org.firstinspires.ftc.teamcode.autonomy;

import androidx.annotation.Nullable;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.ComputerVision.BarcodeDetector;
import org.firstinspires.ftc.teamcode.ComputerVision.StartPosition;
import org.firstinspires.ftc.teamcode.Robot.Robot;

import java.util.concurrent.ScheduledExecutorService;

class StateInitial extends State {
    private final BarcodeDetector.Position mockPosition;
    private final StartPosition.Position startPosition;

    StateInitial(HardwareMap hardwareMap, Telemetry telemetry, ScheduledExecutorService service, @Nullable BarcodeDetector.Position mockedPosition, @Nullable StartPosition.Position startedPosition) {
        super(new Robot(hardwareMap, telemetry, service));
        mockPosition = mockedPosition;
        startPosition = startedPosition;
    }

    @Override
    public State update() {
//		return new StateBarcodeDetect(robot, mockPosition);

        if(startPosition == StartPosition.Position.RED1){

        }
        if(startPosition == StartPosition.Position.RED2){

        }
        if(startPosition == StartPosition.Position.BLUE1){

        }
        if(startPosition == StartPosition.Position.BLUE2){
            
        }

        return new StateBarcodeDetectedRight(robot);
    }
}