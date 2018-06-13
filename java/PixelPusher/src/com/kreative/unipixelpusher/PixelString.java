package com.kreative.unipixelpusher;

public interface PixelString {
	public String id();
	public StringType type();
	public int length();
	public int getRowCount();
	public int getColumnCount();
	public int getPixel(int i);
	public int getPixel(int row, int col);
	public void setPixel(int i, int color);
	public void setPixel(int row, int col, int color);
	public void push();
	
	public static interface WithGammaCurve extends PixelString {
		public GammaCurve getGammaCurve();
		public void setGammaCurve(GammaCurve gamma);
	}
}
