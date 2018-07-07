package com.kreative.unipixelpusher.audiospectrum;

import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import com.kreative.unipixelpusher.ColorConstants;
import com.kreative.unipixelpusher.PixelSequence;
import com.kreative.unipixelpusher.PixelString;
import com.kreative.unipixelpusher.SequenceConfiguration;

public class AudioSpectrumSequence implements PixelSequence {
	public static final String name = "Audio Spectrum";
	
	private static final int[] COLORS_16 = {
		ColorConstants.RED, ColorConstants.SCARLET, ColorConstants.ORANGE, ColorConstants.GOLD,
		ColorConstants.YELLOW, ColorConstants.CHARTREUSE, ColorConstants.GREEN, ColorConstants.AQUAMARINE,
		ColorConstants.CYAN, ColorConstants.AZURE, ColorConstants.BLUE, ColorConstants.INDIGO,
		ColorConstants.VIOLET, ColorConstants.PURPLE, ColorConstants.MAGENTA, ColorConstants.ROSE,
	};
	
	private static final int[] COLORS_8 = {
		ColorConstants.RED, ColorConstants.ORANGE, ColorConstants.YELLOW, ColorConstants.GREEN,
		ColorConstants.CYAN, ColorConstants.BLUE, ColorConstants.VIOLET, ColorConstants.MAGENTA,
	};
	
	private static final int[] COLORS_4 = {
		ColorConstants.RED, ColorConstants.YELLOW, ColorConstants.GREEN, ColorConstants.BLUE,
	};
	
	private static final int[] COLORS_2 = {
		ColorConstants.RED, ColorConstants.BLUE,
	};
	
	private static final int[] COLORS_1 = {
		ColorConstants.RED,
	};
	
	private File levelFile;
	private RandomAccessFile levelIO;
	private FileChannel levelChannel;
	private MappedByteBuffer levelBuffer;
	private int[] levels;
	
	public AudioSpectrumSequence() {
		try {
			this.levelFile = new File("/tmp/com.kreative.pixelpusher.audiospectrum");
			this.levelIO = new RandomAccessFile(levelFile, "rwd");
			this.levelChannel = levelIO.getChannel();
			this.levelBuffer = levelChannel.map(FileChannel.MapMode.READ_WRITE, 0, 16);
			this.levelBuffer.put(new byte[16], 0, 16);
			this.levels = new int[16];
		} catch (IOException e) {
			this.levelFile = null;
			this.levelIO = null;
			this.levelChannel = null;
			this.levelBuffer = null;
			this.levels = null;
		}
	}
	
	@Override
	public void update(PixelString ps, long tick) {
		if (levelBuffer == null || levels == null) return;
		
		int height = ps.getRowCount();
		int width = ps.getColumnCount();
		int[] colors = (width >= 16) ? COLORS_16 : (width >= 8) ? COLORS_8 : (width >= 4) ? COLORS_4 : (width >= 2) ? COLORS_2 : COLORS_1;
		int baseWidth = (width >= 16) ? 16 : (width >= 8) ? 8 : (width >= 4) ? 4 : (width >= 2) ? 2 : 1;
		int baseX = (width - baseWidth) / 2;
		
		for (int i = 0; i < 16; i++) {
			levels[i] = levelBuffer.get(i) & 0xFF;
		}
		if (width < 16) {
			for (int d = 0, s = 0; d < 8; d++, s += 2) {
				int a2 = levels[s] * levels[s];
				int b2 = levels[s+1] * levels[s+1];
				levels[d] = (int)Math.floor(Math.sqrt((a2 + b2) / 2));
			}
		}
		if (width < 8) {
			for (int d = 0, s = 0; d < 4; d++, s += 2) {
				int a2 = levels[s] * levels[s];
				int b2 = levels[s+1] * levels[s+1];
				levels[d] = (int)Math.floor(Math.sqrt((a2 + b2) / 2));
			}
		}
		if (width < 4) {
			for (int d = 0, s = 0; d < 2; d++, s += 2) {
				int a2 = levels[s] * levels[s];
				int b2 = levels[s+1] * levels[s+1];
				levels[d] = (int)Math.floor(Math.sqrt((a2 + b2) / 2));
			}
		}
		if (width < 2) {
			for (int d = 0, s = 0; d < 1; d++, s += 2) {
				int a2 = levels[s] * levels[s];
				int b2 = levels[s+1] * levels[s+1];
				levels[d] = (int)Math.floor(Math.sqrt((a2 + b2) / 2));
			}
		}
		
		for (int x = baseX, i = 0; x < width && i < baseWidth; x++, i++) {
			int level = height - levels[i] * height / 255;
			for (int y = 0; y < level; y++) ps.setPixel(y, x, 0);
			for (int y = level; y < height; y++) ps.setPixel(y, x, colors[i]);
		}
		ps.push();
	}
	
	@Override
	public long getUpdateInterval() {
		return 20;
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
