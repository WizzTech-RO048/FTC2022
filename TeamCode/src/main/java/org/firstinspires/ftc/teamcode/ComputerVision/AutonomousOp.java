/*
package org.firstinspires.ftc.teamcode.ComputerVision;

import org.firstinspires.ftc.teamcode.Robot.*;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

@Autonomous(name = "AutoOp")
public class AutonomousOp {

    private Robot robot;

    private Controller controller1;
    private Controller controller2;

    private static final String VUFORIA_KEY = " -- YOUR NEW VUFORIA KEY GOES HERE  --- ";

    @Override
    private void init(){
		robot = new Robot(hardwareMap, telemetry, Executors.newScheduledThreadPool(1));
    
        controller1 = new Controller(gamepad1);
        controller2 = new Controller(gamepad2);

        robot.throwServo.setPosition(initialThrowServerPos);
    }

    @Override
    private void stop(){

    }

    @Override
    private void loop(){

    }

}

*/