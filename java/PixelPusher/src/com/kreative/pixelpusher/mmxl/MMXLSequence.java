package com.kreative.pixelpusher.mmxl;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import com.kreative.pixelpusher.sequence.PixelSequence;

public class MMXLSequence extends PixelSequence {
	private static final long serialVersionUID = 1L;
	
	private int colorPatternLength;
	private int[] colorPatternColors;
	private int[] colorPatternProbabilities;
	private int colorPatternProbabilityTotal;
	private boolean colorPatternRandom;
	private transient Random randomizer;
	private transient Map<Integer,Integer> randomColors;
	private int blinkPatternFrames;
	private int blinkPatternStrands;
	private int[][] blinkPatternLevels;
	private int msPerFrame;
	private transient int frame;
	
	public MMXLSequence() {
		this(-1);
	}
	
	public MMXLSequence(int... colors) {
		colorPatternLength = colors.length;
		colorPatternColors = new int[colors.length];
		colorPatternProbabilities = new int[colors.length];
		colorPatternProbabilityTotal = colors.length;
		for (int i = 0; i < colors.length; i++) {
			colorPatternColors[i] = colors[i];
			colorPatternProbabilities[i] = 1;
		}
		colorPatternRandom = false;
		randomizer = new Random();
		randomColors = new HashMap<Integer,Integer>();
		blinkPatternFrames = 2;
		blinkPatternStrands = 1;
		blinkPatternLevels = new int[2][1];
		blinkPatternLevels[0][0] = 255;
		blinkPatternLevels[1][0] = 0;
		msPerFrame = 1000;
		frame = 0;
	}
	
	public MMXLSequence(MMXLSequence source) {
		colorPatternLength = source.colorPatternLength;
		colorPatternColors = new int[colorPatternLength];
		colorPatternProbabilities = new int[colorPatternLength];
		colorPatternProbabilityTotal = 0;
		for (int i = 0; i < colorPatternLength; i++) {
			colorPatternColors[i] = source.colorPatternColors[i];
			colorPatternProbabilities[i] = source.colorPatternProbabilities[i];
			colorPatternProbabilityTotal += colorPatternProbabilities[i];
		}
		colorPatternRandom = source.colorPatternRandom;
		randomizer = new Random();
		randomColors = new HashMap<Integer,Integer>();
		blinkPatternFrames = source.blinkPatternFrames;
		blinkPatternStrands = source.blinkPatternStrands;
		blinkPatternLevels = new int[blinkPatternFrames][blinkPatternStrands];
		for (int f = 0; f < blinkPatternFrames; f++) {
			for (int s = 0; s < blinkPatternStrands; s++) {
				blinkPatternLevels[f][s] = source.blinkPatternLevels[f][s];
			}
		}
		msPerFrame = source.msPerFrame;
		frame = 0;
	}
	
	@Override
	public synchronized MMXLSequence clone() {
		return new MMXLSequence(this);
	}
	
	public synchronized int getColorPatternLength() {
		return colorPatternLength;
	}
	
	public synchronized void setColorPatternLength(int length) {
		if (length < 1) length = 1;
		int[] colorPatternColors = new int[length];
		int[] colorPatternProbabilities = new int[length];
		int colorPatternProbabilityTotal = 0;
		for (int i = 0; i < length; i++) {
			colorPatternColors[i] = this.colorPatternColors[i % this.colorPatternColors.length];
			colorPatternProbabilities[i] = this.colorPatternProbabilities[i % this.colorPatternProbabilities.length];
			colorPatternProbabilityTotal += colorPatternProbabilities[i];
		}
		this.colorPatternLength = length;
		this.colorPatternColors = colorPatternColors;
		this.colorPatternProbabilities = colorPatternProbabilities;
		this.colorPatternProbabilityTotal = colorPatternProbabilityTotal;
		randomColors.clear();
	}
	
	public synchronized int getColorPatternColor(int index) {
		return colorPatternColors[index];
	}
	
	public synchronized void setColorPatternColor(int index, int color) {
		colorPatternColors[index] = color;
		randomColors.clear();
	}
	
	public synchronized int getColorPatternProbability(int index) {
		return colorPatternProbabilities[index];
	}
	
	public synchronized void setColorPatternProbability(int index, int probability) {
		if (probability < 1) probability = 1;
		colorPatternProbabilities[index] = probability;
		colorPatternProbabilityTotal = 0;
		for (int i : colorPatternProbabilities) {
			colorPatternProbabilityTotal += i;
		}
		randomColors.clear();
	}
	
	public synchronized int getColorPatternProbabilityTotal() {
		return colorPatternProbabilityTotal;
	}
	
	public synchronized int[] getPatternColors() {
		int[] colors = new int[colorPatternLength];
		for (int i = 0; i < colorPatternLength; i++) {
			colors[i] = colorPatternColors[i];
		}
		return colors;
	}
	
	public synchronized int[] getPatternProbabilities() {
		int[] probabilities = new int[colorPatternLength];
		for (int i = 0; i < colorPatternLength; i++) {
			probabilities[i] = colorPatternProbabilities[i];
		}
		return probabilities;
	}
	
