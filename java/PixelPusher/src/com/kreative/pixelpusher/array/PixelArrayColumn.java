package com.kreative.pixelpusher.array;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import com.kreative.pixelpusher.pixelset.PixelSet;
import com.kreative.pixelpusher.sequence.PixelSequence;

public class PixelArrayColumn extends PixelSequence {
	private static final long serialVersionUID = 1L;
	
	private PixelArray array;
	private int column;
	
	public PixelArrayColumn() {
		this(null, 0);
	}
	
	public PixelArrayColumn(PixelArray array) {
		this(array, 0);
	}
	
	public PixelArrayColumn(int column) {
		this(null, column);
	}
	
	public PixelArrayColumn(PixelArray array, int column) {
		this.array = array;
		this.column = column;
	}
	
	public PixelArrayColumn(PixelArrayColumn source) {
		this.array = source.array;
		this.column = source.column;
	}
	
	@Override
	public synchronized PixelArrayColumn clone() {
		return new PixelArrayColumn(this);
	}
	
	public synchronized PixelArray getArray() {
		return array;
	}
	
	public synchronized void setArray(PixelArray array) {
		checkForCycles(array);
		this.array = array;
	}
	
	public synchronized int getColumnNumber() {
		return column;
	}
	
	public synchronized void setColumnNumber(int columnNumber) {
		this.column = columnNumber;
	}
	
	@Override
	public synchronized int getMsPerFrame() {
		if (array != null) {
			return array.getMsPerFrame();
		} else {
			return 1000;
		}
	}
	
	@Override
	public synchronized void updatePixels(long tick) {
		if (array != null) {
			array.updatePixels(tick);
		}
	}
	
	@Override
	public synchronized int getPixelColor(int index) {
		if (array != null) {
			return array.getPixelColor(index, column);
		} else {
			return 0;
		}
	}
	
	@Override
	public synchronized int[] getPixelColors(int index, int[] colors, int offset, int length) {
		if (colors == null) colors = new int[offset + length];
		if (array != null) {
			while (length > 0) {
				colors[offset] = array.getPixelColor(index, column);
				index++; offset++; length--;
			}
		} else {
			while (length > 0) {
				colors[offset] = 0;
				index++; offset++; length--;
			}
		}
		return colors;
	}
	
	@Override
	public synchronized Collection<? extends PixelSet> getDependencies() {
		return Collections.unmodifiableCollection(Arrays.asList(array));
	}
}
