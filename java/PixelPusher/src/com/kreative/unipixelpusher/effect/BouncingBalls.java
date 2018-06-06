package com.kreative.unipixelpusher.effect;

import com.kreative.unipixelpusher.PixelSequence;
import com.kreative.unipixelpusher.PixelString;

public abstract class BouncingBalls implements PixelSequence.ColorPattern {
	protected static final double START_HEIGHT = 1;
	protected static final double IMPACT_VELOCITY_START = 4.43;
	protected static final double GRAVITY = -9.81;
	
	protected int[] colorPattern = new int[]{-1};
	
	protected double[] height          = null;
	protected double[] impactVelocity  = null;
	protected double[] dampening       = null;
	protected long  [] timeSinceBounce = null;
	
	public static class A extends BouncingBalls {
		protected int count = 0;
		
		public int getCount() {
			return this.count;
		}
		
		public void setCount(int count) {
			this.count = count;
		}
		
		@Override
		public void update(PixelString ps, long tick) {
			render(ps, tick, (count > 0) ? count : 3, false);
		}
		
		@Override
		public String toString() {
			return "Bouncing Balls";
		}
	}
	
	public static class B extends BouncingBalls {
		@Override
		public void update(PixelString ps, long tick) {
			render(ps, tick, (colorPattern != null) ? colorPattern.length : 3, true);
		}
		
		@Override
		public String toString() {
			return "Multicolor Bouncing Balls";
		}
	}
	
	@Override
	public long getUpdateInterval() {
		return 0;
	}
	
	@Override
	public int[] getColorPattern() {
		return this.colorPattern;
	}
	
	@Override
	public void setColorPattern(int[] colors) {
		this.colorPattern = colors;
	}
	
	protected void render(PixelString ps, long tick, int ballCount, boolean multiColor) {
		if (height == null || impactVelocity == null || dampening == null || timeSinceBounce == null) {
			height          = new double[ballCount];
			impactVelocity  = new double[ballCount];
			dampening       = new double[ballCount];
			timeSinceBounce = new long  [ballCount];
			for (int i = 0; i < ballCount; i++) {
				height          [i] = START_HEIGHT;
				impactVelocity  [i] = IMPACT_VELOCITY_START;
				dampening       [i] = 0.90 - (double)i / ((double)ballCount * (double)ballCount);
				timeSinceBounce [i] = tick;
			}
		}
		int n = ps.length();
		for (int i = 0; i < n; i++) ps.setPixel(i, 0);
		for (int i = 0; i < ballCount; i++) {
			double tsb = (tick - timeSinceBounce[i]) / 1000.0;
			height[i] = (0.5 * GRAVITY * tsb + impactVelocity[i]) * tsb;
			if (height[i] < 0) {
				height[i] = 0;
				impactVelocity[i] *= dampening[i];
				if (impactVelocity[i] < 0.01) {
					impactVelocity[i] = IMPACT_VELOCITY_START;
				}
				timeSinceBounce[i] = tick;
			}
			int si = (int)Math.round(height[i] * (n - 1) / START_HEIGHT);
			if (colorPattern == null || colorPattern.length == 0) {
				ps.setPixel(si, -1);
			} else {
				int ci = (multiColor ? i : si) % colorPattern.length;
				ps.setPixel(si, colorPattern[ci]);
			}
		}
		ps.push();
	}
}
