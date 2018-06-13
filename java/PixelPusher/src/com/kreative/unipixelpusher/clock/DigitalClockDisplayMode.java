package com.kreative.unipixelpusher.clock;

import java.util.Random;
import com.kreative.unipixelpusher.PixelString;

public interface DigitalClockDisplayMode {
	public static final DigitalClockDisplayMode SEGMENTED = new Segmented();
	public static final DigitalClockDisplayMode PIPPED = new Pipped();
	public static final DigitalClockDisplayMode UNARY = new Unary();
	public static final DigitalClockDisplayMode[] MODES = { SEGMENTED, PIPPED, UNARY };
	
	public static final int NONE = 0;
	public static final int ODD  = 1;
	public static final int EVEN = 2;
	
	public int widthParity();
	public int heightParity();
	public void render(PixelString ps, int x, int y, int w, int h, int value, int color);
	
	public static class Segmented implements DigitalClockDisplayMode {
		private static final int[] FONT = { 63, 6, 91, 79, 102, 109, 125, 7, 127, 111 };
		@Override public int widthParity() { return NONE; }
		@Override public int heightParity() { return ODD; }
		@Override
		public void render(PixelString ps, int x, int y, int w, int h, int value, int color) {
			int p = FONT[value];
			int x0 = x,                 x2 = x + w - 1;
			int y0 = y, y1 = y + h / 2, y2 = y + h - 1;
			if ((p & 0x01) != 0) {
				for (x = x0; x <= x2; x++) {
					ps.setPixel(y0, x, color);
				}
			}
			if ((p & 0x02) != 0) {
				for (y = y0; y <= y1; y++) {
					ps.setPixel(y, x2, color);
				}
			}
			if ((p & 0x04) != 0) {
				for (y = y1; y <= y2; y++) {
					ps.setPixel(y, x2, color);
				}
			}
			if ((p & 0x08) != 0) {
				for (x = x0; x <= x2; x++) {
					ps.setPixel(y2, x, color);
				}
			}
			if ((p & 0x10) != 0) {
				for (y = y1; y <= y2; y++) {
					ps.setPixel(y, x0, color);
				}
			}
			if ((p & 0x20) != 0) {
				for (y = y0; y <= y1; y++) {
					ps.setPixel(y, x0, color);
				}
			}
			if ((p & 0x40) != 0) {
				for (x = x0; x <= x2; x++) {
					ps.setPixel(y1, x, color);
				}
			}
		}
	}
	
	public static class Pipped implements DigitalClockDisplayMode {
		@Override public int widthParity() { return ODD; }
		@Override public int heightParity() { return ODD; }
		@Override
		public void render(PixelString ps, int x, int y, int w, int h, int value, int color) {
			int x0 = x + (w - 2) / 4, x1 = x + w / 2, x2 = x + w - (w + 2) / 4;
			int y0 = y + (h - 2) / 4, y1 = y + h / 2, y2 = y + h - (h + 2) / 4;
			if ((value & 1) == 1) {
				ps.setPixel(y1, x1, color);
			}
			if (value >= 2) {
				ps.setPixel(y0, x2, color);
				ps.setPixel(y2, x0, color);
			}
			if (value >= 4) {
				ps.setPixel(y0, x0, color);
				ps.setPixel(y2, x2, color);
			}
			if (value >= 6) {
				ps.setPixel(y1, x0, color);
				ps.setPixel(y1, x2, color);
			}
			if (value >= 8) {
				ps.setPixel(y0, x1, color);
				ps.setPixel(y2, x1, color);
			}
		}
	}
	
	public static class Unary implements DigitalClockDisplayMode {
		private final Random random = new Random();
		@Override public int widthParity() { return NONE; }
		@Override public int heightParity() { return NONE; }
		@Override
		public void render(PixelString ps, int x, int y, int w, int h, int value, int color) {
			for (int n = 0, m = w * h; n < value && n < m; n++) {
				while (true) {
					int j = y + random.nextInt(h);
					int i = x + random.nextInt(w);
					if (ps.getPixel(j, i) == 0) {
						ps.setPixel(j, i, color);
						break;
					}
				}
			}
		}
	}
}
