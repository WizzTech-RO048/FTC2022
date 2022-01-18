package org.firstinspires.ftc.teamcode.Robot;

import com.qualcomm.robotcore.hardware.Gamepad;

public class Controller {
	private Gamepad gamepad;

	private int dpadUp, dpadDown, dpadRight, dpadLeft;
	private int x, y, a, b;
	private int leftBumber, rightBumber;

	public double leftStickX, leftStickY;
	public double rightStickX, rightStickY;
	public double leftTrigger, rightTrigger;

	public Controller(Gamepad g){
		gamepad = g;
	}

	public void update(){
		if(gamepad.x){ x++; } else{ x = 0; }
		if(gamepad.y){ y++; } else{ y = 0; }
		if(gamepad.a){ a++; } else{ a = 0; }
		if(gamepad.b){ b++; } else{ b = 0; }

		if(gamepad.dpad_up){ dpadUp++; } else{ dpadUp = 0; }
		if(gamepad.dpad_down){ dpadDown++; } else{ dpadDown = 0; }
		if(gamepad.dpad_right){ dpadRight++; } else{ dpadRight = 0; }
		if(gamepad.dpad_left){ dpadLeft++; } else{ dpadLeft = 0; }

		if(gamepad.right_bumper){ rightBumber++; } else{ rightBumber = 0; }
		if(gamepad.left_bumper){ leftBumber++; } else{ leftBumber = 0; }

		leftStickX = gamepad.left_stick_x;
		leftStickY = gamepad.left_stick_y;
		rightStickX = gamepad.right_stick_x;
		rightStickY = gamepad.right_stick_y;
		leftTrigger = gamepad.left_trigger;
		rightTrigger = gamepad.right_trigger;
	}

	public boolean dpadUp(){ return dpadUp > 0; }
	public boolean dpadDown(){ return dpadDown > 0; }
	public boolean dpadRight(){ return dpadRight > 0; }
	public boolean dpadLeft(){ return dpadLeft > 0; }

	public boolean X(){ return x > 0; }
	public boolean Y(){ return y > 0; }
	public boolean A(){ return a > 0; }
	public boolean B(){ return b > 0; }

	public boolean leftBumber(){ return leftBumber > 0; }
	public boolean rightBumber(){ return rightBumber > 0; }

	public boolean dpadUpOnce(){ return dpadUp == 1; }
	public boolean dpadDownOnce(){ return dpadDown == 1; }
	public boolean dpadRightOnce(){ return dpadRight == 1; }
	public boolean dpadLeftOnce(){ return dpadLeft == 1; }

	public boolean XOnce(){ return x == 1; }
	public boolean YOnce(){ return y == 1; }
	public boolean AOnce(){ return a == 1; }
	public boolean BOnce(){ return b == 1; }

	public boolean leftBumberOnce(){ return leftBumber == 1; }
	public boolean rightBumberOnce(){ return rightBumber == 1; }

}
