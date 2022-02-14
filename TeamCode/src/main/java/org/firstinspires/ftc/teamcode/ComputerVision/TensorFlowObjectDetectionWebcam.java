/* Copyright (c) 2019 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode.ComputerVision;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.opencv.core.Point;
import org.opencv.core.Rect;

import org.firstinspires.ftc.teamcode.Robot.*;

import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior;


/**
 * This 2020-2021 OpMode illustrates the basics of using the TensorFlow Object Detection API to
 * determine the position of the Freight Frenzy game elements.
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list.
 *
 * IMPORTANT: In order to use this OpMode, you need to obtain your own Vuforia license key as
 * is explained below.
 */
@TeleOp(name = "TensorFlow Object Detection Webcam", group = "Concept")
// @Disabled
public class TensorFlowObjectDetectionWebcam extends LinearOpMode {
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

	static final Rect LEFT_ROI = new Rect(
			new Point(0, 0),
			new Point(426, 720));
	static final Rect MIDDLE_ROI = new Rect(
			new Point(426, 0),
			new Point(852, 720));
	static final Rect RIGHT_ROI = new Rect(
			new Point(852, 0),
			new Point(1278, 720));

	public enum BarcodePosition {
		LEFT,
		MIDDLE,
		RIGHT,
		NOT_FOUND
	}

	private static final String TFOD_MODEL_ASSET = "FreightFrenzy_BCDM.tflite";
	private static final String[] LABELS = {
			"Ball",
			"Cube",
			"Duck",
			"Marker"
	};

	Sensors sensors;
	Robot robot;

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

	int k = 0;
	int location = 0;

	private ScheduledFuture<?> lastRotation = null, lastArmRaised = null;


	@Override
	public void runOpMode() {
		// The TFObjectDetector uses the camera frames from the VuforiaLocalizer, so we create that
		// first.
		initVuforia();
		initTfod();

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
			tfod.setZoom(1.5, 16.0 / 9.0);
		}

		/** Wait for the game to begin */
		telemetry.addData(">", "Press Play to start op mode");
		telemetry.update();
		waitForStart();

		robot = new Robot(hardwareMap, telemetry, Executors.newScheduledThreadPool(1));

		// TODO: implement the posibilty of movement during the detection
		if (opModeIsActive()) {
			while (opModeIsActive()) {

				if (robot.objectDetected() == true) {
					k++;
				}
				telemetry.addData("number of the objects on the robot", k);

				if (tfod != null) {
					// getUpdatedRecognitions() will return null if no new information is available since
					// the last time that call was made.
					List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
					if (updatedRecognitions != null) {
						telemetry.addData("# Object Detected", updatedRecognitions.size());
						// step through the list of recognitions and display boundary info.
						int i = 0, numMarkers = 0;
						for (Recognition recognition : updatedRecognitions) {
							if (recognition.getLabel() == "Duck") {
								numMarkers++;
								// TODO: find a way to sort the markers

								int markNumber = markerNumber(recognition);
								telemetry.addData("mark number", markNumber);

								/**
								 * A very easily way would be to sort them using their pixels
								 * locations.
								 *
								 * All the data corelated to a marker should be parsed through
								 * a struct
								 *
								 * Then, we can sort all the markers.
								 *
								 * WARNING: we can try to classify the marker by using their distance
								 * corelated to the screen. (E.G: if a marker is in the westest point of
								 * the screen, then probably it wil be the first marker).*/

								telemetry.addData(String.format("label (%d)", i), recognition.getLabel());
								telemetry.addData(String.format("  left,top (%d)", i), "%.03f , %.03f",
										recognition.getLeft(), recognition.getTop());
								telemetry.addData(String.format("  right,bottom (%d)", i), "%.03f , %.03f",
										recognition.getRight(), recognition.getBottom());


								if(numMarkers == 1){
									break;
								}

								i++;
							}
						}

						robot.forward();

//						if(robot.wheels.returnMotorsValues() >= 400){
//							robot.wheels.stop();
//							break;
//						}

						// robot.left();



						telemetry.update();

						break;
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

		// Loading trackables	 is not necessary for the TensorFlow Object Detection engine.
	}

	/**
	 * Initialize the TensorFlow Object Detection engine.
	 */
	private void initTfod() {
		int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
				"tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
		TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
		// TODO: to a little bit of research referring to the best threshold fof the ML model.
		tfodParameters.minResultConfidence = 0.6f;
		tfodParameters.isModelTensorFlow2 = true;
		tfodParameters.inputSize = 320;
		tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
		tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABELS);
	}

	private boolean isLocated(float left, float right, float top, float bottom, Recognition recognition) {
		if (left <= recognition.getLeft() && right >= recognition.getRight() && top <= recognition.getTop() && bottom >= recognition.getBottom()) {
			return true;
		}
		return false;
	}

	private int markerNumber(Recognition recognition) {
		if (isLocated(0, 426, 0, 720, recognition) == true) {
			return 1; } if (isLocated(426, 852, 0, 720, recognition) == true) {
				return 1; } if (isLocated(852, 1278, 0, 720, recognition)) {
					return 1; }
		return 0;
	}
}