package com.kreative.pixelpusher.colorcycle;

import com.kreative.pixelpusher.common.ColorConstants;
import com.kreative.pixelpusher.common.ColorUtilities;

public class ColorFaderSequence extends ColorCycleSequence {
	private static final long serialVersionUID = 1L;
	
	public ColorFaderSequence() {
		this(
			ColorConstants.RED, ColorConstants.SCARLET,
			ColorConstants.ORANGE, ColorConstants.GOLD,
			ColorConstants.YELLOW, ColorConstants.CHARTREUSE,
			ColorConstants.GREEN, ColorConstants.AQUAMARINE,
			ColorConstants.CYAN, ColorConstants.AZURE,
			ColorConstants.BLUE, ColorConstants.INDIGO,
			ColorConstants.VIOLET, ColorConstants.PURPLE,
			ColorConstants.MAGENTA, ColorConstants.ROSE
		);
	}
	
	public ColorFaderSequence(int... colors) {
		this.colors = new int[colors.length];
		for (int i = 0; i < colors.length; i++) {
			this.colors[i] = colors[i];
		}
		this.msPerFrame = 20;
		this.frame = 0;
	}
	
	public ColorFaderSequence(ColorFaderSequence source) {
		this.colors = new int[source.colors.length];
		for (int i = 0; i < source.colors.length; i++) {
			this.colors[i] = source.colors[i];
		}
		this.msPerFrame = source.msPerFrame;
		this.frame = 0;
	}
	
	@Override
	public synchronized ColorFaderSequence clone() {
		return new ColorFaderSequence(this);
	}
	
	@Override
	public synchronized void updatePixels(long tick) {
		frame = (int)((tick / msPerFrame) % (colors.length * 64));
	}
	
	@Override
	public synchronized int getPixelColor(int index) {
		int c1 = colors[(frame / 64) % colors.length];
		int c2 = colors[((frame / 64) + 1) % colors.length];
		return ColorUtilities.blendColor(c1, c2, frame % 64, 64);
	}
}
