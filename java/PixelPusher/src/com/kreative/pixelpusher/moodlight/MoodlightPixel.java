package com.kreative.pixelpusher.moodlight;

import java.io.Serializable;
import java.util.Random;
import com.kreative.pixelpusher.common.ColorUtilities;

public class MoodlightPixel implements Cloneable, Serializable {
	private static final long serialVersionUID = 1L;
	
	private static final Random RANDOM = new Random();
	private static final int MIN_TRX_TIME = 10;
	private static final int MAX_TRX_TIME = 50;
	
	private int startColor;
	private int endColor;
	private int step;
	private int stepCount;
	private long startTick;
	
	public MoodlightPixel(long tick) {
		startColor = randomColor();
		endColor = randomColor();
		step = 0;
		stepCount = randomTransitionTime();
		startTick = tick;
	}
	
	public MoodlightPixel(MoodlightPixel source) {
		startColor = source.startColor;
		endColor = source.endColor;
		step = source.step;
		stepCount = source.stepCount;
		startTick = source.startTick;
	}
	
	@Override
	public MoodlightPixel clone() {
		return new MoodlightPixel(this);
	}
	
	public void updatePixel(long tick) {
		step = (int)((tick - startTick) / 100L);
		if (step > stepCount) {
			startColor = endColor;
			endColor = randomColor();
			step = 0;
			stepCount = randomTransitionTime();
			startTick = tick;
		}
	}
	
	public int getPixelColor() {
		return ColorUtilities.blendColor(startColor, endColor, step, stepCount);
	}
	
	private static int randomTransitionTime() {
		return MIN_TRX_TIME + RANDOM.nextInt(MAX_TRX_TIME - MIN_TRX_TIME);
	}
	
	private static int randomColor() {
		int r, g, b;
		switch (RANDOM.nextInt(6)) {
		case  0: r = RANDOM.nextInt(256); g = 0; b = 255; break;
		case  1: r = RANDOM.nextInt(256); b = 0; g = 255; break;
		case  2: g = RANDOM.nextInt(256); r = 0; b = 255; break;
		case  3: g = RANDOM.nextInt(256); b = 0; r = 255; break;
		case  4: b = RANDOM.nextInt(256); r = 0; g = 255; break;
		default: b = RANDOM.nextInt(256); g = 0; r = 255; break;
		}
		return (0xFF << 24) | (r << 16) | (g << 8) | (b << 0);
	}
}
