package com.kreative.pixelpusher.marquee;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import com.kreative.pixelpusher.common.GifDecoder;
import com.kreative.pixelpusher.common.MathUtilities;

public class MarqueeAnimationItem extends MarqueeItem {
	private static final long serialVersionUID = 1L;
	
	private int animationFrames;
	private int animationHeight;
	private int animationWidth;
	private BufferedImage[] animationImages;
	private int[] animationMs;
	private int animationMsCommon;
	private int animationMsTotal;
	private transient int animationFrame;
	
	public MarqueeAnimationItem() {
		super();
		animationFrames = 1;
		animationHeight = 1;
		animationWidth = 1;
		animationImages = new BufferedImage[]{new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB)};
		animationMs = new int[]{1000};
		animationMsCommon = 1000;
		animationMsTotal = 1000;
		animationFrame = 0;
	}
	
	public MarqueeAnimationItem(MarqueeAnimationItem toClone) {
		super(toClone);
		animationFrames = toClone.animationFrames;
		animationHeight = toClone.animationHeight;
		animationWidth = toClone.animationWidth;
		animationImages = toClone.animationImages;
		animationMs = toClone.animationMs;
		animationMsCommon = toClone.animationMsCommon;
		animationMsTotal = toClone.animationMsTotal;
		animationFrame = 0;
	}
	
	@Override
	public synchronized MarqueeAnimationItem clone() {
		return new MarqueeAnimationItem(this);
	}
	
	public synchronized int getWidth() {
		return animationWidth;
	}
	
	public synchronized int getHeight() {
		return animationHeight;
	}
	
	public synchronized void setAnimation(File animatedGifFile) throws IOException {
		setAnimation(new FileInputStream(animatedGifFile));
	}
	
	public synchronized void setAnimation(InputStream animatedGifStream) throws IOException {
		GifDecoder decoder = new GifDecoder();
		int errorCode = decoder.read(animatedGifStream);
		if (errorCode != 0) throw new IOException();
		animationFrames = decoder.getFrameCount();
		animationHeight = decoder.getFrameSize().height;
		animationWidth = decoder.getFrameSize().width;
		animationImages = new BufferedImage[animationFrames];
		animationMs = new int[animationFrames];
		animationMsCommon = -1;
		animationMsTotal = 0;
		for (int z = 0; z < animationFrames; z++) {
			BufferedImage frame = decoder.getFrame(z);
			animationImages[z] = frame;
			int delay = decoder.getDelay(z);
			if (delay == 0) delay = 100;
			animationMs[z] = delay;
			animationMsCommon = (animationMsCommon < 0) ? delay : MathUtilities.gcd(animationMsCommon, delay);
			animationMsTotal += delay;
		}
	}
	
	@Override
	public synchronized int getMsPerFrame() {
		return animationMsCommon;
	}
	
	@Override
	public synchronized void updatePixels(long tick) {
		int animationTick = (int)(tick % animationMsTotal);
		for (int i = 0; i < animationFrames; i++) {
			if (animationTick < animationMs[i]) {
				this.animationFrame = i;
				break;
			} else {
				animationTick -= animationMs[i];
			}
		}
	}
	
	@Override
	public synchronized int getInnerWidth() {
		return animationWidth;
	}
	
	@Override
	public synchronized int getInnerHeight() {
		return animationHeight;
	}
	
	@Override
	protected synchronized void paintContent(Graphics2D g, int x, int y) {
		BufferedImage frame = animationImages[animationFrame];
		g.drawImage(frame, null, x, y);
	}
}
