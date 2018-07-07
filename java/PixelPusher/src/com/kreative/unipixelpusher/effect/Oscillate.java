package com.kreative.unipixelpusher.effect;

import com.kreative.unipixelpusher.FrameBasedColorPatternPixelSequence;
import com.kreative.unipixelpusher.PixelString;

public class Oscillate extends FrameBasedColorPatternPixelSequence {
	public static final String name = "Oscillate";
	
	@Override
	protected int[] defaultColorPattern() {
		return white();
	}
	
	@Override
	public void updateFrame(PixelString ps, long frame, boolean frameChanged) {
		int n = ps.length();
		int sf = (int)((frame < n) ? (n - frame - 1) : (frame - n));
		for (int i = 0; i < n; i++) {
			ps.setPixel(i, (i == sf) ? color(i) : 0);
		}
		ps.push();
	}
	
	@Override
	public long getFrameCount(PixelString ps) {
		return ps.length() << 1;
	}
	
	@Override
	public long getFrameDuration() {
		return 50;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
