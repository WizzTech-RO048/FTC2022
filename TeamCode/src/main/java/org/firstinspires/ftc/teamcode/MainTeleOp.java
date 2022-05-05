
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Robot.*;

import java.util.concurrent.*;

@TeleOp(name = "MainTeleOp")
public class MainTeleOp extends OpMode {
    private Robot robot;
    private Controller controller1;
    private Controller controller2;

    private ScheduledFuture<?> lastArmRaised = null;

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
        double r = -gamepad1.right_stick_y;

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

        if (Utils.isDone(lastRotation)) {
            if (isZero(x) && isZero(y) && isZero(r)) {
                robot.stop();
            } else {
                robot.wheels.move(x, y, r);
            }
        }

        controlArm();
//        controlRotation();


        // TODO: add telemetry
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

        if (controller1.dpadLeftOnce()) {
            dpadLeftPressed++;
            if (dpadLeftPressed % 2 == 0) {
                robot.duckServoOn();
            } else {
                robot.duckServoOff();
            }
        }
    }

    private void controlArm() {
        if (!Utils.isDone(lastArmRaised)) {
            return;
        }

        Arm.Position position;
        if (controller1.AOnce()) {
            position = null;
        } else if (controller1.XOnce()) {
            position = Arm.Position.BASE;
        } else if (controller1.BOnce()) {
            position = Arm.Position.MID;
        } else if (controller1.YOnce()) {
            position = Arm.Position.TOP;
        } else {
            return;
        }

        lastArmRaised = robot.arm.raise(position);
    }

    private double rotationDegrees = 0, lastRotationDegreesModiticationTime = 0;
    private ScheduledFuture<?> lastRotation =  null;

    private void controlRotation() {
        if (!Utils.isDone(lastRotation) || Utils.inVicinity(lastRotationDegreesModiticationTime, time, 0.2)) {
            return;
        }

        if (gamepad1.dpad_right) {
            rotationDegrees += 10;
            lastRotationDegreesModiticationTime = time;
        } else if (gamepad1.dpad_left) {
            rotationDegrees -= 10;
            lastRotationDegreesModiticationTime = time;
        }

        telemetry.addData("Rotation", rotationDegrees);
        telemetry.update();

        if (!gamepad1.left_bumper) {
            return;
        }

        telemetry.addLine("Rotating...");
        telemetry.update();

        lastRotation = robot.wheels.rotateFor(rotationDegrees);
    }

    private static boolean isZero(double value) {
        return Utils.inVicinity(value, 0, 0.01);
    }
}