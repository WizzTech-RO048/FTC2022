package org.firstinspires.ftc.teamcode.Robot;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.Telemetry;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import java.util.Objects;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.*;

public class Sensors {
	private ColorSensor colorSensor;

	private final Telemetry telemetry;
	private final ScheduledExecutorService scheduler;

	Sensors(@NonNull final Parameters parameters){
		colorSensor = Objects.requireNonNull(parameters.colorSensor, "Color sensor is not set");

		scheduler = Objects.requireNonNull(parameters.scheduler, "Scheduler is not set");
		telemetry = Objects.requireNonNull(parameters.telemetry, "Telemetry is not set");
	}

	// ------------------------
	// - Color sensor functions
	// ------------------------
	public void startColorSensor(){
		telemetry.addData("R", colorSensor.red());
		telemetry.addData("G", colorSensor.green());
		telemetry.addData("B", colorSensor.blue());
	}

	// TODO: scan if an object is on the robot


	static class Parameters{
		ColorSensor colorSensor;
		Telemetry telemetry;
		ScheduledExecutorService scheduler;
	}

}
