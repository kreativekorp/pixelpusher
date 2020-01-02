package com.kreative.unipixelpusher.effect;

import java.util.Random;
import com.kreative.unipixelpusher.ColorPatternPixelSequence;
import com.kreative.unipixelpusher.ColorUtilities;
import com.kreative.unipixelpusher.PixelString;

public class Flicker extends ColorPatternPixelSequence {
	public static final String name = "Flicker";
	
	protected Random random = new Random();
	
	@Override
	protected int[] defaultColorPattern() {
		return warmWhite();
	}
	
	@Override
	public void update(PixelString ps, long tick) {
		for (int i = 0, n = ps.length(); i < n; i++) {
			int m = random.nextBoolean() ? 255 : (random.nextInt(96) + 160);
			ps.setPixel(i, ColorUtilities.multiply(color(i), m));
		}
		ps.push();
	}
	
	@Override
	public long getUpdateInterval() {
		return 72;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
