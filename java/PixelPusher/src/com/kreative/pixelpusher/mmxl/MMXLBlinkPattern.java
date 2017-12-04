package com.kreative.pixelpusher.mmxl;

import java.util.Arrays;

public class MMXLBlinkPattern {
	private final String name;
	private final int msPerFrame;
	private final int frames;
	private final int strands;
	private final int[][] levels;
	
	public MMXLBlinkPattern() {
		this.name = "Blink Pattern " + System.currentTimeMillis();
		this.msPerFrame = 1000;
		this.frames = 1;
		this.strands = 1;
		this.levels = new int[][]{new int[]{255}};
	}
	
	public MMXLBlinkPattern(String name, int msPerFrame, int frames, int strands, int[][] levels) {
		this.name = (name == null || name.length() == 0) ? ("Blink Pattern " + System.currentTimeMillis()) : name;
		this.msPerFrame = (msPerFrame < 1) ? 1000 : msPerFrame;
		this.frames = (frames < 1) ? 1 : frames;
		this.strands = (strands < 1) ? 1 : strands;
		this.levels = new int[this.frames][this.strands];
		for (int i = 0; i < this.frames; i++) {
			int[] l = (levels == null || levels.length == 0) ? null : levels[i % levels.length];
			for (int j = 0; j < this.strands; j++) {
				this.levels[i][j] = (l == null || l.length == 0) ? 255 : l[j % l.length];
			}
		}
	}
	
	public String name() { return name; }
	public int getMsPerFrame() { return msPerFrame; }
	public int frames() { return frames; }
	public int strands() { return strands; }
	public int level(int i, int j) { return levels[i][j]; }
	
	public void apply(MMXLSequence sequence) {
		sequence.setBlinkPattern(levels, frames, strands);
		sequence.setMsPerFrame(msPerFrame);
	}
	
	public boolean matches(int[][] levels, int msPerFrame) {
		return Arrays.deepEquals(levels, this.levels)
		    && msPerFrame == this.msPerFrame;
	}
}
