package com.kreative.unipixelpusher;

import java.awt.Component;

public interface PixelSequence {
	public void update(PixelString ps, long tick);
	public long getUpdateInterval();
	public void loadConfiguration(SequenceConfiguration config);
	public void saveConfiguration(SequenceConfiguration config);
	public boolean hasConfigurationPanel();
	public Component createConfigurationPanel();
	
	public static interface SpeedAdjust extends PixelSequence {
		public float getSpeedAdjust();
		public void setSpeedAdjust(float speedAdjust);
	}
	
	public static interface ColorPattern extends PixelSequence {
		public int[] getColorPattern();
		public void setColorPattern(int[] colors);
	}
}
