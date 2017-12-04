package com.kreative.pixelpusher.array;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class GenericArrayVisualizer extends PixelArrayVisualizer {
	private static final long serialVersionUID = 1L;
	
	private Dimension minSize;
	private Dimension prefSize;
	private int[][] pixels;
	private BufferedImage buffer;
	private Graphics2D bg;
	
	public GenericArrayVisualizer(PixelArray array) {
		super(array);
		minSize = null;
		prefSize = null;
		pixels = new int[1][1];
		buffer = new BufferedImage(8, 8, BufferedImage.TYPE_INT_ARGB);
		bg = buffer.createGraphics();
		bg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	}
	
	@Override
	public void paintPixelSet(Graphics g, int x, int y, int w, int h, PixelArray array) {
		int pw = w / 8; int ph = h / 8;
		if (pw < 1 || ph < 1) return;
		w = pw * 8; h = ph * 8;
		
		if (pixels.length < ph || pixels[0].length < pw) {
			pixels = new int[ph][pw];
		}
		if (buffer.getWidth() < w || buffer.getHeight() < h) {
			buffer = new BufferedImage(w, h, BufferedImage.TYPE_4BYTE_ABGR);
			bg = buffer.createGraphics();
			bg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		}
		
		array.getPixelColors(0, 0, pixels, 0, 0, ph, pw);
		bg.setColor(Color.gray);
		bg.fillRect(0, 0, w, h);
		for (int ly = 0, row = 0; ly < h; ly += 8, row++) {
			for (int lx = 0, col = 0; lx < w; lx += 8, col++) {
				int c = pixels[row][col];
				int tc = ((((c >> 24) & 0xFF) / 3) << 24) | (c & 0xFFFFFF);
				bg.setColor(new Color(tc, true));
				bg.fillRect(lx, ly, 8, 8);
				bg.setColor(Color.lightGray);
				bg.fillOval(lx + 1, ly + 1, 6, 6);
				bg.setColor(new Color(c, true));
				bg.fillOval(lx + 1, ly + 1, 6, 6);
			}
		}
		g.drawImage(buffer, x, y, x+w, y+h, 0, 0, w, h, null);
	}
	
	@Override
	public synchronized Dimension getMinimumSize() {
		if (minSize != null) return minSize;
		Insets i = getInsets();
		return new Dimension(i.left + i.right + 8, i.top + i.bottom + 8);
	}
	
	@Override
	public synchronized void setMinimumSize(Dimension minSize) {
		this.minSize = minSize;
	}
	
	@Override
	public synchronized Dimension getPreferredSize() {
		if (prefSize != null) return prefSize;
		Insets i = getInsets();
		return new Dimension(i.left + i.right + 8 * 20, i.top + i.bottom + 8 * 12);
	}
	
	@Override
	public synchronized void setPreferredSize(Dimension prefSize) {
		this.prefSize = prefSize;
	}
}
