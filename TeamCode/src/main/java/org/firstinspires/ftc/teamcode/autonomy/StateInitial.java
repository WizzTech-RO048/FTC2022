package org.firstinspires.ftc.teamcode.autonomy;

import androidx.annotation.Nullable;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.ComputerVision.BarcodeDetector;
import org.firstinspires.ftc.teamcode.Robot.Robot;

import java.util.concurrent.ScheduledExecutorService;

class StateInitial extends State {
    private final BarcodeDetector.Position mockPosition;

    StateInitial(HardwareMap hardwareMap, Telemetry telemetry, ScheduledExecutorService service, @Nullable BarcodeDetector.Position mockedPosition) {
        super(new Robot(hardwareMap, telemetry, service));
        mockPosition = mockedPosition;
    }

    @Override
    public State update() {
        return new StateBarcodeDetect(robot, mockPosition);
    }
}
