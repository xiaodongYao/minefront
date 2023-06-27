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
		for (int i = 0; i < test.width * test.height; i++) {
			test.pixels[i] = random.nextInt() * (random.nextInt(5) / 4);
		}
	}

	public void render(Game game) {
		// 相当于清除上一次的东西，清空
		for (int i = 0; i < width * height; i++) {
			pixels[i] = 0;
		}

		render.floor(game);
		render.RenderWalls(0, 0.5, 1.5, 1.5, 0);
		render.RenderWalls(0, 0, 1, 1.5, 0);
		render.RenderWalls(0, 0.5, 1, 1, 0);
		render.RenderWalls(0.5, 0.5, 1, 1.5, 0);
		render.renderDistanceLimiter();

		draw(render, 0, 0);
	}

}
