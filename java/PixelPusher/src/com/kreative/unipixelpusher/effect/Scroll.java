package com.kreative.unipixelpusher.effect;

import com.kreative.unipixelpusher.AbstractPixelSequence;
import com.kreative.unipixelpusher.PixelString;

public abstract class Scroll extends AbstractPixelSequence.ColorPattern {
	public static class Left extends Scroll {
		public static final String name = "Scroll Left";
		
		@Override
		public void updateFrame(PixelString ps, long frame, boolean frameChanged) {
			int f = (int)frame;
			for (int i = 0, n = ps.length(); i < n; i++) {
				ps.setPixel(i, color(f++));
			}
			ps.push();
		}
		
		@Override
		public String toString() {
			return name;
		}
	}
	
	public static class Right extends Scroll {
		public static final String name = "Scroll Right";
		
		@Override
		public void updateFrame(PixelString ps, long frame, boolean frameChanged) {
			int f = (int)(length() - frame - 1);
			for (int i = 0, n = ps.length(); i < n; i++) {
				ps.setPixel(i, color(f++));
			}
			ps.push();
		}
		
		@Override
		public String toString() {
			return name;
		}
	}
	
	@Override
	public long getFrameCount(PixelString ps) {
		return length();
	}
	
	@Override
	public long getFrameDuration() {
		return 50;
	}
}
