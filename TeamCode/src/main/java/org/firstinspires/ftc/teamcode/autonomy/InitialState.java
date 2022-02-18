package org.firstinspires.ftc.teamcode.autonomy;

import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Robot.Robot;

import java.util.concurrent.ScheduledExecutorService;

class InitialState extends State {
    InitialState(HardwareMap hardwareMap, Telemetry telemetry, ScheduledExecutorService service) {
        super(new Robot(hardwareMap, telemetry, service));
        telemetry.addLine("Initialized initial state");
    }

    @Override
    public State update() {
        return new BarcodeDetectionState(robot);
        // return new MoveToShippingState(robot);
    }
}
