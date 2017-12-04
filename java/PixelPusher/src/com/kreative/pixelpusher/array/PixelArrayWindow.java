package com.kreative.pixelpusher.array;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import com.kreative.pixelpusher.pixelset.PixelSet;

public class PixelArrayWindow extends PixelArray {
	private static final long serialVersionUID = 1L;
	
	private PixelArray array;
	private int x;
	private int y;
	private int width;
	private int height;
	
	public PixelArrayWindow() {
		this(null, 0, 0, 8, 8);
	}
	
	public PixelArrayWindow(PixelArray array, int x, int y, int width, int height) {
		this.array = array;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public PixelArrayWindow(PixelArrayWindow source) {
		this.array = source.array;
		this.x = source.x;
		this.y = source.y;
		this.width = source.width;
		this.height = source.height;
	}
	
	@Override
	public synchronized PixelArrayWindow clone() {
		return new PixelArrayWindow(this);
	}
	
	public synchronized PixelArray getArray() {
		return array;
	}
	
	public synchronized void setArray(PixelArray array) {
		checkForCycles(array);
		this.array = array;
	}
	
	public synchronized int getLocationX() {
		return x;
	}
	
	public synchronized int getLocationY() {
		return y;
	}
	
	public synchronized void setLocationX(int x) {
		this.x = x;
	}
	
	public synchronized void setLocationY(int y) {
		this.y = y;
	}
	
	public synchronized void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public synchronized int getWidth() {
		return width;
	}
	
	public synchronized int getHeight() {
		return height;
	}
	
	public synchronized void setWidth(int width) {
		this.width = width;
	}
	
	public synchronized void setHeight(int height) {
		this.height = height;
	}
	
	public synchronized void setSize(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	public synchronized void setArray(PixelArray array, int x, int y, int width, int height) {
		checkForCycles(array);
		this.array = array;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public synchronized void setBounds(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public synchronized boolean contains(int x, int y) {
		return x >= this.x && y >= this.y && x < (this.x + this.width) && y < (this.y + this.height);
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
	public synchronized int getPixelColor(int row, int column) {
		if (array != null && contains(column, row)) {
			return array.getPixelColor(row - y, column - x);
		} else {
			return 0;
		}
	}
	
	@Override
	public synchronized Collection<? extends PixelSet> getDependencies() {
		return Collections.unmodifiableCollection(Arrays.asList(array));
	}
}
