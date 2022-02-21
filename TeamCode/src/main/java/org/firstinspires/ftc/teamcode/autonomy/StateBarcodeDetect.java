package org.firstinspires.ftc.teamcode.autonomy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import org.firstinspires.ftc.teamcode.ComputerVision.BarcodeDetector;
import org.firstinspires.ftc.teamcode.Robot.Robot;
import org.opencv.core.Mat;

class StateBarcodeDetect extends State {
	private final BarcodeDetector.Position mockPosition;

	StateBarcodeDetect(@NonNull Robot robot, @Nullable BarcodeDetector.Position mockedPosition) {
		super(robot);

		mockPosition = mockedPosition;
	}

	@Override
	public State update() {
		if (mockPosition != null) {
			return nextState(mockPosition);
		}

		Mat image = new Mat();
		if (robot.camera.getImage(image)) {
			return nextState(BarcodeDetector.detect(image));
		}

		return new StateException(robot, new Exception("Failed to get image from camera"));
	}

	private State nextState(BarcodeDetector.Position position) {
		switch (position) {
			case LEFT:
			case MIDDLE:
			case RIGHT:
				return new StateBarcodeDetectedRight(robot);
			default:
				return new StateException(robot, new RuntimeException("No barcode position detected"));
		}
	}

	@Override
	public void stop() {
		super.stop();
	}
}