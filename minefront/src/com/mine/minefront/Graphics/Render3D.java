package com.mine.minefront.Graphics;

import com.mine.minefront.Game;
import com.mine.minefront.Input.Controller;

public class Render3D extends Render {

	public double[] zBuffer;
	public double renderDistance = 5000;
	private double forward, right, up, cosine, sine, walking;

	public Render3D(int width, int height) {
		super(width, height);
		zBuffer = new double[width * height];
	}


	public void floor(Game game) {

		double floorPosition = 8;
		double ceilingPosition = 8;

		forward = game.controls.z;
		right = game.controls.x;
		up = game.controls.y;

		walking = 0.0;
		// double rotation = game.time / 100.0;
		double rotation = game.controls.rotation;// Math.sin(game.time / 40.0) * 0.5;
		cosine = Math.cos(rotation);
		sine = Math.sin(rotation);

		for (int y = 0; y < height; y++) {

			double ceiling = (y - height / 2.0) / height;

			double z = (floorPosition + up) / ceiling;

			if (Controller.walk) {
				walking = Math.sin(game.time / 6.0) * 0.5;
				z = (floorPosition + up + walking) / ceiling;
			}
			if (Controller.crouchWalk && Controller.walk) {
				walking = Math.sin(game.time / 6.0) * 0.25;
				z = (floorPosition + up + walking) / ceiling;
			}
			if (Controller.runWalk && Controller.walk) {
				walking = Math.sin(game.time / 6.0) * 0.8;
				z = (floorPosition + up + walking) / ceiling;
			}

			if (ceiling < 0) {
				z = (ceilingPosition - up) / -ceiling;
				if (Controller.walk) {
					z = (ceilingPosition - up - walking) / -ceiling;
				}
			}


			for (int x = 0; x < width; x++) {
				double depth = (x - width / 2.0) / height;
				depth *= z;
				double xx = depth * cosine + z * sine;
				double yy = z * cosine - depth * sine;
				int xPix = (int) (xx + right);
				int ypix = (int) (yy + forward);
				zBuffer[x + y * width] = z;
				// pixels[x + y * width] = ((xPix & 15) * 16) | ((ypix & 15) * 16) << 8;
				pixels[x + y * width] = Texture.floor.pixels[(xPix & 7) + (ypix & 7) * 8];
				if (z > 500) {
					pixels[x + y * width] = 0;
				}
			}

		}
	}

	public void RenderWalls(double xLeft, double xRight, double zDistanceLeft, double zDistanceRight, double yHeight) {
		double upCorrect = 0.062;
		double rightCorrect = 0.062;
		double forwardCorrect = 0.062;
		double walkCorrect = -0.062;

		double xcLeft = (xLeft - (right * rightCorrect)) * 2;
		double zcLeft = (zDistanceLeft - (forward * forwardCorrect)) * 2;

		double rotLeftSideX = xcLeft * cosine - zcLeft * sine;
		double yCornerTL = (-yHeight - (-up * upCorrect + walking * walkCorrect)) * 2;
		double yCornerBL = (0.5 - yHeight - (-up * upCorrect + walking * walkCorrect)) * 2;
		double rotLeftSideZ = zcLeft * cosine + xcLeft * sine;

		double xcRight = (xRight - (right * rightCorrect)) * 2;
		double zcRight = (zDistanceRight - (forward * forwardCorrect)) * 2;

		double rotRightSideX = xcRight * cosine - zcRight * sine;
		double yCornerTR = (-yHeight - (-up * upCorrect + walking * walkCorrect)) * 2;
		double yCornerBR = (0.5 - yHeight - (-up * upCorrect + walking * walkCorrect)) * 2;
		double rotRightSideZ = zcRight * cosine + xcRight * sine;
		
		double xPixelLeft = (rotLeftSideX / rotLeftSideZ * height + width / 2);
		double xPixelRight = (rotRightSideX / rotRightSideZ * height + width / 2);

		if (xPixelLeft >= xPixelRight)
			return;

		int xPixelLeftInt = (int) xPixelLeft;
		int xPixelRightInt = (int) xPixelRight;

		if (xPixelLeftInt < 0) {
			xPixelLeftInt = 0;
		}

		if (xPixelRightInt > width) {
			xPixelRightInt = width;
		}

		double yPixelLeftTop = yCornerTL / rotLeftSideZ * height + height / 2.0;
		double yPixelLeftButtom = yCornerBL / rotLeftSideZ * height + height / 2.0;
		double yPixelRightTop = yCornerTR / rotRightSideZ * height + height / 2.0;
		double yPixelRightButtom = yCornerBR / rotRightSideZ * height + height / 2.0;

		double tex1 = 1 / rotLeftSideZ;
		double tex2 = 1 / rotRightSideZ;
		double tex3 = 0 / rotLeftSideZ;
		double tex4 = 8 / rotRightSideZ - tex3;

		for (int x = xPixelLeftInt; x < xPixelRightInt; ++x) {
			double pixelRotation = (x - xPixelLeft) / (xPixelRight - xPixelLeft);
			
			int xTexture = (int) ((tex3 + tex4 * pixelRotation) / (tex1 + (tex2 - tex1) * pixelRotation));

			double yPixelTop = yPixelLeftTop + (yPixelRightTop - yPixelLeftTop) * pixelRotation;
			double yPixelButtom = yPixelLeftButtom + (yPixelRightButtom - yPixelLeftButtom) * pixelRotation;

			int yPixelTopInt = (int) yPixelTop;
			int yPixelButtomInt = (int) yPixelButtom;

			if (yPixelTopInt < 0) {
				yPixelTopInt = 0;
			}

			if (yPixelButtomInt > height) {
				yPixelButtomInt = height;
			}

			for (int y = yPixelTopInt; y < yPixelButtomInt; ++y) {
				double pixelRotationY = (y - yPixelTop) / (yPixelButtom - yPixelTop);
				int yTexture = (int) (8 * pixelRotationY);
				pixels[x + y * width] = xTexture * 100 + yTexture * 100 * 256; // 0x1B91E0;
				zBuffer[x + y * width] = 1 / (tex1 + (tex2 - tex1) * pixelRotation) * 8;
			}

		}

	}

	public void renderDistanceLimiter() {
		for (int i = 0; i < width * height; ++i) {
			int color = pixels[i];
			int brightness = (int) (renderDistance / zBuffer[i]);

			if (brightness < 0)
				brightness = 0;

			if (brightness > 255)
				brightness = 255;

			int r = (color >> 16) & 0xff;
			int g = (color >> 8) & 0xff;
			int b = (color) & 0xff;
			
			r = r * brightness / 255;
			g = g * brightness / 255;
			b = b * brightness / 255;
			pixels[i] = r << 16 | g << 8 | b;
		}
	}

}
