package com.kreative.pixelpusher.colorcycle;

import com.kreative.pixelpusher.common.ColorConstants;
import com.kreative.pixelpusher.sequence.PixelSequence;

public class ColorCycleSequence extends PixelSequence {
	private static final long serialVersionUID = 1L;
	
	protected int[] colors;
	protected int msPerFrame;
	protected transient int frame;
	
	public ColorCycleSequence() {
		this(
			ColorConstants.RED,
			ColorConstants.ORANGE,
			ColorConstants.YELLOW,
			ColorConstants.GREEN,
			ColorConstants.CYAN,
			ColorConstants.BLUE,
			ColorConstants.VIOLET,
			ColorConstants.MAGENTA
		);
	}
	
	public ColorCycleSequence(int... colors) {
		this.colors = new int[colors.length];
		for (int i = 0; i < colors.length; i++) {
			this.colors[i] = colors[i];
		}
		this.msPerFrame = 1000;
		this.frame = 0;
	}
	
	public ColorCycleSequence(ColorCycleSequence source) {
		this.colors = new int[source.colors.length];
		for (int i = 0; i < source.colors.length; i++) {
			this.colors[i] = source.colors[i];
		}
		this.msPerFrame = source.msPerFrame;
		this.frame = 0;
	}
	
	@Override
	public synchronized ColorCycleSequence clone() {
		return new ColorCycleSequence(this);
	}
	
	public synchronized int getColorCount() {
		return colors.length;
	}
	
	public synchronized void setColorCount(int count) {
		if (count < 1) count = 1;
		int[] colors = new int[count];
		for (int i = 0; i < count; i++) {
			colors[i] = this.colors[i % this.colors.length];
		}
		this.colors = colors;
		this.frame = 0;
	}
	
	public synchronized int getColor(int index) {
		return colors[index];
	}
	
	public synchronized void setColor(int index, int color) {
		colors[index] = color;
	}
	
	public synchronized int[] getColors() {
		int[] colors = new int[this.colors.length];
		for (int i = 0; i < this.colors.length; i++) {
			colors[i] = this.colors[i];
		}
		return colors;
	}
	
	public synchronized void setColors(int... colors) {
		this.colors = new int[colors.length];
		for (int i = 0; i < colors.length; i++) {
			this.colors[i] = colors[i];
		}
		this.frame = 0;
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
		frame = (int)((tick / msPerFrame) % colors.length);
	}
	
	@Override
	public synchronized int getPixelColor(int index) {
		return colors[frame % colors.length];
	}
}