	public synchronized void setColorPattern(int[] colors, int[] probabilities, int length) {
		if (length < 1) length = 1;
		int[] colorPatternColors = new int[length];
		int[] colorPatternProbabilities = new int[length];
		int colorPatternProbabilityTotal = 0;
		for (int i = 0; i < length; i++) {
			colorPatternColors[i] = colors[i % colors.length];
			int probability = probabilities[i % probabilities.length];
			if (probability < 1) probability = 1;
			colorPatternProbabilities[i] = probability;
			colorPatternProbabilityTotal += probability;
		}
		this.colorPatternLength = length;
		this.colorPatternColors = colorPatternColors;
		this.colorPatternProbabilities = colorPatternProbabilities;
		this.colorPatternProbabilityTotal = colorPatternProbabilityTotal;
		randomColors.clear();
	}
	
	public synchronized boolean isColorPatternRandom() {
		return colorPatternRandom;
	}
	
	public synchronized void setColorPatternRandom(boolean random) {
		this.colorPatternRandom = random;
		randomColors.clear();
	}
	
	public synchronized int getBlinkPatternFrames() {
		return blinkPatternFrames;
	}
	
	public synchronized void setBlinkPatternFrames(int frames) {
		if (frames < 1) frames = 1;
		int[][] blinkPatternLevels = new int[frames][blinkPatternStrands];
		for (int f = 0; f < frames; f++) {
			for (int s = 0; s < blinkPatternStrands; s++) {
				blinkPatternLevels[f][s] = this.blinkPatternLevels[f % blinkPatternFrames][s];
			}
		}
		this.blinkPatternFrames = frames;
		this.blinkPatternLevels = blinkPatternLevels;
	}
	
	public synchronized int getBlinkPatternStrands() {
		return blinkPatternStrands;
	}
	
	public synchronized void setBlinkPatternStrands(int strands) {
		if (strands < 1) strands = 1;
		int[][] blinkPatternLevels = new int[blinkPatternFrames][strands];
		for (int f = 0; f < blinkPatternFrames; f++) {
			for (int s = 0; s < strands; s++) {
				blinkPatternLevels[f][s] = this.blinkPatternLevels[f][s % blinkPatternStrands];
			}
		}
		this.blinkPatternStrands = strands;
		this.blinkPatternLevels = blinkPatternLevels;
	}
	
	public synchronized int getBlinkPatternLevel(int frame, int strand) {
		return blinkPatternLevels[frame][strand];
	}
	
	public synchronized void setBlinkPatternLevel(int frame, int strand, int level) {
		if (level < 0) level = 0;
		if (level > 255) level = 255;
		blinkPatternLevels[frame][strand] = level;
	}
	
	public synchronized int[][] getBlinkPatternLevels() {
		int[][] blinkPatternLevels = new int[blinkPatternFrames][blinkPatternStrands];
		for (int f = 0; f < blinkPatternFrames; f++) {
			for (int s = 0; s < blinkPatternStrands; s++) {
				blinkPatternLevels[f][s] = this.blinkPatternLevels[f][s];
			}
		}
		return blinkPatternLevels;
	}
	
	public synchronized void setBlinkPattern(int[][] levels, int frames, int strands) {
		if (frames < 1) frames = 1;
		if (strands < 1) strands = 1;
		int[][] blinkPatternLevels = new int[frames][strands];
		for (int f = 0; f < frames; f++) {
			for (int s = 0; s < strands; s++) {
				int level = levels[f][s];
				if (level < 0) level = 0;
				if (level > 255) level = 255;
				blinkPatternLevels[f][s] = level;
			}
		}
		this.blinkPatternFrames = frames;
		this.blinkPatternStrands = strands;
		this.blinkPatternLevels = blinkPatternLevels;
	}
	
	@Override
	public synchronized int getMsPerFrame() {
		return msPerFrame;
	}
	
	public synchronized void setMsPerFrame(int msPerFrame) {
		if (msPerFrame < 20) msPerFrame = 20;
		this.msPerFrame = msPerFrame;
	}
	
	@Override
	public synchronized void updatePixels(long tick) {
		frame = (int)((tick / msPerFrame) % blinkPatternFrames);
	}
	
	@Override
	public synchronized int getPixelColor(int index) {
		int level = blinkPatternLevels[frame % blinkPatternFrames][index % blinkPatternStrands];
		int color;
		if (colorPatternRandom) {
			if (randomColors.containsKey(index)) {
				color = randomColors.get(index);
			} else {
				color = randomizer.nextInt(colorPatternProbabilityTotal);
				for (int i = 0; i < colorPatternLength; i++) {
					if (color < colorPatternProbabilities[i]) {
						color = i;
						break;
					} else {
						color -= colorPatternProbabilities[i];
					}
				}
				color = colorPatternColors[color];
				randomColors.put(index, color);
			}
		} else {
			color = colorPatternColors[index % colorPatternLength];
		}
		int a = ((color >> 24) & 0xFF) * level / 255;
		return (a << 24) | (color & 0xFFFFFF);
	}
}
