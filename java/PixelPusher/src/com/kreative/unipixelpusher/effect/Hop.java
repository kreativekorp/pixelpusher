package com.kreative.unipixelpusher.effect;

import com.kreative.unipixelpusher.FrameBasedColorPatternPixelSequence;
import com.kreative.unipixelpusher.PixelString;

public abstract class Hop extends FrameBasedColorPatternPixelSequence {
	public static class Left extends Hop {
		public static final String name = "Hop Left";
		
		@Override
		public void updateFrame(PixelString ps, long frame, boolean frameChanged) {
			int n = ps.length();
			int sf = (int)(n - frame - 1);
			for (int i = 0; i < n; i++) {
				ps.setPixel(i, (i == sf) ? color(i) : 0);
			}
			ps.push();
		}
		
		@Override
		public String toString() {
			return name;
		}
	}
	
	public static class Right extends Hop {
		public static final String name = "Hop Right";
		
		@Override
		public void updateFrame(PixelString ps, long frame, boolean frameChanged) {
			int n = ps.length();
			int sf = (int)frame;
			for (int i = 0; i < n; i++) {
				ps.setPixel(i, (i == sf) ? color(i) : 0);
			}
			ps.push();
		}
		
		@Override
		public String toString() {
			return name;
		}
	}
	
	@Override
	protected int[] defaultColorPattern() {
		return white();
	}
	
	@Override
	public long getFrameCount(PixelString ps) {
		return ps.length();
	}
	
	@Override
	public long getFrameDuration() {
		return 50;
	}
}
