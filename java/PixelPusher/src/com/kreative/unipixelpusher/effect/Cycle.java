package com.kreative.unipixelpusher.effect;

import com.kreative.unipixelpusher.FrameBasedColorPatternPixelSequence;
import com.kreative.unipixelpusher.PixelString;

public class Cycle extends FrameBasedColorPatternPixelSequence {
	public static class OnOff extends Cycle {
		public static final String name = "Cycle On/Off";
		
		@Override
		public void updateFrame(PixelString ps, long frame, boolean frameChanged) {
			int color = ((frame & 1) != 0) ? color((int)(frame >> 1)) : 0;
			for (int i = 0, n = ps.length(); i < n; i++) {
				ps.setPixel(i, color);
			}
			ps.push();
		}
		
		@Override
		public long getFrameCount(PixelString ps) {
			return length() << 1;
		}
		
		@Override
		public String toString() {
			return name;
		}
	}
	
	public static final String name = "Cycle";
	
	@Override
	protected int[] defaultColorPattern() {
		return rainbow();
	}
	
	@Override
	public void updateFrame(PixelString ps, long frame, boolean frameChanged) {
		int color = color((int)frame);
		for (int i = 0, n = ps.length(); i < n; i++) {
			ps.setPixel(i, color);
		}
		ps.push();
	}
	
	@Override
	public long getFrameCount(PixelString ps) {
		return length();
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
