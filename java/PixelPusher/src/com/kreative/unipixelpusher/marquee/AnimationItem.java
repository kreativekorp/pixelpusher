package com.kreative.unipixelpusher.marquee;

import java.awt.Graphics2D;
import com.kreative.imagetool.animation.Animation;
import com.kreative.imagetool.animation.AnimationFrame;
import com.kreative.unipixelpusher.SequenceConfiguration;

public class AnimationItem extends MarqueeItem {
	protected Animation animation;
	protected long frameTime;
	protected long frame;
	
	public AnimationItem() {
		this.animation = null;
		this.frameTime = 0;
		this.frame = 0;
	}
	
	public AnimationItem(Animation animation) {
		this.animation = animation;
		this.frameTime = 0;
		this.frame = 0;
	}
	
	public synchronized Animation getAnimation() {
		return this.animation;
	}
	
	public synchronized void setAnimation(Animation animation) {
		this.animation = animation;
		this.frameTime = 0;
		this.frame = 0;
	}
	
	@Override
	public synchronized int getInnerWidth(long tick) {
		return (animation != null) ? animation.width : 0;
	}
	
	@Override
	public synchronized int getInnerHeight(long tick) {
		return (animation != null) ? animation.height : 0;
	}
	
	@Override
	protected synchronized void paintContent(Graphics2D g, int x, int y, long tick) {
		if (animation == null) return;
		if (animation.frames == null) return;
		if (animation.frames.size() == 0) return;
		
		if (frameTime == 0) {
			frameTime = tick;
		} else {
			AnimationFrame af = animation.frames.get((int)frame);
			long ms = (long)Math.round(af.duration * 1000);
			long advance = (tick - frameTime) / ms;
			if (advance > 0) {
				frame += advance;
				frame %= animation.frames.size();
				frameTime = tick;
			}
		}
		
		AnimationFrame af = animation.frames.get((int)frame);
		g.drawImage(af.image, null, x, y);
	}
	
	@Override
	public long getUpdateInterval() {
		return 20;
	}
	
	@Override
	public synchronized void loadConfiguration(SequenceConfiguration config, String prefix) {
		super.loadConfiguration(config, prefix);
		this.animation = config.get(prefix + ".animation", (Animation)null);
		this.frameTime = 0;
		this.frame = 0;
	}
	
	@Override
	public synchronized void saveConfiguration(SequenceConfiguration config, String prefix) {
		super.saveConfiguration(config, prefix);
		config.put(prefix + ".animation", animation);
	}
}
