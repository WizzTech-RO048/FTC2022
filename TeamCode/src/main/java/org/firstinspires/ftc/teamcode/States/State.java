package org.firstinspires.ftc.teamcode.States;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.ComputerVision.BarcodeDetector;
import org.firstinspires.ftc.teamcode.ComputerVision.StartPosition;
import org.firstinspires.ftc.teamcode.Robot.Robot;

import java.util.concurrent.Executors;

public abstract class State {
    protected final Robot robot;
    /** The current time, in seconds. */
    protected double time = 0;
    /** The time this state was initialized, in seconds. */
    protected double startTime = 0;
    /** Seconds passed since this state was initialized. */
    protected double timePassed = 0;

    State(@NonNull Robot robot) {
        this.robot = robot;
    }

    public void setTime(double time) {
        this.time = time;
        if (startTime == 0) {
            startTime = time;
        }
        timePassed = time - startTime;
    }

    public abstract State update();

    public void stop() {
        robot.stop();
        robot.camera.stop();
    }

    public static State initial(HardwareMap hardwareMap, Telemetry telemetry, @Nullable BarcodeDetector.Position mockPosition, @Nullable StartPosition.Position startPosition) {
        return new StateInitial(hardwareMap, telemetry, Executors.newScheduledThreadPool(1), mockPosition, startPosition);
    }
}