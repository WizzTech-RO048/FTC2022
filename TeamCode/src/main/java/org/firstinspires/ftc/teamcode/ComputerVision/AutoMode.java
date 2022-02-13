package org.firstinspires.ftc.teamcode.ComputerVision;

import org.firstinspires.ftc.teamcode.Robot.*;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;

@Autonomous(name="Skystone Detecotor", group="Auto")
public class AutoMode extends LinearOpMode {
	private OpenCvCamera webcam;

	@Override
	public void runOpMode() throws InterruptedException {

		SkystoneDetector detector = new SkystoneDetector(telemetry);

		// OpenCV webcam
		int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
		webcam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
		//OpenCV Pipeline

		webcam.setPipeline(detector);

		// Webcam Streaming
		webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
			@Override
			public void onOpened() {
				webcam.startStreaming(1920, 1080, OpenCvCameraRotation.UPRIGHT);
			}

			@Override
			public void onError(int errorCode) {

			}
		});

		waitForStart();
		switch (detector.getLocation()) {
			case 2:
				// ...
				break;
			case 1:
				// ...
				break;
			case 0:
				// ...
		}

	}
}