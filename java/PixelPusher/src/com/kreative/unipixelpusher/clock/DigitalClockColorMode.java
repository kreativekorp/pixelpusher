package com.kreative.unipixelpusher.clock;

import com.kreative.unipixelpusher.ColorConstants;

public interface DigitalClockColorMode {
	public static final int HOUR_TENS = -1;
	public static final int HOUR_ONES = -2;
	public static final int HM_SEPARATOR = -3;
	public static final int MINUTE_TENS = -4;
	public static final int MINUTE_ONES = -5;
	public static final int MS_SEPARATOR = -6;
	public static final int SECOND_TENS = -7;
	public static final int SECOND_ONES = -8;
	
	public int getColor(int position, int value);
	
	public static class Monochrome implements DigitalClockColorMode {
		public final int color;
		
		public Monochrome() {
			this(ColorConstants.RED);
		}
		
		public Monochrome(int color) {
			this.color = color;
		}
		
		@Override
		public int getColor(int position, int value) {
			return color;
		}
	}
	
	public static class Positional implements DigitalClockColorMode {
		public final int hourTens;
		public final int hourOnes;
		public final int hmSeparator;
		public final int minuteTens;
		public final int minuteOnes;
		public final int msSeparator;
		public final int secondTens;
		public final int secondOnes;
		
		public Positional() {
			this(
				ColorConstants.YELLOW, ColorConstants.RED, ColorConstants.WHITE,
				ColorConstants.GREEN, ColorConstants.BLUE, ColorConstants.WHITE,
				ColorConstants.ORANGE, ColorConstants.VIOLET
			);
		}
		
		public Positional(
			int hourTens, int hourOnes, int hmSeparator,
			int minuteTens, int minuteOnes, int msSeparator,
			int secondTens, int secondOnes
		) {
			this.hourTens = hourTens;
			this.hourOnes = hourOnes;
			this.hmSeparator = hmSeparator;
			this.minuteTens = minuteTens;
			this.minuteOnes = minuteOnes;
			this.msSeparator = msSeparator;
			this.secondTens = secondTens;
			this.secondOnes = secondOnes;
		}
		
		@Override
		public int getColor(int position, int value) {
			switch (position) {
				case HOUR_TENS: return hourTens;
				case HOUR_ONES: return hourOnes;
				case HM_SEPARATOR: return hmSeparator;
				case MINUTE_TENS: return minuteTens;
				case MINUTE_ONES: return minuteOnes;
				case MS_SEPARATOR: return msSeparator;
				case SECOND_TENS: return secondTens;
				case SECOND_ONES: return secondOnes;
				default: return 0;
			}
		}
	}
	
	public static class Synaesthetic implements DigitalClockColorMode {
		public final int color0;
		public final int color1;
		public final int color2;
		public final int color3;
		public final int color4;
		public final int color5;
		public final int color6;
		public final int color7;
		public final int color8;
		public final int color9;
		public final int hmSeparator;
		public final int msSeparator;
		
		public Synaesthetic() {
			this(
				ColorConstants.BLACK, ColorConstants.BROWN, ColorConstants.RED,
				ColorConstants.ORANGE, ColorConstants.YELLOW, ColorConstants.GREEN,
				ColorConstants.BLUE, ColorConstants.VIOLET, ColorConstants.GRAY,
				ColorConstants.WHITE, ColorConstants.WHITE, ColorConstants.WHITE
			);
		}
		
		public Synaesthetic(
			int color0, int color1, int color2, int color3, int color4,
			int color5, int color6, int color7, int color8, int color9,
			int hmSeparator, int msSeparator
		) {
			this.color0 = color0;
			this.color1 = color1;
			this.color2 = color2;
			this.color3 = color3;
			this.color4 = color4;
			this.color5 = color5;
			this.color6 = color6;
			this.color7 = color7;
			this.color8 = color8;
			this.color9 = color9;
			this.hmSeparator = hmSeparator;
			this.msSeparator = msSeparator;
		}
		
		@Override
		public int getColor(int position, int value) {
			switch (value) {
				case 0: return color0;
				case 1: return color1;
				case 2: return color2;
				case 3: return color3;
				case 4: return color4;
				case 5: return color5;
				case 6: return color6;
				case 7: return color7;
				case 8: return color8;
				case 9: return color9;
				case HM_SEPARATOR: return hmSeparator;
				case MS_SEPARATOR: return msSeparator;
				default: return 0;
			}
		}
	}
}
