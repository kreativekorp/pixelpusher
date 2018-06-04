package com.kreative.unipixelpusher.effect;

import com.kreative.unipixelpusher.AbstractPixelSequence;
import com.kreative.unipixelpusher.PixelString;

public abstract class Walk extends AbstractPixelSequence.ColorPattern {
	public static class Left extends Walk {
		@Override
		protected void updateFrame(PixelString ps, long frame, boolean frameChanged) {
			int n = ps.length();
			int nf = n << 1;
			int sf = (int)(nf - frame - 1);
			int i1 = sf >> 1;
			int i2 = (i1 + (sf & 1)) % n;
			for (int i = 0; i < n; i++) {
				ps.setPixel(i, (i == i1 || i == i2) ? color(i) : 0);
			}
			ps.push();
		}
		
		@Override
		protected long getFrameCount(PixelString ps) {
			return ps.length() << 1;
		}
		
		@Override
		public String toString() {
			return "Walk Left";
		}
	}
	
	public static class Right extends Walk {
		@Override
		protected void updateFrame(PixelString ps, long frame, boolean frameChanged) {
			int n = ps.length();
			int sf = (int)frame;
			int i1 = sf >> 1;
			int i2 = (i1 + (sf & 1)) % n;
			for (int i = 0; i < n; i++) {
				ps.setPixel(i, (i == i1 || i == i2) ? color(i) : 0);
			}
			ps.push();
		}
		
		@Override
		protected long getFrameCount(PixelString ps) {
			return ps.length() << 1;
		}
		
		@Override
		public String toString() {
			return "Walk Right";
		}
	}
	
	public static class LeftRight extends Walk {
		@Override
		protected void updateFrame(PixelString ps, long frame, boolean frameChanged) {
			int n = ps.length();
			int nf = n << 1;
			int sf = (int)((frame < nf) ? (nf - frame - 1) : (frame - nf));
			int i1 = sf >> 1;
			int i2 = (i1 + (sf & 1)) % n;
			for (int i = 0; i < n; i++) {
				ps.setPixel(i, (i == i1 || i == i2) ? color(i) : 0);
			}
			ps.push();
		}
		
		@Override
		protected long getFrameCount(PixelString ps) {
			return ps.length() << 2;
		}
		
		@Override
		public String toString() {
			return "Walk Left/Right";
		}
	}
	
	@Override
	protected long getFrameDuration() {
		return 50;
	}
}