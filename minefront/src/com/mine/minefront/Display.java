package com.mine.minefront;

import java.awt.Canvas;

import javax.swing.JFrame;

public class Display extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;
	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;
	public static final String Title = "Minefront Pre-Alpha 0.01";

	private Thread thread;
	private boolean running = false;

	private void start() {
		if (running)
			return;

		running = true;
		thread = new Thread(this);
		thread.start();
		System.out.println("working");
	}

	private void stop() {
		if (!running)
			return;

		running = false;
		try {
			thread.join(); //当前线程阻塞，等待thread线程完成，main线程再结束
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	public void run() {
		while(running) {
			
		}
	}

	public static void main(String[] args) {

		Display game = new Display();
		JFrame frame = new JFrame();
		frame.add(game);
		frame.pack(); // 根据窗口中的组件自动调整窗口的大小
		frame.setTitle(Title);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 设置窗口的关闭操作
		frame.setSize(WIDTH, HEIGHT);
		frame.setLocationRelativeTo(null); // 根据窗口中的组件自动调整窗口的大小
		frame.setResizable(true);
		frame.setVisible(true);

		System.out.println("Running....!");
		
		game.start();
	}
}
