package com.kreative.pixelpusher.colorbars;

import com.kreative.pixelpusher.array.PixelArray;
import com.kreative.pixelpusher.common.ColorConstants;

public class ColorBarsArray extends PixelArray {
	private static final long serialVersionUID = 1L;
	
	private static final int[] MAIN_COLORS = {
		ColorConstants.YELLOW,
		ColorConstants.CYAN,
		ColorConstants.GREEN,
		ColorConstants.ORANGE,
		ColorConstants.MAGENTA,
		ColorConstants.RED,
		ColorConstants.VIOLET,
		ColorConstants.BLUE,
	};
	
	private static final int[] RAINBOW_COLORS = {
		ColorConstants.RED,
		ColorConstants.CYAN,
		ColorConstants.SCARLET,
		ColorConstants.AZURE,
		ColorConstants.ORANGE,
		ColorConstants.BLUE,
		ColorConstants.GOLD,
		ColorConstants.INDIGO,
		ColorConstants.YELLOW,
		ColorConstants.VIOLET,
		ColorConstants.CHARTREUSE,
		ColorConstants.PURPLE,
		ColorConstants.GREEN,
		ColorConstants.MAGENTA,
		ColorConstants.AQUAMARINE,
		ColorConstants.ROSE,
	};
	
	private int height;
	private int bottomBarHeight;
	private int bottomBarRainbowThresh;
	private int bottomBarGraysThresh;
	private int bottomBarRedThresh;
	private int bottomBarGreenThresh;
	private int bottomBarBlueThresh;
	private int width;
	private int mainBarWidth;
	private int mainBarMinX;
	private int mainBarMaxX;
	private int rainbowBarWidth;
	private int rainbowBarMinX;
	private int rainbowBarMaxX;
	private int rampWidth;
	
	public ColorBarsArray() {
		setHeight(12);
		setWidth(20);
	}
	
	public ColorBarsArray(int height, int width) {
		setHeight(height);
		setWidth(width);
	}
	
	public ColorBarsArray(ColorBarsArray source) {
		setHeight(source.height);
		setWidth(source.width);
	}
	
	@Override
	public synchronized PixelArray clone() {
		return new ColorBarsArray(this);
	}
	
	public synchronized int getHeight() {
		return height;
	}
	
	public synchronized void setHeight(int height) {
		if (height < 1) height = 1;
		this.height = height;
		bottomBarHeight = height / 12;
		if (bottomBarHeight < 1) bottomBarHeight = 1;
		bottomBarBlueThresh = height - bottomBarHeight;
		bottomBarGreenThresh = bottomBarBlueThresh - bottomBarHeight;
		bottomBarRedThresh = bottomBarGreenThresh - bottomBarHeight;
		bottomBarGraysThresh = bottomBarRedThresh - bottomBarHeight;
		bottomBarRainbowThresh = bottomBarGraysThresh - bottomBarHeight;
	}
	
	public synchronized int getWidth() {
		return width;
	}
	
	public synchronized void setWidth(int width) {
		if (width < 1) width = 1;
		this.width = width;
		mainBarWidth = width / MAIN_COLORS.length;
		if (mainBarWidth < 1) mainBarWidth = 1;
		mainBarMinX = (width - mainBarWidth * MAIN_COLORS.length) / 2;
		mainBarMaxX = mainBarMinX + mainBarWidth * MAIN_COLORS.length;
		rainbowBarWidth = width / RAINBOW_COLORS.length;
		if (rainbowBarWidth < 1) rainbowBarWidth = 1;
		rainbowBarMinX = (width - rainbowBarWidth * RAINBOW_COLORS.length) / 2;
		rainbowBarMaxX = rainbowBarMinX + rainbowBarWidth * RAINBOW_COLORS.length;
		rampWidth = (width + 1) / 2;
	}
	
	@Override
	public synchronized int getMsPerFrame() {
		return 1000;
	}
	
	@Override
	public synchronized void updatePixels(long tick) {
		// Nothing to do.
	}
	
	@Override
	public synchronized int getPixelColor(int row, int column) {
		row %= height;
		column %= width;
		if (row < bottomBarRainbowThresh) {
			// Main Bars
			if (column < mainBarMinX) {
				return ColorConstants.WHITE;
			} else if (column < mainBarMaxX) {
				column -= mainBarMinX;
				column /= mainBarWidth;
				return MAIN_COLORS[column];
			} else {
				return ColorConstants.BLACK;
			}
		} else if (row < bottomBarGraysThresh) {
			// Rainbow
			if (column < rainbowBarMinX) {
				return ColorConstants.BLACK;
			} else if (column < rainbowBarMaxX) {
				column -= rainbowBarMinX;
				column /= rainbowBarWidth;
				return RAINBOW_COLORS[column];
			} else {
				return ColorConstants.WHITE;
			}
		} else {
			// Ramps
			int v = 255 - ((column % rampWidth) * 255 / (rampWidth - 1));
			if (row < bottomBarRedThresh) {
				// Grays
				return 0xFF000000 | (v << 16) | (v << 8) | (v << 0);
			} else if (row < bottomBarGreenThresh) {
				// Red
				if (column < rampWidth) {
					return 0xFF000000 | (v << 16);
				} else {
					return 0xFFFF0000 | (v << 8) | (v << 0);
				}
			} else if (row < bottomBarBlueThresh) {
				// Green
				if (column < rampWidth) {
					return 0xFF000000 | (v << 8);
				} else {
					return 0xFF00FF00 | (v << 16) | (v << 0);
				}
			} else {
				// Blue
				if (column < rampWidth) {
					return 0xFF000000 | (v << 0);
				} else {
					return 0xFF0000FF | (v << 16) | (v << 8);
				}
			}
		}
	}
}
