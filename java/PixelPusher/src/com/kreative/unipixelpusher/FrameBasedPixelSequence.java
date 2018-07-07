package com.kreative.unipixelpusher;

import java.awt.Component;
import com.kreative.unipixelpusher.gui.SpeedAdjustPanel;

public abstract class FrameBasedPixelSequence implements PixelSequence.SpeedAdjust {
	protected float speedAdjust = 1;
	protected boolean initialized = false;
	protected boolean frameChanged;
	protected long frameTime;
	protected long frame;
	
	public abstract void updateFrame(PixelString ps, long frame, boolean frameChanged);
	public abstract long getFrameCount(PixelString ps);
	public abstract long getFrameDuration();
	
	@Override
	public void update(PixelString ps, long tick) {
		if (!initialized) {
			initialized = true;
			frameChanged = true;
			frameTime = tick;
			frame = 0;
		} else {
			long elapsed = (long)((tick - frameTime) * speedAdjust);
			long frameDuration = getFrameDuration();
			if ((frameChanged = (elapsed >= frameDuration))) {
				frameTime = tick;
				frame += elapsed / frameDuration;
				long frameCount = getFrameCount(ps);
				if (frameCount > 0 && frame >= frameCount) frame = 0;
			}
		}
		updateFrame(ps, frame, frameChanged);
	}
	
	@Override
	public long getUpdateInterval() {
		return (long)(getFrameDuration() / speedAdjust);
	}
	
	@Override
	public float getSpeedAdjust() {
		return this.speedAdjust;
	}
	
	@Override
	public void setSpeedAdjust(float speedAdjust) {
		this.speedAdjust = speedAdjust;
	}
	
	@Override
	public void loadConfiguration(SequenceConfiguration config) {
		this.speedAdjust = config.get("speedAdjust", 1f);
		this.initialized = false;
	}
	
	@Override
	public void saveConfiguration(SequenceConfiguration config) {
		config.put("speedAdjust", speedAdjust);
	}
	
	@Override
	public boolean hasConfigurationPanel() {
		return true;
	}
	
	@Override
	public Component createConfigurationPanel() {
		return new SpeedAdjustPanel(this, true);
	}
}
