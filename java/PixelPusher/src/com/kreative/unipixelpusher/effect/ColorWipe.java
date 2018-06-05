package com.kreative.unipixelpusher.effect;

import com.kreative.unipixelpusher.AbstractPixelSequence;
import com.kreative.unipixelpusher.PixelString;

public class ColorWipe extends AbstractPixelSequence.ColorPattern {
	public static class OnOff extends ColorWipe {
		@Override
		protected void updateFrame(PixelString ps, long frame, boolean frameChanged) {
			int n = ps.length();
			int ci = (int)(frame / n);
			int si = (int)(frame % n);
			int color = ((ci & 1) != 0) ? 0 : color(ci >> 1);
			for (int i = 0; i <= si; i++) {
				ps.setPixel(i, color);
			}
			ps.push();
		}
		
		@Override
		protected long getFrameCount(PixelString ps) {
			return ps.length() * length() * 2;
		}
		
		@Override
		public String toString() {
			return "Color Wipe On/Off";
		}
	}
	
	@Override
	protected void updateFrame(PixelString ps, long frame, boolean frameChanged) {
		int n = ps.length();
		int ci = (int)(frame / n);
		int si = (int)(frame % n);
		int color = color(ci);
		for (int i = 0; i <= si; i++) {
			ps.setPixel(i, color);
		}
		ps.push();
	}
	
	@Override
	protected long getFrameCount(PixelString ps) {
		return ps.length() * length();
	}
	
	@Override
	protected long getFrameDuration() {
		return 50;
	}
	
	@Override
	public String toString() {
		return "Color Wipe";
	}
}
