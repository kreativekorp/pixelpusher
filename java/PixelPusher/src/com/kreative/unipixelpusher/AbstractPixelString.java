package com.kreative.unipixelpusher;

public abstract class AbstractPixelString implements PixelString {
	protected int rowCount = 0;
	protected int columnCount = 0;
	protected WindingOrder windingOrder = WindingOrder.LTR_TTB;
	
	@Override
	public int getRowCount() {
		if (this.rowCount > 0) return this.rowCount;
		if (this.columnCount > 0) return this.length() / this.columnCount;
		return this.windingOrder.vertical ? this.length() : 1;
	}
	
	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}
	
	@Override
	public int getColumnCount() {
		if (this.columnCount > 0) return this.columnCount;
		if (this.rowCount > 0) return this.length() / this.rowCount;
		return this.windingOrder.vertical ? 1 : this.length();
	}
	
	public void setColumnCount(int columnCount) {
		this.columnCount = columnCount;
	}
	
	public WindingOrder getWindingOrder() {
		return this.windingOrder;
	}
	
	public void setWindingOrder(WindingOrder windingOrder) {
		this.windingOrder = windingOrder;
	}
	
	@Override
	public int getPixel(int row, int col) {
		if (row < 0 || col < 0) return 0;
		int rows = getRowCount(), cols = getColumnCount();
		if (row >= rows || col >= cols) return 0;
		return getPixel(windingOrder.getIndex(rows, cols, row, col));
	}
	
	@Override
	public void setPixel(int row, int col, int color) {
		if (row < 0 || col < 0) return;
		int rows = getRowCount(), cols = getColumnCount();
		if (row >= rows || col >= cols) return;
		setPixel(windingOrder.getIndex(rows, cols, row, col), color);
	}
}
