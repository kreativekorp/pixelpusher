package com.kreative.unipixelpusher.animation;

import java.awt.image.BufferedImage;
import com.kreative.imagetool.animation.Animation;
import com.kreative.imagetool.animation.AnimationFrame;
import com.kreative.unipixelpusher.PixelSequence;
import com.kreative.unipixelpusher.PixelString;

public class AnimationSequence implements PixelSequence {
	public static final String name = "Animation";
	
	private Animation animation = null;
	private int animationHeight = 0;
	private int animationWidth = 0;
	private int animationFrames = 0;
	private int[][][] animationData = null;
	private int[] animationMs = null;
	private boolean wrapHorizontal = true;
	private boolean wrapVertical = true;
	private int scrollDirectionHorizontal = 0;
	private int scrollDirectionVertical = 0;
	private int scrollMsHorizontal = 100;
	private int scrollMsVertical = 100;
	private long animationFrameTime = 0;
	private int animationFrame = 0;
	private long scrollFrameTimeHorizontal = 0;
	private int scrollFrameHorizontal = 0;
	private long scrollFrameTimeVertical = 0;
	private int scrollFrameVertical = 0;
	
	public synchronized Animation getAnimation() {
		return this.animation;
	}
	
	public synchronized void setAnimation(Animation animation) {
		this.animation = animation;
		this.animationHeight = animation.height;
		this.animationWidth = animation.width;
		this.animationFrames = animation.frames.size();
		this.animationData = new int[animationFrames][animationHeight][animationWidth];
		this.animationMs = new int[animationFrames];
		for (int z = 0; z < animationFrames; z++) {
			AnimationFrame frame = animation.frames.get(z);
			BufferedImage image = frame.image;
			for (int y = 0; y < animationHeight; y++) {
				image.getRGB(0, y, animationWidth, 1, animationData[z][y], 0, animationWidth);
			}
			int delay = (int)Math.round(frame.duration * 1000);
			if (delay < 20) delay = 20;
			this.animationMs[z] = delay;
		}
		
		this.animationFrameTime = 0;
		this.animationFrame = 0;
		this.scrollFrameTimeHorizontal = 0;
		this.scrollFrameHorizontal = 0;
		this.scrollFrameTimeVertical = 0;
		this.scrollFrameVertical = 0;
	}
	
	public synchronized boolean getWrapHorizontal() {
		return this.wrapHorizontal;
	}
	
	public synchronized boolean getWrapVertical() {
		return this.wrapVertical;
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
		return this.scrollDirectionHorizontal;
	}
	
	public synchronized int getScrollDirectionVertical() {
		return this.scrollDirectionVertical;
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
		return this.scrollMsHorizontal;
	}
	
	public synchronized int getScrollMsPerFrameVertical() {
		return this.scrollMsVertical;
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
	public synchronized void update(PixelString ps, long tick) {
		if (animation == null) return;
		if (animationHeight < 1) return;
		if (animationWidth < 1) return;
		if (animationFrames < 1) return;
		if (animationData == null || animationData.length == 0) return;
		if (animationMs == null || animationMs.length == 0) return;
		
		long advance;
		if (animationFrameTime == 0) {
			animationFrameTime = tick;
		} else if ((advance = (tick - animationFrameTime) / animationMs[animationFrame]) > 0) {
			animationFrame += advance;
			animationFrame %= animationFrames;
			animationFrameTime = tick;
		}
		if (scrollDirectionHorizontal == 0 || scrollMsHorizontal == 0 || scrollFrameTimeHorizontal == 0) {
			scrollFrameTimeHorizontal = tick;
		} else if ((advance = (tick - scrollFrameTimeHorizontal) / scrollMsHorizontal) > 0) {
			scrollFrameHorizontal += (scrollDirectionHorizontal < 0) ? advance : (animationWidth - advance);
			scrollFrameHorizontal %= animationWidth;
			scrollFrameTimeHorizontal = tick;
		}
		if (scrollDirectionVertical == 0 || scrollMsVertical == 0 || scrollFrameTimeVertical == 0) {
			scrollFrameTimeVertical = tick;
		} else if ((advance = (tick - scrollFrameTimeVertical) / scrollMsVertical) > 0) {
			scrollFrameVertical += (scrollDirectionVertical < 0) ? advance : (animationHeight - advance);
			scrollFrameVertical %= animationHeight;
			scrollFrameTimeVertical = tick;
		}
		
		int[][] frame = animationData[animationFrame];
		int rows = ps.getRowCount();
		int cols = ps.getColumnCount();
		if (!wrapVertical && rows > animationHeight) rows = animationHeight;
		if (!wrapHorizontal && cols > animationWidth) cols = animationWidth;
		for (int row = 0; row < rows; row++) {
			int y = (row + scrollFrameVertical) % animationHeight;
			for (int col = 0; col < cols; col++) {
				int x = (col + scrollFrameHorizontal) % animationWidth;
				ps.setPixel(row, col, frame[y][x]);
			}
		}
		ps.push();
	}
	
	@Override
	public long getUpdateInterval() {
		return 20;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
