package com.kreative.unipixelpusher.effect;

import java.util.Random;
import com.kreative.unipixelpusher.ColorUtilities;
import com.kreative.unipixelpusher.FrameBasedColorPatternPixelSequence;
import com.kreative.unipixelpusher.PixelString;

public class Moodlight extends FrameBasedColorPatternPixelSequence {
	public static final String name = "Moodlight";
	
	protected Random random = new Random();
	protected int[] fromColor = null;
	protected int[] toColor = null;
	
	@Override
	protected int[] defaultColorPattern() {
		return rainbow();
	}
	
	@Override
	public void updateFrame(PixelString ps, long dnu1, boolean dnu2) {
		int n = ps.length();
		if (fromColor == null || fromColor.length != n || toColor == null || toColor.length != n) {
			fromColor = new int[n];
			toColor = new int[n];
			for (int i = 0; i < n; i++) {
				fromColor[i] = color(random.nextInt(length()));
				toColor[i] = color(random.nextInt(length()));
			}
		} else if (this.frame > 0xFF) {
			for (int i = 0; i < n; i++) {
				fromColor[i] = toColor[i];
				toColor[i] = color(random.nextInt(length()));
			}
		}
		int sf = (int)(this.frame &= 0xFF);
		for (int i = 0; i < n; i++) {
			ps.setPixel(i, ColorUtilities.blend(fromColor[i], toColor[i], sf, 0x100));
		}
		ps.push();
	}
	
	@Override
	public long getFrameCount(PixelString ps) {
		return 0;
	}
	
	@Override
	public long getFrameDuration() {
		return 3;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
