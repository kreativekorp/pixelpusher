package com.kreative.unipixelpusher.effect;

import com.kreative.unipixelpusher.ColorUtilities;
import com.kreative.unipixelpusher.FrameBasedColorPatternPixelSequence;
import com.kreative.unipixelpusher.PixelString;

public abstract class SlowFade extends FrameBasedColorPatternPixelSequence {
	public static class InOut extends SlowFade {
		public static final String name = "Slow Fade In/Out";
		
		@Override
		protected int[] defaultColorPattern() {
			return white();
		}
		
		@Override
		public void updateFrame(PixelString ps, long frame, boolean frameChanged) {
			int m = (int)((frame >> 8) & 0x03);
			switch (m) {
				case 0: m = (int)(frame & 0xFF); break;
				case 1: m = 255; break;
				case 2: m = (int)((~frame) & 0xFF); break;
				case 3: m = 0; break;
			}
			for (int i = 0, n = ps.length(); i < n; i++) {
				ps.setPixel(i, ColorUtilities.multiply(color(i), m));
			}
			ps.push();
		}
		
		@Override
		public long getFrameCount(PixelString ps) {
			return 0x400;
		}
		
		@Override
		public String toString() {
			return name;
		}
	}
	
	public static class Cycle extends SlowFade {
		public static final String name = "Slow Fade Cycle";
		
		@Override
		public void updateFrame(PixelString ps, long frame, boolean frameChanged) {
			int ci = (int)(frame >> 9);
			int sf = ((frame & 0x100) != 0) ? 255 : (int)(frame & 0xFF);
			int color = ColorUtilities.blend(color(ci), color(ci + 1), sf, 0x100);
			for (int i = 0, n = ps.length(); i < n; i++) {
				ps.setPixel(i, color);
			}
			ps.push();
		}
		
		@Override
		public long getFrameCount(PixelString ps) {
			return length() << 9;
		}
		
		@Override
		public String toString() {
			return name;
		}
	}
	
	public static class CycleInOut extends SlowFade {
		public static final String name = "Slow Fade Cycle In/Out";
		
		@Override
		public void updateFrame(PixelString ps, long frame, boolean frameChanged) {
			int ci = (int)(frame >> 10);
			int m = (int)((frame >> 8) & 0x03);
			switch (m) {
				case 0: m = (int)(frame & 0xFF); break;
				case 1: m = 255; break;
				case 2: m = (int)((~frame) & 0xFF); break;
				case 3: m = 0; break;
			}
			int color = ColorUtilities.multiply(color(ci), m);
			for (int i = 0, n = ps.length(); i < n; i++) {
				ps.setPixel(i, color);
			}
			ps.push();
		}
		
		@Override
		public long getFrameCount(PixelString ps) {
			return length() << 10;
		}
		
		@Override
		public String toString() {
			return name;
		}
	}
	
	@Override
	protected int[] defaultColorPattern() {
		return rainbow();
	}
	
	@Override
	public long getFrameDuration() {
		return 5;
	}
}
