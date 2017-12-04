package com.kreative.pixelpusher.pixelset;

import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.JComponent;

public abstract class PixelSetVisualizer<T extends PixelSet> extends JComponent {
	private static final long serialVersionUID = 1L;
	
	protected T pixelSet;
	
	public PixelSetVisualizer(T pixelSet) {
		this.pixelSet = pixelSet;
	}
	
	public T getPixelSet() {
		return pixelSet;
	}
	
	public void setPixelSet(T pixelSet) {
		this.pixelSet = pixelSet;
		repaint();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		Insets i = getInsets();
		int x = i.left;
		int y = i.top;
		int w = getWidth() - i.left - i.right;
		int h = getHeight() - i.top - i.bottom;
		pixelSet.updatePixels(System.currentTimeMillis());
		paintPixelSet(g, x, y, w, h, pixelSet);
	}
	
	public abstract void paintPixelSet(Graphics g, int x, int y, int w, int h, T pixelSet);
}
