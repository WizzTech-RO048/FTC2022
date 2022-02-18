
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Robot.*;

import java.util.concurrent.*;

@TeleOp
public class MainTeleOp extends OpMode {
    private Robot robot;
    private Controller controller1;
    private Controller controller2;

    private ScheduledFuture<?> lastRotation = null, lastArmRaised = null, lastThrow = null;

    private double targetPosition;

    private int rbPressed = 0;
    private int dpadLeftPressed = 0;

    boolean turbo = false;

    @Override
    public void init() {
        robot = new Robot(hardwareMap, telemetry, Executors.newScheduledThreadPool(1));

        controller1 = new Controller(gamepad1);
        controller2 = new Controller(gamepad2);

        robot.arm.retractBox();
    }

    @Override
    public void stop() {
        robot.wheels.stop();
        robot.arm.stop();
        if (lastArmRaised != null) {
            lastArmRaised.cancel(true);
        }
        if (lastThrow != null) {
            lastThrow.cancel(true);
        }

        turbo = false;

        robot.duckServoOff();
        robot.stopIntake();
    }

    @Override
    public void loop() {
        controller1.update();
        controller2.update();

        double y = gamepad1.left_stick_x;
        double x = -gamepad1.right_stick_x;
        double r = -gamepad1.left_stick_y;

        if (x >= 0.7) {
            x = 0.7;
        }
        if (y >= 0.7) {
            y = 0.7;
        }
        if (r >= 0.7) {
            r = 0.7;
        }

        double rightTrigger = controller1.rightTrigger;
        double leftTrigger = controller1.leftTrigger;
        boolean leftBumber = controller1.leftBumber();

        if (Utils.isDone(lastRotation)) {
            if (isZero(x) && isZero(y) && isZero(r)) {
                robot.wheels.stop();
            } else {
                robot.wheels.move(x, y, r);
            }
        }

        if (controller1.AOnce()) {
            targetPosition = 0.0;
        }
        if (controller1.XOnce()) {
            targetPosition = 0.1;
        }
        if (controller1.BOnce()) {
            targetPosition = 0.3;
        }
        if (controller1.YOnce()) {
            targetPosition = 0.6;
        }

        if (controller1.dpadUpOnce() && targetPosition <= 1.0) {
            targetPosition += 0.1;
        }
        if (controller1.dpadDownOnce() && targetPosition >= 0.0) {
            targetPosition -= 0.1;
        }

        lastArmRaised = robot.arm.moveArm(targetPosition);

        // rotating the throwing servo
        if (controller1.rightBumberOnce()) {
            rbPressed++;
            if (rbPressed % 2 == 1) {
                robot.arm.throwObjectFromBox();
            } else {
                robot.arm.retractBox();
            }
        }

        if (leftTrigger == 0.0) {
            robot.intake(rightTrigger);
        }
        if (rightTrigger == 0.0) {
            robot.intake(-leftTrigger);
        }

        if (controller1.startOnce()) {
            stop();
        } // emergency stop button

        if (controller1.dpadLeftOnce()) {
            dpadLeftPressed++;
            if (dpadLeftPressed % 2 == 0) {
                robot.duckServoOn();
            } else {
                robot.duckServoOff();
            }
        }
    }

    private static boolean isZero(double value) {
        return Utils.inVicinity(value, 0, 0.01);
    }
}