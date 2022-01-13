<<<<<<< HEAD
=======
/*
* coding to do list
* - write code for the intake ramp
* - implement the moving
*/

>>>>>>> 475e411d084e7fbd2c823c5e68aa15abe7a93f8b
package org.firstinspires.ftc.teamcode.Testing;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
<<<<<<< HEAD
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;

public class Robot {
	private final HardwareMap hardwareMap;
	private final Telemetry telemetry;
=======
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

public class Robot {
	private final DcMotor intakeMotor;
>>>>>>> 475e411d084e7fbd2c823c5e68aa15abe7a93f8b

	private final DcMotor leftFront;
	private final DcMotor leftRear;
	private final DcMotor rightFront;
	private final DcMotor rightRear;

<<<<<<< HEAD
	private final DcMotor intakeMotor;

	private BNO055IMU imu;
	private double headingOffset = 0.0;
	private Orientation angles;
	private Acceleration gravity;


	public Robot(final HardwareMap _hardwareMap, final Telemetry _telemetry){
		hardwareMap = _hardwareMap;
		telemetry = _telemetry;

		leftFront = hardwareMap.dcMotor.get("lf");
        leftRear = hardwareMap.dcMotor.get("rf");
		rightFront = hardwareMap.dcMotor.get("lr");
		rightRear = hardwareMap.dcMotor.get("rr");
	    intakeMotor = hardwareMap.dcMotor.get("intakeMotor");

	    leftFront.setDirection(DcMotorSimple.Direction.FORWARD);
	    leftRear.setDirection(DcMotorSimple.Direction.FORWARD);
	    rightFront.setDirection(DcMotorSimple.Direction.FORWARD);
	    rightRear.setDirection(DcMotorSimple.Direction.FORWARD);
	    intakeMotor.setDirection(DcMotorSimple.Direction.FORWARD);

		imu = hardwareMap.get(BNO055IMU.class, "imu");
	    BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
	    parameters.angleUnit = BNO055IMU.AngleUnit.RADIANS;
	    parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
	    parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
	    parameters.loggingEnabled = true;
	    parameters.loggingTag = "IMU";
	    parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();
	    imu.initialize(parameters);
	}

	private void setMotorMode(DcMotor.RunMode mode, DcMotor... motors) {
		leftFront.setMode(mode);
	    leftRear.setMode(mode);
	    rightFront.setMode(mode);
	    rightRear.setMode(mode);
	    
	}

	public void runUsingEncoders() { setMotorMode(DcMotor.RunMode.RUN_USING_ENCODER, leftFront, leftRear, rightFront, rightRear, intakeMotor); }
	public void runWithoutEncoders() { setMotorMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER, leftFront, leftRear, rightFront, rightRear, intakeMotor); }

	public boolean isGyroCalibrated(){ return imu.isGyroCalibrated(); }

	public void loop() {
		angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.RADIANS);
        gravity = imu.getGravity();
	}

	public void intake(){ intakeMotor.setPower(1.0); }
	public void stopIntake(){ intakeMotor.setPower(0.0); }

	private double getRawHeading(){ return angles.firstAngle; }
	public double getHeading(){ return (getRawHeading() - headingOffset) % (2.0 * Math.PI); }
	public double getHeadingDegrees() { return Math.toDegrees(getHeading()); }
	public void resetHeading(){ headingOffset = getRawHeading(); }

	private static double maxAbs(double... xs) {
		double ret = Double.MIN_VALUE;
	    for (double x : xs) {
	    	if (Math.abs(x) > ret) {
	            ret = Math.abs(x);
	        }
	    }
	    return ret;
	}

	public void setMotors(double _leftFront, double _leftRear, double _rightFront, double _rightRear, boolean nitro) {
		final double divider = (nitro ? 1.0 : 7.5);
	    final double scale = maxAbs(1.0, _leftFront, _leftRear, _rightFront, _rightRear);

	    leftFront.setPower(_leftFront / scale / divider);
	    leftRear.setPower(_leftRear / scale / divider);
	    rightFront.setPower(_rightFront / scale / divider);
	    rightRear.setPower(_rightRear / scale / divider);
	}
