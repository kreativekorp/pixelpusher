package com.kreative.unipixelpusher.effect;

import java.util.Random;
import com.kreative.unipixelpusher.PixelSequence;
import com.kreative.unipixelpusher.PixelString;
import com.kreative.unipixelpusher.SequenceConfiguration;

public class Static implements PixelSequence.ColorPattern {
	public static final String name = "Static";
	
	protected Random random = new Random();
	protected int[] colorPattern = new int[]{-1};
	
	@Override
	public void update(PixelString ps, long tick) {
		if (colorPattern == null || colorPattern.length == 0) {
			for (int i = 0, n = ps.length(); i < n; i++) {
				ps.setPixel(i, random.nextBoolean() ? -1 : 0);
			}
		} else {
			for (int i = 0, n = ps.length(); i < n; i++) {
				int ci = random.nextInt(colorPattern.length + 1);
				ps.setPixel(i, (ci < colorPattern.length) ? colorPattern[ci] : 0);
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
	public void loadConfiguration(SequenceConfiguration config) {
		this.colorPattern = config.get("colorPattern", new int[]{-1});
	}
	
	@Override
	public void saveConfiguration(SequenceConfiguration config) {
		config.put("colorPattern", colorPattern);
	}
	
	@Override
	public String toString() {
		return name;
	}
}
