package com.kreative.pixelpusher.sequence;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import com.kreative.pixelpusher.common.MathUtilities;
import com.kreative.pixelpusher.pixelset.PixelSet;

public class PixelInterleave extends PixelSequence implements List<PixelSequence> {
	private static final long serialVersionUID = 1L;
	
	private List<PixelSequence> sequences;
	
	public PixelInterleave() {
		this.sequences = new ArrayList<PixelSequence>();
	}
	
	public PixelInterleave(PixelSequence... sequences) {
		this.sequences = new ArrayList<PixelSequence>();
		this.sequences.addAll(Arrays.asList(sequences));
	}
	
	public PixelInterleave(Collection<? extends PixelSequence> sequences) {
		this.sequences = new ArrayList<PixelSequence>();
		this.sequences.addAll(sequences);
	}
	
	public PixelInterleave(PixelInterleave source) {
		this.sequences = new ArrayList<PixelSequence>();
		this.sequences.addAll(source.sequences);
	}
	
	@Override
	public synchronized PixelInterleave clone() {
		return new PixelInterleave(this);
	}
	
	@Override
	public synchronized boolean add(PixelSequence sequence) {
		checkForCycles(sequence);
		return this.sequences.add(sequence);
	}
	
	@Override
	public synchronized void add(int index, PixelSequence sequence) {
		checkForCycles(sequence);
		this.sequences.add(index, sequence);
	}
	
	@Override
	public synchronized boolean addAll(Collection<? extends PixelSequence> sequences) {
		for (PixelSequence sequence : sequences) checkForCycles(sequence);
		return this.sequences.addAll(sequences);
	}
	
	@Override
	public synchronized boolean addAll(int index, Collection<? extends PixelSequence> sequences) {
		for (PixelSequence sequence : sequences) checkForCycles(sequence);
		return this.sequences.addAll(index, sequences);
	}
	
	@Override
	public synchronized void clear() {
		this.sequences.clear();
	}
	
	@Override
	public synchronized boolean contains(Object sequence) {
		return this.sequences.contains(sequence);
	}
	
	@Override
	public synchronized boolean containsAll(Collection<?> sequences) {
		return this.sequences.containsAll(sequences);
	}
	
	@Override
	public synchronized PixelSequence get(int index) {
		return this.sequences.get(index);
	}
	
	@Override
	public synchronized int indexOf(Object sequence) {
		return this.sequences.indexOf(sequence);
	}
	
	@Override
	public synchronized boolean isEmpty() {
		return this.sequences.isEmpty();
	}
	
	@Override
	public synchronized Iterator<PixelSequence> iterator() {
		return this.sequences.iterator();
	}
	
	@Override
	public synchronized int lastIndexOf(Object sequence) {
		return this.sequences.lastIndexOf(sequence);
	}
	
	@Override
	public synchronized ListIterator<PixelSequence> listIterator() {
		return this.sequences.listIterator();
	}
	
	@Override
	public synchronized ListIterator<PixelSequence> listIterator(int index) {
		return this.sequences.listIterator(index);
	}
	
	@Override
	public synchronized boolean remove(Object sequence) {
		return this.sequences.remove(sequence);
	}
	
	@Override
	public synchronized PixelSequence remove(int index) {
		return this.sequences.remove(index);
	}
	
	@Override
	public synchronized boolean removeAll(Collection<?> sequences) {
		return this.sequences.removeAll(sequences);
	}
	
	@Override
	public synchronized boolean retainAll(Collection<?> sequences) {
		return this.sequences.retainAll(sequences);
	}
	
	@Override
	public synchronized PixelSequence set(int index, PixelSequence sequence) {
		checkForCycles(sequence);
		return this.sequences.set(index, sequence);
	}
	
	@Override
	public synchronized int size() {
		return this.sequences.size();
	}
	
	@Override
	public synchronized List<PixelSequence> subList(int fromIndex, int toIndex) {
		return this.sequences.subList(fromIndex, toIndex);
	}
	
	@Override
	public synchronized Object[] toArray() {
		return this.sequences.toArray();
	}
	
	@Override
	public synchronized <T> T[] toArray(T[] a) {
		return this.sequences.toArray(a);
	}
	
	@Override
	public synchronized int getMsPerFrame() {
		if (!sequences.isEmpty()) {
			int msPerFrame = sequences.get(0).getMsPerFrame();
			if (msPerFrame <= 20) return 20;
			for (int i = 1; i < sequences.size(); i++) {
				msPerFrame = MathUtilities.gcd(msPerFrame, sequences.get(i).getMsPerFrame());
				if (msPerFrame <= 20) return 20;
			}
			return msPerFrame;
		} else {
			return 1000;
		}
	}
	
	@Override
	public synchronized void updatePixels(long tick) {
		for (PixelSequence sequence : sequences) {
			sequence.updatePixels(tick);
		}
	}
	
	@Override
	public synchronized int getPixelColor(int index) {
		if (!sequences.isEmpty()) {
			return sequences.get(index % sequences.size()).getPixelColor(index / sequences.size());
		} else {
			return 0;
		}
	}
	
	@Override
	public synchronized int[] getPixelColors(int index, int[] colors, int offset, int length) {
		if (colors == null) colors = new int[offset + length];
		if (!sequences.isEmpty()) {
			while (length > 0) {
				colors[offset] = sequences.get(index % sequences.size()).getPixelColor(index / sequences.size());
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
		return Collections.unmodifiableCollection(sequences);
	}
}
