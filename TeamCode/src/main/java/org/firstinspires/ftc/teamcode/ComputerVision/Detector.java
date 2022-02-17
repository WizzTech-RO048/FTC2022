package org.firstinspires.ftc.teamcode.ComputerVision;

import com.sun.tools.javac.util.ArrayUtils.*;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.opencv.core.*;

import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;
import java.util.List;

public class Detector extends OpenCvPipeline {
	Telemetry telemetry;
	Mat mat = new Mat();

	static final Rect LEFT_ROI = new Rect(
			new Point(30, 30),
			new Point(620, 1050));
	static final Rect MIDDLE_ROI = new Rect(
			new Point(620, 30),
			new Point(1240, 1050));
	static final Rect RIGHT_ROI = new Rect(
			new Point(1240, 30),
			new Point(1860, 1050));

	Scalar minBGR = new Scalar(25, 146, 190);
	Scalar maxBGR = new Scalar(150, 246, 255);

	int[] areaValues;
	int max = 0;
	int pos;

	public enum BarcodePosition {
		LEFT,
		MIDDLE,
		RIGHT,
		NOT_FOUND
	}

	private BarcodePosition barcodePosition;

	public Detector(Telemetry t) {
		telemetry = t;
	}

	@Override
	public Mat processFrame(Mat input) {

		// Core.inRange(mat, lowHSV, highHSV, mat);

		// double leftValue = Core.sumElems(left).val[0] / LEFT_ROI.area() / 255;
		// double middleValue = Core.sumElems(middle).val[0] / MIDDLE_ROI.area() / 255;
		// double rightValue = Core.sumElems(right).val[0] / RIGHT_ROI.area() / 255;

		// leftValue.release();
		// middleValue.release();
		// rightValue.release();

		// telemetry.addData("Left raw value", (int) Core.sumElems(left).val[0]);
		// telemetry.addData("Right raw value", (int) Core.sumElems(right).val[0]);
		// telemetry.addData("Middle raw value", (int) Core.sumElems(right).val[0]);

		// telemetry.addData("Left percentage", Math.round(leftValue * 100) + "%");
		// telemetry.addData("Right percentage", Math.round(rightValue * 100) + "%");
		// telemetry.addData("Middle percentage", Math.round(rightValue * 100) + "%");
		// boolean objectLeft = leftValue > COLOR_THRESHOLD;
		// boolean objectMiddle = middleValue > COLOR_THRESHOLD;
		// boolean objectRight = rightValue > COLOR_THRESHOLD;

		// Imgproc.cvtColor(mat, mat, Imgproc.COLOR_GRAY2BGR);

		/*
		if (objectLeft) {
			telemetry.addLine("left");
		}
		if (objectMiddle) {
			telemetry.addLine("middle");
		}
		if (objectRight) {
			telemetry.addLine("right");
		} else {
			telemetry.addLine("barcode not found");
		}
		 */

		// Core.bitwise_and(mat, mat, input);

		prepareFrame(input);

		drawRectangles(input);

		return input;
	}

	private void prepareFrame(Mat input){
		Imgproc.GaussianBlur(input, input, new Size(5, 5), 0);

		Mat mask = new Mat(input.size(), CvType.CV_8UC1);
		Core.inRange(input, minBGR, maxBGR, mask);
		Core.bitwise_and(input, input, mask);

		saveImage(input, "/storage/self/primary/DCIM/image.png");
	}

	private BarcodePosition returnPosition(Mat input){
		Mat leftROI = mat.submat(LEFT_ROI);
		Mat middleROI = mat.submat(MIDDLE_ROI);
		Mat rightROI = mat.submat(RIGHT_ROI);

		List<Integer> areaValues = new ArrayList<Integer>();

		areaValues.add(area(leftROI));
		areaValues.add(area(middleROI));
		areaValues.add(area(rightROI));

		for(int i = 0; i < 3; i++){
			if(areaValues.get(i) > max){
				max = areaValues.get(i);
				pos = i;
			}
		}

		if(max == 0){
			return BarcodePosition.NOT_FOUND;
		} else{
			if(pos == 0){ return BarcodePosition.LEFT; }
			if(pos == 1){ return BarcodePosition.MIDDLE; }
			if(pos == 2){ return BarcodePosition.RIGHT; }
		}
		return BarcodePosition.NOT_FOUND;
	}

	private int area(Mat ROI){
		return Core.countNonZero(ROI);
	}

	private void drawRectangles(Mat input){
		Scalar elementColor = new Scalar(255, 0, 0);
		Scalar notElement = new Scalar(0, 255, 0);

		Imgproc.rectangle(input, LEFT_ROI, barcodePosition == BarcodePosition.LEFT ? notElement : elementColor);
		Imgproc.rectangle(input, RIGHT_ROI, barcodePosition == BarcodePosition.RIGHT ? notElement : elementColor);
		Imgproc.rectangle(input, MIDDLE_ROI, barcodePosition == BarcodePosition.MIDDLE ? notElement : elementColor);
	}


	public void saveImage(Mat mat, String outputFileName){
		Imgcodecs imageCodecs = new Imgcodecs();
		imageCodecs.imwrite(outputFileName, mat);
	}

	public BarcodePosition getBarcodePosition() {
		return barcodePosition;
	}

}