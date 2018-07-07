package com.kreative.unipixelpusher.effect;

import java.util.Random;
import com.kreative.unipixelpusher.ColorPatternPixelSequence;
import com.kreative.unipixelpusher.PixelString;

public class Static extends ColorPatternPixelSequence {
	public static final String name = "Static";
	
	protected Random random = new Random();
	
	@Override
	protected int[] defaultColorPattern() {
		return white();
	}
	
	@Override
	public void update(PixelString ps, long tick) {
		for (int i = 0, n = ps.length(), nc = length(); i < n; i++) {
			int ci = random.nextInt(nc + 1);
			ps.setPixel(i, (ci < nc) ? color(ci) : 0);
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
