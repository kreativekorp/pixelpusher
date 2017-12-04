package com.kreative.pixelpusher.sequence;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import com.kreative.pixelpusher.common.MathUtilities;
import com.kreative.pixelpusher.pixelset.PixelSet;

public class PixelConcatenate extends PixelSequence {
	private static final long serialVersionUID = 1L;
	
	private List<PixelSequence> sequences;
	private List<Integer> lengths;
	
	public PixelConcatenate() {
		this.sequences = new ArrayList<PixelSequence>();
		this.lengths = new ArrayList<Integer>();
	}
	
	public PixelConcatenate(PixelSequence[] sequences, int lengthPerSequence) {
		this.sequences = new ArrayList<PixelSequence>();
		this.lengths = new ArrayList<Integer>();
		for (PixelSequence sequence : sequences) {
			this.sequences.add(sequence);
			this.lengths.add(lengthPerSequence);
		}
	}
	
	public PixelConcatenate(Collection<? extends PixelSequence> sequences, int lengthPerSequence) {
		this.sequences = new ArrayList<PixelSequence>();
		this.lengths = new ArrayList<Integer>();
		for (PixelSequence sequence : sequences) {
			this.sequences.add(sequence);
			this.lengths.add(lengthPerSequence);
		}
	}
	
	public PixelConcatenate(PixelSequence[] sequences, int[] lengths, int count) {
		this.sequences = new ArrayList<PixelSequence>();
		this.lengths = new ArrayList<Integer>();
		for (int i = 0; i < count; i++) {
			this.sequences.add(sequences[i % sequences.length]);
			this.lengths.add(lengths[i % lengths.length]);
		}
	}
	
	public PixelConcatenate(List<? extends PixelSequence> sequences, List<? extends Integer> lengths, int count) {
		this.sequences = new ArrayList<PixelSequence>();
		this.lengths = new ArrayList<Integer>();
		for (int i = 0; i < count; i++) {
			this.sequences.add(sequences.get(i % sequences.size()));
			this.lengths.add(lengths.get(i % lengths.size()));
		}
	}
	
	public PixelConcatenate(PixelConcatenate source) {
		this.sequences = new ArrayList<PixelSequence>();
		this.lengths = new ArrayList<Integer>();
		this.sequences.addAll(source.sequences);
		this.lengths.addAll(source.lengths);
	}
	
	@Override
	public synchronized PixelConcatenate clone() {
		return new PixelConcatenate(this);
	}
	
	public synchronized void add(PixelSequence sequence, int length) {
		checkForCycles(sequence);
		this.sequences.add(sequence);
		this.lengths.add(length);
	}
	
	public synchronized void add(int index, PixelSequence sequence, int length) {
		checkForCycles(sequence);
		this.sequences.add(index, sequence);
		this.lengths.add(index, length);
	}
	
	public synchronized void addAll(PixelSequence[] sequences, int lengthPerSequence) {
		for (PixelSequence sequence : sequences) {
			checkForCycles(sequence);
			this.sequences.add(sequence);
			this.lengths.add(lengthPerSequence);
		}
	}
	
	public synchronized void addAll(Collection<? extends PixelSequence> sequences, int lengthPerSequence) {
		for (PixelSequence sequence : sequences) {
			checkForCycles(sequence);
			this.sequences.add(sequence);
			this.lengths.add(lengthPerSequence);
		}
	}
	
	public synchronized void addAll(PixelSequence[] sequences, int[] lengths, int count) {
		for (int i = 0; i < count; i++) {
			checkForCycles(sequences[i % sequences.length]);
			this.sequences.add(sequences[i % sequences.length]);
			this.lengths.add(lengths[i % lengths.length]);
		}
	}
	
	public synchronized void addAll(List<? extends PixelSequence> sequences, List<? extends Integer> lengths, int count) {
		for (int i = 0; i < count; i++) {
			checkForCycles(sequences.get(i % sequences.size()));
			this.sequences.add(sequences.get(i % sequences.size()));
			this.lengths.add(lengths.get(i % lengths.size()));
		}
	}
	
	public synchronized void addAll(int index, PixelSequence[] sequences, int lengthPerSequence) {
		for (PixelSequence sequence : sequences) {
			checkForCycles(sequence);
			this.sequences.add(index, sequence);
			this.lengths.add(index, lengthPerSequence);
			index++;
		}
	}
	
	public synchronized void addAll(int index, Collection<? extends PixelSequence> sequences, int lengthPerSequence) {
		for (PixelSequence sequence : sequences) {
			checkForCycles(sequence);
			this.sequences.add(index, sequence);
			this.lengths.add(index, lengthPerSequence);
			index++;
		}
	}
	
