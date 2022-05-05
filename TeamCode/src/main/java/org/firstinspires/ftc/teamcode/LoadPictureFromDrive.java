package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.ComputerVision.BarcodeDetector;
import org.firstinspires.ftc.teamcode.ComputerVision.StartPosition;
import org.firstinspires.ftc.teamcode.Robot.Robot;
import org.firstinspires.ftc.teamcode.States.State;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

@Autonomous(name = "Load Picture from Drive")
@Disabled
public class LoadPictureFromDrive extends OpMode {

	private State state;
	private BarcodeDetector.Position detectedPosition;
	private Robot robot;

	@Override
	public void init(){
		// --------- reading the image from storage of REV --------
//		String pathOfTheImage = "";
//		Mat firstFrame = Imgcodecs.imread(pathOfTheImage);

//		robot.camera.getImage();

	}

	@Override
	public void stop(){
//		state.setTime(time);
//		state.stop();
	}

	@Override
	public void loop(){
//		state.setTime(time);


	}

}
