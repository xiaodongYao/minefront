package com.mine.minefront.Graphics;

import java.util.Random;

public class Screen extends Render {

	private Render test;

	public Screen(int width, int height) {
		super(width, height);
		test = new Render(256, 256);
		Random random = new Random();
		for (int i = 0; i < 256 * 256; ++i) {
			test.pixels[i] = random.nextInt() * (random.nextInt(5) / 4);
		}

	}

	public void render() {
		for (int i = 0; i < width * height; ++i) {
			pixels[i] = 0;
		}

		for (int i = 0; i < 25; ++i) {
			long nowTime = System.currentTimeMillis() + i * 4;
			double param = nowTime % 2000 / 2000.0 * Math.PI * 2;

			int animOne = (int) (Math.sin(param) * 200);
			int animTwo = (int) (Math.cos(param) * 200);

			Draw(test, (width - 256) / 2 + animOne, (height - 256) / 2 - animTwo);
		}

	}
}