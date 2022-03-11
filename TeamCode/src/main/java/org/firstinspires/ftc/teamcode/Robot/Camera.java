package org.firstinspires.ftc.teamcode.Robot;

import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.ComputerVision.DetectieBarcode;
import org.opencv.core.Mat;
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

public class Camera {
    private final Telemetry telemetry;
    OpenCvWebcam webcam;

    Camera(HardwareMap hardwareMap, Telemetry telemetry) {
        this.telemetry = telemetry;


        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        webcam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
        webcam.setMillisecondsPermissionTimeout(2500);
        webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                webcam.startStreaming(1280, 720, OpenCvCameraRotation.UPRIGHT);
                telemetry.addLine("Camera a fost incarcata");
                telemetry.update();
            }

            @Override
            public void onError(int errorCode)
            {
                telemetry.addLine("Eroare: nu s-a putut deschide camera");
                telemetry.update();
            }
        });

    }

    public void applyPipeline(OpenCvPipeline pipeline){
        webcam.setPipeline(pipeline);
    }


    // TODO: fix the function. It returns a false value.
    public boolean getImage(Mat mat) {
        return false;
    }

    public void stop() {}
}
