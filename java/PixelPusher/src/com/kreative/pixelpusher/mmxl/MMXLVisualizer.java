package com.kreative.pixelpusher.mmxl;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import javax.swing.SwingConstants;
import com.kreative.pixelpusher.sequence.PixelSequence;
import com.kreative.pixelpusher.sequence.PixelSequenceVisualizer;

public class MMXLVisualizer extends PixelSequenceVisualizer {
	private static final long serialVersionUID = 1L;
	
	private int[] colors;
	private Dimension minSize;
	private Dimension prefSize;
	
	public MMXLVisualizer() {
		this(new MMXLSequence());
	}
	
	public MMXLVisualizer(PixelSequence sequence) {
		super(sequence);
		colors = new int[0];
		minSize = null;
		prefSize = null;
	}
	
	@Override
	public synchronized void paintPixelSet(Graphics g, int x, int y, int w, int h, PixelSequence sequence) {
		((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		int nx = w / 32;
		int ny = h / 32;
		int n = nx + nx + ny + ny - 4;
		if (colors.length != n) colors = new int[n];
		sequence.getPixelColors(0, colors, 0, n);
		int i = 0;
		MMXLUtilities.drawBulb(g, x, y, SwingConstants.NORTH_WEST, colors[i++]);
		for (int xi = 1; xi < nx - 1; xi++) {
			int lx = x + (w - 32) * xi / (nx - 1);
			MMXLUtilities.drawBulb(g, lx, y, SwingConstants.NORTH, colors[i++]);
		}
		MMXLUtilities.drawBulb(g, x + w - 32, y, SwingConstants.NORTH_EAST, colors[i++]);
		for (int yi = 1; yi < ny - 1; yi++) {
			int ly = y + (h - 32) * yi / (ny - 1);
			MMXLUtilities.drawBulb(g, x + w - 32, ly, SwingConstants.EAST, colors[i++]);
		}
		MMXLUtilities.drawBulb(g, x + w - 32, y + h - 32, SwingConstants.SOUTH_EAST, colors[i++]);
		for (int xi = nx - 2; xi > 0; xi--) {
			int lx = x + (w - 32) * xi / (nx - 1);
			MMXLUtilities.drawBulb(g, lx, y + h - 32, SwingConstants.SOUTH, colors[i++]);
		}
		MMXLUtilities.drawBulb(g, x, y + h - 32, SwingConstants.SOUTH_WEST, colors[i++]);
		for (int yi = ny - 2; yi > 0; yi--) {
			int ly = y + (h - 32) * yi / (ny - 1);
			MMXLUtilities.drawBulb(g, x, ly, SwingConstants.WEST, colors[i++]);
		}
	}
	
	@Override
	public synchronized Dimension getMinimumSize() {
		if (minSize != null) return minSize;
		Insets i = getInsets();
		return new Dimension(i.left + i.right + 64, i.top + i.bottom + 64);
	}
	
	@Override
	public synchronized void setMinimumSize(Dimension minSize) {
		this.minSize = minSize;
	}
	
	@Override
	public synchronized Dimension getPreferredSize() {
		if (prefSize != null) return prefSize;
		Insets i = getInsets();
		return new Dimension(i.left + i.right + 256, i.top + i.bottom + 256);
	}
	
	@Override
	public synchronized void setPreferredSize(Dimension prefSize) {
		this.prefSize = prefSize;
	}
}