	public synchronized void addAll(int index, PixelSequence[] sequences, int[] lengths, int count) {
		for (int i = 0; i < count; i++) {
			checkForCycles(sequences[i % sequences.length]);
			this.sequences.add(index, sequences[i % sequences.length]);
			this.lengths.add(index, lengths[i % lengths.length]);
			index++;
		}
	}
	
	public synchronized void addAll(int index, List<? extends PixelSequence> sequences, List<? extends Integer> lengths, int count) {
		for (int i = 0; i < count; i++) {
			checkForCycles(sequences.get(i % sequences.size()));
			this.sequences.add(index, sequences.get(i % sequences.size()));
			this.lengths.add(index, lengths.get(i % lengths.size()));
			index++;
		}
	}
	
	public synchronized void clear() {
		this.sequences = new ArrayList<PixelSequence>();
		this.lengths = new ArrayList<Integer>();
	}
	
	public synchronized boolean contains(PixelSequence sequence) {
		return this.sequences.contains(sequence);
	}
	
	public synchronized boolean containsAll(Collection<? extends PixelSequence> sequences) {
		return this.sequences.containsAll(sequences);
	}
	
	public synchronized PixelSequence getSequence(int index) {
		return this.sequences.get(index);
	}
	
	public synchronized int getLength(int index) {
		return this.lengths.get(index);
	}
	
	public synchronized int indexOf(PixelSequence sequence) {
		return this.sequences.indexOf(sequence);
	}
	
	public synchronized boolean isEmpty() {
		return this.sequences.isEmpty();
	}
	
	public synchronized Iterator<PixelSequence> iterator() {
		return Collections.unmodifiableList(this.sequences).iterator();
	}
	
	public synchronized int lastIndexOf(PixelSequence sequence) {
		return this.sequences.lastIndexOf(sequence);
	}
	
	public synchronized ListIterator<PixelSequence> listIterator() {
		return Collections.unmodifiableList(this.sequences).listIterator();
	}
	
	public synchronized ListIterator<PixelSequence> listIterator(int index) {
		return Collections.unmodifiableList(this.sequences).listIterator(index);
	}
	
	public synchronized void remove(PixelSequence sequence) {
		int index = this.sequences.indexOf(sequence);
		if (index >= 0) {
			this.sequences.remove(index);
			this.lengths.remove(index);
		}
	}
	
	public synchronized void remove(int index) {
		this.sequences.remove(index);
		this.lengths.remove(index);
	}
	
	public synchronized void removeAll(Collection<? extends PixelSequence> sequences) {
		for (PixelSequence sequence : sequences) {
			int index = this.sequences.indexOf(sequence);
			if (index >= 0) {
				this.sequences.remove(index);
				this.lengths.remove(index);
			}
		}
	}
	
	public synchronized void retainAll(Collection<? extends PixelSequence> sequences) {
		for (int i = this.sequences.size() - 1; i >= 0; i--) {
			if (!sequences.contains(this.sequences.get(i))) {
				this.sequences.remove(i);
				this.lengths.remove(i);
			}
		}
	}
	
	public synchronized void setSequence(int index, PixelSequence sequence) {
		checkForCycles(sequence);
		this.sequences.set(index, sequence);
	}
	
	public synchronized void setLength(int index, int length) {
		this.lengths.set(index, length);
	}
	
	public synchronized void set(int index, PixelSequence sequence, int length) {
		checkForCycles(sequence);
		this.sequences.set(index, sequence);
		this.lengths.set(index, length);
	}
	
	public synchronized int size() {
		return this.sequences.size();
	}
	
	public synchronized List<PixelSequence> subList(int fromIndex, int toIndex) {
		return Collections.unmodifiableList(this.sequences).subList(fromIndex, toIndex);
	}
	
	public synchronized Object[] toArray() {
		return this.sequences.toArray();
	}
	
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
	
	private synchronized int getPixelColorInternal(int index) {
		while (true) {
			for (int i = 0; i < sequences.size(); i++) {
				if (index < lengths.get(i)) {
					return sequences.get(i).getPixelColor(index);
				} else {
					index -= lengths.get(i);
				}
			}
		}
	}
	
	@Override
	public synchronized int getPixelColor(int index) {
		if (!sequences.isEmpty()) {
			return getPixelColorInternal(index);
		} else {
			return 0;
		}
	}
	
	@Override
	public synchronized int[] getPixelColors(int index, int[] colors, int offset, int length) {
		if (colors == null) colors = new int[offset + length];
		if (!sequences.isEmpty()) {
			while (length > 0) {
				colors[offset] = getPixelColorInternal(index);
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
