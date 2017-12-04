package com.kreative.pixelpusher.sequence;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Shape;
import com.kreative.pixelpusher.resources.Resources;

public class GenericSequenceVisualizer extends PixelSequenceVisualizer {
	private static final long serialVersionUID = 1L;
	
	private static final Image STRIP_SEGMENT_IMAGE = Resources.getImage("strip_segment");
	
	private Dimension minSize;
	private Dimension prefSize;
	
	public GenericSequenceVisualizer(PixelSequence sequence) {
		super(sequence);
		minSize = null;
		prefSize = null;
	}
	
	@Override
	public synchronized void paintPixelSet(Graphics g, int x, int y, int w, int h, PixelSequence sequence) {
		((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		Shape s = g.getClip();
		y += (h - 24) / 2;
		g.clipRect(x, y, w, 24);
		for (int d = 0; d < w; d += 48) {
			g.drawImage(STRIP_SEGMENT_IMAGE, x + d, y, null);
		}
		for (int i = 0, d = 0; d < w; i++, d += 24) {
			int c = sequence.getPixelColor(i);
			int tc = ((((c >> 24) & 0xFF) / 3) << 24) | (c & 0xFFFFFF);
			g.setColor(new Color(tc, true));
			g.fillRect(x + d, y, 24, 24);
			g.setColor(Color.white);
			g.fillOval(x + d + 7, y + 7, 10, 10);
			g.setColor(new Color(c, true));
			g.fillOval(x + d + 7, y + 7, 10, 10);
		}
		g.setClip(s);
	}
	
	@Override
	public synchronized Dimension getMinimumSize() {
		if (minSize != null) return minSize;
		Insets i = getInsets();
		return new Dimension(i.left + i.right + 48, i.top + i.bottom + 24);
	}
	
	@Override
	public synchronized void setMinimumSize(Dimension minSize) {
		this.minSize = minSize;
	}
	
	@Override
	public synchronized Dimension getPreferredSize() {
		if (prefSize != null) return prefSize;
		Insets i = getInsets();
		return new Dimension(i.left + i.right + 576, i.top + i.bottom + 24);
	}
	
	@Override
	public synchronized void setPreferredSize(Dimension prefSize) {
		this.prefSize = prefSize;
	}
}
