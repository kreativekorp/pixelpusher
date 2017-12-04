package com.kreative.pixelpusher.colorcycle;

import com.kreative.pixelpusher.common.ColorConstants;

public class ColorScrollerSequence extends ColorCycleSequence {
	private static final long serialVersionUID = 1L;
	
	public ColorScrollerSequence() {
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
	
	public ColorScrollerSequence(int... colors) {
		this.colors = new int[colors.length];
		for (int i = 0; i < colors.length; i++) {
			this.colors[i] = colors[i];
		}
		this.msPerFrame = 20;
		this.frame = 0;
	}
	
	public ColorScrollerSequence(ColorScrollerSequence source) {
		this.colors = new int[source.colors.length];
		for (int i = 0; i < source.colors.length; i++) {
			this.colors[i] = source.colors[i];
		}
		this.msPerFrame = source.msPerFrame;
		this.frame = 0;
	}
	
	@Override
	public synchronized ColorScrollerSequence clone() {
		return new ColorScrollerSequence(this);
	}
	
	@Override
	public synchronized int getPixelColor(int index) {
		return colors[((colors.length - frame) + index) % colors.length];
	}
}
