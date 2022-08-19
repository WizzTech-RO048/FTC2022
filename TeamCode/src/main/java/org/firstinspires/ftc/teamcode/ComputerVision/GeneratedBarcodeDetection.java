//package org.firstinspires.ftc.teamcode.ComputerVision;
//
//import java.io.File;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//import java.util.HashMap;
//
//import org.opencv.core.*;
//import org.opencv.core.Core.*;
//import org.opencv.features2d.FeatureDetector;
//import org.opencv.imgcodecs.Imgcodecs;
//import org.opencv.imgproc.*;
//import org.opencv.objdetect.*;
//
//// TODO: needs a double check (ok, what if there are no ducks ?)
//// double verification if on the spot with a least amount of red pixels is also the spot with the most amount of yellow
//
//public class GeneratedBarcodeDetection {
//
//    //Outputs
//    private Mat hsvThresholdOutput = new Mat();
//    private Mat cvRectangleOutput = new Mat();
//
//    static {
//        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
//    }
//
//    /**
//     * This is the primary method that runs the entire pipeline and updates the outputs.
//     */
//    public void process(Mat source0) {
//        // Basically the HSV threshold values
//        Mat hsvThresholdInput = source0;
//        double[] hsvThresholdHue = {0.0, 180.0};
//        double[] hsvThresholdSaturation = {0.0, 181.02388663503496};
//        double[] hsvThresholdValue = {0.0, 255.0};
//        hsvThreshold(hsvThresholdInput, hsvThresholdHue, hsvThresholdSaturation, hsvThresholdValue, hsvThresholdOutput);
//
//        // Step CV_rectangle0:
//        Mat cvRectangleSrc = hsvThresholdOutput;
//        Point cvRectanglePt1 = new Point(0, 0);
//        Point cvRectanglePt2 = new Point(0, 0);
//        Scalar cvRectangleColor = new Scalar(0.0, 0.0, 0.0, 0.0);
//        double cvRectangleThickness = 3.0;
//        int cvRectangleLinetype = Core.LINE_4;
//        double cvRectangleShift = 2.0;
//        cvRectangle(cvRectangleSrc, cvRectanglePt1, cvRectanglePt2, cvRectangleColor, cvRectangleThickness, cvRectangleLinetype, cvRectangleShift, cvRectangleOutput);
//
//    }
//
//    /**
//     * This method is a generated getter for the output of a HSV_Threshold.
//     * @return Mat output from HSV_Threshold.
//     */
//    public Mat hsvThresholdOutput() {
//        return hsvThresholdOutput;
//    }
//
//    /**
//     * This method is a generated getter for the output of a CV_rectangle.
//     * @return Mat output from CV_rectangle.
//     */
//    public Mat cvRectangleOutput() {
//        return cvRectangleOutput;
//    }
//
//
//
//    private void hsvThreshold(Mat input, double[] hue, double[] sat, double[] val, Mat out) {
//        /**
//         * Segment an image based on hue, saturation, and value ranges.
//         *
//         * @param input The image on which to perform the HSL threshold.
//         * @param hue The min and max hue
//         * @param sat The min and max saturation
//         * @param val The min and max value
//         * @param output The image in which to store the output.
//         */
//        Imgproc.cvtColor(input, out, Imgproc.COLOR_BGR2HSV);
//        Core.inRange(out, new Scalar(hue[0], sat[0], val[0]), new Scalar(hue[1], sat[1], val[1]), out);
//    }
//
//    /**
//     * Draws a rectangle on an image.
//     * @param src Image to draw rectangle on.
//     * @param pt1 one corner of the rectangle.
//     * @param pt2 opposite corner of the rectangle.
//     * @param color Scalar indicating color to make the rectangle.
//     * @param thickness Thickness of the lines of the rectangle.
//     * @param lineType Type of line for the rectangle.
//     * @param shift Number of decimal places in the points.
//     * @param dst output image.
//     */
//    private void cvRectangle(Mat src, Point pt1, Point pt2, Scalar color,
//                             double thickness, int lineType, double shift, Mat dst) {
//        src.copyTo(dst);
//        if (color == null) {
//            color = Scalar.all(1.0);
//        }
//        Imgproc.rectangle(dst, pt1, pt2, color, (int)thickness, lineType, (int)shift);
//    }
//
//
//
//
//}