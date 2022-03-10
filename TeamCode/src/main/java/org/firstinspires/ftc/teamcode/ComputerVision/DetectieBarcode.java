package org.firstinspires.ftc.teamcode.ComputerVision;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

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

abstract public class DetectieBarcode extends LinearOpMode
{
    OpenCvWebcam webcam;
    int detected_pos=0;

     public void detector(){
         //asta e functia care face captura, deschide camera etc si deschide pipeline-ul PipelineProcesare()

         int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
         webcam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
         webcam.setPipeline(new PipelineProcesare());
         webcam.setMillisecondsPermissionTimeout(2500); // Timeout for obtaining permission is configurable. Set before opening.
         webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
         {
             @Override
             public void onOpened()
             {
                 webcam.startStreaming(1280, 720, OpenCvCameraRotation.UPRIGHT);
             }

             @Override
             public void onError(int errorCode)
             {
                 telemetry.addLine("Eroare: nu s-a putut deschide camera");
                 telemetry.update();
             }
         });
     }

     //aici vreau sa procesez totul. Voi adauga probabil un contor si sa verific daca acel contor e mai mare decat 1 in functia processFrame.
    //Ideea e ca eu vreau sa procesez o singura data si asta e singura abordare la care m-am putut gandi


     public static class PipelineProcesare extends OpenCvPipeline{

         @Override
         public Mat processFrame(Mat input)
         {

             Imgproc.rectangle(
                     input,
                     new Point(
                             input.cols()/4,
                             input.rows()/4),
                     new Point(
                             input.cols()*(3f/4f),
                             input.rows()*(3f/4f)),
                     new Scalar(0, 255, 0), 4);

             return input;
         }

     }



}