package com.kreative.pixelpusher.mmxl;

import java.util.Arrays;

public class MMXLColorPattern {
	private final String name;
	private final int length;
	private final int[] colors;
	private final int[] probabilities;
	private final boolean random;
	
	public MMXLColorPattern() {
		this.name = "Color Pattern " + System.currentTimeMillis();
		this.length = 1;
		this.colors = new int[]{-1};
		this.probabilities = new int[]{1};
		this.random = false;
	}
	
	public MMXLColorPattern(String name, int length, int[] colors, int[] probs, boolean random) {
		this.name = (name == null || name.length() == 0) ? ("Color Pattern " + System.currentTimeMillis()) : name;
		this.length = (length < 1) ? 1 : length;
		this.colors = new int[this.length];
		this.probabilities = new int[this.length];
		for (int i = 0; i < this.length; i++) {
			this.colors[i] = (colors == null || colors.length == 0) ? -1 : colors[i % colors.length];
			this.probabilities[i] = (probs == null || probs.length == 0) ? 1 : probs[i % probs.length];
		}
		this.random = random;
	}
	
	public String name() { return name; }
	public int length() { return length; }
	public int color(int i) { return colors[i]; }
	public int probability(int i) { return probabilities[i]; }
	public boolean random() { return random; }
	
	public void apply(MMXLSequence sequence) {
		sequence.setColorPattern(colors, probabilities, length);
		sequence.setColorPatternRandom(random);
	}
	
	public boolean matches(int[] colors, int[] probs, boolean random) {
		return Arrays.equals(colors, this.colors)
		    && Arrays.equals(probs, this.probabilities)
		    && random == this.random;
	}
}
