package com.kreative.unipixelpusher.effect;

import java.util.Random;
import com.kreative.unipixelpusher.ColorPatternPixelSequence;
import com.kreative.unipixelpusher.PixelString;

public class Sparkle extends ColorPatternPixelSequence {
	public static final String name = "Sparkle";
	
	protected Random random = new Random();
	
	@Override
	protected int[] defaultColorPattern() {
		return white();
	}
	
	@Override
	public void update(PixelString ps, long tick) {
		int n = ps.length();
		int si = random.nextInt(n);
		for (int i = 0; i < n; i++) {
			ps.setPixel(i, (i == si) ? color(i) : 0);
		}
		ps.push();
	}
	
	@Override
	public long getUpdateInterval() {
		return 0;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
