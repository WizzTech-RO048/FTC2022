package org.firstinspires.ftc.teamcode.ComputerVision;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;
import java.util.ArrayList;
import java.util.List;
import org.opencv.core.MatOfPoint;
import java.lang.Math;



public class DetectieBarcode extends OpenCvPipeline{
    Telemetry telemetry;
    public int detected_pos=0;

    public DetectieBarcode(Telemetry t){ telemetry=t; }

    @Override
    public Mat processFrame(Mat input){

        Mat hsv=new Mat(input.size(), CvType.CV_8U);
        Imgproc.cvtColor(input, hsv, Imgproc.COLOR_BGR2HSV);

        Scalar lower_bound=new Scalar(20, 80, 80);
        Scalar upper_bound=new Scalar(55, 255, 255);

        Mat mask=new Mat(input.size(), CvType.CV_8U);
        Core.inRange(hsv, lower_bound, upper_bound, mask);

        Mat segmented_img=new Mat(input.size(), CvType.CV_8U);
        Core.bitwise_and(input, input, segmented_img, mask);

        Mat gray = new Mat(segmented_img.rows(), segmented_img.cols(), segmented_img.type());
        Imgproc.cvtColor(segmented_img, gray, Imgproc.COLOR_BGR2GRAY);

        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchey=new Mat(input.size(), CvType.CV_8U);

        Imgproc.findContours(gray, contours, hierarchey, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        gray.release();
        mask.release();
        segmented_img.release();

        //center point
        int x_center=100;
        int y_center=390;

        detected_pos = 2; //nici aici nu se modifica, ramane tot 0
        // FIXME: nu returneaza valoarea corecta
//        for (int i=0; i<contours.size(); i++){
//            double cont_area = Imgproc.contourArea(contours.get(i));
//            if (cont_area >= 5000){
//                Rect rect=Imgproc.boundingRect(contours.get(i));
//                double x = rect.x;
//                double y = rect.y;
//                double w = rect.width;
//                double h = rect.height;
//
//                Point center = new Point(x, y);
//                Point point2 = new Point(x+w,y+h);
//                Scalar color = new Scalar(0, 0, 255);
//
//                Imgproc.rectangle (input, center, point2, color, 3);
//
//                detected_pos=detect_pos(calculate_dis(x_center,y_center,x,y));
//
//
//            }
//        }

        Scalar color = new Scalar(0, 0, 255);
        Imgproc.drawContours(input, contours, -1, color, 3);

        hierarchey.release();

        return input;
    }


    private int calculate_dis(double x1, double y1, double x2, double y2){
        double distance = Math.sqrt(Math.pow((x2-x1),2)+Math.pow((y2-y1),2));
        return (int) Math.round(distance);
    }

    private int detect_pos(int distance){
        if (distance<=300){
            return 1;
        }
        if (distance<=700 && distance>=300){
            return 2;
        }
        if (distance<=1000 && distance>=700){
            return 3;
        }

        return 4;
    }

    public int getLocation(){
        return detected_pos;
    }
}