package com.kreative.unipixelpusher.gui;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;

public class Icons {
	public static Image getIcon(Class<?> cls, int size) {
		while (cls != null) {
			try {
				String name = cls.getSimpleName() + "." + size + ".png";
				Class<?> ecls = cls.getEnclosingClass();
				while (ecls != null) {
					name = ecls.getSimpleName() + "." + name;
					ecls = ecls.getEnclosingClass();
				}
				URL url = cls.getResource(name);
				if (url != null) {
					Image image = ImageIO.read(url);
					if (image != null) return image;
				}
			} catch (IOException ignored) {}
			cls = cls.getSuperclass();
		}
		return new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
	}
	
	public static <E extends Enum<E>> Image getIcon(Class<E> enumType, E value, int size) {
		if (enumType != null && value != null) {
			try {
				String name = enumType.getSimpleName() + "." + value.name() + "." + size + ".png";
				Class<?> ecls = enumType.getEnclosingClass();
				while (ecls != null) {
					name = ecls.getSimpleName() + "." + name;
					ecls = ecls.getEnclosingClass();
				}
				URL url = enumType.getResource(name);
				if (url != null) {
					Image image = ImageIO.read(url);
					if (image != null) return image;
				}
			} catch (IOException ignored) {}
		}
		return getIcon(enumType, size);
	}
}
