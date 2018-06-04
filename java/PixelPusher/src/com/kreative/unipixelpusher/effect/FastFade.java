package com.kreative.unipixelpusher.effect;

import com.kreative.unipixelpusher.AbstractPixelSequence;
import com.kreative.unipixelpusher.ColorUtilities;
import com.kreative.unipixelpusher.PixelString;

public abstract class FastFade extends AbstractPixelSequence.ColorPattern {
	public static class InOut extends FastFade {
		@Override
		protected void updateFrame(PixelString ps, long frame, boolean frameChanged) {
			int m = (int)(((frame & 0x100) != 0) ? (~frame) : frame) & 0xFF;
			for (int i = 0, n = ps.length(); i < n; i++) {
				ps.setPixel(i, ColorUtilities.multiply(color(i), m));
			}
			ps.push();
		}
		
		@Override
		protected long getFrameCount(PixelString ps) {
			return 0x200;
		}
		
		@Override
		public String toString() {
			return "Fast Fade In/Out";
		}
	}
	
	public static class Cycle extends FastFade {
		@Override
		protected void updateFrame(PixelString ps, long frame, boolean frameChanged) {
			int ci = (int)(frame >> 8);
			int sf = (int)(frame & 0xFF);
			int color = ColorUtilities.blend(color(ci), color(ci + 1), sf, 0x100);
			for (int i = 0, n = ps.length(); i < n; i++) {
				ps.setPixel(i, color);
			}
			ps.push();
		}
		
		@Override
		protected long getFrameCount(PixelString ps) {
			return length() << 8;
		}
		
		@Override
		public String toString() {
			return "Fast Fade Cycle";
		}
	}
	
	public static class CycleInOut extends FastFade {
		@Override
		protected void updateFrame(PixelString ps, long frame, boolean frameChanged) {
			int ci = (int)(frame >> 9);
			int m = (int)(((frame & 0x100) != 0) ? (~frame) : frame) & 0xFF;
			int color = ColorUtilities.multiply(color(ci), m);
			for (int i = 0, n = ps.length(); i < n; i++) {
				ps.setPixel(i, color);
			}
			ps.push();
		}
		
		@Override
		protected long getFrameCount(PixelString ps) {
			return length() << 9;
		}
		
		@Override
		public String toString() {
			return "Fast Fade Cycle In/Out";
		}
	}
	
	@Override
	protected long getFrameDuration() {
		return 3;
	}
}
