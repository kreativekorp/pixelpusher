package com.kreative.unipixelpusher.effect;

import java.awt.Component;
import com.kreative.unipixelpusher.ColorPatternPixelSequence;
import com.kreative.unipixelpusher.PixelString;
import com.kreative.unipixelpusher.SequenceConfiguration;
import com.kreative.unipixelpusher.gui.ColorPatternAndIntegerPanel;

public abstract class BouncingBalls extends ColorPatternPixelSequence {
	protected static final double START_HEIGHT = 1;
	protected static final double IMPACT_VELOCITY_START = 4.43;
	protected static final double GRAVITY = -9.81;
	
	protected double[] height          = null;
	protected double[] impactVelocity  = null;
	protected double[] dampening       = null;
	protected long  [] timeSinceBounce = null;
	
	public static class A extends BouncingBalls {
		public static final String name = "Bouncing Balls";
		
		protected int count = 0;
		
		public int getCount() {
			return this.count;
		}
		
		public void setCount(int count) {
			this.count           = count;
			this.height          = null;
			this.impactVelocity  = null;
			this.dampening       = null;
			this.timeSinceBounce = null;
		}
		
		@Override
		protected int[] defaultColorPattern() {
			return white();
		}
		
		@Override
		public void update(PixelString ps, long tick) {
			render(ps, tick, (count > 0) ? count : 3, false);
		}
		
		@Override
		public void loadConfiguration(SequenceConfiguration config) {
			super.loadConfiguration(config);
			this.count = config.get("count", 0);
		}
		
		@Override
		public void saveConfiguration(SequenceConfiguration config) {
			super.saveConfiguration(config);
			config.put("count", count);
		}
		
		@Override
		public Component createConfigurationPanel() {
			return new ColorPatternAndIntegerPanel(this, "Balls:", 0, 999) {
				private static final long serialVersionUID = 1L;
				@Override public int getValue() { return getCount(); }
				@Override public void setValue(int value) { setCount(value); }
			};
		}
		
		@Override
		public String toString() {
			return name;
		}
	}
	
	public static class B extends BouncingBalls {
		public static final String name = "Multicolor Bouncing Balls";
		
		@Override
		protected int[] defaultColorPattern() {
			return rgb();
		}
		
		@Override
		public void update(PixelString ps, long tick) {
			render(ps, tick, length(), true);
		}
		
		@Override
		public String toString() {
			return name;
		}
	}
	
	@Override
	public long getUpdateInterval() {
		return 0;
	}
	
	@Override
	public void setColorPattern(int[] colors) {
		super.setColorPattern(colors);
		this.height          = null;
		this.impactVelocity  = null;
		this.dampening       = null;
		this.timeSinceBounce = null;
	}
	
	@Override
	public void loadConfiguration(SequenceConfiguration config) {
		super.loadConfiguration(config);
		this.height          = null;
		this.impactVelocity  = null;
		this.dampening       = null;
		this.timeSinceBounce = null;
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
			ps.setPixel(si, color(multiColor ? i : si));
		}
		ps.push();
	}
}
