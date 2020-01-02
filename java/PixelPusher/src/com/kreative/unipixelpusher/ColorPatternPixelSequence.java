package com.kreative.unipixelpusher;

import java.awt.Component;
import com.kreative.unipixelpusher.gui.ColorPatternPanel;

public abstract class ColorPatternPixelSequence implements PixelSequence.ColorPattern {
	protected static int[] white() { return new int[]{
		ColorConstants.WHITE
	}; }
	protected static int[] warmWhite() { return new int[]{
		ColorConstants.WARM_WHITE_LED
	}; }
	protected static int[] red() { return new int[]{
		ColorConstants.RED
	}; }
	protected static int[] rgb() { return new int[]{
		ColorConstants.RED, ColorConstants.GREEN, ColorConstants.BLUE
	}; }
	protected static int[] rainbow() { return new int[]{
		ColorConstants.RED, ColorConstants.ORANGE, ColorConstants.YELLOW, ColorConstants.GREEN,
		ColorConstants.CYAN, ColorConstants.BLUE, ColorConstants.VIOLET, ColorConstants.MAGENTA
	}; }
	
	protected int[] colorPattern = defaultColorPattern();
	
	protected abstract int[] defaultColorPattern();
	
	@Override
	public int[] getColorPattern() {
		return this.colorPattern;
	}
	
	@Override
	public void setColorPattern(int[] colors) {
		this.colorPattern = (colors == null || colors.length == 0) ? defaultColorPattern() : colors;
	}
	
	@Override
	public void loadConfiguration(SequenceConfiguration config) {
		this.colorPattern = config.get("colorPattern", defaultColorPattern());
	}
	
	@Override
	public void saveConfiguration(SequenceConfiguration config) {
		config.put("colorPattern", colorPattern);
	}
	
	@Override
	public boolean hasConfigurationPanel() {
		return true;
	}
	
	@Override
	public Component createConfigurationPanel() {
		return new ColorPatternPanel(this);
	}
	
	protected int color(int index) {
		return colorPattern[index % colorPattern.length];
	}
	
	protected int length() {
		return colorPattern.length;
	}
}
