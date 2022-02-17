package org.firstinspires.ftc.teamcode.autonomy;

import androidx.annotation.NonNull;
import org.firstinspires.ftc.teamcode.ComputerVision.BarcodeDetector;
import org.firstinspires.ftc.teamcode.Robot.Robot;

public class LeftBarcodeState extends State {
	LeftBarcodeState(@NonNull Robot robot, BarcodeDetector.Position detectedPosition) {
		super(robot);
	}

	@Override
	public State update() {
		return new NoopState(robot);
	}

	@Override
	public void stop(){
		return ;
	}

}
