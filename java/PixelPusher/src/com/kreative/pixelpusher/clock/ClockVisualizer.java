package com.kreative.pixelpusher.clock;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import com.kreative.pixelpusher.sequence.PixelSequence;
import com.kreative.pixelpusher.sequence.PixelSequenceVisualizer;

public class ClockVisualizer extends PixelSequenceVisualizer {
	private static final long serialVersionUID = 1L;
	
	private Stroke barStroke;
	private int[] barColors;
	private Dimension minSize;
	private Dimension prefSize;
	
	public ClockVisualizer() {
		this(new ClockSequence());
	}
	
	public ClockVisualizer(PixelSequence sequence) {
		super(sequence);
		barStroke = new BasicStroke(8, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
		barColors = new int[60];
		minSize = null;
		prefSize = null;
	}
	
	public synchronized Stroke getBarStroke() {
		return barStroke;
	}
	
	public synchronized void setBarStroke(Stroke barStroke) {
		this.barStroke = barStroke;
		repaint();
	}
	
	public synchronized int getBarCount() {
		return barColors.length;
	}
	
	public synchronized void setBarCount(int barCount) {
		if (barCount < 1) barCount = 1;
		this.barColors = new int[barCount];
		repaint();
	}
	
	@Override
	public synchronized void paintPixelSet(Graphics g, int x, int y, int w, int h, PixelSequence sequence) {
		((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		((Graphics2D)g).setStroke(barStroke);
		double ab = 90.0;
		double xc = x + w / 2.0;
		double yc = y + h / 2.0;
		double ir = 100.0;
		double or = Math.min(w, h) / 2.0 - 20.0;
		sequence.getPixelColors(0, barColors, 0, barColors.length);
		for (int i = 0; i < barColors.length; i++) {
			int color = barColors[i];
			if ((color & 0xFFFFFF) != 0) {
				double angle = Math.toRadians(ab - 360.0 * i / barColors.length);
				double x1 = xc + ir * Math.cos(angle);
				double y1 = yc - ir * Math.sin(angle);
				double x2 = xc + or * Math.cos(angle);
				double y2 = yc - or * Math.sin(angle);
				g.setColor(new Color(color, true));
				((Graphics2D)g).draw(new Line2D.Double(x1, y1, x2, y2));
			}
		}
	}
	
	@Override
	public synchronized Dimension getMinimumSize() {
		if (minSize != null) return minSize;
		Insets i = getInsets();
		return new Dimension(i.left + i.right + 250, i.top + i.bottom + 250);
	}
	
	@Override
	public synchronized void setMinimumSize(Dimension minSize) {
		this.minSize = minSize;
	}
	
	@Override
	public synchronized Dimension getPreferredSize() {
		if (prefSize != null) return prefSize;
		Insets i = getInsets();
		return new Dimension(i.left + i.right + 500, i.top + i.bottom + 500);
	}
	
	@Override
	public synchronized void setPreferredSize(Dimension prefSize) {
		this.prefSize = prefSize;
	}
}
