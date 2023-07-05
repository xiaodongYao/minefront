package com.mine.minefront.example;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Throw {

	public Throw() {
		try {
			loadImage();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void loadImage() throws IOException {
		BufferedImage image = ImageIO.read(getClass().getResource("test.png"));
	}
}
