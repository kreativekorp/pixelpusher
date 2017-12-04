package com.kreative.pixelpusher.marquee;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import com.kreative.pixelpusher.array.PixelArray;

public class PixelArrayMarqueeItem extends MarqueeItem {
	private static final long serialVersionUID = 1L;
	
	private PixelArray array;
	private int width;
	private int height;
	private transient int[][] colors;
	private transient BufferedImage image;
	
	public PixelArrayMarqueeItem() {
		super();
		this.array = null;
		this.width = 8;
		this.height = 8;
		this.colors = new int[height][width];
		this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	}
	
	public PixelArrayMarqueeItem(PixelArray array, int width, int height) {
		super();
		this.array = array;
		this.width = width;
		this.height = height;
		this.colors = new int[height][width];
		this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	}
	
	public PixelArrayMarqueeItem(PixelArrayMarqueeItem toClone) {
		super(toClone);
		this.array = toClone.array;
		this.width = toClone.width;
		this.height = toClone.height;
		this.colors = new int[height][width];
		this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	}
	
	@Override
	public synchronized PixelArrayMarqueeItem clone() {
		return new PixelArrayMarqueeItem(this);
	}
	
	public synchronized PixelArray getArray() {
		return array;
	}
	
	public synchronized void setArray(PixelArray array) {
		this.array = array;
		this.colors = new int[height][width];
		this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	}
	
	public synchronized int getWidth() {
		return width;
	}
	
	public synchronized int getHeight() {
		return height;
	}
	
	public synchronized void setWidth(int width) {
		this.width = width;
		this.colors = new int[height][width];
		this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	}
	
	public synchronized void setHeight(int height) {
		this.height = height;
		this.colors = new int[height][width];
		this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	}
	
	public synchronized void setSize(int width, int height) {
		this.width = width;
		this.height = height;
		this.colors = new int[height][width];
		this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	}
	
	public synchronized void setArray(PixelArray array, int width, int height) {
		this.array = array;
		this.width = width;
		this.height = height;
		this.colors = new int[height][width];
		this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	}
	
	@Override
	public synchronized int getMsPerFrame() {
		if (array != null) {
			return array.getMsPerFrame();
		} else {
			return 1000;
		}
	}
	
	@Override
	public synchronized void updatePixels(long tick) {
		if (array != null) {
			array.updatePixels(tick);
			array.getPixelColors(0, 0, colors, 0, 0, height, width);
			for (int y = 0; y < height; y++) {
				image.setRGB(0, y, width, 1, colors[y], 0, width);
			}
		}
	}
	
	@Override
	public synchronized int getInnerWidth() {
		return width;
	}
	
	@Override
	public synchronized int getInnerHeight() {
		return height;
	}
	
	@Override
	protected synchronized void paintContent(Graphics2D g, int x, int y) {
		g.drawImage(image, null, x, y);
	}
}
