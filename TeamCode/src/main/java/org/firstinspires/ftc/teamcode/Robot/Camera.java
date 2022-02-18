package org.firstinspires.ftc.teamcode.Robot;

import android.graphics.ImageFormat;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.android.util.Size;
import org.firstinspires.ftc.robotcore.external.hardware.camera.*;
import org.firstinspires.ftc.robotcore.internal.system.Deadline;
import org.opencv.core.Mat;

import java.util.concurrent.TimeUnit;

public class Camera {
    private final Telemetry telemetry;

    Camera(HardwareMap hardwareMap, Telemetry telemetry) {
        this.telemetry = telemetry;
    }

    public boolean getImage(Mat mat) {
        return false;
    }

    public void stop() {
//        camera.close();
    }
}
