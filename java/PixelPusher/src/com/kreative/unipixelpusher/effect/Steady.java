package com.kreative.unipixelpusher.effect;

import com.kreative.unipixelpusher.PixelSequence;
import com.kreative.unipixelpusher.PixelString;

public class Steady implements PixelSequence.ColorPattern {
	public static final String name = "Steady";
	
	protected int[] colorPattern = new int[]{-1};
	
	@Override
	public void update(PixelString ps, long tick) {
		if (colorPattern == null || colorPattern.length == 0) {
			for (int i = 0, n = ps.length(); i < n; i++) {
				ps.setPixel(i, -1);
			}
		} else {
			for (int i = 0, n = ps.length(); i < n; i++) {
				ps.setPixel(i, colorPattern[i % colorPattern.length]);
			}
		}
		ps.push();
	}
	
	@Override
	public long getUpdateInterval() {
		return 1000;
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
		return name;
	}
}
