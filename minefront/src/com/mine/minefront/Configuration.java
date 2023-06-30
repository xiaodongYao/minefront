package com.mine.minefront;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class Configuration {

	Properties prop = new Properties();

	public void saveConfiguration(String key, int value) {

		String path = "res/Settings/config.xml";

		try {
			File file = new File(path);
			boolean exist = file.exists();
			if (!exist) {
				file.createNewFile();
			}
			OutputStream stream = new FileOutputStream(path);
			prop.setProperty(key, Integer.toString(value));
			prop.storeToXML(stream, "Resolution");
			stream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void loadConfiguration(String path) {
		try {
			InputStream read = new FileInputStream(path);
			prop.loadFromXML(read);
			String width = prop.getProperty("width");
			String height = prop.getProperty("height");
			setResolution(Integer.parseInt(width), Integer.parseInt(height));
			read.close();
		} catch (FileNotFoundException e) {
			saveConfiguration("width", 800);
			saveConfiguration("height", 600);
			loadConfiguration("res/Settings/config.xml");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public void setResolution(int width, int height) {
		Display.width = width;
		Display.height = height;
	}

}
