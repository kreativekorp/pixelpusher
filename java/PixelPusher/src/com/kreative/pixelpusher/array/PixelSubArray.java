package com.kreative.pixelpusher.array;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import com.kreative.pixelpusher.pixelset.PixelSet;

public class PixelSubArray extends PixelArray {
	private static final long serialVersionUID = 1L;
	
	private PixelArray array;
	private int x;
	private int y;
	private int width;
	private int height;
	private int xStep;
	private int yStep;
	
	public PixelSubArray() {
		this(null, 0, 0, 0, 0, 1, 1);
	}
	
	public PixelSubArray(PixelArray array) {
		this(array, 0, 0, 0, 0, 1, 1);
	}
	
	public PixelSubArray(PixelArray array, int x, int y) {
		this(array, x, y, 0, 0, 1, 1);
	}
	
	public PixelSubArray(PixelArray array, int x, int y, int width, int height) {
		this(array, x, y, width, height, 1, 1);
	}
	
	public PixelSubArray(PixelArray array, int x, int y, int width, int height, int xStep, int yStep) {
		this.array = array;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.xStep = xStep;
		this.yStep = yStep;
	}
	
	public PixelSubArray(PixelSubArray source) {
		this.array = source.array;
		this.x = source.x;
		this.y = source.y;
		this.width = source.width;
		this.height = source.height;
		this.xStep = source.xStep;
		this.yStep = source.yStep;
	}
	
	@Override
	public synchronized PixelSubArray clone() {
		return new PixelSubArray(this);
	}
	
	public synchronized PixelArray getArray() {
		return array;
	}
	
	public synchronized void setArray(PixelArray array) {
		checkForCycles(array);
		this.array = array;
	}
	
	public synchronized int getOffsetX() {
		return x;
	}
	
	public synchronized int getOffsetY() {
		return y;
	}
	
	public synchronized void setOffsetX(int x) {
		this.x = x;
	}
	
	public synchronized void setOffsetY(int y) {
		this.y = y;
	}
	
	public synchronized void setOffset(int x, int y) {
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
	
	public synchronized int getStepX() {
		return xStep;
	}
	
	public synchronized int getStepY() {
		return yStep;
	}
	
	public synchronized void setStepX(int xStep) {
		this.xStep = xStep;
	}
	
	public synchronized void setStepY(int yStep) {
		this.yStep = yStep;
	}
	
	public synchronized void setStep(int xStep, int yStep) {
		this.xStep = xStep;
		this.yStep = yStep;
	}
	
	public synchronized void setArray(PixelArray array, int x, int y, int width, int height, int xStep, int yStep) {
		checkForCycles(array);
		this.array = array;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.xStep = xStep;
		this.yStep = yStep;
	}
	
	public synchronized void setOffsetWidthHeight(int x, int y, int width, int height, int xStep, int yStep) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.xStep = xStep;
		this.yStep = yStep;
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
		if (array != null) {
			if (height != 0) row %= height;
			if (width != 0) column %= width;
			return array.getPixelColor(y + row * yStep, x + column * xStep);
		} else {
			return 0;
		}
	}
	
	@Override
	public synchronized Collection<? extends PixelSet> getDependencies() {
		return Collections.unmodifiableCollection(Arrays.asList(array));
	}
}
