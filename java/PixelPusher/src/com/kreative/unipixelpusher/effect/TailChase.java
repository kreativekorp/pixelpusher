package com.kreative.unipixelpusher.effect;

import com.kreative.unipixelpusher.AbstractPixelSequence;
import com.kreative.unipixelpusher.ColorUtilities;
import com.kreative.unipixelpusher.PixelString;

public abstract class TailChase extends AbstractPixelSequence.ColorPattern {
	public static class Left extends TailChase {
		public static final String name = "Tail Chase Left";
		
		@Override
		public void updateFrame(PixelString ps, long frame, boolean frameChanged) {
			int n = ps.length();
			int f = (int)(frame % n);
			for (int i = 0; i < n; i++) {
				int m = 255 - (255 * f / (n - 1));
				ps.setPixel(i, ColorUtilities.multiply(color(i), m));
				f++; if (f >= n) f -= n;
			}
			ps.push();
		}
		
		@Override
		public String toString() {
			return name;
		}
	}
	
	public static class Right extends TailChase {
		public static final String name = "Tail Chase Right";
		
		@Override
		public void updateFrame(PixelString ps, long frame, boolean frameChanged) {
			int n = ps.length();
			int f = (int)(frame % n);
			for (int i = 0; i < n; i++) {
				int m = 255 - (255 * f / (n - 1));
				ps.setPixel(i, ColorUtilities.multiply(color(i), m));
				f--; if (f < 0) f += n;
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
		return ps.length();
	}
	
	@Override
	public long getFrameDuration() {
		return 10;
	}
}
