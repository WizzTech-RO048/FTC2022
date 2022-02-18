package org.firstinspires.ftc.teamcode.Robot;

import android.graphics.Bitmap;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.function.Continuation;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.ComputerVision.BarcodeDetector;
import org.firstinspires.ftc.teamcode.autonomy.BarcodeDetectionPipeline;
import org.openftc.easyopencv.*;

import java.util.concurrent.*;

public class Camera {
    private final Telemetry telemetry;
    private final OpenCvCamera camera;
    private final CompletableFuture<?> cameraOpened;

    private static final int WIDTH = 1920, HEIGHT = 1080;

    BarcodeDetectionPipeline detector = new BarcodeDetectionPipeline(WIDTH, HEIGHT);

    Camera(HardwareMap map, Telemetry telemetry) {
        this.telemetry = telemetry;

        int cameraMonitorViewID = map.appContext
                .getResources()
                .getIdentifier("cameraMonitorViewId", "id", map.appContext.getPackageName());
        WebcamName name = map.get(WebcamName.class, "Webcam 1");
        camera = OpenCvCameraFactory.getInstance().createWebcam(name, cameraMonitorViewID);

        cameraOpened = new CompletableFuture<>();
        camera.setPipeline(detector);

        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                camera.startStreaming(WIDTH, HEIGHT, OpenCvCameraRotation.UPRIGHT);
                cameraOpened.complete(null);
            }

            @Override
            public void onError(int errorCode) {
                cameraOpened.completeExceptionally(new RuntimeException(String.format("Failed to initialize camera: error code %d", errorCode)));
            }
        });
    }

    public Future<Bitmap> getImage() {
        return cameraOpened.thenCompose(_i -> {
            CompletableFuture<Bitmap> image = new CompletableFuture<>();
            camera.getFrameBitmap(Continuation.createTrivial(image::complete));

            return image;
        });
    }

    public void stop() {
        // TODO: This may hang, fix
        camera.stopStreaming();
        camera.closeCameraDevice();
    }
}
