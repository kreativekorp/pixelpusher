package com.kreative.unipixelpusher.effect;

import java.util.Random;
import com.kreative.unipixelpusher.PixelSequence;
import com.kreative.unipixelpusher.PixelString;

public class SparkleRandom implements PixelSequence.ColorPattern {
	protected Random random = new Random();
	protected int[] colorPattern = new int[]{-1};
	
	@Override
	public void update(PixelString ps, long tick) {
		int n = ps.length();
		int si = random.nextInt(n);
		if (colorPattern == null || colorPattern.length == 0) {
			for (int i = 0; i < n; i++) {
				ps.setPixel(i, (i == si) ? -1 : 0);
			}
		} else {
			int ci = random.nextInt(colorPattern.length);
			for (int i = 0; i < n; i++) {
				ps.setPixel(i, (i == si) ? colorPattern[ci] : 0);
			}
		}
		ps.push();
	}
	
	@Override
	public long getUpdateInterval() {
		return 0;
	}
	
	@Override
	public int[] getColorPattern() {
		return this.colorPattern;
	}
	
	@Override
	public void setColorPattern(int[] colors) {
		this.colorPattern = colors;
	}
	
	@Override
	public String toString() {
		return "Random Sparkle";
	}
}
