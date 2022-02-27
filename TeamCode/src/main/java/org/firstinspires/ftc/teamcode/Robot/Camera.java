package org.firstinspires.ftc.teamcode.Robot;

import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.opencv.core.Mat;

public class Camera {
    private final Telemetry telemetry;

    Camera(HardwareMap hardwareMap, Telemetry telemetry) {
        this.telemetry = telemetry;
    }

    public boolean getImage(Mat mat) {
        return false;
    }

    public void stop() {}
}
