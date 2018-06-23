package com.kreative.unipixelpusher.effect;

import java.util.Random;
import com.kreative.unipixelpusher.AbstractPixelSequence;
import com.kreative.unipixelpusher.PixelString;
import com.kreative.unipixelpusher.SequenceConfiguration;

public class Twinkle extends AbstractPixelSequence.ColorPattern {
	public static final String name = "Twinkle";
	
	protected Random random = new Random();
	protected int count = 0;
	
	public int getCount() {
		return this.count;
	}
	
	public void setCount(int count) {
		this.count = count;
	}
	
	@Override
	public void updateFrame(PixelString ps, long frame, boolean frameChanged) {
		if (frameChanged) {
			if (frame == 0) {
				for (int i = 0, n = ps.length(); i < n; i++) {
					ps.setPixel(i, 0);
				}
			}
			int si = random.nextInt(ps.length());
			ps.setPixel(si, color(si));
			ps.push();
		}
	}
	
	@Override
	public long getFrameCount(PixelString ps) {
		return (count > 0) ? count : ((ps.length() + 5) / 6);
	}
	
	@Override
	public long getFrameDuration() {
		return 100;
	}
	
	@Override
	public void loadConfiguration(SequenceConfiguration config) {
		super.loadConfiguration(config);
		this.count = config.get("count", 0);
	}
	
	@Override
	public void saveConfiguration(SequenceConfiguration config) {
		super.saveConfiguration(config);
		config.put("count", count);
	}
	
	@Override
	public String toString() {
		return name;
	}
}
