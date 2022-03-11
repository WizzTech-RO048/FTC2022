/*
* Aceasta autonomie a fost dezvoltata in cazul in care robotul este menit sa se miste doar cateva
* */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.Robot.Robot;
import org.firstinspires.ftc.teamcode.Robot.Wheels;

@Autonomous(name = "TEST")
public class AutonomyTest extends LinearOpMode{

    Robot robot;

    @Override
    public void runOpMode() throws InterruptedException{

        waitForStart();

        while(opModeIsActive()){
            robot.wheels.move(0, 1, 0);
            Thread.sleep(1);
            robot.stop();
        }
    }
}