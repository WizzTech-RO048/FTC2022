package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.Robot.Robot;
import org.firstinspires.ftc.teamcode.Robot.Utils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;

@TeleOp
public class MovementTeleOp extends OpMode {
    private Robot robot;
    private ScheduledFuture<?> lastMovement = null;

    private double movementMeters = 0, lastMovementModification = 0;
    private double lastStopTime = 0;

    @Override
    public void init() {
        robot = new Robot(hardwareMap, telemetry, Executors.newScheduledThreadPool(1));
    }

    @Override
    public void stop() {
        robot.wheels.stop();
    }

    @Override
    public void loop() {
        controlDistanceMovement();
        controlWheelsStop();
    }

    private void controlDistanceMovement() {
        if (Utils.inVicinity(lastMovementModification, time, 0.2)) {
            return;
        }

        if (gamepad1.dpad_up) {
            movementMeters += 0.1;
        } else if (gamepad1.dpad_down) {
            movementMeters -= 0.1;
        } else if (gamepad1.x) {
            movementMeters = 0;
        } else if (gamepad1.a && Utils.isDone(lastMovement)) {
            lastMovement = robot.wheels.moveFor(movementMeters, 1.0 / 3);
        }

        lastMovementModification = time;

        telemetry.addData("Meters to move", movementMeters);
    }

    private void controlWheelsStop() {
        if (Utils.inVicinity(lastStopTime, time, 0.2) || !gamepad1.b) {
            return;
        }

        robot.wheels.stop();
    }
}