=======
	private final BNO055IMU imu;
	private double headingOffset = 0.0;

	public Robot(final HardwareMap hardwareMap, final Telemetry telemetry){
		// intake motor
		intakeMotor = hardwareMap.dcMotor.get("intakeMotor");
		intakeMotor.setDirection(DcMotorSimple.Direction.FORWARD);

		// robot wheels motors
		leftFront = hardwareMap.dcMotor.get("lf");
		leftRear = hardwareMap.dcMotor.get("lr");
		rightFront = hardwareMap.dcMotor.get("rf");
		rightRear = hardwareMap.dcMotor.get("rr");
		leftFront.setDirection(DcMotorSimple.Direction.FORWARD);
		leftRear.setDirection(DcMotorSimple.Direction.FORWARD);
		rightFront.setDirection(DcMotorSimple.Direction.FORWARD);
		rightRear.setDirection(DcMotorSimple.Direction.FORWARD);

		// sensor setup
		imu = hardwareMap.get(BNO055IMU.class, "imu");
		BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
		parameters.angleUnit = BNO055IMU.AngleUnit.RADIANS;
		parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
		parameters.calibrationDataFile = "BNO055IMUCalibration.json";
		parameters.loggingEnabled = true;
		parameters.loggingTag = "IMU";
		parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();
		imu.initialize(parameters);
	}

	// ------------------------
	// - Setup functions
	// ------------------------
	public void runUsingEncoders(){ setMotorsMode(DcMotor.RunMode.RUN_USING_ENCODER, intakeMotor); }
	public void runWithoutEncoders(){ setMotorsMode(DcMotor.RunMode.RUN_USING_ENCODER, intakeMotor); }

	private void setMotorsMode(DcMotor.RunMode mode, DcMotor ... motors){
		for(DcMotor motor : motors){
			motor.setMode(mode);
		}
	}

	// ------------------------
	// - Intake functions
	// ------------------------
	public void intake(){ intakeMotor.setPower(0.0); }
	public void stopIntake(){
		intakeMotor.setPower(0.0);
	}

	// ------------------------
	// - Movement functions
	// ------------------------
	public double getAngularOrientation(){ return imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.RADIANS).firstAngle; }
	public void resetHeading(){ headingOffset = getAngularOrientation(); }

	public void setMotors(double lf, double lr, double rf, double rr, boolean turbo){
		// implementation of maxAbs function from the 2020 code
		final double divider = (turbo ? 1.0 : 7.5);
		final double scale = maxAbs(1.0, lf, lr, rf, rr);

		leftFront.setPower(lf / scale / divider);
		leftRear.setPower(lr / scale / divider);
		rightFront.setPower(rf / scale / divider);
		rightRear.setPower(rr / scale / divider);
	}

	public void rotateRobot(double rotationValue, boolean turbo) {
		setMotors(-rotationValue, -rotationValue, rotationValue, rotationValue, turbo);
	}
	public void moveRobot(double x, double y, double rotationValue, boolean turbo){
		final double orientation = getAngularOrientation();
		final double heading = (orientation - headingOffset) % (2.0 * Math.PI);
		headingOffset = orientation;

		final double direction = Math.atan2(x, y) - heading;
		final double speed = Math.min(1.0, Math.sqrt(x * x + y * y));
		final double rotation = Math.pow(rotationValue, 3.0);

		double lf = speed * Math.sin(direction + Math.PI / 4.0) - rotation;
		double lr = speed * Math.cos(direction + Math.PI / 4.0) + rotation;
		double rf = speed * Math.cos(direction + Math.PI / 4.0) - rotation;
		double rr = speed * Math.sin(direction + Math.PI / 4.0) + rotation;

		setMotors(lf, lr, rf, rr, turbo);
	}

	public void stopRobot(){ setMotors(0.0, 0.0, 0.0, 0.0, false); }

	// ------------------------
	// - Auxiliar functions
	// ------------------------
	private static double maxAbs(double ... xs){
		double ret = Double.MIN_VALUE;
		for(double x : xs){
			if(Math.abs(x) > ret){
				ret = Math.abs(x);
			}
		}
		return ret;
	}

>>>>>>> 475e411d084e7fbd2c823c5e68aa15abe7a93f8b
}
