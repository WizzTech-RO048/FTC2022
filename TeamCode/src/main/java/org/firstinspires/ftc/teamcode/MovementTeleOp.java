package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.Robot.*;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;

import static org.firstinspires.ftc.teamcode.Robot.Wheels.MoveDirection.*;

@TeleOp
public class MovementTeleOp extends OpMode {
    private Robot robot;
    private ScheduledFuture<?> lastMovement = null;

    private double
            movementMeters = 0,
            movementPower = 0.1,
            lastMovementMetersModificationTime = 0,
            lastMovementPowerModificationTime = 0,
            lastMovementTime = 0;
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
        controlMovementMeters();
        controlMovementPower();

        telemetry.addData("Movement meters", movementMeters).addData("Movement power", movementPower);
        telemetry.update();

        controlMovement();
        controlWheelsStop();
    }

    private void controlMovementMeters() {
        if (wasRecentlyPressed(lastMovementMetersModificationTime)) {
            return;
        }

        lastMovementMetersModificationTime = time;
        if (gamepad1.right_bumper) {
            movementMeters += 0.1;
        } else if (gamepad1.left_bumper) {
            movementMeters -= 0.1;
        } else if (gamepad1.x) {
            movementMeters = 0;
        }
    }

    private void controlMovementPower() {
        if (wasRecentlyPressed(lastMovementPowerModificationTime)) {
            return;
        }

        double powerIncrement = gamepad1.left_stick_button ? -0.1 : gamepad1.right_stick_button ? 0.1 : 0;
        if (powerIncrement == 0) {
            return;
        }

        lastMovementPowerModificationTime = time;
        movementPower = Utils.clamp(movementPower + powerIncrement, -1, 1);
    }

    private void controlMovement() {
        if (wasRecentlyPressed(lastMovementTime) || !Utils.isDone(lastMovement) || movementMeters < 1e-3) {
            return;
        }

        Wheels.MoveDirection direction;
        if (gamepad1.dpad_right) {
            direction = RIGHT;
        } else if (gamepad1.dpad_left) {
            direction = LEFT;
        } else if (gamepad1.dpad_down) {
            direction = BACKWARD;
        } else if (gamepad1.dpad_up) {
            direction = FORWARD;
        } else {
            return;
        }

        lastMovementTime = time;
        // Cele mai precise viteze:
        // - laterale: 0.4
        // - fata/spate: 0.3
        lastMovement = robot.wheels.moveFor(movementMeters, movementPower, direction);
    }

    private void controlWheelsStop() {
        if (wasRecentlyPressed(lastStopTime) || !gamepad1.b) {
            return;
        }

        lastStopTime = time;
        robot.wheels.stop();
    }

    private boolean wasRecentlyPressed(double lastModificationTime) {
        return Utils.inVicinity(lastModificationTime, time, 0.2);
    }
}
