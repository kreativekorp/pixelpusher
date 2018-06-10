package com.kreative.unipixelpusher.mmxl;

import java.util.Arrays;
import com.kreative.unipixelpusher.PixelSequence;

public class MMXLColorPattern {
	private final String name;
	private final int[] colors;
	
	public MMXLColorPattern(String name, int[] colors) {
		this.name = (name == null || name.length() == 0) ? "Color Pattern" : name;
		this.colors = deepCopy(colors);
	}
	
	@Override
	public String toString() {
		return this.name;
	}
	
	public int color(int index) {
		if (colors == null || colors.length == 0) return -1;
		return colors[index % colors.length];
	}
	
	public int length() {
		if (colors == null || colors.length == 0) return 1;
		return colors.length;
	}
	
	public void apply(PixelSequence.ColorPattern sequence) {
		sequence.setColorPattern(deepCopy(colors));
	}
	
	public boolean matches(int[] colors) {
		return Arrays.equals(this.colors, colors);
	}
	
	private static int[] deepCopy(int[] a) {
		if (a == null) return null;
		int[] b = new int[a.length];
		for (int i = 0; i < a.length; i++) b[i] = a[i];
		return b;
	}
}
