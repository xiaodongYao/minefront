package com.mine.minefront.Graphics;

public class Render {

	public int width;
	public int height;
	public int[] pixels;

	public Render(int width, int height) {
		this.width = width;
		this.height = height;
		this.pixels = new int[width * height];
	}

	public void Draw(Render render, int xOffset, int yOffset) {

		for (int y = 0; y < render.height; ++y) {
			int yPix = y + yOffset;
			if (yPix < 0 || yPix >= height)
				continue;

			for (int x = 0; x < render.width; ++x) {
				int xPix = x + xOffset;
				if (xPix < 0 || xPix >= width)
					continue;

				int alpha = render.pixels[x + y * render.width];
				if (alpha > 0) {
					pixels[xPix + yPix * width] = alpha;
				}
			}
		}

	}
}
