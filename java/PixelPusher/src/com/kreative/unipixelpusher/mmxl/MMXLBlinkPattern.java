package com.kreative.unipixelpusher.mmxl;

import java.util.Arrays;

public class MMXLBlinkPattern {
	private final String name;
	private final int frameDuration;
	private final int[][] levels;
	
	public MMXLBlinkPattern(String name, int frameDuration, int[][] levels) {
		this.name = (name == null || name.length() == 0) ? "Blink Pattern" : name;
		this.frameDuration = (frameDuration < 1) ? 1000 : frameDuration;
		this.levels = deepCopy(levels);
	}
	
	@Override
	public String toString() {
		return this.name;
	}
	
	public int getFrameDuration() {
		return this.frameDuration;
	}
	
	public int getFrameCount() {
		if (levels == null || levels.length == 0) return 1;
		return levels.length;
	}
	
	public int getChannelCount() {
		if (levels == null || levels.length == 0) return 1;
		if (levels[0] == null || levels[0].length == 0) return 1;
		return levels[0].length;
	}
	
	public int level(int f, int c) {
		if (levels == null || levels.length == 0) return 255;
		int[] frame = levels[f % levels.length];
		if (frame == null || frame.length == 0) return 255;
		return frame[c % frame.length];
	}
	
	public void apply(MMXLSequence sequence) {
		sequence.setFrameDuration(frameDuration);
		sequence.setLevels(deepCopy(levels));
	}
	
	public boolean matches(int frameDuration, int[][] levels) {
		return this.frameDuration == frameDuration && Arrays.deepEquals(this.levels, levels);
	}
	
	private static int[] deepCopy(int[] a) {
		if (a == null) return null;
		int[] b = new int[a.length];
		for (int i = 0; i < a.length; i++) b[i] = a[i];
		return b;
	}
	
	private static int[][] deepCopy(int[][] a) {
		if (a == null) return null;
		int[][] b = new int[a.length][];
		for (int i = 0; i < a.length; i++) b[i] = deepCopy(a[i]);
		return b;
	}
}
