package com.mine.minefront;

import java.awt.Canvas;

import javax.swing.JFrame;

public class Display extends Canvas {

	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;

	public static void main(String[] args) {

		Display game = new Display();
		JFrame frame = new JFrame();
		frame.add(game);
		frame.setSize(WIDTH, HEIGHT);
		frame.setResizable(true);
		frame.setVisible(true);
		// System.out.println("Hello World!");
	}
}
