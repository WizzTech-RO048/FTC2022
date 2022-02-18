package org.firstinspires.ftc.teamcode.autonomy;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class BarcodeDetectionPipeline extends OpenCvPipeline {
	public enum Positions {
		LEFT(new Rect(new Point(30, 30), new Point(620, 1050))),
		MIDDLE(new Rect(new Point(620, 30), new Point(1240, 1050))),
		RIGHT(new Rect(new Point(1240, 30), new Point(1860, 1050))),
		NONE(new Rect(0, 0, 0, 0));

		public final Rect roi;

		Positions(Rect roi) {
			this.roi = roi;
		}

		private int getArea(Mat input) {
			return Core.countNonZero(input.submat(roi));
		}

		@Override
		public String toString() {
			switch (this) {
				case LEFT:
					return "Left";
				case MIDDLE:
					return "Middle";
				case RIGHT:
					return "Right";
				default:
					return "None";
			}
		}
	}

	private static Scalar minBGR = new Scalar(176, 153, 28);
	private static Scalar maxBGR = new Scalar(255, 233, 1);

	private final Mat mask, dst;

	public BarcodeDetectionPipeline(int width, int height) {
		mask = new Mat(height, width, CvType.CV_8U);
		dst = new Mat(height, width, CvType.CV_8U);
	}

	@Override
	public Mat processFrame(Mat input) {
		Core.inRange(input, minBGR, maxBGR, mask);
		Core.bitwise_and(input, input, dst, mask);

		Imgproc.rectangle(dst, Positions.LEFT.roi, minBGR, 20);
		Imgproc.rectangle(dst, Positions.RIGHT.roi, maxBGR, 20);

		return dst;
	}
}
