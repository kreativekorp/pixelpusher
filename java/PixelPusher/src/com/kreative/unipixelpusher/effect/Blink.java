package com.kreative.unipixelpusher.effect;

import com.kreative.unipixelpusher.AbstractPixelSequence;
import com.kreative.unipixelpusher.PixelString;

public class Blink extends AbstractPixelSequence.ColorPattern {
	public static final String name = "Blink";
	
	@Override
	public void updateFrame(PixelString ps, long frame, boolean frameChanged) {
		if ((frame & 1) != 0) {
			for (int i = 0, n = ps.length(); i < n; i++) {
				ps.setPixel(i, color(i));
			}
		} else {
			for (int i = 0, n = ps.length(); i < n; i++) {
				ps.setPixel(i, 0);
			}
		}
		ps.push();
	}
	
	@Override
	public long getFrameCount(PixelString ps) {
		return 2;
	}
	
	@Override
	public long getFrameDuration() {
		return 1000;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
