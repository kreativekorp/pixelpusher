package com.kreative.pixelpusher.sequence;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import com.kreative.pixelpusher.array.ArrayOrdering;
import com.kreative.pixelpusher.array.PixelArray;
import com.kreative.pixelpusher.pixelset.PixelSet;

public class PixelSequenceToArray extends PixelArray {
	private static final long serialVersionUID = 1L;
	
	private PixelSequence sequence;
	private ArrayOrdering ordering;
	private int rows;
	private int columns;
	
	public PixelSequenceToArray() {
		this(null, ArrayOrdering.LTR_TTB, 8, 8);
	}
	
	public PixelSequenceToArray(PixelSequence sequence, ArrayOrdering ordering, int rows, int columns) {
		if (ordering == null) ordering = ArrayOrdering.LTR_TTB;
		if (rows < 1) rows = 1;
		if (columns < 1) columns = 1;
		this.sequence = sequence;
		this.ordering = ordering;
		this.rows = rows;
		this.columns = columns;
	}
	
	public PixelSequenceToArray(PixelSequenceToArray source) {
		this.sequence = source.sequence;
		this.ordering = source.ordering;
		this.rows = source.rows;
		this.columns = source.columns;
	}
	
	@Override
	public synchronized PixelSequenceToArray clone() {
		return new PixelSequenceToArray(this);
	}
	
	public synchronized PixelSequence getSequence() {
		return sequence;
	}
	
	public synchronized void setSequence(PixelSequence sequence) {
		checkForCycles(sequence);
		this.sequence = sequence;
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
		if (sequence != null) {
			return sequence.getMsPerFrame();
		} else {
			return 1000;
		}
	}
	
	@Override
	public synchronized void updatePixels(long tick) {
		if (sequence != null) {
			sequence.updatePixels(tick);
		}
	}
	
	@Override
	public synchronized int getPixelColor(int row, int column) {
		if (sequence != null) {
			int index = ordering.getIndex(rows, columns, row, column);
			return sequence.getPixelColor(index);
		} else {
			return 0;
		}
	}
	
	@Override
	public synchronized Collection<? extends PixelSet> getDependencies() {
		return Collections.unmodifiableCollection(Arrays.asList(sequence));
	}
}
