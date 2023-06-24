package com.mine.minefront.graphics;

import com.mine.minefront.Game;
import com.mine.minefront.Input.Controller;

public class Render3D extends Render {

	public double[] zBuffer;
	public double renderDistance = 5000;

	public Render3D(int width, int height) {
		super(width, height);
		zBuffer = new double[width * height];
	}


	public void floor(Game game) {

		double floorPosition = 8;
		double ceilingPosition = 800;

		double forward = game.controls.z;
		double right = game.controls.x;
		double up = game.controls.y;
		double walking = Math.sin(game.time / 6.0) * 0.5;

		if (Controller.crouchWalk) {
			walking = Math.sin(game.time / 6.0) * 0.2;
		}
		if (Controller.runWalk) {
			walking = Math.sin(game.time / 6.0) * 1.0;
		}

		// double rotation = game.time / 100.0;
		double rotation = game.controls.rotation;
		double cosine = Math.cos(rotation);
		double sine = Math.sin(rotation);

		for (int y = 0; y < height; y++) {

			double ceiling = (y - height / 2.0) / height;

			double z = (floorPosition + up) / ceiling;

			if (Controller.walk) {
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
