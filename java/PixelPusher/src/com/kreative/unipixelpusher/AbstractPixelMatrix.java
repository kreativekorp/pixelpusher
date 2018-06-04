package com.kreative.unipixelpusher;

public abstract class AbstractPixelMatrix implements PixelString {
	protected WindingOrder windingOrder = WindingOrder.LTR_TTB;
	
	@Override
	public int length() {
		return this.getRowCount() * this.getColumnCount();
	}
	
	public WindingOrder getWindingOrder() {
		return this.windingOrder;
	}
	
	public void setWindingOrder(WindingOrder windingOrder) {
		this.windingOrder = windingOrder;
	}
	
	@Override
	public int getPixel(int i) {
		if (i < 0) return 0;
		int rows = getRowCount(), cols = getColumnCount();
		if (i >= rows * cols) return 0;
		int[] yx = windingOrder.getYX(rows, cols, i, null);
		return getPixel(yx[0], yx[1]);
	}
	
	@Override
	public void setPixel(int i, int color) {
		if (i < 0) return;
		int rows = getRowCount(), cols = getColumnCount();
		if (i >= rows * cols) return;
		int[] yx = windingOrder.getYX(rows, cols, i, null);
		setPixel(yx[0], yx[1], color);
	}
}
