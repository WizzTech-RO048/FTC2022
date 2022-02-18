package org.firstinspires.ftc.teamcode.ComputerVision;

import android.graphics.Bitmap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.opencv.android.Utils;
import org.opencv.core.*;

import org.opencv.imgproc.Imgproc;

import java.util.*;

public class BarcodeDetector {
    public enum Position {
        LEFT(new Rect(new Point(30, 30), new Point(620, 1050))),
        MIDDLE(new Rect(new Point(620, 30), new Point(1240, 1050))),
        RIGHT(new Rect(new Point(1240, 30), new Point(1860, 1050)));

        public final Rect roi;

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

    public static Position detect(Bitmap input, Telemetry telemetry) {
        Mat mat = new Mat(input.getHeight(), input.getWidth(), CvType.CV_8UC1);
        telemetry.addLine("Converting Bitmap to Mat...");
        telemetry.update();
        Utils.bitmapToMat(input, mat);
        telemetry.addLine("Converted to Mat, processing...");
        telemetry.update();

        return getPosition(prepareFrame(mat, telemetry), telemetry);
    }

    public static Mat prepareFrame(Mat input, Telemetry telemetry) {
        telemetry.addLine("Preparing frame...");
        telemetry.update();
        Imgproc.GaussianBlur(input, input, new Size(5, 5), 0);
        Imgproc.cvtColor(input, input, Imgproc.COLOR_RGB2HSV);

        // the BGRA format
        // Scalar minBGRA = new Scalar(176, 153, 28);
        // Scalar maxBGRA = new Scalar(255, 233, 1);

        // HSV format
        Scalar lowHSV = new Scalar(64, 100, 100); // lower bound HSV for yellow
        Scalar highHSV = new Scalar(70, 255, 255); // higher bound HSV for yellow

        telemetry.addLine("Applying mask...");
        telemetry.update();
        Mat mask = new Mat(input.size(), CvType.CV_8U);
        Core.inRange(input, lowHSV, highHSV, mask);

        telemetry.addLine("Applying bitwise AND...");
        telemetry.update();

        Imgproc.Canny(mask, mask, 100, 300);
        Mat dst = new Mat(mask.size(), mask.type());
        Core.bitwise_and(input, input, dst, mask);

        return dst;
    }

    private static Position getPosition(Mat input, Telemetry telemetry) {
        telemetry.addLine("Getting position...");
        telemetry.update();
        Position max = Arrays.stream(Position.values()).max(Comparator.comparingInt(a -> a.getArea(input))).get();
        boolean hasArea = max.getArea(input) == 0;
        telemetry.addLine("Position retrieved!");
        telemetry.update();

        if (hasArea) {
            return max;
        }

        return null;
    }

}