package org.firstinspires.ftc.teamcode.autonomy;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.Robot.Robot;
import org.firstinspires.ftc.teamcode.Robot.Wheels;

import java.util.concurrent.ScheduledFuture;

public class MoveToShippingState extends State {

	private ScheduledFuture<?> lastMovement = null;
	private int step;

	MoveToShippingState(@NonNull Robot robot) {
		super(robot);

		lastMovement = robot.wheels.moveFor(1.5, 0.3, Wheels.MoveDirection.RIGHT);
		step = 0;
	}

	@Override
	public State update() {
		/*
		if(lastMovement.isDone()) {
			step ++;

			switch (step) {
				case 1:
					lastMovement = robot.wheels.moveFor(0.2, 0.3, Wheels.MoveDirection.FORWARD);
					break;
				case 2:
					try {
						double initialPower = -Math.signum(30);
						double rotation = Math.abs(30);

						robot.wheels.move(0, Wheels.normalizeRotationPower(initialPower, rotation), 0);

						Thread.sleep(1000);
						robot.wheels.stop();
					} catch (Exception e) {
						break ;
					}

					// lastMovement = robot.wheels.rotateFor(140);
					break;
				case 3:
					lastMovement = robot.arm.moveArm(0.5);
					break;
				case 4:
					try {
						robot.arm.throwObject();
						Thread.sleep(1000);

						robot.arm.retract();
						Thread.sleep(1000);
					} catch (Exception e) {
						break ;
					}

					break;
				case 5:
					lastMovement = robot.arm.moveArm(0);

					break;
				case 6:
					return new NoopState(robot);
			}
		}
		*/
		return this;

	}

}
