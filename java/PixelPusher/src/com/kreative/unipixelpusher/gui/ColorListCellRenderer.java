package com.kreative.unipixelpusher.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;

public abstract class ColorListCellRenderer extends DefaultListCellRenderer {
	private static final long serialVersionUID = 1L;
	
	private final Map<Object,Image> cache = new HashMap<Object,Image>();
	
	protected abstract Image createImage(Color color);
	
	@Override
	public final Component getListCellRendererComponent(JList c, Object v, int i, boolean s, boolean f) {
		JLabel l = (JLabel)super.getListCellRendererComponent(c, null, i, s, f);
		Image image = cache.get(v);
		if (image == null) {
			if (v instanceof Color) {
				image = createImage((Color)v);
			} else if (v instanceof Number) {
				image = createImage(new Color(((Number)v).intValue(), true));
			} else {
				image = createImage(Color.black);
			}
			cache.put(v, image);
		}
		l.setIcon(new ImageIcon(image));
		return l;
	}
	
	public static class SmallCircle extends ColorListCellRenderer {
		private static final long serialVersionUID = 1L;
		@Override
		protected Image createImage(Color color) {
			BufferedImage i = new BufferedImage(20, 20, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = i.createGraphics();
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g.setColor(color);
			g.fillOval(4, 4, 12, 12);
			g.dispose();
			return i;
		}
	}
	
	public static class LargeCircle extends ColorListCellRenderer {
		private static final long serialVersionUID = 1L;
		@Override
		protected Image createImage(Color color) {
			BufferedImage i = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = i.createGraphics();
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g.setColor(color);
			g.fillOval(4, 4, 24, 24);
			g.dispose();
			return i;
		}
	}
	
	public static class MMXL extends ColorListCellRenderer {
		private static final long serialVersionUID = 1L;
		@Override
		protected Image createImage(Color color) {
			BufferedImage i = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = i.createGraphics();
			
			int[] sx = new int[]{ 16, 19, 19, 16, 13, 13, 16 };
			int[] sy = new int[]{  5,  8, 24, 27, 24,  8,  5 };
			g.setColor(color);
			g.fillPolygon(sx, sy, sx.length);
			
			int na = 255 - (255 - color.getAlpha()) * 3 / 4;
			int nr = color.getRed()   * 3 / 4;
			int ng = color.getGreen() * 3 / 4;
			int nb = color.getBlue()  * 3 / 4;
			g.setColor(new Color(nr, ng, nb, na));
			g.drawPolygon(sx, sy, sx.length);
			
			sx = new int[]{ 12, 20, 20, 12, 12 };
			sy = new int[]{ 24, 24, 32, 32, 24 };
			g.setColor(new Color(0xFF009933));
			g.fillPolygon(sx, sy, sx.length);
			g.setColor(new Color(0xFF006600));
			g.drawPolygon(sx, sy, sx.length);
			
			g.dispose();
			return i;
		}
	}
}
