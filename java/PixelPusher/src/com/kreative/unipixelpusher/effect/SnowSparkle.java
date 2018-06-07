package com.kreative.unipixelpusher.effect;

import java.util.Random;
import com.kreative.unipixelpusher.AbstractPixelSequence;
import com.kreative.unipixelpusher.ColorUtilities;
import com.kreative.unipixelpusher.PixelString;

public class SnowSparkle extends AbstractPixelSequence.ColorPattern {
	protected Random random = new Random();
	
	@Override
	public void updateFrame(PixelString ps, long frame, boolean frameChanged) {
		int n = ps.length();
		for (int i = 0; i < n; i++) {
			ps.setPixel(i, ColorUtilities.multiply(color(i), 88));
		}
		if (frame == 0) {
			int i = random.nextInt(n);
			ps.setPixel(i, color(i));
		}
		ps.push();
	}
	
	@Override
	public long getFrameCount(PixelString ps) {
		return 11;
	}
	
	@Override
	public long getFrameDuration() {
		return 20;
	}
	
	@Override
	public String toString() {
		return "Snow Sparkle";
	}
}
