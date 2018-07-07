package com.kreative.unipixelpusher.effect;

import java.awt.Component;
import java.util.Random;
import com.kreative.unipixelpusher.FrameBasedColorPatternPixelSequence;
import com.kreative.unipixelpusher.PixelString;
import com.kreative.unipixelpusher.SequenceConfiguration;
import com.kreative.unipixelpusher.gui.ColorPatternAndIntegerAndSpeedAdjustPanel;

public class TwinkleRandom extends FrameBasedColorPatternPixelSequence {
	public static final String name = "Random Twinkle";
	
	protected Random random = new Random();
	protected int count = 0;
	
	public int getCount() {
		return this.count;
	}
	
	public void setCount(int count) {
		this.count = count;
	}
	
	@Override
	protected int[] defaultColorPattern() {
		return rainbow();
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
			int ci = random.nextInt(length());
			ps.setPixel(si, color(ci));
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
	public Component createConfigurationPanel() {
		return new ColorPatternAndIntegerAndSpeedAdjustPanel(this, this, "Max Twinkles:", 0, 999) {
			private static final long serialVersionUID = 1L;
			@Override public int getValue() { return count; }
			@Override public void setValue(int value) { count = value; }
		};
	}
	
	@Override
	public String toString() {
		return name;
	}
}
