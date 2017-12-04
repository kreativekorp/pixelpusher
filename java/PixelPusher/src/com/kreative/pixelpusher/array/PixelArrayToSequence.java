package com.kreative.pixelpusher.array;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import com.kreative.pixelpusher.pixelset.PixelSet;
import com.kreative.pixelpusher.sequence.PixelSequence;

public class PixelArrayToSequence extends PixelSequence {
	private static final long serialVersionUID = 1L;
	
	private PixelArray array;
	private ArrayOrdering ordering;
	private int rows;
	private int columns;
	
	public PixelArrayToSequence() {
		this(null, null, 8, 8);
	}
	
	public PixelArrayToSequence(PixelArray array, ArrayOrdering ordering, int rows, int columns) {
		if (ordering == null) ordering = ArrayOrdering.LTR_TTB;
		if (rows < 1) rows = 1;
		if (columns < 1) columns = 1;
		this.array = array;
		this.ordering = ordering;
		this.rows = rows;
		this.columns = columns;
	}
	
	public PixelArrayToSequence(PixelArrayToSequence source) {
		this.array = source.array;
		this.ordering = source.ordering;
		this.rows = source.rows;
		this.columns = source.columns;
	}
	
	@Override
	public synchronized PixelArrayToSequence clone() {
		return new PixelArrayToSequence(this);
	}
	
	public synchronized PixelArray getArray() {
		return array;
	}
	
	public synchronized void setArray(PixelArray array) {
		checkForCycles(array);
		this.array = array;
	}
	
	public synchronized ArrayOrdering getOrdering() {
		return ordering;
	}
	
	public synchronized void setOrdering(ArrayOrdering ordering) {
		if (ordering == null) ordering = ArrayOrdering.LTR_TTB;
		this.ordering = ordering;
	}
	
	public synchronized int getRowCount() {
		return rows;
	}
	
	public synchronized void setRowCount(int rowCount) {
		if (rowCount < 1) rowCount = 1;
		this.rows = rowCount;
	}
	
	public synchronized int getColumnCount() {
		return columns;
	}
	
	public synchronized void setColumnCount(int columnCount) {
		if (columnCount < 1) columnCount = 1;
		this.columns = columnCount;
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
			int[] yx = ordering.getYX(rows, columns, index, null);
			return array.getPixelColor(yx[0], yx[1]);
		} else {
			return 0;
		}
	}
	
	@Override
	public synchronized int[] getPixelColors(int index, int[] colors, int offset, int length) {
		if (colors == null) colors = new int[offset + length];
		if (array != null) {
			int[] yx = new int[2];
			while (length > 0) {
				ordering.getYX(rows, columns, index, yx);
				colors[offset] = array.getPixelColor(yx[0], yx[1]);
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
