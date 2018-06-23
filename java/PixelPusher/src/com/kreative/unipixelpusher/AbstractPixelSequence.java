package com.kreative.unipixelpusher;

public abstract class AbstractPixelSequence implements PixelSequence.SpeedAdjust {
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
	
	public static abstract class ColorPattern extends AbstractPixelSequence implements PixelSequence.ColorPattern {
		protected int[] colorPattern = new int[]{-1};
		
		@Override
		public int[] getColorPattern() {
			return this.colorPattern;
		}
		
		@Override
		public void setColorPattern(int[] colors) {
			this.colorPattern = colors;
		}
		
		@Override
		public void loadConfiguration(SequenceConfiguration config) {
			super.loadConfiguration(config);
			this.colorPattern = config.get("colorPattern", new int[]{-1});
		}
		
		@Override
		public void saveConfiguration(SequenceConfiguration config) {
			super.saveConfiguration(config);
			config.put("colorPattern", colorPattern);
		}
		
		protected int color(int index) {
			if (colorPattern == null || colorPattern.length == 0) return -1;
			return colorPattern[index % colorPattern.length];
		}
		
		protected int length() {
			if (colorPattern == null || colorPattern.length == 0) return 1;
			return colorPattern.length;
		}
	}
}
