package com.kreative.unipixelpusher.marquee;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import com.kreative.unipixelpusher.SequenceConfiguration;

public abstract class TextItem extends MarqueeItem {
	private static final Font loadFont(String filename, int size) {
		try {
			InputStream in = TextItem.class.getResourceAsStream(filename);
			Font font = Font.createFont(Font.TRUETYPE_FONT, in); in.close();
			return font.deriveFont((float)size);
		} catch (Exception e) {
			return null;
		}
	}
	
	public static final Font FIXED_4x5 = loadFont("kkfixed4x5.ttf", 5);
	public static final Font FIXED_4x7 = loadFont("kkfixed4x7.ttf", 7);
	public static final Font PROPORTIONAL_4 = loadFont("kkpx4.ttf", 4);
	public static final Font PROPORTIONAL_7 = loadFont("kkpx7.ttf", 7);
	public static final Font PROPORTIONAL_9 = loadFont("kkpx9.ttf", 9);
	
	protected int foregroundColor = -1;
	protected Font font = PROPORTIONAL_7;
	protected int fontWeight = 1;
	protected int fontSpacing = 0;
	protected String prevText = null;
	protected BufferedImage image = null;
	
	public final synchronized int getForegroundColor() { return foregroundColor; }
	public final synchronized void setForegroundColor(int fgcolor) { foregroundColor = fgcolor; prevText = null; image = null; }
	public final synchronized Font getFont() { return font; }
	public final synchronized void setFont(Font font) { this.font = font; prevText = null; image = null; }
	public final synchronized int getFontSize() { return font.getSize(); }
	public final synchronized void setFontSize(int size) { font = font.deriveFont((float)((size > 1) ? size : 1)); prevText = null; image = null; }
	public final synchronized int getFontStyle() { return font.getStyle(); }
	public final synchronized void setFontStyle(int style) { font = font.deriveFont(style); prevText = null; image = null; }
	public final synchronized int getFontWeight() { return fontWeight; }
	public final synchronized void setFontWeight(int weight) { fontWeight = (weight > 1) ? weight : 1; prevText = null; image = null; }
	public final synchronized int getFontSpacing() { return fontSpacing; }
	public final synchronized void setFontSpacing(int spacing) { fontSpacing = spacing; prevText = null; image = null; }
	
	public abstract String getText(long tick);
	
	private final synchronized void update(long tick) {
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
	public final synchronized int getInnerWidth(long tick) {
		update(tick);
		return (image != null) ? image.getWidth() : 0;
	}
	
	@Override
	public final synchronized int getInnerHeight(long tick) {
		update(tick);
		return (image != null) ? image.getHeight() : 0;
	}
	
	@Override
	protected final synchronized void paintContent(Graphics2D g, int x, int y, long tick) {
		update(tick);
		if (image != null) g.drawImage(image, null, x, y);
	}
	
	@Override
	public synchronized void loadConfiguration(SequenceConfiguration config, String prefix) {
		super.loadConfiguration(config, prefix);
		this.foregroundColor = config.get(prefix + ".fgcolor", -1);
		
		String family = config.get(prefix + ".font.family", "KK Px7");
		int style = config.get(prefix + ".font.style", Font.PLAIN);
		float size = config.get(prefix + ".font.size", 7);
		String nf = family.replaceAll("\\s+", "").toLowerCase();
		if (nf.equals("kkfixed4x5")) this.font = FIXED_4x5.deriveFont(style).deriveFont(size);
		else if (nf.equals("kkfixed4x7")) this.font = FIXED_4x7.deriveFont(style).deriveFont(size);
		else if (nf.equals("kkpx4")) this.font = PROPORTIONAL_4.deriveFont(style).deriveFont(size);
		else if (nf.equals("kkpx7")) this.font = PROPORTIONAL_7.deriveFont(style).deriveFont(size);
		else if (nf.equals("kkpx9")) this.font = PROPORTIONAL_9.deriveFont(style).deriveFont(size);
		else this.font = new Font(family, style, (int)size);
		
		this.fontWeight = config.get(prefix + ".font.weight", 1);
		this.fontSpacing = config.get(prefix + ".font.spacing", 0);
		this.prevText = null;
		this.image = null;
	}
	
	@Override
	public synchronized void saveConfiguration(SequenceConfiguration config, String prefix) {
		super.saveConfiguration(config, prefix);
		config.put(prefix + ".fgcolor", foregroundColor);
		config.put(prefix + ".font.family", font.getFamily());
		config.put(prefix + ".font.size", font.getSize());
		config.put(prefix + ".font.style", font.getStyle());
		config.put(prefix + ".font.weight", fontWeight);
		config.put(prefix + ".font.spacing", fontSpacing);
	}
}
