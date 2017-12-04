package com.kreative.pixelpusher.array;

import com.kreative.pixelpusher.pixelset.PixelSet;
import com.kreative.pixelpusher.sequence.PixelSequence;

public abstract class PixelArray extends PixelSet {
	private static final long serialVersionUID = 1L;
	
	@Override public abstract PixelArray clone();
	@Override public final boolean isSequence() { return false; }
	@Override public final PixelSequence sequence() { return null; }
	@Override public final boolean isArray() { return true; }
	@Override public final PixelArray array() { return this; }
	@Override public abstract int getMsPerFrame();
	@Override public abstract void updatePixels(long tick);
	public abstract int getPixelColor(int row, int column);
	
	public synchronized int[][] getPixelColors(int row, int column, int[][] colors, int y, int x, int height, int width) {
		if (colors == null) colors = new int[y + height][x + width];
		while (height > 0) {
			int tmpColumn = column;
			int tmpX = x;
			int tmpWidth = width;
			while (tmpWidth > 0) {
				colors[y][tmpX] = getPixelColor(row, tmpColumn);
				tmpColumn++; tmpX++; tmpWidth--;
			}
			row++; y++; height--;
		}
		return colors;
	}
	
	public final synchronized PixelSubArray subarray(int x, int y) {
		return new PixelSubArray(this, x, y);
	}
	
	public final synchronized PixelSubArray subarray(int x, int y, int width, int height) {
		return new PixelSubArray(this, x, y, width, height);
	}
	
	public final synchronized PixelSubArray subarray(int x, int y, int width, int height, int xStep, int yStep) {
		return new PixelSubArray(this, x, y, width, height, xStep, yStep);
	}
	
	public final synchronized PixelArrayRow getRow(int row) {
		return new PixelArrayRow(this, row);
	}
	
	public final synchronized PixelArrayColumn getColumn(int column) {
		return new PixelArrayColumn(this, column);
	}
	
	public final synchronized PixelArrayToSequence toSequence(ArrayOrdering ordering, int rows, int columns) {
		return new PixelArrayToSequence(this, ordering, rows, columns);
	}
}
