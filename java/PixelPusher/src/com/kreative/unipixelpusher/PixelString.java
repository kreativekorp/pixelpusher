package com.kreative.unipixelpusher;

public interface PixelString {
	public int length();
	public int getRowCount();
	public int getColumnCount();
	public int getPixel(int i);
	public int getPixel(int row, int col);
	public void setPixel(int i, int color);
	public void setPixel(int row, int col, int color);
	public void push();
	public StringType type();
}
