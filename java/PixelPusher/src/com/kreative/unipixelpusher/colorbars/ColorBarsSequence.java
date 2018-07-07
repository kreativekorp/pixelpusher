package com.kreative.unipixelpusher.colorbars;

import java.awt.Component;
import com.kreative.unipixelpusher.ColorConstants;
import com.kreative.unipixelpusher.PixelSequence;
import com.kreative.unipixelpusher.PixelString;
import com.kreative.unipixelpusher.SequenceConfiguration;

public class ColorBarsSequence implements PixelSequence {
	public static final String name = "Color Bars";
	
	private static final int[] MAIN_COLORS = {
		ColorConstants.YELLOW,
		ColorConstants.CYAN,
		ColorConstants.ORANGE,
		ColorConstants.GREEN,
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
	
	@Override
	public void update(PixelString ps, long tick) {
		int height = ps.getRowCount();
		int bottomBarHeight = height / 12;
		if (bottomBarHeight < 1) bottomBarHeight = 1;
		int bottomBarBlueThresh = height - bottomBarHeight;
		int bottomBarGreenThresh = bottomBarBlueThresh - bottomBarHeight;
		int bottomBarRedThresh = bottomBarGreenThresh - bottomBarHeight;
		int bottomBarGraysThresh = bottomBarRedThresh - bottomBarHeight;
		int bottomBarRainbowThresh = bottomBarGraysThresh - bottomBarHeight;
		
		int width = ps.getColumnCount();
		int mainBarWidth = width / MAIN_COLORS.length;
		if (mainBarWidth < 1) mainBarWidth = 1;
		int mainBarMinX = (width - mainBarWidth * MAIN_COLORS.length) / 2;
		int mainBarMaxX = mainBarMinX + mainBarWidth * MAIN_COLORS.length;
		int rainbowBarWidth = width / RAINBOW_COLORS.length;
		if (rainbowBarWidth < 1) rainbowBarWidth = 1;
		int rainbowBarMinX = (width - rainbowBarWidth * RAINBOW_COLORS.length) / 2;
		int rainbowBarMaxX = rainbowBarMinX + rainbowBarWidth * RAINBOW_COLORS.length;
		int rampWidth = (width + 1) / 2;
		
		for (int row = 0; row < bottomBarRainbowThresh; row++) {
			for (int col = 0; col < mainBarMinX; col++) {
				ps.setPixel(row, col, ColorConstants.WHITE);
			}
			for (int col = mainBarMinX, i = 0; col < mainBarMaxX; col++, i++) {
				ps.setPixel(row, col, MAIN_COLORS[i / mainBarWidth]);
			}
			for (int col = mainBarMaxX; col < width; col++) {
				ps.setPixel(row, col, ColorConstants.BLACK);
			}
		}
		for (int row = bottomBarRainbowThresh; row < bottomBarGraysThresh; row++) {
			for (int col = 0; col < rainbowBarMinX; col++) {
				ps.setPixel(row, col, ColorConstants.BLACK);
			}
			for (int col = rainbowBarMinX, i = 0; col < rainbowBarMaxX; col++, i++) {
				ps.setPixel(row, col, RAINBOW_COLORS[i / rainbowBarWidth]);
			}
			for (int col = rainbowBarMaxX; col < width; col++) {
				ps.setPixel(row, col, ColorConstants.WHITE);
			}
		}
		for (int row = bottomBarGraysThresh; row < bottomBarRedThresh; row++) {
			for (int col = 0; col < rampWidth; col++) {
				int v = 255 - (col * 255 / (rampWidth - 1));
				ps.setPixel(row, col, 0xFF000000 | (v << 16) | (v << 8) | (v << 0));
			}
			for (int col = rampWidth, i = 0; col < width; col++, i++) {
				int v = 255 - (i * 255 / (rampWidth - 1));
				ps.setPixel(row, col, 0xFF000000 | (v << 16) | (v << 8) | (v << 0));
			}
		}
		for (int row = bottomBarRedThresh; row < bottomBarGreenThresh; row++) {
			for (int col = 0; col < rampWidth; col++) {
				int v = 255 - (col * 255 / (rampWidth - 1));
				ps.setPixel(row, col, 0xFF000000 | (v << 16));
			}
			for (int col = rampWidth, i = 0; col < width; col++, i++) {
				int v = 255 - (i * 255 / (rampWidth - 1));
				ps.setPixel(row, col, 0xFFFF0000 | (v << 8) | (v << 0));
			}
		}
		for (int row = bottomBarGreenThresh; row < bottomBarBlueThresh; row++) {
			for (int col = 0; col < rampWidth; col++) {
				int v = 255 - (col * 255 / (rampWidth - 1));
				ps.setPixel(row, col, 0xFF000000 | (v << 8));
			}
			for (int col = rampWidth, i = 0; col < width; col++, i++) {
				int v = 255 - (i * 255 / (rampWidth - 1));
				ps.setPixel(row, col, 0xFF00FF00 | (v << 16) | (v << 0));
			}
		}
		for (int row = bottomBarBlueThresh; row < height; row++) {
			for (int col = 0; col < rampWidth; col++) {
				int v = 255 - (col * 255 / (rampWidth - 1));
				ps.setPixel(row, col, 0xFF000000 | (v << 0));
			}
			for (int col = rampWidth, i = 0; col < width; col++, i++) {
				int v = 255 - (i * 255 / (rampWidth - 1));
				ps.setPixel(row, col, 0xFF0000FF | (v << 16) | (v << 8));
			}
		}
		ps.push();
	}
	
	@Override
	public long getUpdateInterval() {
		return 1000;
	}
	
	@Override public void loadConfiguration(SequenceConfiguration config) {}
	@Override public void saveConfiguration(SequenceConfiguration config) {}
	@Override public boolean hasConfigurationPanel() { return false; }
	@Override public Component createConfigurationPanel() { return null; }
	
	@Override
	public String toString() {
		return name;
	}
}
