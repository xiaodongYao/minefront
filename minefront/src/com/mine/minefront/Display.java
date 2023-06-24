package com.mine.minefront;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

import com.mine.minefront.Input.Controller;
import com.mine.minefront.Input.InputHandler;
import com.mine.minefront.graphics.Screen;

public class Display extends Canvas implements Runnable  {
	
    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;
    public static final String TITLE = "MineFront Pre-Alpha 0.01";

    private Thread thread;
    private boolean running = false;
    private Screen screen;
    private Game game;
	private InputHandler input;

    private BufferedImage img;
    private int[] pixels;

	private int newX = 0;
	private int oldX = 0;

	private int fps;
    public Display() {
		Dimension size = new Dimension(WIDTH, HEIGHT);
		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);
        screen = new Screen(WIDTH, HEIGHT);
        game = new Game();
        img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        pixels = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
		input = new InputHandler();

		addKeyListener(input);
		addFocusListener(input);
		addMouseListener(input);
		addMouseMotionListener(input);
    }

    private void start() {
        if (running)
            return;
        running = true;
        thread = new Thread(this);
        thread.start();

        System.out.println("working....");
    }

    private void stop() {
        if (!running)
            return;
        running = false;
        try {
            thread.join();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    @Override
    public void run() {
        int frames = 0;
        double unprocessedSeconds = 0; //尚未处理的秒数
        long previousTime = System.nanoTime();
        double secondsPerTick = 1 / 60.0;
        int tickCount = 0;
        boolean ticked = false;
        while (running) {
            long currentTime = System.nanoTime();
            long passedTime = currentTime - previousTime;
            previousTime = currentTime; //更新当前时间
            unprocessedSeconds += passedTime / 1000000000.0;

            while (unprocessedSeconds > secondsPerTick) {
                tick();
                unprocessedSeconds -= secondsPerTick;
                ticked = true;
                tickCount++;
                if (tickCount % 60 == 0) {
					// System.out.println(frames + "fps");
					fps = frames;
                    previousTime += 1000;
                    frames = 0;
                }
            }
            if (ticked) {
                render(); //渲染呈现
                frames++;
            }
            render();
            frames++;

			newX = InputHandler.MouseX;
			if (newX > oldX) {
				Controller.turnRight = true;
			}
			if (newX < oldX) {
				Controller.turnLeft = true;
			}
			if (newX == oldX) {
				Controller.turnRight = false;
				Controller.turnLeft = false;
			}
			oldX = newX;
        }

    }

    private void tick() {
		game.tick(input.key);
    }

    private void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }

        screen.render(game);
        for (int i = 0; i < WIDTH * HEIGHT; i++) {
            pixels[i] = screen.pixels[i];
        }

        Graphics g = bs.getDrawGraphics();
        g.drawImage(img, 0, 0, WIDTH, HEIGHT, null);
		// 字体
		g.setFont(new Font("Consola", 0, 50));
		g.setColor(Color.WHITE);
		g.drawString(fps + " FPS", 20, 50);

        g.dispose();
        bs.show();
    }


    public static void main(String[] args) {
		BufferedImage cursor = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		Cursor blank = Toolkit.getDefaultToolkit().createCustomCursor(cursor, new Point(0, 0), "blank");

        Display game = new Display();
        JFrame frame = new JFrame();

        frame.add(game);
        frame.pack();

		frame.getContentPane().setCursor(blank);

        frame.setTitle(TITLE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //界面关闭,程序也关闭

        //frame.setSize(WIDTH, HEIGHT);
        frame.setLocationRelativeTo(null); //居中打开
        frame.setResizable(true);  //调整大小
        frame.setVisible(true);  //可见性

        game.start();
        System.out.println("Running....");
    }

}
