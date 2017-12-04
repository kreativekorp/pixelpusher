package com.kreative.pixelpusher.sequence;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import com.kreative.pixelpusher.pixelset.PixelSet;

public class PixelSubSequence extends PixelSequence {
	private static final long serialVersionUID = 1L;
	
	private PixelSequence sequence;
	private int offset;
	private int length;
	private int step;
	
	public PixelSubSequence() {
		this(null, 0, 0, 1);
	}
	
	public PixelSubSequence(PixelSequence sequence) {
		this(sequence, 0, 0, 1);
	}
	
	public PixelSubSequence(PixelSequence sequence, int offset) {
		this(sequence, offset, 0, 1);
	}
	
	public PixelSubSequence(PixelSequence sequence, int offset, int length) {
		this(sequence, offset, length, 1);
	}
	
	public PixelSubSequence(PixelSequence sequence, int offset, int length, int step) {
		this.sequence = sequence;
		this.offset = offset;
		this.length = length;
		this.step = step;
	}
	
	public PixelSubSequence(PixelSubSequence source) {
		this.sequence = source.sequence;
		this.offset = source.offset;
		this.length = source.length;
		this.step = source.step;
	}
	
	@Override
	public synchronized PixelSubSequence clone() {
		return new PixelSubSequence(this);
	}
	
	public synchronized PixelSequence getSequence() {
		return sequence;
	}
	
	public synchronized void setSequence(PixelSequence sequence) {
		checkForCycles(sequence);
		this.sequence = sequence;
	}
	
	public synchronized int getOffset() {
		return offset;
	}
	
	public synchronized void setOffset(int offset) {
		this.offset = offset;
	}
	
	public synchronized int getLength() {
		return length;
	}
	
	public synchronized void setLength(int length) {
		this.length = length;
	}
	
	public synchronized int getStep() {
		return step;
	}
	
	public synchronized void setStep(int step) {
		this.step = step;
	}
	
	public synchronized void setSequence(PixelSequence sequence, int offset, int length, int step) {
		checkForCycles(sequence);
		this.sequence = sequence;
		this.offset = offset;
		this.length = length;
		this.step = step;
	}
	
	public synchronized void setOffsetLength(int offset, int length, int step) {
		this.offset = offset;
		this.length = length;
		this.step = step;
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
	public synchronized int getPixelColor(int index) {
		if (sequence != null) {
			if (length != 0) index %= length;
			return sequence.getPixelColor(offset + index * step);
		} else {
			return 0;
		}
	}
	
	@Override
	public synchronized int[] getPixelColors(int index, int[] colors, int offset, int length) {
		if (colors == null) colors = new int[offset + length];
		if (sequence != null) {
			while (length > 0) {
				if (this.length != 0) index %= this.length;
				colors[offset] = sequence.getPixelColor(this.offset + index * this.step);
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
		return Collections.unmodifiableCollection(Arrays.asList(sequence));
	}
}
