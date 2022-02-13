/**
 * (1920 x 1080)
 *
 *
 *
 * */

package org.firstinspires.ftc.teamcode.ComputerVision;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

public class SkystoneDetector extends OpenCvPipeline {
	Telemetry telemetry;
	Mat mat = new Mat();

	public int location;

	static final Rect LEFT_ROI = new Rect(
			new Point(30, 30),
			new Point(120, 75));
	static final Rect RIGHT_ROI = new Rect(
			new Point(140, 35),
			new Point(200, 75));
	static final Rect MIDDLE_ROI = new Rect(
			new Point(),
			new Point());
	static double PERCENT_COLOR_THRESHOLD = 0.4;

	public SkystoneDetector(Telemetry t) { telemetry = t; }

	@Override
	public Mat processFrame(Mat input) {
		Imgproc.cvtColor(input, mat, Imgproc.COLOR_RGB2HSV);
		Scalar lowHSV = new Scalar(23, 50, 70);
		Scalar highHSV = new Scalar(32, 255, 255);

		Core.inRange(mat, lowHSV, highHSV, mat);

		Mat left = mat.submat(LEFT_ROI);
		Mat middle = mat.submat(MIDDLE_ROI);
		Mat right = mat.submat(RIGHT_ROI);

		double leftValue = Core.sumElems(left).val[0] / LEFT_ROI.area() / 255;
		double rightValue = Core.sumElems(right).val[0] / RIGHT_ROI.area() / 255;
		double middleValue = Core.sumElems(middle).val[0] / MIDDLE_ROI.area() / 255;

		left.release();
		middle.release();
		right.release();

		telemetry.addData("Left raw value", (int) Core.sumElems(left).val[0]);
		telemetry.addData("Right raw value", (int) Core.sumElems(right).val[0]);
		telemetry.addData("Middle raw value", (int) Core.sumElems(right).val[0]);

		telemetry.addData("Left percentage", Math.round(leftValue * 100) + "%");
		telemetry.addData("Right percentage", Math.round(rightValue * 100) + "%");
		telemetry.addData("Middle percentage", Math.round(rightValue * 100) + "%");

		boolean leftMark = leftValue > PERCENT_COLOR_THRESHOLD;
		boolean middleMark = middleValue > PERCENT_COLOR_THRESHOLD;
		boolean rightMark = rightValue > PERCENT_COLOR_THRESHOLD;

		if (leftMark && rightMark) {
			location = 0;
			telemetry.addData("Skystone Location", "not found");
		}
		else if (leftMark) {
			location = 1;
			telemetry.addData("Skystone Location", "right");
		}
		else {
			location = 2;
			telemetry.addData("Skystone Location", "left");
		}
		telemetry.update();

		Imgproc.cvtColor(mat, mat, Imgproc.COLOR_GRAY2RGB);

		Scalar markColor = new Scalar(255, 0, 0);

		// Imgproc.rectangle(mat, LEFT_ROI, location == "LEFT"? colorSkystone:colorStone);
		// Imgproc.rectangle(mat, RIGHT_ROI, location == "RIGHT"? colorSkystone:colorStone);

		return mat;
	}

	public int getLocation(){
		return location;
	}
}