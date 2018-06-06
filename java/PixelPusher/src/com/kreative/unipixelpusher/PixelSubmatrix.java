package com.kreative.unipixelpusher;

public class PixelSubmatrix extends AbstractPixelMatrix {
	protected PixelString parent;
	protected int rowOffset;
	protected int columnOffset;
	protected int rowCount;
	protected int columnCount;
	
	public PixelSubmatrix(PixelString parent, int row, int col, int rows, int cols) {
		this.parent = parent;
		this.rowOffset = row;
		this.columnOffset = col;
		this.rowCount = rows;
		this.columnCount = cols;
	}
	
	public PixelString getParent() {
		return this.parent;
	}
	
	public void setParent(PixelString parent) {
		this.parent = parent;
	}
	
	public int getRowOffset() {
		return this.rowOffset;
	}
	
	public void setRowOffset(int rowOffset) {
		this.rowOffset = rowOffset;
	}
	
	public int getColumnOffset() {
		return this.columnOffset;
	}
	
	public void setColumnOffset(int columnOffset) {
		this.columnOffset = columnOffset;
	}
	
	@Override
	public int getRowCount() {
		return this.rowCount;
	}
	
	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}
	
	@Override
	public int getColumnCount() {
		return this.columnCount;
	}
	
	public void setColumnCount(int columnCount) {
		this.columnCount = columnCount;
	}
	
	@Override
	public int getPixel(int row, int col) {
		if (row < 0 || row >= rowCount || col < 0 || col >= columnCount) return 0;
		return parent.getPixel(rowOffset + row, columnOffset + col);
	}
	
	@Override
	public void setPixel(int row, int col, int color) {
		if (row < 0 || row >= rowCount || col < 0 || col >= columnCount) return;
		parent.setPixel(rowOffset + row, columnOffset + col, color);
	}
	
	@Override
	public void push() {
		parent.push();
	}
	
	@Override
	public StringType type() {
		return parent.type();
	}
}
