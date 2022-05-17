package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Robot.*;

import java.util.concurrent.*;

@TeleOp(name="MainTeleOp")
public class MainTeleOp extends OpMode {
    private Robot robot;
    private Controller controller1;
    private Controller controller2;
    private Utils utils;

    private ScheduledFuture<?> lastRotation = null, lastArmRaised = null, lastThrow = null;

    private double targetPosition;

    private int rbPressed = 0;
    private int dpadLeftPressed = 0;
    private int dpadRightPressed = 0;

    boolean turbo = false;

    @Override
    public void init() {
        robot = new Robot(hardwareMap, telemetry, Executors.newScheduledThreadPool(1));

        controller1 = new Controller(gamepad1);
        controller2 = new Controller(gamepad2);

        robot.arm.retract();
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

        double x = -gamepad1.left_stick_x;
        double y = -gamepad1.right_stick_x;
        double r = -gamepad1.left_stick_y;

		if(x >= 0.75){
			x = 0.75;
		} else if(y >= 0.75){
			y = 0.75;
		} else if(r >= 0.75){
			r = 0.75;
		}

        // enabling the boost
        boolean leftBumber = controller1.leftBumber();
        if(leftBumber){
            x = 1.0;
            y = 1.0;
            r = 1.0;
        }

        if (Utils.isDone(lastRotation)) {
            if (isZero(x) && isZero(y) && isZero(r)) {
                robot.wheels.stop();
            } else {
                robot.wheels.move(x, y, r);
            }
        }

        // controlling the intake system
        if (controller1.leftTrigger == 0.0) { robot.intake(controller1.rightTrigger); }
        if (controller1.rightTrigger == 0.0) { robot.intake(-controller1.leftTrigger); }

        // controlling the duck servo
        if (controller1.dpadLeftOnce()) {
            dpadLeftPressed++;
            if (dpadLeftPressed % 2 == 0) {
                robot.duckServoRed();
            } else {
                robot.duckServoOff();
            }
        }

        if (controller1.dpadRightOnce()) {
            dpadRightPressed++;
            if (dpadRightPressed % 2 == 0) {
                robot.duckServoBlue();
            } else {
                robot.duckServoOff();
            }
        }

        // controlling the arm
        if (controller1.AOnce()) { targetPosition = 0.0; }
        if (controller1.XOnce()) { targetPosition = 0.1; }
        if (controller1.BOnce()) { targetPosition = 0.35; }
        if (controller1.YOnce()) { targetPosition = 0.7; }
        controlArm();

        // rotating the arm servo motor
        if (controller1.rightBumberOnce()) {
            rbPressed++;
            if (rbPressed % 2 == 1) {
                robot.arm.throwObject();
            } else {
                robot.arm.retract();
            }
        }

        // emergency stop button
        if(controller1.startOnce()){ stop(); }
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

    private static boolean isZero(double value) {
        return Utils.inVicinity(value, 0, 0.01);
    }
}