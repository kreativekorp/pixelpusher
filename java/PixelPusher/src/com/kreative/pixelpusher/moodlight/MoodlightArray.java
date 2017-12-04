package com.kreative.pixelpusher.moodlight;

import java.util.HashMap;
import java.util.Map;
import com.kreative.pixelpusher.array.PixelArray;

public class MoodlightArray extends PixelArray {
	private static final long serialVersionUID = 1L;
	
	private Map<Integer,Map<Integer,MoodlightPixel>> pixels;
	private long lastTick;
	
	public MoodlightArray() {
		this.pixels = new HashMap<Integer,Map<Integer,MoodlightPixel>>();
		this.lastTick = 0;
	}
	
	@Override
	public synchronized MoodlightArray clone() {
		return new MoodlightArray();
	}
	
	@Override
	public synchronized int getMsPerFrame() {
		return 100;
	}
	
	@Override
	public synchronized void updatePixels(long tick) {
		for (Map<Integer,MoodlightPixel> r : pixels.values()) {
			for (MoodlightPixel pixel : r.values()) {
				pixel.updatePixel(tick);
			}
		}
		lastTick = tick;
	}
	
	@Override
	public synchronized int getPixelColor(int row, int column) {
		Map<Integer,MoodlightPixel> r;
		if (pixels.containsKey(row)) {
			r = pixels.get(row);
		} else {
			r = new HashMap<Integer,MoodlightPixel>();
			pixels.put(row, r);
		}
		if (r.containsKey(column)) {
			return r.get(column).getPixelColor();
		} else {
			MoodlightPixel pixel = new MoodlightPixel(lastTick);
			r.put(column, pixel);
			return pixel.getPixelColor();
		}
	}
}
