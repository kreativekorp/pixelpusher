package com.kreative.unipixelpusher.effect;

import com.kreative.unipixelpusher.AbstractPixelSequence;
import com.kreative.unipixelpusher.ColorUtilities;
import com.kreative.unipixelpusher.PixelString;

public abstract class TailChase extends AbstractPixelSequence.ColorPattern {
	public static class Left extends TailChase {
		@Override
		protected void updateFrame(PixelString ps, long frame, boolean frameChanged) {
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
			return "Tail Chase Left";
		}
	}
	
	public static class Right extends TailChase {
		@Override
		protected void updateFrame(PixelString ps, long frame, boolean frameChanged) {
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
			return "Tail Chase Right";
		}
	}
	
	@Override
	protected long getFrameCount(PixelString ps) {
		return ps.length();
	}
	
	@Override
	protected long getFrameDuration() {
		return 10;
	}
}
