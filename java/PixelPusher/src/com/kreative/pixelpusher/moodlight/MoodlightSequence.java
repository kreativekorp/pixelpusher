package com.kreative.pixelpusher.moodlight;

import java.util.HashMap;
import java.util.Map;
import com.kreative.pixelpusher.sequence.PixelSequence;

public class MoodlightSequence extends PixelSequence {
	private static final long serialVersionUID = 1L;
	
	private Map<Integer,MoodlightPixel> pixels;
	private long lastTick;
	
	public MoodlightSequence() {
		this.pixels = new HashMap<Integer,MoodlightPixel>();
		this.lastTick = 0;
	}
	
	@Override
	public synchronized MoodlightSequence clone() {
		return new MoodlightSequence();
	}
	
	@Override
	public synchronized int getMsPerFrame() {
		return 100;
	}
	
	@Override
	public synchronized void updatePixels(long tick) {
		for (MoodlightPixel pixel : pixels.values()) {
			pixel.updatePixel(tick);
		}
		lastTick = tick;
	}
	
	@Override
	public synchronized int getPixelColor(int index) {
		if (pixels.containsKey(index)) {
			return pixels.get(index).getPixelColor();
		} else {
			MoodlightPixel pixel = new MoodlightPixel(lastTick);
			pixels.put(index, pixel);
			return pixel.getPixelColor();
		}
	}
}
