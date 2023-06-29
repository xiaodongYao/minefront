package com.mine.minefront;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class RunGame {

	public RunGame() {
		BufferedImage cursor = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		Cursor blank = Toolkit.getDefaultToolkit().createCustomCursor(cursor, new Point(0, 0), "blank");

		Display game = new Display();
		JFrame frame = new JFrame();

		frame.add(game);
		frame.pack();

		// frame.getContentPane().setCursor(blank);

		frame.setTitle(Display.TITLE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 界面关闭,程序也关闭

		// frame.setSize(WIDTH, HEIGHT);
		frame.setLocationRelativeTo(null); // 居中打开
		frame.setResizable(true); // 调整大小
		frame.setVisible(true); // 可见性

		game.start();
		System.out.println("Running....");
	}

}
