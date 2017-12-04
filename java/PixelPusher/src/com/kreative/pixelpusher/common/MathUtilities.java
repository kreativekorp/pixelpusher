package com.kreative.pixelpusher.common;

public class MathUtilities {
	private MathUtilities() {}
	
	public static int gcd(int a, int b) {
		while (true) {
			if (b == 0) return a;
			a %= b;
			if (a == 0) return b;
			b %= a;
		}
	}
}
