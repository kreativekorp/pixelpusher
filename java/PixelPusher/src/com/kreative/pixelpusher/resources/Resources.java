package com.kreative.pixelpusher.resources;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Resources {
	private Resources() {}
	
	public static InputStream getInputStream(String name) {
		return Resources.class.getResourceAsStream(name.toLowerCase());
	}
	
	private static Map<String, Image> imageCache = new HashMap<String, Image>();
	
	public static Image getImage(String name) {
		name = name.toLowerCase() + ".png";
		if (imageCache.containsKey(name)) {
			return imageCache.get(name);
		} else {
			URL url = Resources.class.getResource(name);
			Image image = Toolkit.getDefaultToolkit().createImage(url);
			imageCache.put(name, image);
			return image;
		}
	}
	
	public static Image getImage(String name, int size) {
		return getImage(name + "_" + size);
	}
	
	public static Image getImage(String category, String name) {
		return getImage(category + "_" + name);
	}
	
	public static Image getImage(String category, String name, int size) {
		return getImage(category + "_" + name + "_" + size);
	}
	
	public static Image getImage(String category, String name, String value) {
		return getImage(category + "_" + name + "_" + value);
	}
	
	public static Image getImage(String category, String name, String value, int size) {
		return getImage(category + "_" + name + "_" + value + "_" + size);
	}
	
	private static Map<String, Font> fontCache = new HashMap<String, Font>();
	
	public static Font getFont(String name) {
		name = name.toLowerCase() + ".ttf";
		if (fontCache.containsKey(name)) {
			return fontCache.get(name);
		} else try {
			InputStream in = Resources.class.getResourceAsStream(name);
			Font font = Font.createFont(Font.TRUETYPE_FONT, in);
			in.close();
			fontCache.put(name, font);
			return font;
		} catch (FontFormatException ffe) {
			return null;
		} catch (IOException ioe) {
			return null;
		}
	}
	
	public static Font getFont(String name, int size) {
		Font font = getFont(name);
		if (font == null) return null;
		return font.deriveFont((float)size);
	}
}
