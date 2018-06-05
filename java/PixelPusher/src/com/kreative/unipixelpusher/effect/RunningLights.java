package com.kreative.unipixelpusher.effect;

import com.kreative.unipixelpusher.AbstractPixelSequence;
import com.kreative.unipixelpusher.ColorUtilities;
import com.kreative.unipixelpusher.PixelString;

public abstract class RunningLights extends AbstractPixelSequence.ColorPattern {
	public static abstract class A extends RunningLights {
		public static class Left extends A {
			@Override
			protected void updateFrame(PixelString ps, long frame, boolean frameChanged) {
				int ph = (int)(frame % 6);
				int ci = 0;
				for (int i = 0, n = ps.length(); i < n; i++) {
					ps.setPixel(i, getColor(ph++, ci++));
				}
				ps.push();
			}
			
			@Override
			public String toString() {
				return "Running Lights A Left";
			}
		}
		
		public static class Right extends A {
			@Override
			protected void updateFrame(PixelString ps, long frame, boolean frameChanged) {
				int ph = 5 - (int)(frame % 6);
				int ci = 0;
				for (int i = 0, n = ps.length(); i < n; i++) {
					ps.setPixel(i, getColor(ph++, ci++));
				}
				ps.push();
			}
			
			@Override
			public String toString() {
				return "Running Lights A Right";
			}
		}
		
		@Override
		protected long getFrameCount(PixelString ps) {
			return 6;
		}
	}
	
	public static abstract class B extends RunningLights {
		public static class Left extends B {
			@Override
			protected void updateFrame(PixelString ps, long frame, boolean frameChanged) {
				int ph = (int)(frame % 6);
				int ci = (int)(frame / 6);
				for (int i = 0, n = ps.length(); i < n; i++) {
					ps.setPixel(i, getColor(ph++, ci));
					if (ph >= 6) { ph = 0; ci++; }
				}
				ps.push();
			}
			
			@Override
			public String toString() {
				return "Running Lights B Left";
			}
		}
		
		public static class Right extends B {
			@Override
			protected void updateFrame(PixelString ps, long frame, boolean frameChanged) {
				int ph = 5 - (int)(frame % 6);
				int ci = length() - (int)(frame / 6);
				for (int i = 0, n = ps.length(); i < n; i++) {
					ps.setPixel(i, getColor(ph++, ci));
					if (ph >= 6) { ph = 0; ci++; }
				}
				ps.push();
			}
			
			@Override
			public String toString() {
				return "Running Lights B Right";
			}
		}
		
		@Override
		protected long getFrameCount(PixelString ps) {
			return length() * 6;
		}
	}
	
	@Override
	protected long getFrameDuration() {
		return 50;
	}
	
	protected int getColor(int ph, int ci) {
		switch (ph % 6) {
			case 0: return 0;
			case 1: return ColorUtilities.multiply(color(ci), 150);
			case 2: return ColorUtilities.multiply(color(ci), 228);
			case 3: return color(ci);
			case 4: return ColorUtilities.multiply(color(ci), 228);
			case 5: return ColorUtilities.multiply(color(ci), 150);
		}
		throw new IllegalStateException("negative mod?");
	}
}
