package com.kreative.unipixelpusher.mmxl;

import com.kreative.unipixelpusher.AbstractPixelSequence;
import com.kreative.unipixelpusher.ColorUtilities;
import com.kreative.unipixelpusher.PixelString;

public class MMXLSequence extends AbstractPixelSequence.ColorPattern {
	public static final String name = "MoreMore Christmas Lights (MMXL)";
	
	protected long frameDuration;
	protected int[][] levels;
	
	public MMXLSequence() {
		this.frameDuration = 1000;
		this.levels = new int[][]{{255}};
	}
	
	public MMXLSequence(long frameDuration, int[][] levels) {
		this.frameDuration = frameDuration;
		this.levels = levels;
	}
	
	@Override
	public long getFrameDuration() {
		return this.frameDuration;
	}
	
	public void setFrameDuration(long frameDuration) {
		this.frameDuration = frameDuration;
	}
	
	@Override
	public long getFrameCount(PixelString ps) {
		if (levels == null || levels.length == 0) return 1;
		return levels.length;
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
	
	public int[][] getLevels() {
		return this.levels;
	}
	
	public void setLevels(int[][] levels) {
		this.levels = levels;
	}
	
	@Override
	public void updateFrame(PixelString ps, long frame, boolean frameChanged) {
		for (int i = 0, n = ps.length(); i < n; i++) {
			int m = level((int)frame, i);
			int c = ColorUtilities.multiply(color(i), m);
			ps.setPixel(i, c);
		}
		ps.push();
	}
	
	protected int level(int f, int c) {
		if (levels == null || levels.length == 0) return 255;
		int[] frame = levels[f % levels.length];
		if (frame == null || frame.length == 0) return 255;
		return frame[c % frame.length];
	}
	
	@Override
	public String toString() {
		return name;
	}
}
