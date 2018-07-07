package com.kreative.unipixelpusher.effect;

import com.kreative.unipixelpusher.ColorPatternPixelSequence;
import com.kreative.unipixelpusher.PixelString;

public class Steady extends ColorPatternPixelSequence {
	public static final String name = "Steady";
	
	@Override
	protected int[] defaultColorPattern() {
		return white();
	}
	
	@Override
	public void update(PixelString ps, long tick) {
		for (int i = 0, n = ps.length(); i < n; i++) {
			ps.setPixel(i, color(i));
		}
		ps.push();
	}
	
	@Override
	public long getUpdateInterval() {
		return 1000;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
