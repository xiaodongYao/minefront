package com.mine.minefront.Graphics;

import java.util.Random;

import com.mine.minefront.Game;

public class Screen extends Render {

	private Render test;
	private Render3D render;

	public Screen(int width, int height) {
		super(width, height);
		Random random = new Random();
		test = new Render(256, 256);
		render = new Render3D(width, height);
		for (int i = 0; i < 256 * 256; ++i) {
			test.pixels[i] = random.nextInt() * (random.nextInt(5) / 4);
		}

	}

	public void render(Game game) {
		for (int i = 0; i < width * height; ++i) {
			pixels[i] = 0;
		}

//		for (int i = 0; i < 50; ++i) {
//
//			int animOne = (int) (Math.sin((game.time + i * 2) % 1000.0 / 100) * 100);
//			int animTwo = (int) (Math.cos((game.time + i * 2) % 1000.0 / 100) * 100);
//
//			// Draw(test, (width - 256) / 2 + animOne, (height - 256) / 2 - animTwo);
//
//		}
		render.floor();
		Draw(render, 0, 0);

	}
}