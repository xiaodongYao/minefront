package com.mine.minefront;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import com.mine.minefront.Graphics.Screen;
import com.mine.minefront.Gui.Launcher;
import com.mine.minefront.Input.Controller;
import com.mine.minefront.Input.InputHandler;

public class Display extends Canvas implements Runnable  {
	

	private static final long serialVersionUID = 1L;
	public static final int WIDTH = 800;
    public static final int HEIGHT = 600;

	public static int width = 800;
	public static int height = 600;

	public static final String TITLE = "MineFront Pre-Alpha 0.02";

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

	public static int MouseSpeed = 0;

	public static int selection = 0;

    public Display() {
		Dimension size = new Dimension(GetGameWidth(), GetGameHeight());
		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);
		screen = new Screen(GetGameWidth(), GetGameHeight());
        game = new Game();
		img = new BufferedImage(GetGameWidth(), GetGameHeight(), BufferedImage.TYPE_INT_RGB);
        pixels = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
		input = new InputHandler();

		addKeyListener(input);
		addFocusListener(input);
		addMouseListener(input);
		addMouseMotionListener(input);
    }

	public int GetGameWidth() {
		return width;
	}

	public int GetGameHeight() {
		return height;
	}

	public synchronized void start() {
        if (running)
            return;
        running = true;
		thread = new Thread(this, "game");
        thread.start();

        System.out.println("working....");
    }

	public synchronized void stop() {
        if (!running)
            return;
        running = false;
        try {
            thread.join();
        } catch (Exception e) {
            e.printStackTrace();
			System.exit(1);
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
		requestFocus();
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
					System.out.println(frames + "fps");
					fps = frames;
                    previousTime += 1000;
                    frames = 0;
                }
				if (ticked) {
//    				render(); // 渲染呈现
					frames++;
				}
            }
//				else {
//				render();
//				frames++;
//			}

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
			MouseSpeed = Math.abs(newX - oldX);
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
		for (int i = 0; i < GetGameWidth() * GetGameHeight(); i++) {
            pixels[i] = screen.pixels[i];
        }

        Graphics g = bs.getDrawGraphics();
		g.drawImage(img, 0, 0, GetGameWidth(), GetGameHeight(), null);
		// 字体
		g.setFont(new Font("Consola", 0, 50));
		g.setColor(Color.WHITE);
		g.drawString(fps + " FPS", 20, 50);

        g.dispose();
        bs.show();
    }


    public static void main(String[] args) {
		Display display = new Display();
		new Launcher(0, display);
    }

}
