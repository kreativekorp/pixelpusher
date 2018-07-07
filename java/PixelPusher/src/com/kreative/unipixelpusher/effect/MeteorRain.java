package com.kreative.unipixelpusher.effect;

import java.awt.Component;
import java.util.Random;
import com.kreative.unipixelpusher.AbstractPixelSequence;
import com.kreative.unipixelpusher.PixelString;
import com.kreative.unipixelpusher.SequenceConfiguration;
import com.kreative.unipixelpusher.gui.ColorPatternAndIntegerAndSpeedAdjustPanel;

public class MeteorRain extends AbstractPixelSequence.ColorPattern {
	public static final String name = "Meteor Rain";
	
	protected Random random = new Random();
	protected int meteorSize = 0;
	
	public int getMeteorSize() {
		return this.meteorSize;
	}
	
	public void setMeteorSize(int meteorSize) {
		this.meteorSize = meteorSize;
	}
	
	@Override
	public void updateFrame(PixelString ps, long frame, boolean frameChanged) {
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
	public long getFrameCount(PixelString ps) {
		return ps.length() * 2;
	}
	
	@Override
	public long getFrameDuration() {
		return 30;
	}
	
	@Override
	public void loadConfiguration(SequenceConfiguration config) {
		super.loadConfiguration(config);
		this.meteorSize = config.get("meteorSize", 0);
	}
	
	@Override
	public void saveConfiguration(SequenceConfiguration config) {
		super.saveConfiguration(config);
		config.put("meteorSize", meteorSize);
	}
	
	@Override
	public Component createConfigurationPanel() {
		return new ColorPatternAndIntegerAndSpeedAdjustPanel(this, this, "Meteor Size:", 0, 999) {
			private static final long serialVersionUID = 1L;
			@Override public int getValue() { return meteorSize; }
			@Override public void setValue(int value) { meteorSize = value; }
		};
	}
	
	@Override
	public String toString() {
		return name;
	}
}
