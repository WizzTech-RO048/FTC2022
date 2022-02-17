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

	public Positions location;

	static Scalar minBGR = new Scalar(25, 146, 190);
	static Scalar maxBGR = new Scalar(150, 246, 255);

	public int width;

	public BarcodeDetectionPipeline(int width) {
		this.width = width;
	}

//	@Override
//	public Mat processFrame(Mat input) {
//		 //location = getPosition(prepareFrame(input));
//		Imgproc.GaussianBlur(input, input, new Size(5, 5), 0);
//
//		Mat mask = new Mat(input.size(), CvType.CV_8U);
//		Core.inRange(input, minBGR, maxBGR, mask);
//
//		// TODO: Check if mask is applied as desired
//		Mat dst = new Mat(mask.size(), mask.type());
//		Core.bitwise_and(input, input, mask, dst);
//
//		dst.copyTo(input);
//
//		mask.release();
//
//		 // Imgproc.rectangle(input, location.roi, new Scalar(255, 0, 0), 20);
//
//		 return input;
//	}

	public static Mat prepareFrame(Mat input) {
		Imgproc.GaussianBlur(input, input, new Size(5, 5), 0);

		Mat mask = new Mat(input.size(), CvType.CV_8U);
		Core.inRange(input, minBGR, maxBGR, mask);

		// TODO: Check if mask is applied as desired
		Mat dst = new Mat(mask.size(), mask.type());
		Core.bitwise_and(input, input, mask, dst);

		mask.release();

		return dst;
	}

	private static Positions getPosition(Mat input) {
		// TODO: Check if this returns the correct position
		Positions max = Arrays.stream(Positions.values()).max(Comparator.comparingInt(a -> a.getArea(input))).get();

		if (max.getArea(input) == 0) {
			return Positions.NONE;
		}

		input.release();

		return max;
	}

	@Override
	public Mat processFrame(Mat input) {
		Mat mat = new Mat();
		Imgproc.cvtColor(input, mat, Imgproc.COLOR_BGR2HSV);

		if (mat.empty()) {
			location = Positions.NONE;
			return input;
		}

		// Scalar lowHSV = new Scalar(170, 170, 60);
		Scalar lowHSV = new Scalar(170, 170, 60);
		//Scalar highHSV = new Scalar(210, 210, 90);
		Scalar highHSV = new Scalar(39, 100, 100);
		Mat thresh = new Mat();

		Imgproc.rectangle(mat, Positions.LEFT.roi, lowHSV, 20);
		Imgproc.rectangle(mat, Positions.RIGHT.roi, highHSV, 20);

		return mat;

		// Core.inRange(mat, lowHSV, highHSV, thresh);

		// return thresh;

//		Mat edges = new Mat();
//		Imgproc.Canny(thresh, edges, 100, 300);
//
//		edges.copyTo(input);
//		return input;

//		List<MatOfPoint> contours = new ArrayList<>();
//		Mat hierarchy = new Mat();
//		Imgproc.findContours(edges, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
//
//		MatOfPoint2f[] contoursPoly  = new MatOfPoint2f[contours.size()];
//		Rect[] boundRect = new Rect[contours.size()];
//		for (int i = 0; i < contours.size(); i++) {
//			contoursPoly[i] = new MatOfPoint2f();
//			Imgproc.approxPolyDP(new MatOfPoint2f(contours.get(i).toArray()), contoursPoly[i], 3, true);
//			boundRect[i] = Imgproc.boundingRect(new MatOfPoint(contoursPoly[i].toArray()));
//		}
//
//		double left_x = 0.25 * width;
//		double right_x = 0.75 * width;
//		boolean left = false; // true if regular stone found on the left side
//		boolean right = false; // "" "" on the right side
//		for (int i = 0; i != boundRect.length; i++) {
//			if (boundRect[i].x < left_x)
//				left = true;
//			if (boundRect[i].x + boundRect[i].width > right_x)
//				right = true;
//
//			Imgproc.rectangle(mat, boundRect[i], new Scalar(0.5, 76.9, 89.8));
//		}
//
//		if (!left) location = Positions.LEFT;
//		else if (!right) location = Positions.RIGHT;
//		else location = Positions.NONE;
//
//		return mat; // return the mat with rectangles drawn
	}

	public Positions getLocation() {
		if(location != null)
			return location;
		else
			return Positions.NONE;
	}
}
