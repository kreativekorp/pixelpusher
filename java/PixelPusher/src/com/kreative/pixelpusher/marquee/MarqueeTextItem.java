package com.kreative.pixelpusher.marquee;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import com.kreative.pixelpusher.resources.Resources;

public abstract class MarqueeTextItem extends MarqueeItem {
	private static final long serialVersionUID = 1L;
	
	protected int foregroundColor;
	protected Font font;
	protected int fontWeight;
	protected int fontSpacing;
	protected transient String prevText;
	protected transient BufferedImage image;
	
	protected MarqueeTextItem() {
		super();
		foregroundColor = -1;
		font = Resources.getFont("kkpx7", 7);
		fontWeight = 1;
		fontSpacing = 0;
		prevText = null;
		image = null;
	}
	
	protected MarqueeTextItem(MarqueeTextItem toClone) {
		super(toClone);
		foregroundColor = toClone.foregroundColor;
		font = toClone.font;
		fontWeight = toClone.fontWeight;
		fontSpacing = toClone.fontSpacing;
		prevText = null;
		image = null;
	}
	
	@Override public abstract MarqueeTextItem clone();
	@Override public abstract int getMsPerFrame();
	public abstract String getText(long tick);
	
	public final synchronized int getForegroundColor() {
		return foregroundColor;
	}
	
	public final synchronized void setForegroundColor(int foreground) {
		foregroundColor = foreground;
		prevText = null;
		image = null;
	}
	
	public final synchronized Font getFont() {
		return font;
	}
	
	public final synchronized void setFont(Font font) {
		this.font = font;
		prevText = null;
		image = null;
	}
	
	public final synchronized int getFontSize() {
		return font.getSize();
	}
	
	public final synchronized void setFontSize(int size) {
		if (size < 1) size = 1;
		font = font.deriveFont((float) size);
		prevText = null;
		image = null;
	}
	
	public final synchronized int getFontStyle() {
		return font.getStyle();
	}
	
	public final synchronized void setFontStyle(int style) {
		font = font.deriveFont(style);
		prevText = null;
		image = null;
	}
	
	public final synchronized int getFontWeight() {
		return fontWeight;
	}
	
	public final synchronized void setFontWeight(int weight) {
		if (weight < 1) weight = 1;
		fontWeight = weight;
		prevText = null;
		image = null;
	}
	
	public final synchronized int getFontSpacing() {
		return fontSpacing;
	}
	
	public final synchronized void setFontSpacing(int spacing) {
		fontSpacing = spacing;
		prevText = null;
		image = null;
	}
	
	@Override
	public final synchronized void updatePixels(long tick) {
		String text = getText(tick);
		if ((image != null) && ((text == null) ? (prevText == null) : text.equals(prevText))) return;
		
		image = new BufferedImage(8, 8, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = image.createGraphics();
		FontMetrics fm = g.getFontMetrics(font);
		int h = fm.getHeight();
		int w;
		if (text == null || text.length() == 0) {
			w = 1;
		} else if (fontSpacing == 0) {
			w = fm.stringWidth(text);
			w += fontWeight - 1;
			if (w < 1) w = 1;
		} else {
			w = 0;
			int i = 0;
			while (i < text.length()) {
				int ch = text.codePointAt(i);
				w += fm.charWidth(ch);
				w += fontSpacing;
				i += Character.charCount(ch);
			}
			w -= fontSpacing;
			w += fontWeight - 1;
			if (w < 1) w = 1;
		}
		g.dispose();
		
		image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		g = image.createGraphics();
		g.setColor(new Color(foregroundColor, true));
		g.setFont(font);
		fm = g.getFontMetrics();
		int y = fm.getAscent();
		if (text == null || text.length() == 0) {
			// Nothing.
		} else if (fontSpacing == 0) {
			for (int m = 0; m < fontWeight; m++) {
				g.drawString(text, m, y);
			}
		} else {
			int x = 0;
			int i = 0;
			while (i < text.length()) {
				char[] ch = Character.toChars(text.codePointAt(i));
				for (int m = 0; m < fontWeight; m++) {
					g.drawChars(ch, 0, ch.length, x + m, y);
				}
				x += fm.charsWidth(ch, 0, ch.length);
				x += fontSpacing;
				i += ch.length;
			}
		}
		g.dispose();
		
		prevText = text;
	}
	
	@Override
	public final synchronized int getInnerWidth() {
		if (image != null) {
			return image.getWidth();
		} else {
			return 0;
		}
	}
	
	@Override
	public final synchronized int getInnerHeight() {
		if (image != null) {
			return image.getHeight();
		} else {
			return 0;
		}
	}
	
	@Override
	protected final synchronized void paintContent(Graphics2D g, int x, int y) {
		if (image != null) {
			g.drawImage(image, null, x, y);
		}
	}
}
