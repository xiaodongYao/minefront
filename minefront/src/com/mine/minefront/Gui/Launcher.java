package com.mine.minefront.Gui;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferStrategy;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import com.mine.minefront.Configuration;
import com.mine.minefront.Display;
import com.mine.minefront.RunGame;
import com.mine.minefront.Input.InputHandler;

public class Launcher extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;

	protected JPanel window = new JPanel();
	private JButton play, options, help, quit;
	private Rectangle rplay, roptions, rhelp, rquit;
	Configuration config = new Configuration();

	private int width = 800;
	private int height = 400;

	protected int button_width = 80;
	protected int button_height = 40;

	private Thread thread;
	private boolean running;
	JFrame frame = new JFrame();

	public Launcher(int type, Display display) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		frame.setUndecorated(true);
		frame.setTitle("Minefront Launcher");
		frame.setSize(new Dimension(width, height));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// getContentPane().add(window);
		frame.add(this);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
		window.setLayout(null);
		if (type == 0) {
			drawButtons();
		}
		InputHandler input = new InputHandler();
		addKeyListener(input);
		addFocusListener(input);
		addMouseListener(input);
		addMouseMotionListener(input);

		startMenu();
		display.start();
		frame.repaint();

	}

	private void updateFrame() {
		if (InputHandler.dragged) {
			Point p = frame.getLocation();
			frame.setLocation(p.x + InputHandler.MouseDX - InputHandler.MousePX, p.y + InputHandler.MouseDY - InputHandler.MousePY);
		}
	}

	private void startMenu() {
		if (running)
			return;
		running = true;
		thread = new Thread(this, "menu");
		thread.start();
	}

	private void stopMenu() {
		if (!running)
			return;
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while (running) {
			renderMenu();
			updateFrame();
			// Thread.sleep(1);
		}
	}

	private void renderMenu() throws IllegalStateException {
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 800, 400);
		try {
			g.drawImage(ImageIO.read(Launcher.class.getResource("/menu_image.jpg")), 0, 0, 800, 400, null);
			if (InputHandler.MouseX > 690 && InputHandler.MouseX < 690 + 80 && InputHandler.MouseY > 130 && InputHandler.MouseY < 130 + 30) {
				g.drawImage(ImageIO.read(Launcher.class.getResource("/menu/play_on.png")), 690, 130, 80, 30, null);
				g.drawImage(ImageIO.read(Launcher.class.getResource("/menu/arrow.png")), 690 + 80, 134, 20, 20, null);
				if (InputHandler.MouseButton == 1) {
					config.loadConfiguration("res/Settings/config.xml");
					frame.dispose();// 关闭当前窗口
					new RunGame();
				}
			}else {
				g.drawImage(ImageIO.read(Launcher.class.getResource("/menu/play_off.png")), 690, 130, 80, 30, null);	
			}
			if (InputHandler.MouseX > 641 && InputHandler.MouseX < 641 + 130 && InputHandler.MouseY > 170 && InputHandler.MouseY < 170 + 30) {
				g.drawImage(ImageIO.read(Launcher.class.getResource("/menu/options_on.png")), 641, 170, 130, 30, null);
				g.drawImage(ImageIO.read(Launcher.class.getResource("/menu/arrow.png")), 641 + 130, 174, 20, 20, null);
				if (InputHandler.MouseButton == 1) {

				}
			} else {
				g.drawImage(ImageIO.read(Launcher.class.getResource("/menu/options_off.png")), 641, 170, 130, 30, null);
			}
			if (InputHandler.MouseX > 690 && InputHandler.MouseX < 690 + 80 && InputHandler.MouseY > 210 && InputHandler.MouseY < 210 + 30) {
				g.drawImage(ImageIO.read(Launcher.class.getResource("/menu/help_on.png")), 690, 210, 80, 30, null);
				g.drawImage(ImageIO.read(Launcher.class.getResource("/menu/arrow.png")), 690 + 80, 214, 20, 20, null);
				if (InputHandler.MouseButton == 1) {

				}
			} else {
				g.drawImage(ImageIO.read(Launcher.class.getResource("/menu/help_off.png")), 690, 210, 80, 30, null);
			}
			if (InputHandler.MouseX > 690 && InputHandler.MouseX < 690 + 80 && InputHandler.MouseY > 250 && InputHandler.MouseY < 250 + 30) {
				g.drawImage(ImageIO.read(Launcher.class.getResource("/menu/exit_on.png")), 690, 250, 80, 30, null);
				g.drawImage(ImageIO.read(Launcher.class.getResource("/menu/arrow.png")), 690 + 80, 254, 20, 20, null);
				if (InputHandler.MouseButton == 1) {
					System.exit(0);
				}
			} else {
				g.drawImage(ImageIO.read(Launcher.class.getResource("/menu/exit_off.png")), 690, 250, 80, 30, null);
			}
			

		} catch (IOException e) {
			e.printStackTrace();
		}
//		g.setColor(Color.white);
//		g.setFont(new Font("Consolas", 0, 30));
//		g.drawString("Play", 700, 90);
		g.dispose();
		bs.show();
	}

	private void drawButtons() {
		play = new JButton("Play");
		rplay = new Rectangle(width / 2 - button_width / 2, 90, button_width, button_height);
		play.setBounds(rplay);
		window.add(play);

		options = new JButton("Options");
		roptions = new Rectangle(width / 2 - button_width / 2, 140, button_width, button_height);
		options.setBounds(roptions);
		window.add(options);

		help = new JButton("Help");
		rhelp = new Rectangle(width / 2 - button_width / 2, 190, button_width, button_height);
		help.setBounds(rhelp);
		window.add(help);

		quit = new JButton("Quit");
		rquit = new Rectangle(width / 2 - button_width / 2, 240, button_width, button_height);
		quit.setBounds(rquit);
		window.add(quit);

		play.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				config.loadConfiguration("res/Settings/config.xml");
				frame.dispose();// 关闭当前窗口
				new RunGame();
			}
		});

		options.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				new Options();
			}
		});

		help.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

			}
		});

		quit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
	}

}
