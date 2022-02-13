package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

public class Detector extends OpenCvPipeline {
	Telemetry telemetry;
	Mat mat = new Mat();

	static final Rect LEFT_ROI = new Rect(
			new Point(30, 30),
			new Point(640, 1050));

	static final Rect MIDDLE_ROI = new Rect(
			new Point(640, 30),
			new Point(1250, 1050));

	static final Rect RIGHT_ROI = new Rect(
			new Point(1250, 30),
			new Point(1860, 1050));

	static double COLOR_THRESHOLD = 0.4;

	public Detector(Telemetry t){ telemetry = t; }

	@Override
	public Mat processFrame(Mat input){
		Imgproc.cvtColor(input, mat, Imgproc.COLOR_RGB2HSV);
		Scalar lowHSV = new Scalar(212, 100, 14);
		Scalar highHSV = new Scalar(212, 100, 55);

		Core.inRange(mat, lowHSV, highHSV, mat);

		Mat left = mat.submat(LEFT_ROI);
		Mat middle = mat.submat(MIDDLE_ROI);
		Mat right = mat.submat(RIGHT_ROI);

		double leftValue = Core.sumElems(left).val[0] / LEFT_ROI.area() / 2;
		double middleValue = Core.sumElems(middle).val[0] / MIDDLE_ROI.area() / 2;
		double rightValue = Core.sumElems(right).val[0] / RIGHT_ROI.area() / 2;

		// leftValue.release();
		// middleValue.release();
		// rightValue.release();

		telemetry.addData("Left raw value", (int) Core.sumElems(left).val[0]);
		telemetry.addData("Right raw value", (int) Core.sumElems(right).val[0]);
		telemetry.addData("Middle raw value", (int) Core.sumElems(right).val[0]);

		telemetry.addData("Left percentage", Math.round(leftValue * 100) + "%");
		telemetry.addData("Right percentage", Math.round(rightValue * 100) + "%");
		telemetry.addData("Middle percentage", Math.round(rightValue * 100) + "%");

		boolean objectLeft = leftValue > COLOR_THRESHOLD;
		boolean objectMiddle = middleValue > COLOR_THRESHOLD;
		boolean objectRight = rightValue > COLOR_THRESHOLD;

		Imgproc.cvtColor(mat, mat, Imgproc.COLOR_GRAY2BGR);

		if(objectLeft){ telemetry.addLine("left"); }
		if(objectMiddle){ telemetry.addLine("middle"); }
		if(objectRight){ telemetry.addLine("right"); }

		telemetry.update();

		return mat;
	}
}