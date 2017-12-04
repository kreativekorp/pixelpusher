package com.kreative.pixelpusher.audiospectrum;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import com.kreative.pixelpusher.array.PixelArray;
import com.kreative.pixelpusher.common.ColorConstants;

public class AudioSpectrumArray extends PixelArray {
	private static final long serialVersionUID = 1L;
	
	private static final int[] COLORS_16 = {
		ColorConstants.RED, ColorConstants.SCARLET,
		ColorConstants.ORANGE, ColorConstants.GOLD,
		ColorConstants.YELLOW, ColorConstants.CHARTREUSE,
		ColorConstants.GREEN, ColorConstants.AQUAMARINE,
		ColorConstants.CYAN, ColorConstants.AZURE,
		ColorConstants.BLUE, ColorConstants.INDIGO,
		ColorConstants.VIOLET, ColorConstants.PURPLE,
		ColorConstants.MAGENTA, ColorConstants.ROSE,
	};
	
	private static final int[] COLORS_8 = {
		ColorConstants.RED,
		ColorConstants.ORANGE,
		ColorConstants.YELLOW,
		ColorConstants.GREEN,
		ColorConstants.CYAN,
		ColorConstants.BLUE,
		ColorConstants.VIOLET,
		ColorConstants.MAGENTA,
	};
	
	private static final int[] COLORS_4 = {
		ColorConstants.RED,
		ColorConstants.YELLOW,
		ColorConstants.GREEN,
		ColorConstants.BLUE,
	};
	
	private static final int[] COLORS_2 = {
		ColorConstants.RED,
		ColorConstants.BLUE,
	};
	
	private static final int[] COLORS_1 = {
		ColorConstants.RED,
	};
	
	private transient File levelFile;
	private transient RandomAccessFile levelIO;
	private transient FileChannel levelChannel;
	private transient MappedByteBuffer levelBuffer;
	private transient int[] levels;
	private int height;
	private int width;
	private transient int[] colors;
	private transient int baseWidth;
	private transient int baseX;
	
	public AudioSpectrumArray() {
		this(8, 16);
	}
	
	public AudioSpectrumArray(int height, int width) {
		if (height < 1) height = 1;
		if (width < 1) width = 1;
		try {
			this.levelFile = new File("/tmp/com.kreative.pixelpusher.audiospectrum");
			this.levelIO = new RandomAccessFile(levelFile, "rwd");
			this.levelChannel = levelIO.getChannel();
			this.levelBuffer = levelChannel.map(FileChannel.MapMode.READ_WRITE, 0, 16);
			this.levelBuffer.put(new byte[16], 0, 16);
		} catch (IOException e) {
			this.levelFile = null;
			this.levelIO = null;
			this.levelChannel = null;
			this.levelBuffer = null;
		}
		this.levels = new int[16];
		this.height = height;
		this.width = width;
		this.colors = (width >= 16) ? COLORS_16 : (width >= 8) ? COLORS_8 : (width >= 4) ? COLORS_4 : (width >= 2) ? COLORS_2 : COLORS_1;
		this.baseWidth = (width >= 16) ? 16 : (width >= 8) ? 8 : (width >= 4) ? 4 : (width >= 2) ? 2 : 1;
		this.baseX = (width - baseWidth) / 2;
	}
	
	public AudioSpectrumArray(AudioSpectrumArray source) {
		this(source.height, source.width);
	}
	
	@Override
	public synchronized PixelArray clone() {
		return new AudioSpectrumArray(this);
	}
	
	public synchronized int getHeight() {
		return height;
	}
	
	public synchronized void setHeight(int height) {
		if (height < 1) height = 1;
		this.height = height;
	}
	
	public synchronized int getWidth() {
		return width;
	}
	
	public synchronized void setWidth(int width) {
		if (width < 1) width = 1;
		this.width = width;
		this.colors = (width >= 16) ? COLORS_16 : (width >= 8) ? COLORS_8 : (width >= 4) ? COLORS_4 : (width >= 2) ? COLORS_2 : COLORS_1;
		this.baseWidth = (width >= 16) ? 16 : (width >= 8) ? 8 : (width >= 4) ? 4 : (width >= 2) ? 2 : 1;
		this.baseX = (width - baseWidth) / 2;
	}
	
	@Override
	public synchronized int getMsPerFrame() {
		return 20;
	}
	
	@Override
	public synchronized void updatePixels(long tick) {
		if (levelBuffer != null) {
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
		}
	}
	
	@Override
	public synchronized int getPixelColor(int row, int column) {
		column -= baseX;
		if (row < 0 || row >= height) return 0;
		if (column < 0 || column >= baseWidth) return 0;
		int level = levels[column] * height / 255;
		return (level >= (height - row)) ? colors[column] : 0;
	}
}
