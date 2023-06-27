package com.mine.minefront.Graphics;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class Texture {
	public static Render floor = loadBitmap("/Textures/floor.png");

	public static Render loadBitmap(String fileName) {
		try {
			BufferedImage image = ImageIO.read(Texture.class.getResource(fileName));
			int width = image.getWidth();
			int hegiht = image.getHeight();
			Render result = new Render(width, hegiht);
			image.getRGB(0, 0, width, hegiht, result.pixels, 0, width);
			return result;

		} catch (Exception e) {
			System.out.println("CRASH!");
			throw new RuntimeException(e);

		}
	}
}
