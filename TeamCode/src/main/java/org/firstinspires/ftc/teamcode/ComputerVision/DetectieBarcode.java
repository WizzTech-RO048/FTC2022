package org.firstinspires.ftc.teamcode.ComputerVision;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvPipeline;
import org.openftc.easyopencv.OpenCvWebcam;


public class DetectieBarcode extends OpenCvPipeline{
    Telemetry telemetry;
    Mat mat=new Mat();
    public int detected_pos=0;

    public DetectieBarcode(Telemetry t){ telemetry=t; }

    @Override
    public Mat processFrame(Mat input){
        /// https://www.tabnine.com/code/java/methods/org.opencv.imgproc.Imgproc/morphologyEx

        Imgproc.cvtColor(input, mat, Imgproc.COLOR_RGB2HSV);
        Scalar lower_bound=new Scalar(20, 80, 80);
        Scalar upper_bound=new Scalar(55, 255, 255);

        return mat;
    }

    public int getLocation(){
        return detected_pos;
    }
}