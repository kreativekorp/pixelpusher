package com.kreative.unipixelpusher.effect;

import java.util.Random;
import com.kreative.unipixelpusher.FrameBasedColorPatternPixelSequence;
import com.kreative.unipixelpusher.PixelString;

public class TwinkleEndless extends FrameBasedColorPatternPixelSequence {
	public static final String name = "Endless Twinkle";
	
	protected Random random = new Random();
	
	@Override
	protected int[] defaultColorPattern() {
		return rainbow();
	}
	
	@Override
	public void updateFrame(PixelString ps, long frame, boolean frameChanged) {
		if (frameChanged) {
			int si = random.nextInt(ps.length());
			int ci = random.nextInt(length());
			ps.setPixel(si, color(ci));
			ps.push();
		}
	}
	
	@Override
	public long getFrameCount(PixelString ps) {
		return 0;
	}
	
	@Override
	public long getFrameDuration() {
		return 100;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
