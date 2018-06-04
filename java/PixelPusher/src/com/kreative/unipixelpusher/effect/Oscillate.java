package com.kreative.unipixelpusher.effect;

import com.kreative.unipixelpusher.AbstractPixelSequence;
import com.kreative.unipixelpusher.PixelString;

public class Oscillate extends AbstractPixelSequence.ColorPattern {
	@Override
	protected void updateFrame(PixelString ps, long frame, boolean frameChanged) {
		int n = ps.length();
		int sf = (int)((frame < n) ? (n - frame - 1) : (frame - n));
		for (int i = 0; i < n; i++) {
			ps.setPixel(i, (i == sf) ? color(i) : 0);
		}
		ps.push();
	}
	
	@Override
	protected long getFrameCount(PixelString ps) {
		return ps.length() << 1;
	}
	
	@Override
	protected long getFrameDuration() {
		return 50;
	}
	
	@Override
	public String toString() {
		return "Oscillate";
	}
}
