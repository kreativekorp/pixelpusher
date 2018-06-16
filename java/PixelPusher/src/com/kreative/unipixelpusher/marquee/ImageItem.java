package com.kreative.unipixelpusher.marquee;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class ImageItem extends MarqueeItem {
	protected BufferedImage image;
	
	public ImageItem(BufferedImage image) {
		this.image = image;
	}
	
	public synchronized BufferedImage getImage() {
		return this.image;
	}
	
	public synchronized void setImage(BufferedImage image) {
		this.image = image;
	}
	
	@Override
	public synchronized int getInnerWidth(long tick) {
		return (image != null) ? image.getWidth() : 0;
	}
	
	@Override
	public synchronized int getInnerHeight(long tick) {
		return (image != null) ? image.getHeight() : 0;
	}
	
	@Override
	protected synchronized void paintContent(Graphics2D g, int x, int y, long tick) {
		if (image != null) g.drawImage(image, null, x, y);
	}
	
	@Override
	public long getUpdateInterval() {
		return 1000;
	}
}
