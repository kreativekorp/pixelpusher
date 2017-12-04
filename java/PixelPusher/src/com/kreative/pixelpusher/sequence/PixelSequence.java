package com.kreative.pixelpusher.sequence;

import com.kreative.pixelpusher.array.ArrayOrdering;
import com.kreative.pixelpusher.array.PixelArray;
import com.kreative.pixelpusher.pixelset.PixelSet;

public abstract class PixelSequence extends PixelSet {
	private static final long serialVersionUID = 1L;
	
	@Override public abstract PixelSequence clone();
	@Override public final boolean isSequence() { return true; }
	@Override public final PixelSequence sequence() { return this; }
	@Override public final boolean isArray() { return false; }
	@Override public final PixelArray array() { return null; }
	@Override public abstract int getMsPerFrame();
	@Override public abstract void updatePixels(long tick);
	public abstract int getPixelColor(int index);
	
	public synchronized int[] getPixelColors(int index, int[] colors, int offset, int length) {
		if (colors == null) colors = new int[offset + length];
		while (length > 0) {
			colors[offset] = getPixelColor(index);
			index++; offset++; length--;
		}
		return colors;
	}
	
	public final synchronized PixelSubSequence subsequence(int offset) {
		return new PixelSubSequence(this, offset);
	}
	
	public final synchronized PixelSubSequence subsequence(int offset, int length) {
		return new PixelSubSequence(this, offset, length);
	}
	
	public final synchronized PixelSubSequence subsequence(int offset, int length, int step) {
		return new PixelSubSequence(this, offset, length, step);
	}
	
	public final synchronized PixelSequenceToArray toArray(ArrayOrdering ordering, int rows, int columns) {
		return new PixelSequenceToArray(this, ordering, rows, columns);
	}
}
