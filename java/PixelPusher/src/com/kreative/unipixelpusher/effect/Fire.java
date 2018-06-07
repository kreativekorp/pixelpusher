package com.kreative.unipixelpusher.effect;

import java.util.Random;
import com.kreative.unipixelpusher.AbstractPixelSequence;
import com.kreative.unipixelpusher.PixelString;

public class Fire extends AbstractPixelSequence {
	protected static final int COOLING = 55;
	protected static final int SPARKING = 120;
	protected static final int SPEED = 15;
	
	protected Random random = new Random();
	protected int[] heat = null;
	
	@Override
	public void updateFrame(PixelString ps, long frame, boolean frameChanged) {
		int n = ps.length();
		if (heat == null || heat.length != n) heat = new int[n];
		if (frameChanged) {
			
			// Step 1. Cool down every cell a little.
			for (int i = 0; i < n; i++) {
				int cooldown = random.nextInt(COOLING * 10 / n + 2);
				if (cooldown >= heat[i]) {
					heat[i] = 0;
				} else {
					heat[i] -= cooldown;
				}
			}
			
			// Step 2. Heat from each cell drifts up and diffuses a little.
			for (int i = n - 1; i >= 2; i--) {
				heat[i] = (heat[i - 1] + heat[i - 2] + heat[i - 2]) / 3;
			}
			
			// Step 3. Randomly ignite new sparks near the bottom.
			if (random.nextInt(255) < SPARKING) {
				int i = random.nextInt(7);
				heat[i] += random.nextInt(255 - 160) + 160;
				heat[i] &= 0xFF;
			}
			
			// Step 4. Convert heat to LED colors.
			for (int i = 0; i < n; i++) {
				if (heat[i] < 0) {
					ps.setPixel(i, 0);
				} else if (heat[i] <  85) {
					ps.setPixel(i, 0xFF000000 + 0x030000 * heat[i]);
				} else if (heat[i] < 170) {
					ps.setPixel(i, 0xFFFF0000 + 0x000300 * (heat[i] -  85));
				} else if (heat[i] < 255) {
					ps.setPixel(i, 0xFFFFFF00 + 0x000003 * (heat[i] - 170));
				} else {
					ps.setPixel(i, -1);
				}
			}
			
			ps.push();
		}
	}
	
	@Override
	public long getFrameCount(PixelString ps) {
		return 0;
	}
	
	@Override
	public long getFrameDuration() {
		return SPEED;
	}
	
	@Override
	public String toString() {
		return "Fire";
	}
}
