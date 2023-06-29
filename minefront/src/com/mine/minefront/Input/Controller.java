package com.mine.minefront.Input;

import com.mine.minefront.Display;

public class Controller {

	public double x, y, z, rotation, xa, za, rotationa;

	public static boolean turnLeft = false;
	public static boolean turnRight = false;
	public static boolean walk = false;
	public static boolean crouchWalk = false;
	public static boolean runWalk = false;

	public void tick(boolean forward, boolean back, boolean left, boolean right, boolean jump, boolean crouch, boolean run) {
		double rotationSpeed = 0.002 * Display.MouseSpeed;
		double walkSpeed = 0.5;
		double jumpHeight = 0.5;

		double crouchHeight = 0.3;
		double xMove = 0;
		double zMove = 0;

		if (forward) {
			zMove++;
			walk = true;
		}

		if (back) {
			zMove--;
			walk = true;
		}

		if (left) {
			xMove--;
			walk = true;
		}

		if (right) {
			xMove++;
			walk = true;
		}

		if (turnLeft) {
			if (InputHandler.MouseButton == 3) {

			} else {
				rotationa -= rotationSpeed;
				walk = true;
			}

		}

		if (turnRight) {
			if (InputHandler.MouseButton == 3) {

			} else {
				rotationa += rotationSpeed;
				walk = true;
			}

		}

		if (jump) {
			y += jumpHeight;
			run = false;
		}
		if (crouch) {
			y -= crouchHeight;
			run = false;
			crouchWalk = true;
			walkSpeed = 0.2;
		}
		if (run) {
			walkSpeed = 1;
			walk = true;
			runWalk = true;
		}

		if (!forward && !back && !left && !right) {
			walk = false;
		}

		if (!crouch) {
			crouchWalk = false;
		}
		if (!run) {
			runWalk = false;
		}

		xa += (xMove * Math.cos(rotation) + zMove * Math.sin(rotation)) * walkSpeed;
		za += (zMove * Math.cos(rotation) - xMove * Math.sin(rotation)) * walkSpeed;


		x += xa;
		y *= 0.9;
		z += za;

		xa *= 0.1;
		za *= 0.1;

		 if (turnLeft || turnRight) {
			rotation += rotationa;
			rotationa *= 0.4;
		 }

	}
}
