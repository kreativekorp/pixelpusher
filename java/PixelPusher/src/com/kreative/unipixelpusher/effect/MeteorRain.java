package com.kreative.unipixelpusher.effect;

import java.util.Random;
import com.kreative.unipixelpusher.AbstractPixelSequence;
import com.kreative.unipixelpusher.PixelString;

public class MeteorRain extends AbstractPixelSequence.ColorPattern {
	protected Random random = new Random();
	protected int meteorSize = 0;
	
	public int getMeteorSize() {
		return this.meteorSize;
	}
	
	public void setMeteorSize(int meteorSize) {
		this.meteorSize = meteorSize;
	}
	
	@Override
	protected void updateFrame(PixelString ps, long frame, boolean frameChanged) {
		if (frameChanged) {
			int n = ps.length();
			for (int i = 0; i < n; i++) {
				if (random.nextBoolean()) {
					int c = ps.getPixel(i);
					int a = (c >> 24) & 0xFF;
					if (a <= 72) a = 0;
					else a -= a / 10;
					c = (a << 24) | (c & 0xFFFFFF);
					ps.setPixel(i, c);
				}
			}
			int m = (meteorSize > 0) ? meteorSize : ((n + 5) / 6);
			for (int i = 0, si = (int)frame; i < m && si >= 0; i++, si--) {
				if (si < n) ps.setPixel(si, color(si));
			}
			ps.push();
		}
	}
	
	@Override
	protected long getFrameCount(PixelString ps) {
		return ps.length() * 2;
	}
	
	@Override
	protected long getFrameDuration() {
		return 30;
	}
	
	@Override
	public String toString() {
		return "Meteor Rain";
	}
}
