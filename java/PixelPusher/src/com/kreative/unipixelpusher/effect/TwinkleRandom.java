package com.kreative.unipixelpusher.effect;

import java.util.Random;
import com.kreative.unipixelpusher.AbstractPixelSequence;
import com.kreative.unipixelpusher.PixelString;

public class TwinkleRandom extends AbstractPixelSequence.ColorPattern {
	protected Random random = new Random();
	protected int count = 0;
	
	public int getCount() {
		return this.count;
	}
	
	public void setCount(int count) {
		this.count = count;
	}
	
	@Override
	protected void updateFrame(PixelString ps, long frame, boolean frameChanged) {
		if (frameChanged) {
			if (frame == 0) {
				for (int i = 0, n = ps.length(); i < n; i++) {
					ps.setPixel(i, 0);
				}
			}
			int si = random.nextInt(ps.length());
			int ci = random.nextInt(length());
			ps.setPixel(si, color(ci));
			ps.push();
		}
	}
	
	@Override
	protected long getFrameCount(PixelString ps) {
		return (count > 0) ? count : ((ps.length() + 5) / 6);
	}
	
	@Override
	protected long getFrameDuration() {
		return 100;
	}
	
	@Override
	public String toString() {
		return "Random Twinkle";
	}
}
