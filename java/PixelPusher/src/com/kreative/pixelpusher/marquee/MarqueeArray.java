package com.kreative.pixelpusher.marquee;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import com.kreative.pixelpusher.array.PixelArray;
import com.kreative.pixelpusher.common.MathUtilities;
import com.kreative.pixelpusher.resources.Resources;

public class MarqueeArray extends PixelArray {
	private static final long serialVersionUID = 1L;
	
	private int msPerFrame;
	private String messageName;
	private MarqueeString marqueeString;
	private transient BufferedImage image;
	private transient boolean updateBase;
	private transient long tickBase;
	private transient int frameBase;
	private transient int frame;
	
	public MarqueeArray() {
		msPerFrame = 100;
		messageName = "Hello, world!";
		marqueeString = new MarqueeString();
		marqueeString.add(new MarqueeStaticTextItem(messageName));
		marqueeString.get(0).setPadding(1, 4, 1, 4);
		try {
			MarqueeAnimationItem mai = new MarqueeAnimationItem();
			mai.setAnimation(Resources.getInputStream("globe8.gif"));
			marqueeString.add(mai);
		} catch (IOException e) {
			// Ignored.
		}
		image = null;
		updateBase = true;
		tickBase = 0;
		frameBase = 0;
		frame = 0;
	}
	
	public MarqueeArray(MarqueeArray source) {
		msPerFrame = source.msPerFrame;
		messageName = source.messageName;
		marqueeString = source.marqueeString.clone();
		image = null;
		updateBase = true;
		tickBase = 0;
		frameBase = 0;
		frame = 0;
	}
	
	@Override
	public synchronized PixelArray clone() {
		return new MarqueeArray(this);
	}
	
	public synchronized String messageName() {
		return messageName;
	}
	
	public synchronized MarqueeString marqueeString() {
		return marqueeString;
	}
	
	public synchronized void marqueeStringChanged() {
		updateBase = true;
	}
	
	public synchronized void setMarqueeString(String name, MarqueeString str) {
		this.messageName = name;
		this.marqueeString = str;
		this.updateBase = true;
	}
	
	@Override
	public synchronized int getMsPerFrame() {
		if (msPerFrame == 0) {
			return marqueeString.getMsPerFrame();
		} else {
			return MathUtilities.gcd(msPerFrame, marqueeString.getMsPerFrame());
		}
	}
	
	public synchronized int getScrollingMsPerFrame() {
		return msPerFrame;
	}
	
	public synchronized void setScrollingMsPerFrame(int ms) {
		if (ms <= 0) {
			this.msPerFrame = 0;
		} else {
			if (ms < 20) ms = 20;
			this.msPerFrame = ms;
		}
	}
	
	@Override
	public synchronized void updatePixels(long tick) {
		marqueeString.updatePixels(tick);
		int w = marqueeString.getOuterWidth();
		int h = marqueeString.getOuterHeight();
		image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = image.createGraphics();
		marqueeString.paint(g, 0, 0, w, h);
		g.dispose();
		
		if (updateBase) {
			updateBase = false;
			tickBase = tick;
			frameBase = frame;
		}
		frame = (int)((((tick - tickBase) / msPerFrame) + frameBase) % w);
	}
	
	@Override
	public synchronized int getPixelColor(int row, int column) {
		if (image != null) {
			int x = (this.frame + column) % image.getWidth();
			int y = row % image.getHeight();
			return image.getRGB(x, y);
		} else {
			return 0;
		}
	}
}
