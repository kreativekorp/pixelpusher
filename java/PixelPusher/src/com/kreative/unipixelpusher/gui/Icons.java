package com.kreative.unipixelpusher.gui;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

public class Icons {
	private static final Map<Pair<?,Integer>,Image> cache = new HashMap<Pair<?,Integer>,Image>();
	
	public static Image getIcon(Class<?> cls, int size) {
		while (cls != null) {
			Pair<Class<?>,Integer> cacheKey = new Pair<Class<?>,Integer>(cls, size);
			if (cache.containsKey(cacheKey)) return cache.get(cacheKey);
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
					if (image != null) {
						cache.put(cacheKey, image);
						return image;
					}
				}
			} catch (IOException ignored) {}
			cls = cls.getSuperclass();
		}
		return new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
	}
	
	public static <E extends Enum<E>> Image getIcon(Class<E> enumType, E value, int size) {
		if (enumType != null && value != null) {
			Pair<E,Integer> cacheKey = new Pair<E,Integer>(value, size);
			if (cache.containsKey(cacheKey)) return cache.get(cacheKey);
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
					if (image != null) {
						cache.put(cacheKey, image);
						return image;
					}
				}
			} catch (IOException ignored) {}
		}
		return getIcon(enumType, size);
	}
	
	private static class Pair<A,B> {
		private final A a;
		private final B b;
		private Pair(A a, B b) {
			this.a = a;
			this.b = b;
		}
		@Override
		public boolean equals(Object o) {
			if (o == this) return true;
			if (o == null) return false;
			if (o instanceof Pair) {
				Pair<?,?> that = (Pair<?,?>)o;
				return equals(this.a, that.a)
				    && equals(this.b, that.b);
			}
			return false;
		}
		@Override
		public int hashCode() {
			return ((a != null) ? a.hashCode() : 0)
			     + ((b != null) ? b.hashCode() : 0);
		}
		private static boolean equals(Object a, Object b) {
			if (a == b) return true;
			if (a == null || b == null) return false;
			return a.equals(b);
		}
	}
}
