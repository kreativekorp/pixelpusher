package com.kreative.pixelpusher.animation;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import com.kreative.pixelpusher.array.PixelArray;
import com.kreative.pixelpusher.common.GifDecoder;
import com.kreative.pixelpusher.common.MathUtilities;

public class AnimationArray extends PixelArray {
	private static final long serialVersionUID = 1L;
	
	private int animationFrames;
	private int animationHeight;
	private int animationWidth;
	private int[][][] animationData;
	private int[] animationMs;
	private int animationMsCommon;
	private int animationMsTotal;
	private boolean wrapHorizontal;
	private boolean wrapVertical;
	private int scrollDirectionHorizontal;
	private int scrollDirectionVertical;
	private int scrollMsHorizontal;
	private int scrollMsVertical;
	private transient int animationFrame;
	private transient int scrollFrameHorizontal;
	private transient int scrollFrameVertical;
	
	public AnimationArray() {
		animationFrames = 1;
		animationHeight = 1;
		animationWidth = 1;
		animationData = new int[][][]{new int[][]{new int[]{0}}};
		animationMs = new int[]{1000};
		animationMsCommon = 1000;
		animationMsTotal = 1000;
		wrapHorizontal = true;
		wrapVertical = true;
		scrollDirectionHorizontal = 0;
		scrollDirectionVertical = 0;
		scrollMsHorizontal = 100;
		scrollMsVertical = 100;
		animationFrame = 0;
		scrollFrameHorizontal = 0;
		scrollFrameVertical = 0;
	}
	
	public AnimationArray(AnimationArray source) {
		this.animationFrames = source.animationFrames;
		this.animationHeight = source.animationHeight;
		this.animationWidth = source.animationWidth;
		this.animationData = source.animationData;
		this.animationMs = source.animationMs;
		this.animationMsCommon = source.animationMsCommon;
		this.animationMsTotal = source.animationMsTotal;
		this.wrapHorizontal = source.wrapHorizontal;
		this.wrapVertical = source.wrapVertical;
		this.scrollDirectionHorizontal = source.scrollDirectionHorizontal;
		this.scrollDirectionVertical = source.scrollDirectionVertical;
		this.scrollMsHorizontal = source.scrollMsHorizontal;
		this.scrollMsVertical = source.scrollMsVertical;
		this.animationFrame = 0;
		this.scrollFrameHorizontal = 0;
		this.scrollFrameVertical = 0;
	}
	
	@Override
	public synchronized AnimationArray clone() {
		return new AnimationArray(this);
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
		animationData = new int[animationFrames][animationHeight][animationWidth];
		animationMs = new int[animationFrames];
		animationMsCommon = -1;
		animationMsTotal = 0;
		for (int z = 0; z < animationFrames; z++) {
			BufferedImage frame = decoder.getFrame(z);
			for (int y = 0; y < animationHeight; y++) {
				frame.getRGB(0, y, animationWidth, 1, animationData[z][y], 0, animationWidth);
			}
			int delay = decoder.getDelay(z);
			if (delay == 0) delay = 100;
			animationMs[z] = delay;
			animationMsCommon = (animationMsCommon < 0) ? delay : MathUtilities.gcd(animationMsCommon, delay);
			animationMsTotal += delay;
		}
	}
	
	public synchronized boolean getWrapHorizontal() {
		return wrapHorizontal;
	}
	
	public synchronized boolean getWrapVertical() {
		return wrapVertical;
	}
	
	public synchronized void setWrapHorizontal(boolean wrap) {
		this.wrapHorizontal = wrap;
	}
	
	public synchronized void setWrapVertical(boolean wrap) {
		this.wrapVertical = wrap;
	}
	
	public synchronized void setWrap(boolean horizontal, boolean vertical) {
		this.wrapHorizontal = horizontal;
		this.wrapVertical = vertical;
	}
	
	public synchronized int getScrollDirectionHorizontal() {
		return scrollDirectionHorizontal;
	}
	
	public synchronized int getScrollDirectionVertical() {
		return scrollDirectionVertical;
	}
	
	public synchronized void setScrollDirectionHorizontal(int dir) {
		this.scrollDirectionHorizontal = dir;
	}
	
	public synchronized void setScrollDirectionVertical(int dir) {
		this.scrollDirectionVertical = dir;
	}
	
	public synchronized void setScrollDirection(int horizontal, int vertical) {
		this.scrollDirectionHorizontal = horizontal;
		this.scrollDirectionVertical = vertical;
	}
	
	public synchronized int getScrollMsPerFrameHorizontal() {
		return scrollMsHorizontal;
	}
	
	public synchronized int getScrollMsPerFrameVertical() {
		return scrollMsVertical;
	}
	
	public synchronized void setScrollMsPerFrameHorizontal(int ms) {
		this.scrollMsHorizontal = ms;
	}
	
	public synchronized void setScrollMsPerFrameVertical(int ms) {
		this.scrollMsVertical = ms;
	}
	
	public synchronized void setScrollMsPerFrame(int horizontal, int vertical) {
		this.scrollMsHorizontal = horizontal;
		this.scrollMsVertical = vertical;
	}
	
	@Override
	public synchronized int getMsPerFrame() {
		int ms = animationMsCommon;
		if (scrollDirectionHorizontal != 0) ms = MathUtilities.gcd(ms, scrollMsHorizontal);
		if (scrollDirectionVertical != 0) ms = MathUtilities.gcd(ms, scrollMsVertical);
		return ms;
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
		int scrollTickHorizontal = (int)((tick / scrollMsHorizontal) % animationWidth);
		if (scrollDirectionHorizontal < 0) {
			this.scrollFrameHorizontal = scrollTickHorizontal;
		} else if (scrollDirectionHorizontal > 0 && scrollTickHorizontal != 0) {
			this.scrollFrameHorizontal = animationWidth - scrollTickHorizontal;
		} else {
			this.scrollFrameHorizontal = 0;
		}
		int scrollTickVertical = (int)((tick / scrollMsVertical) % animationHeight);
		if (scrollDirectionVertical < 0) {
			this.scrollFrameVertical = scrollTickVertical;
		} else if (scrollDirectionVertical > 0 && scrollTickVertical != 0) {
			this.scrollFrameVertical = animationHeight - scrollTickVertical;
		} else {
			this.scrollFrameVertical = 0;
		}
	}
	
	@Override
	public synchronized int getPixelColor(int row, int column) {
		int[][] frame = animationData[animationFrame];
		row += scrollFrameVertical;
		if (wrapVertical || scrollDirectionVertical != 0) row %= animationHeight;
		else if (row < 0 || row >= animationHeight) return 0;
		int[] line = frame[row];
		column += scrollFrameHorizontal;
		if (wrapHorizontal || scrollDirectionHorizontal != 0) column %= animationWidth;
		else if (column < 0 || column >= animationWidth) return 0;
		return line[column];
	}
}
