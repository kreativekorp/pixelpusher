package com.kreative.unipixelpusher.effect;

import com.kreative.unipixelpusher.FrameBasedColorPatternPixelSequence;
import com.kreative.unipixelpusher.PixelString;

public class ColorWipe extends FrameBasedColorPatternPixelSequence {
	public static class OnOff extends ColorWipe {
		public static final String name = "Color Wipe On/Off";
		
		@Override
		public void updateFrame(PixelString ps, long frame, boolean frameChanged) {
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
		public long getFrameCount(PixelString ps) {
			return ps.length() * length() * 2;
		}
		
		@Override
		public String toString() {
			return name;
		}
	}
	
	public static final String name = "Color Wipe";
	
	@Override
	protected int[] defaultColorPattern() {
		return rgb();
	}
	
	@Override
	public void updateFrame(PixelString ps, long frame, boolean frameChanged) {
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
	public long getFrameCount(PixelString ps) {
		return ps.length() * length();
	}
	
	@Override
	public long getFrameDuration() {
		return 50;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
