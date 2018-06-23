package com.kreative.unipixelpusher.marquee;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import com.kreative.unipixelpusher.SequenceConfiguration;

public class ImageItem extends MarqueeItem {
	protected BufferedImage image;
	
	public ImageItem() {
		this.image = null;
	}
	
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
	
	@Override
	public synchronized void loadConfiguration(SequenceConfiguration config, String prefix) {
		super.loadConfiguration(config, prefix);
		this.image = config.get(prefix + ".image", (BufferedImage)null);
	}
	
	@Override
	public synchronized void saveConfiguration(SequenceConfiguration config, String prefix) {
		super.saveConfiguration(config, prefix);
		config.put(prefix + ".image", image);
	}
}
