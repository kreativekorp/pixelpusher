package com.kreative.unipixelpusher;

public interface PixelSequence {
	public void update(PixelString ps, long tick);
	public long getUpdateInterval();
	
	public static interface SpeedAdjust extends PixelSequence {
		public float getSpeedAdjust();
		public void setSpeedAdjust(float speedAdjust);
	}
	
	public static interface ColorPattern extends PixelSequence {
		public int[] getColorPattern();
		public void setColorPattern(int[] colors);
	}
}
