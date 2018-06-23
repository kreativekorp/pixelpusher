package com.kreative.unipixelpusher.xlm;

import com.kreative.unipixelpusher.SequenceConfiguration;

public class XLMChannel {
	private int patternLength;
	private int[] patternColors;
	private int[] patternCounts;
	private int patternTotal;
	private int msPerFrame;
	private boolean reversed;
	private boolean stopped;
	private long frameTime;
	private int frame;
	
	public XLMChannel(int color) {
		this.patternLength = 2;
		this.patternColors = new int[]{ color, 0 };
		this.patternCounts = new int[]{ 1, 1 };
		this.patternTotal = 2;
		this.msPerFrame = 100;
		this.reversed = false;
		this.stopped = false;
		this.frameTime = 0;
		this.frame = 0;
	}
	
	public XLMChannel(XLMChannel source) {
		this.patternLength = source.patternLength;
		this.patternColors = new int[patternLength];
		this.patternCounts = new int[patternLength];
		this.patternTotal = 0;
		for (int i = 0; i < patternLength; i++) {
			this.patternColors[i] = source.patternColors[i];
			this.patternCounts[i] = source.patternCounts[i];
			this.patternTotal += patternCounts[i];
		}
		this.msPerFrame = source.msPerFrame;
		this.reversed = source.reversed;
		this.stopped = source.stopped;
		this.frameTime = 0;
		this.frame = 0;
	}
	
	public synchronized int getPatternLength() {
		return this.patternLength;
	}
	
	public synchronized void setPatternLength(int length) {
		if (length < 1) length = 1;
		int[] patternColors = new int[length];
		int[] patternCounts = new int[length];
		int patternTotal = 0;
		for (int i = 0; i < length; i++) {
			patternColors[i] = this.patternColors[i % this.patternColors.length];
			patternCounts[i] = this.patternCounts[i % this.patternCounts.length];
			patternTotal += patternCounts[i];
		}
		this.patternLength = length;
		this.patternColors = patternColors;
		this.patternCounts = patternCounts;
		this.patternTotal = patternTotal;
	}
	
	public synchronized int getPatternColor(int index) {
		return this.patternColors[index];
	}
	
	public synchronized void setPatternColor(int index, int color) {
		this.patternColors[index] = color;
	}
	
	public synchronized int getPatternCount(int index) {
		return this.patternCounts[index];
	}
	
	public synchronized void setPatternCount(int index, int count) {
		if (count < 1) count = 1;
		this.patternCounts[index] = count;
		this.patternTotal = 0;
		for (int i : this.patternCounts) {
			this.patternTotal += i;
		}
	}
	
	public synchronized int getMsPerFrame() {
		return this.msPerFrame;
	}
	
	public synchronized void setMsPerFrame(int msPerFrame) {
		if (msPerFrame < 20) msPerFrame = 20;
		this.msPerFrame = msPerFrame;
	}
	
	public synchronized boolean isReversed() {
		return this.reversed;
	}
	
	public synchronized void setReversed(boolean reversed) {
		this.reversed = reversed;
	}
	
	public synchronized boolean isStopped() {
		return this.stopped;
	}
	
	public synchronized void setStopped(boolean stopped) {
		this.stopped = stopped;
	}
	
	public synchronized void update(long tick) {
		if (frameTime == 0 || stopped) {
			frameTime = tick;
		} else {
			long t = (tick - frameTime) / msPerFrame;
			if (t > 0) {
				frame += reversed ? t : (patternTotal - t);
				frame %= patternTotal;
				frameTime = tick;
			}
		}
	}
	
	public synchronized int getPixelColor(int index) {
		int frame = (this.frame + index) % patternTotal;
		for (int i = 0; i < patternLength; i++) {
			if (frame < patternCounts[i]) {
				return patternColors[i];
			} else {
				frame -= patternCounts[i];
			}
		}
		return patternColors[0];
	}
	
	public synchronized void loadConfiguration(SequenceConfiguration config, String prefix) {
		this.patternColors = config.get(prefix + ".color", new int[]{ -1, 0 });
		this.patternCounts = config.get(prefix + ".count", new int[]{  1, 1 });
		this.patternLength = Math.min(patternColors.length, patternCounts.length);
		this.patternTotal = 0;
		for (int i = 0; i < patternLength; i++) {
			this.patternTotal += patternCounts[i];
		}
		this.msPerFrame = config.get(prefix + ".speed", 100);
		this.reversed = config.get(prefix + ".reverse", false);
		this.stopped = config.get(prefix + ".freeze", false);
		this.frameTime = 0;
		this.frame = 0;
	}
	
	public synchronized void saveConfiguration(SequenceConfiguration config, String prefix) {
		config.put(prefix + ".color", patternColors);
		config.put(prefix + ".count", patternCounts);
		config.put(prefix + ".speed", msPerFrame);
		config.put(prefix + ".reverse", reversed);
		config.put(prefix + ".freeze", stopped);
	}
}
