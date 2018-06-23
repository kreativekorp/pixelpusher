package com.kreative.unipixelpusher.effect;

import com.kreative.unipixelpusher.AbstractPixelSequence;
import com.kreative.unipixelpusher.ColorUtilities;
import com.kreative.unipixelpusher.PixelString;
import com.kreative.unipixelpusher.SequenceConfiguration;

public abstract class LarsonScanner extends AbstractPixelSequence.ColorPattern {
	protected int eyeSize = 0;
	
	public int getEyeSize() {
		return this.eyeSize;
	}
	
	public void setEyeSize(int eyeSize) {
		this.eyeSize = eyeSize;
	}
	
	public static class A extends LarsonScanner {
		public static final String name = "Larson Scanner A";
		
		@Override
		public void updateFrame(PixelString ps, long frame, boolean frameChanged) {
			int n = ps.length();
			int e = (eyeSize > 0 && eyeSize + 2 < n) ? eyeSize : (n < 12) ? 1 : 4;
			int n1 = n - e - 2;
			int n2 = n1 + 5;
			int n3 = n2 + n1;
			int f = (frame < n1) ? (int)frame : (frame < n2) ? n1 : (frame < n3) ? (int)(n3 - frame) : 0;
			renderFrameA(ps, f, e, n);
		}
		
		@Override
		public long getFrameCount(PixelString ps) {
			int n = ps.length();
			int e = (eyeSize > 0 && eyeSize + 2 < n) ? eyeSize : (n < 12) ? 1 : 4;
			return (n - e - 2 + 5) * 2;
		}
		
		@Override
		public String toString() {
			return name;
		}
	}
	
	public static class B extends LarsonScanner {
		public static final String name = "Larson Scanner B";
		
		@Override
		public void updateFrame(PixelString ps, long frame, boolean frameChanged) {
			int n = ps.length();
			int e = (eyeSize > 0 && eyeSize + 2 < n) ? eyeSize : (n < 20) ? 1 : 8;
			int n1 = n - e - 2;
			int n2 = n1 + 5;
			int f = (int)(frame % n2);
			int s = (int)(frame / n2);
			switch (s) {
				case 0: case 4: renderFrameA(ps, (f < n1) ? (n1 - f) : 0, e, n); break;
				case 1: case 3: renderFrameA(ps, (f < n1) ? f : n1, e, n); break;
				case 2: case 5: renderFrameB(ps, (f < n1) ? f : 0, e, n); break;
			}
		}
		
		@Override
		public long getFrameCount(PixelString ps) {
			int n = ps.length();
			int e = (eyeSize > 0 && eyeSize + 2 < n) ? eyeSize : (n < 20) ? 1 : 8;
			return (n - e - 2 + 5) * 6;
		}
		
		@Override
		public String toString() {
			return name;
		}
	}
	
	@Override
	public long getFrameDuration() {
		return 10;
	}
	
	@Override
	public void loadConfiguration(SequenceConfiguration config) {
		super.loadConfiguration(config);
		this.eyeSize = config.get("eyeSize", 0);
	}
	
	@Override
	public void saveConfiguration(SequenceConfiguration config) {
		super.saveConfiguration(config);
		config.put("eyeSize", eyeSize);
	}
	
	protected void renderFrameA(PixelString ps, int f, int e, int n) {
		for (int i = 0; i < n; i++) ps.setPixel(i, 0);
		
		ps.setPixel(f, ColorUtilities.multiply(color(f), 105));
		f++;
		
		while (e > 0) {
			ps.setPixel(f, color(f));
			f++;
			e--;
		}
		
		ps.setPixel(f, ColorUtilities.multiply(color(f), 105));
		ps.push();
	}
	
	protected void renderFrameB(PixelString ps, int f, int e, int n) {
		int n1 = n - e - 2;
		int n2 = n1 >> 1;
		int n3 = n >> 1;
		
		for (int i = 0; i < n; i++) ps.setPixel(i, 0);
		int i1 = (f < n2) ? f : (n1 - f);
		int i2 = n - i1 - 1;
		
		ps.setPixel(i1, ColorUtilities.multiply(color(i1), 105));
		ps.setPixel(i2, ColorUtilities.multiply(color(i2), 105));
		i1++; i2--; if (i2 < n3) { ps.push(); return; }
		
		while (e > 0) {
			ps.setPixel(i1, color(i1));
			ps.setPixel(i2, color(i2));
			i1++; i2--; if (i2 < n3) { ps.push(); return; }
			e--;
		}
		
		ps.setPixel(i1, ColorUtilities.multiply(color(i1), 105));
		ps.setPixel(i2, ColorUtilities.multiply(color(i2), 105));
		ps.push();
	}
}
