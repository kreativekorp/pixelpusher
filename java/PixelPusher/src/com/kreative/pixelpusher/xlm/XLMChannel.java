package com.kreative.pixelpusher.xlm;

import com.kreative.pixelpusher.common.ColorConstants;
import com.kreative.pixelpusher.sequence.PixelSequence;

public class XLMChannel extends PixelSequence {
	private static final long serialVersionUID = 1L;
	
	private int patternLength;
	private int[] patternColors;
	private int[] patternCounts;
	private int patternCountTotal;
	private int msPerFrame;
	private boolean reversed;
	private boolean stopped;
	private transient boolean updateBase;
	private transient long tickBase;
	private transient int frameBase;
	private transient int frame;
	
	public XLMChannel() {
		this(ColorConstants.RED, 0);
	}
	
	public XLMChannel(int colorOn, int colorOff) {
		patternLength = 2;
		patternColors = new int[]{colorOn, colorOff};
		patternCounts = new int[]{1, 1};
		patternCountTotal = 2;
		msPerFrame = 100;
		reversed = false;
		stopped = false;
		updateBase = true;
		tickBase = 0;
		frameBase = 0;
		frame = 0;
	}
	
	public XLMChannel(XLMChannel source) {
		patternLength = source.patternLength;
		patternColors = new int[patternLength];
		patternCounts = new int[patternLength];
		patternCountTotal = 0;
		for (int i = 0; i < patternLength; i++) {
			patternColors[i] = source.patternColors[i];
			patternCounts[i] = source.patternCounts[i];
			patternCountTotal += patternCounts[i];
		}
		msPerFrame = source.msPerFrame;
		reversed = source.reversed;
		stopped = source.stopped;
		updateBase = true;
		tickBase = 0;
		frameBase = 0;
		frame = 0;
	}
	
	@Override
	public synchronized XLMChannel clone() {
		return new XLMChannel(this);
	}
	
	public synchronized int getPatternLength() {
		return patternLength;
	}
	
	public synchronized void setPatternLength(int length) {
		if (length < 1) length = 1;
		int[] patternColors = new int[length];
		int[] patternCounts = new int[length];
		int patternCountTotal = 0;
		for (int i = 0; i < length; i++) {
			patternColors[i] = this.patternColors[i % this.patternColors.length];
			patternCounts[i] = this.patternCounts[i % this.patternCounts.length];
			patternCountTotal += patternCounts[i];
		}
		this.patternLength = length;
		this.patternColors = patternColors;
		this.patternCounts = patternCounts;
		this.patternCountTotal = patternCountTotal;
		updateBase = true;
	}
	
	public synchronized int getPatternColor(int index) {
		return patternColors[index];
	}
	
	public synchronized void setPatternColor(int index, int color) {
		patternColors[index] = color;
	}
	
	public synchronized int getPatternCount(int index) {
		return patternCounts[index];
	}
	
	public synchronized void setPatternCount(int index, int count) {
		if (count < 1) count = 1;
		patternCounts[index] = count;
		patternCountTotal = 0;
		for (int i : patternCounts) {
			patternCountTotal += i;
		}
		updateBase = true;
	}
	
	public synchronized int[] getPatternColors() {
		int[] colors = new int[patternLength];
		for (int i = 0; i < patternLength; i++) {
			colors[i] = patternColors[i];
		}
		return colors;
	}
	
	public synchronized int[] getPatternCounts() {
		int[] counts = new int[patternLength];
		for (int i = 0; i < patternLength; i++) {
			counts[i] = patternCounts[i];
		}
		return counts;
	}
	
	public synchronized void setPattern(int[] colors, int[] counts, int length) {
		if (length < 1) length = 1;
		int[] patternColors = new int[length];
		int[] patternCounts = new int[length];
		int patternCountTotal = 0;
		for (int i = 0; i < length; i++) {
			patternColors[i] = colors[i % colors.length];
			int count = counts[i % counts.length];
			if (count < 1) count = 1;
			patternCounts[i] = count;
			patternCountTotal += count;
		}
		this.patternLength = length;
		this.patternColors = patternColors;
		this.patternCounts = patternCounts;
		this.patternCountTotal = patternCountTotal;
		updateBase = true;
	}
	
	@Override
	public synchronized int getMsPerFrame() {
		return msPerFrame;
	}
	
	public synchronized void setMsPerFrame(int msPerFrame) {
		if (msPerFrame < 20) msPerFrame = 20;
		this.msPerFrame = msPerFrame;
		updateBase = true;
	}
	
	public synchronized boolean isReversed() {
		return reversed;
	}
	
	public synchronized void setReversed(boolean reversed) {
		this.reversed = reversed;
		updateBase = true;
	}
	
	public synchronized boolean isStopped() {
		return stopped;
	}
	
	public synchronized void setStopped(boolean stopped) {
		this.stopped = stopped;
		updateBase = true;
	}
	
	@Override
	public synchronized void updatePixels(long tick) {
		if (updateBase) {
			updateBase = false;
			tickBase = tick;
			frameBase = reversed ? frame : (patternCountTotal - frame);
		}
		if (!stopped) {
			int t = (int)((((tick - tickBase) / msPerFrame) + frameBase) % patternCountTotal);
			frame = reversed ? t : (patternCountTotal - t);
		}
	}
	
	@Override
	public synchronized int getPixelColor(int index) {
		int frame = (this.frame + index) % patternCountTotal;
		for (int i = 0; i < patternCounts.length; i++) {
			if (frame < patternCounts[i]) {
				return patternColors[i];
			} else {
				frame -= patternCounts[i];
			}
		}
		return patternColors[0];
	}
}
