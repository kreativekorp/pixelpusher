package com.kreative.pixelpusher.array;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import com.kreative.pixelpusher.pixelset.PixelSet;
import com.kreative.pixelpusher.sequence.PixelSequence;

public class PixelArrayRow extends PixelSequence {
	private static final long serialVersionUID = 1L;
	
	private PixelArray array;
	private int row;
	
	public PixelArrayRow() {
		this(null, 0);
	}
	
	public PixelArrayRow(PixelArray array) {
		this(array, 0);
	}
	
	public PixelArrayRow(int row) {
		this(null, row);
	}
	
	public PixelArrayRow(PixelArray array, int row) {
		this.array = array;
		this.row = row;
	}
	
	public PixelArrayRow(PixelArrayRow source) {
		this.array = source.array;
		this.row = source.row;
	}
	
	@Override
	public synchronized PixelArrayRow clone() {
		return new PixelArrayRow(this);
	}
	
	public synchronized PixelArray getArray() {
		return array;
	}
	
	public synchronized void setArray(PixelArray array) {
		checkForCycles(array);
		this.array = array;
	}
	
	public synchronized int getRowNumber() {
		return row;
	}
	
	public synchronized void setRowNumber(int rowNumber) {
		this.row = rowNumber;
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
			return array.getPixelColor(row, index);
		} else {
			return 0;
		}
	}
	
	@Override
	public synchronized int[] getPixelColors(int index, int[] colors, int offset, int length) {
		if (colors == null) colors = new int[offset + length];
		if (array != null) {
			while (length > 0) {
				colors[offset] = array.getPixelColor(row, index);
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
