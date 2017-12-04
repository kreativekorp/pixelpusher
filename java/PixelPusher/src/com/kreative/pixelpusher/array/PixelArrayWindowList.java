package com.kreative.pixelpusher.array;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import com.kreative.pixelpusher.common.MathUtilities;
import com.kreative.pixelpusher.pixelset.PixelSet;

public class PixelArrayWindowList extends PixelArray implements List<PixelArrayWindow> {
	private static final long serialVersionUID = 1L;
	
	private List<PixelArrayWindow> windows;
	
	public PixelArrayWindowList() {
		this.windows = new ArrayList<PixelArrayWindow>();
	}
	
	public PixelArrayWindowList(PixelArrayWindow... windows) {
		this.windows = new ArrayList<PixelArrayWindow>();
		this.windows.addAll(Arrays.asList(windows));
	}
	
	public PixelArrayWindowList(Collection<? extends PixelArrayWindow> windows) {
		this.windows = new ArrayList<PixelArrayWindow>();
		this.windows.addAll(windows);
	}
	
	public PixelArrayWindowList(PixelArrayWindowList source) {
		this.windows = new ArrayList<PixelArrayWindow>();
		this.windows.addAll(source.windows);
	}
	
	@Override
	public synchronized PixelArrayWindowList clone() {
		return new PixelArrayWindowList(this);
	}
	
	@Override
	public synchronized boolean add(PixelArrayWindow window) {
		checkForCycles(window);
		return this.windows.add(window);
	}
	
	@Override
	public synchronized void add(int index, PixelArrayWindow window) {
		checkForCycles(window);
		this.windows.add(index, window);
	}
	
	@Override
	public synchronized boolean addAll(Collection<? extends PixelArrayWindow> windows) {
		for (PixelArrayWindow window : windows) checkForCycles(window);
		return this.windows.addAll(windows);
	}
	
	@Override
	public synchronized boolean addAll(int index, Collection<? extends PixelArrayWindow> windows) {
		for (PixelArrayWindow window : windows) checkForCycles(window);
		return this.windows.addAll(index, windows);
	}
	
	@Override
	public synchronized void clear() {
		this.windows.clear();
	}
	
	@Override
	public synchronized boolean contains(Object window) {
		return this.windows.contains(window);
	}
	
	@Override
	public synchronized boolean containsAll(Collection<?> windows) {
		return this.windows.containsAll(windows);
	}
	
	@Override
	public synchronized PixelArrayWindow get(int index) {
		return this.windows.get(index);
	}
	
	@Override
	public synchronized int indexOf(Object window) {
		return this.windows.indexOf(window);
	}
	
	@Override
	public synchronized boolean isEmpty() {
		return this.windows.isEmpty();
	}
	
	@Override
	public synchronized Iterator<PixelArrayWindow> iterator() {
		return this.windows.iterator();
	}
	
	@Override
	public synchronized int lastIndexOf(Object window) {
		return this.windows.lastIndexOf(window);
	}
	
	@Override
	public synchronized ListIterator<PixelArrayWindow> listIterator() {
		return this.windows.listIterator();
	}
	
	@Override
	public synchronized ListIterator<PixelArrayWindow> listIterator(int index) {
		return this.windows.listIterator(index);
	}
	
	@Override
	public synchronized boolean remove(Object window) {
		return this.windows.remove(window);
	}
	
	@Override
	public synchronized PixelArrayWindow remove(int index) {
		return this.windows.remove(index);
	}
	
	@Override
	public synchronized boolean removeAll(Collection<?> windows) {
		return this.windows.removeAll(windows);
	}
	
	@Override
	public synchronized boolean retainAll(Collection<?> windows) {
		return this.windows.retainAll(windows);
	}
	
	@Override
	public synchronized PixelArrayWindow set(int index, PixelArrayWindow window) {
		checkForCycles(window);
		return this.windows.set(index, window);
	}
	
	@Override
	public synchronized int size() {
		return this.windows.size();
	}
	
	@Override
	public synchronized List<PixelArrayWindow> subList(int fromIndex, int toIndex) {
		return this.windows.subList(fromIndex, toIndex);
	}
	
	@Override
	public synchronized Object[] toArray() {
		return this.windows.toArray();
	}
	
	@Override
	public synchronized <T> T[] toArray(T[] a) {
		return this.windows.toArray(a);
	}
	
	@Override
	public synchronized int getMsPerFrame() {
		if (!windows.isEmpty()) {
			int msPerFrame = windows.get(0).getMsPerFrame();
			if (msPerFrame <= 20) return 20;
			for (int i = 1; i < windows.size(); i++) {
				msPerFrame = MathUtilities.gcd(msPerFrame, windows.get(i).getMsPerFrame());
				if (msPerFrame <= 20) return 20;
			}
			return msPerFrame;
		} else {
			return 1000;
		}
	}
	
	@Override
	public synchronized void updatePixels(long tick) {
		for (PixelArrayWindow window : windows) {
			window.updatePixels(tick);
		}
	}
	
	@Override
	public synchronized int getPixelColor(int row, int column) {
		for (PixelArrayWindow window : windows) {
			if (window.contains(column, row)) {
				return window.getPixelColor(row, column);
			}
		}
		return 0;
	}
	
	@Override
	public synchronized Collection<? extends PixelSet> getDependencies() {
		return Collections.unmodifiableCollection(windows);
	}
}
