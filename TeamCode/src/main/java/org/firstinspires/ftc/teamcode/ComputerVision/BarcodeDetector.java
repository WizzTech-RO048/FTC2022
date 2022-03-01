package org.firstinspires.ftc.teamcode.ComputerVision;

import org.opencv.android.Utils;
import org.opencv.core.*;

import org.opencv.imgproc.Imgproc;

import java.util.*;

public class BarcodeDetector {
    public enum Position {
        LEFT(new Rect(new Point(30, 30), new Point(620, 1050))),
        MIDDLE(new Rect(new Point(620, 30), new Point(1240, 1050))),
        RIGHT(new Rect(new Point(1240, 30), new Point(1860, 1050)));

        private final Rect roi;

        Position(Rect roi) {
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

    static Scalar minBGR = new Scalar(25, 146, 190);
    static Scalar maxBGR = new Scalar(150, 246, 255);

    public static Position detect(Mat input) {
//        Mat mat = new Mat(input.getHeight(), input.getWidth(), CvType.CV_8UC1);
//        Utils.bitmapToMat(input, mat);

        return getPosition(prepareFrame(input));
    }

    public static Mat prepareFrame(Mat input) {
        Imgproc.GaussianBlur(input, input, new Size(5, 5), 0);

        Mat mask = new Mat(input.size(), CvType.CV_8U);
        Core.inRange(input, minBGR, maxBGR, mask);

        Mat dst = new Mat(mask.size(), mask.type());
        Core.bitwise_and(input, input, dst, mask);

        return dst;
    }

    private static Position getPosition(Mat input) {
        // TODO: Check if this returns the correct position
        Position max = Arrays.stream(Position.values()).max(Comparator.comparingInt(a -> a.getArea(input))).get();
        boolean hasArea = max.getArea(input) == 0;
//        input.release();

        if (hasArea) {
            return null;
        }

        return max;
    }
}