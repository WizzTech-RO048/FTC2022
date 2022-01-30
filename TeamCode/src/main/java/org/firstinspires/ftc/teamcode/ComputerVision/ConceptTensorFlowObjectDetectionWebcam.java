// Documentation of the code:
// https://github.com/FIRST-Tech-Challenge/FtcRobotController/wiki/Java-Sample-Op-Mode-for-TensorFlow-Object-Detection

package org.firstinspires.ftc.teamcode.ComputerVision;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import java.util.List;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;

@TeleOp(name = "Concept: TensorFlow Object Detection Webcam", group = "Concept")
// @Disabled
public class ConceptTensorFlowObjectDetectionWebcam extends LinearOpMode {
	/* Note: This sample uses the all-objects Tensor Flow model (FreightFrenzy_BCDM.tflite), which contains
	 * the following 4 detectable objects
	 *  0: Ball,
	 *  1: Cube,
	 *  2: Duck,
	 *  3: Marker (duck location tape marker)
	 *
	 *  Two additional model assets are available which only contain a subset of the objects:
	 *  FreightFrenzy_BC.tflite  0: Ball,  1: Cube
	 *  FreightFrenzy_DM.tflite  0: Duck,  1: Marker
	 */
	private static final String TFOD_MODEL_ASSET = "FreightFrenzy_BCDM.tflite";
	private static final String[] LABELS = {
			"Ball",
			"Cube",
			"Duck",
			"Marker"
	};

	/*
	 * IMPORTANT: You need to obtain your own license key to use Vuforia. The string below with which
	 * 'parameters.vuforiaLicenseKey' is initialized is for illustration only, and will not function.
	 * A Vuforia 'Development' license key, can be obtained free of charge from the Vuforia developer
	 * web site at https://developer.vuforia.com/license-manager.
	 *
	 * Vuforia license keys are always 380 characters long, and look as if they contain mostly
	 * random data. As an example, here is a example of a fragment of a valid key:
	 *      ... yIgIzTqZ4mWjk9wd3cZO9T1axEqzuhxoGlfOOI2dRzKS4T0hQ8kT ...
	 * Once you've obtained a license key, copy the string from the Vuforia web site
	 * and paste it in to your code on the next line, between the double quotes.
	 */
	private static final String VUFORIA_KEY = "AZtkMg3/////AAABmZjYixNWcEfbuTq15tKicqpjjfFzqLRwe2V27jrNCzLtcnULWa9S85kD50soeUiClWOFutoGkVSFfth+G8Rm+03s2jIs+nUUR4NZPJdiQEwruEdNQGnHNWO4YmYXG7tYxh0TGQjtQe1uI5FnlWLxT8bFuUpw900HxPdYWj6itBoh4ZpJHAT0XfwvwrqZ71vs6L09VLTL+GSU7v+hRFDiaYXMZP0D8vUMhzhH/b4PDhA8P33HAyObujviT0pPEWGXJ8R00WD08xyKoJrzPJByMRWCdVFVDS+1rcAwEMo4NU0PeGxXiwiOCIv2gASIDAyeRbEYk2FhgbB8i7wlHHosAp3o6KWZ7sw1/Fg7//FJ/a0f";

	/**
	 * {@link #vuforia} is the variable we will use to store our instance of the Vuforia
	 * localization engine.
	 */
	private VuforiaLocalizer vuforia;

	/**
	 * {@link #tfod} is the variable we will use to store our instance of the TensorFlow Object
	 * Detection engine.
	 */
	private TFObjectDetector tfod;

	@Override
	public void runOpMode() {
		// The TFObjectDetector uses the camera frames from the VuforiaLocalizer, so we create that
		// first.
		initVuforia();
		initTfod(); // TFOD = TensorFlow Object Detector

		/**
		 * Activate TensorFlow Object Detection before we wait for the start command.
		 * Do it here so that the Camera Stream window will have the TensorFlow annotations visible.
		 **/
		if (tfod != null) {
			tfod.activate();

			// The TensorFlow software will scale the input images from the camera to a lower resolution.
			// This can result in lower detection accuracy at longer distances (> 55cm or 22").
			// If your target is at distance greater than 50 cm (20") you can adjust the magnification value
			// to artificially zoom in to the center of image.  For best results, the "aspectRatio" argument
			// should be set to the value of the images used to create the TensorFlow Object Detection model
			// (typically 16/9).
			tfod.setZoom(2.5, 16.0/9.0);
		}

		/** Wait for the game to begin */
		telemetry.addData(">", "Press Play to start op mode");
		telemetry.update();
		waitForStart();

		if (opModeIsActive()) {
			while (opModeIsActive()) {
				if (tfod != null) {
					// getUpdatedRecognitions() will return null if no new information is available since
					// the last time that call was made.
					List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
					if (updatedRecognitions != null) {
						telemetry.addData("# Object Detected", updatedRecognitions.size());
						// step through the list of recognitions and display boundary info.
						int i = 0;
						for (Recognition recognition : updatedRecognitions) {
							telemetry.addData(String.format("label (%d)", i), recognition.getLabel());
							telemetry.addData(String.format("  left,top (%d)", i), "%.03f , %.03f",
									recognition.getLeft(), recognition.getTop());
							telemetry.addData(String.format("  right,bottom (%d)", i), "%.03f , %.03f",
									recognition.getRight(), recognition.getBottom());
							i++;
						}
						telemetry.update();
					}
				}
			}
		}
	}

	/**
	 * Initialize the Vuforia localization engine.
	 */
	private void initVuforia() {
		/*
		 * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
		 */
		VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

		parameters.vuforiaLicenseKey = VUFORIA_KEY;
		parameters.cameraName = hardwareMap.get(WebcamName.class, "Webcam 1");

		//  Instantiate the Vuforia engine
		vuforia = ClassFactory.getInstance().createVuforia(parameters);

		// Loading trackables is not necessary for the TensorFlow Object Detection engine.
	}

	/**
	 * Initialize the TensorFlow Object Detection engine.
	 */
	private void initTfod() {
		int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
				"tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
		TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
		tfodParameters.minResultConfidence = 0.8f;
		tfodParameters.isModelTensorFlow2 = true;
		tfodParameters.inputSize = 320;
		tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
		tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABELS);
	}
}