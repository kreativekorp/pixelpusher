package com.kreative.pixelpusher.marquee;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class MarqueeImageItem extends MarqueeItem {
	private static final long serialVersionUID = 1L;
	
	protected BufferedImage image;
	
	public MarqueeImageItem() {
		super();
		setImage(null);
	}
	
	public MarqueeImageItem(BufferedImage image) {
		super();
		setImage(image);
	}
	
	public MarqueeImageItem(MarqueeImageItem toClone) {
		super(toClone);
		setImage(toClone.image);
	}
	
	@Override
	public synchronized MarqueeImageItem clone() {
		return new MarqueeImageItem(this);
	}
	
	public synchronized Image getImage() {
		return image;
	}
	
	public synchronized void setImage(BufferedImage image) {
		if (image == null) {
			this.image = null;
		} else {
			int w = image.getWidth();
			int h = image.getHeight();
			int[] rgb = new int[w * h];
			image.getRGB(0, 0, w, h, rgb, 0, w);
			this.image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
			this.image.setRGB(0, 0, w, h, rgb, 0, w);
		}
	}
	
	@Override
	public synchronized int getMsPerFrame() {
		return 1000;
	}
	
	@Override
	public synchronized void updatePixels(long tick) {
		// Nothing.
	}
	
	@Override
	public synchronized int getInnerWidth() {
		if (image != null) {
			return image.getWidth();
		} else {
			return 0;
		}
	}
	
	@Override
	public synchronized int getInnerHeight() {
		if (image != null) {
			return image.getHeight();
		} else {
			return 0;
		}
	}
	
	@Override
	protected synchronized void paintContent(Graphics2D g, int x, int y) {
		if (image != null) {
			g.drawImage(image, null, x, y);
		}
	}
}
