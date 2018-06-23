package com.kreative.unipixelpusher.clock;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import com.kreative.unipixelpusher.PixelSequence;
import com.kreative.unipixelpusher.PixelString;
import com.kreative.unipixelpusher.SequenceConfiguration;

public class DigitalClockSequence implements PixelSequence {
	public static final String name = "Digital Clock";
	
	private boolean is24Hour = false;
	private boolean showSeparators = true;
	private boolean flashSeparators = true;
	private boolean showSeconds = false;
	private DigitalClockColorMode colorMode = new DigitalClockColorMode.Monochrome();
	private DigitalClockDisplayMode displayMode = new DigitalClockDisplayMode.Segmented();
	private HashMap<String,int[]> lastValues = null;
	
	public boolean getIs24Hour() { return this.is24Hour; }
	public void setIs24Hour(boolean is24Hour) {
		this.is24Hour = is24Hour;
		this.lastValues = null;
	}
	
	public boolean getShowSeparators() { return this.showSeparators; }
	public void setShowSeparators(boolean showSeparators) {
		this.showSeparators = showSeparators;
		this.lastValues = null;
	}
	
	public boolean getFlashSeparators() { return this.flashSeparators; }
	public void setFlashSeparators(boolean flashSeparators) {
		this.flashSeparators = flashSeparators;
		this.lastValues = null;
	}
	
	public boolean getShowSeconds() { return this.showSeconds; }
	public void setShowSeconds(boolean showSeconds) {
		this.showSeconds = showSeconds;
		this.lastValues = null;
	}
	
	public DigitalClockColorMode getColorMode() { return this.colorMode; }
	public void setColorMode(DigitalClockColorMode colorMode) {
		this.colorMode = colorMode;
		this.lastValues = null;
	}
	
	public DigitalClockDisplayMode getDisplayMode() { return this.displayMode; }
	public void setDisplayMode(DigitalClockDisplayMode displayMode) {
		this.displayMode = displayMode;
		this.lastValues = null;
	}
	
	@Override
	public void update(PixelString ps, long tick) {
		int rows = ps.getRowCount();
		int cols = ps.getColumnCount();
		
		if (lastValues == null) lastValues = new HashMap<String,int[]>();
		int[] lastValue = lastValues.get(ps.id());
		if (lastValue == null || lastValue[6] != rows || lastValue[7] != cols) {
			lastValue = new int[] { -1, -1, -1, -1, -1, -1, rows, cols };
			lastValues.put(ps.id(), lastValue);
			for (int i = 0, n = ps.length(); i < n; i++) ps.setPixel(i, 0);
		}
		
		GregorianCalendar now = new GregorianCalendar();
		int h = now.get(Calendar.HOUR_OF_DAY);
		int m = now.get(Calendar.MINUTE);
		int s = now.get(Calendar.SECOND);
		if (!is24Hour) {
			if (h <  1) h += 12;
			if (h > 12) h -= 12;
		}
		
		int v;
		if (cols >= rows) {
			
			int b = 3;
			if (!is24Hour) b++;
			if (showSeparators || showSeconds) b += 2;
			if (showSeparators && showSeconds) b += 4;
			int d = 3;
			if (is24Hour) d++;
			if (showSeconds) d += 2;
			
			int dw = (cols - b) / d;
			switch (displayMode.widthParity()) {
				case DigitalClockDisplayMode.ODD: if ((dw & 1) == 0) dw--; break;
				case DigitalClockDisplayMode.EVEN: if ((dw & 1) == 1) dw--; break;
			}
			if (dw < 1) return;
			int dx = (cols - (dw * d + b)) / 2;
			
			int dh = rows;
			switch (displayMode.heightParity()) {
				case DigitalClockDisplayMode.ODD: if ((dh & 1) == 0) dh--; break;
				case DigitalClockDisplayMode.EVEN: if ((dh & 1) == 1) dh--; break;
			}
			if (dh < 1) return;
			int cy1 = (dh - 2) / 3;
			int cy2 = dh - cy1 - 1;
			
			if (is24Hour) {
				if (lastValue[0] != (v = h / 10)) {
					lastValue[0] = v;
					clearRect(ps, dx, 0, dw, dh);
					int c = colorMode.getColor(DigitalClockColorMode.HOUR_TENS, v);
					displayMode.render(ps, dx, 0, dw, dh, v, c);
				}
				dx += dw + 1;
			} else {
				if (lastValue[0] != (v = h / 10)) {
					lastValue[0] = v;
					clearRect(ps, dx, 0, 1, dh);
					if (h >= 10) {
						int c = colorMode.getColor(DigitalClockColorMode.HOUR_TENS, v);
						displayMode.render(ps, dx, 0, 1, dh, v, c);
					}
				}
				dx += 2;
			}
			if (lastValue[1] != (v = h % 10)) {
				lastValue[1] = v;
				clearRect(ps, dx, 0, dw, dh);
				int c = colorMode.getColor(DigitalClockColorMode.HOUR_ONES, v);
				displayMode.render(ps, dx, 0, dw, dh, v, c);
			}
			dx += dw + 1;
			
			if (showSeparators) {
				if ((s & 1) == 0 || !flashSeparators) {
					int c = colorMode.getColor(DigitalClockColorMode.HM_SEPARATOR, DigitalClockColorMode.HM_SEPARATOR);
					ps.setPixel(cy1, dx, c);
					ps.setPixel(cy2, dx, c);
				} else {
					ps.setPixel(cy1, dx, 0);
					ps.setPixel(cy2, dx, 0);
				}
				dx += 2;
			}
			
			if (lastValue[2] != (v = m / 10)) {
				lastValue[2] = v;
				clearRect(ps, dx, 0, dw, dh);
				int c = colorMode.getColor(DigitalClockColorMode.MINUTE_TENS, v);
				displayMode.render(ps, dx, 0, dw, dh, v, c);
			}
			dx += dw + 1;
			if (lastValue[3] != (v = m % 10)) {
				lastValue[3] = v;
				clearRect(ps, dx, 0, dw, dh);
				int c = colorMode.getColor(DigitalClockColorMode.MINUTE_ONES, v);
				displayMode.render(ps, dx, 0, dw, dh, v, c);
			}
			dx += dw + 1;
			
			if (showSeconds) {
				if (showSeparators) {
					if ((s & 1) == 0 || !flashSeparators) {
						int c = colorMode.getColor(DigitalClockColorMode.MS_SEPARATOR, DigitalClockColorMode.MS_SEPARATOR);
						ps.setPixel(cy1, dx, c);
						ps.setPixel(cy2, dx, c);
					} else {
						ps.setPixel(cy1, dx, 0);
						ps.setPixel(cy2, dx, 0);
					}
					dx += 2;
				}
				
				if (lastValue[4] != (v = s / 10)) {
					lastValue[4] = v;
					clearRect(ps, dx, 0, dw, dh);
					int c = colorMode.getColor(DigitalClockColorMode.SECOND_TENS, v);
					displayMode.render(ps, dx, 0, dw, dh, v, c);
				}
				dx += dw + 1;
				if (lastValue[5] != (v = s % 10)) {
					lastValue[5] = v;
					clearRect(ps, dx, 0, dw, dh);
					int c = colorMode.getColor(DigitalClockColorMode.SECOND_ONES, v);
					displayMode.render(ps, dx, 0, dw, dh, v, c);
				}
			}
			
		} else {
			
			int b = 3;
			if (showSeparators || showSeconds) b += 2;
			if (showSeparators && showSeconds) b += 4;
			int d = 4;
			if (showSeconds) d += 2;
			
			int dh = (rows - b) / d;
			switch (displayMode.heightParity()) {
				case DigitalClockDisplayMode.ODD: if ((dh & 1) == 0) dh--; break;
				case DigitalClockDisplayMode.EVEN: if ((dh & 1) == 1) dh--; break;
			}
			if (dh < 1) return;
			int dy = (rows - (dh * d + b)) / 2;
			
			int dw = cols;
			switch (displayMode.widthParity()) {
				case DigitalClockDisplayMode.ODD: if ((dw & 1) == 0) dw--; break;
				case DigitalClockDisplayMode.EVEN: if ((dw & 1) == 1) dw--; break;
			}
			if (dw < 1) return;
			int cx1 = (dw - 2) / 3;
			int cx2 = dw - cx1 - 1;
			
			if (lastValue[0] != (v = h / 10)) {
				lastValue[0] = v;
				clearRect(ps, 0, dy, dw, dh);
				int c = colorMode.getColor(DigitalClockColorMode.HOUR_TENS, v);
				displayMode.render(ps, 0, dy, dw, dh, v, c);
			}
			dy += dh + 1;
			if (lastValue[1] != (v = h % 10)) {
				lastValue[1] = v;
				clearRect(ps, 0, dy, dw, dh);
				int c = colorMode.getColor(DigitalClockColorMode.HOUR_ONES, v);
				displayMode.render(ps, 0, dy, dw, dh, v, c);
			}
			dy += dh + 1;
			
			if (showSeparators) {
				if ((s & 1) == 0 || !flashSeparators) {
					int c = colorMode.getColor(DigitalClockColorMode.HM_SEPARATOR, DigitalClockColorMode.HM_SEPARATOR);
					ps.setPixel(dy, cx1, c);
					ps.setPixel(dy, cx2, c);
				} else {
					ps.setPixel(dy, cx1, 0);
					ps.setPixel(dy, cx2, 0);
				}
				dy += 2;
			}
			
			if (lastValue[2] != (v = m / 10)) {
				lastValue[2] = v;
				clearRect(ps, 0, dy, dw, dh);
				int c = colorMode.getColor(DigitalClockColorMode.MINUTE_TENS, v);
				displayMode.render(ps, 0, dy, dw, dh, v, c);
			}
			dy += dh + 1;
			if (lastValue[3] != (v = m % 10)) {
				lastValue[3] = v;
				clearRect(ps, 0, dy, dw, dh);
				int c = colorMode.getColor(DigitalClockColorMode.MINUTE_ONES, v);
				displayMode.render(ps, 0, dy, dw, dh, v, c);
			}
			dy += dh + 1;
			
			if (showSeconds) {
				if (showSeparators) {
					if ((s & 1) == 0 || !flashSeparators) {
						int c = colorMode.getColor(DigitalClockColorMode.MS_SEPARATOR, DigitalClockColorMode.MS_SEPARATOR);
						ps.setPixel(dy, cx1, c);
						ps.setPixel(dy, cx2, c);
					} else {
						ps.setPixel(dy, cx1, 0);
						ps.setPixel(dy, cx2, 0);
					}
					dy += 2;
				}
				
				if (lastValue[4] != (v = s / 10)) {
					lastValue[4] = v;
					clearRect(ps, 0, dy, dw, dh);
					int c = colorMode.getColor(DigitalClockColorMode.SECOND_TENS, v);
					displayMode.render(ps, 0, dy, dw, dh, v, c);
				}
				dy += dh + 1;
				if (lastValue[5] != (v = s % 10)) {
					lastValue[5] = v;
					clearRect(ps, 0, dy, dw, dh);
					int c = colorMode.getColor(DigitalClockColorMode.SECOND_ONES, v);
					displayMode.render(ps, 0, dy, dw, dh, v, c);
				}
			}
			
		}
		
		ps.push();
	}
	
	private static void clearRect(PixelString ps, int x, int y, int w, int h) {
		for (int j = 0; j < h; j++) {
			for (int i = 0; i < w; i++) {
				ps.setPixel(y + j, x + i, 0);
			}
		}
	}
	
	@Override
	public long getUpdateInterval() {
		return 250;
	}
	
	@Override
	public void loadConfiguration(SequenceConfiguration config) {
		this.is24Hour = config.get("is24Hour", false);
		this.showSeparators = config.get("showSeparators", true);
		this.flashSeparators = config.get("flashSeparators", true);
		this.showSeconds = config.get("showSeconds", false);
		
		String colorMode = config.get("colorMode", "MONOCHROME");
		if (colorMode.equalsIgnoreCase("MONOCHROME")) {
			this.colorMode = new DigitalClockColorMode.Monochrome(
				config.get("colorMode.color", -1)
			);
		} else if (colorMode.equalsIgnoreCase("POSITIONAL")) {
			this.colorMode = new DigitalClockColorMode.Positional(
				config.get("colorMode.color.hourTens", -1),
				config.get("colorMode.color.hourOnes", -1),
				config.get("colorMode.color.hmSeparator", -1),
				config.get("colorMode.color.minuteTens", -1),
				config.get("colorMode.color.minuteOnes", -1),
				config.get("colorMode.color.msSeparator", -1),
				config.get("colorMode.color.secondTens", -1),
				config.get("colorMode.color.secondOnes", -1)
			);
		} else if (colorMode.equalsIgnoreCase("SYNAESTHETIC")) {
			this.colorMode = new DigitalClockColorMode.Synaesthetic(
				config.get("colorMode.color.digit0", -1),
				config.get("colorMode.color.digit1", -1),
				config.get("colorMode.color.digit2", -1),
				config.get("colorMode.color.digit3", -1),
				config.get("colorMode.color.digit4", -1),
				config.get("colorMode.color.digit5", -1),
				config.get("colorMode.color.digit6", -1),
				config.get("colorMode.color.digit7", -1),
				config.get("colorMode.color.digit8", -1),
				config.get("colorMode.color.digit9", -1),
				config.get("colorMode.color.hmSeparator", -1),
				config.get("colorMode.color.msSeparator", -1)
			);
		} else {
			this.colorMode = new DigitalClockColorMode.Monochrome();
		}
		
		String displayMode = config.get("displayMode", "SEGMENTED");
		if (displayMode.equalsIgnoreCase("SEGMENTED")) {
			this.displayMode = new DigitalClockDisplayMode.Segmented();
		} else if (displayMode.equalsIgnoreCase("PIPPED")) {
			this.displayMode = new DigitalClockDisplayMode.Pipped();
		} else if (displayMode.equalsIgnoreCase("UNARY")) {
			this.displayMode = new DigitalClockDisplayMode.Unary();
		} else {
			this.displayMode = new DigitalClockDisplayMode.Segmented();
		}
		
		this.lastValues = null;
	}
	
	@Override
	public void saveConfiguration(SequenceConfiguration config) {
		config.put("is24Hour", is24Hour);
		config.put("showSeparators", showSeparators);
		config.put("flashSeparators", flashSeparators);
		config.put("showSeconds", showSeconds);
		
		if (colorMode instanceof DigitalClockColorMode.Monochrome) {
			DigitalClockColorMode.Monochrome cm = (DigitalClockColorMode.Monochrome)colorMode;
			config.put("colorMode", "MONOCHROME");
			config.put("colorMode.color", cm.color);
		} else if (colorMode instanceof DigitalClockColorMode.Positional) {
			DigitalClockColorMode.Positional cm = (DigitalClockColorMode.Positional)colorMode;
			config.put("colorMode", "POSITIONAL");
			config.put("colorMode.color.hourTens", cm.hourTens);
			config.put("colorMode.color.hourOnes", cm.hourOnes);
			config.put("colorMode.color.hmSeparator", cm.hmSeparator);
			config.put("colorMode.color.minuteTens", cm.minuteTens);
			config.put("colorMode.color.minuteOnes", cm.minuteOnes);
			config.put("colorMode.color.msSeparator", cm.msSeparator);
			config.put("colorMode.color.secondTens", cm.secondTens);
			config.put("colorMode.color.secondOnes", cm.secondOnes);
		} else if (colorMode instanceof DigitalClockColorMode.Synaesthetic) {
			DigitalClockColorMode.Synaesthetic cm = (DigitalClockColorMode.Synaesthetic)colorMode;
			config.put("colorMode", "SYNAESTHETIC");
			config.put("colorMode.color.digit0", cm.color0);
			config.put("colorMode.color.digit1", cm.color1);
			config.put("colorMode.color.digit2", cm.color2);
			config.put("colorMode.color.digit3", cm.color3);
			config.put("colorMode.color.digit4", cm.color4);
			config.put("colorMode.color.digit5", cm.color5);
			config.put("colorMode.color.digit6", cm.color6);
			config.put("colorMode.color.digit7", cm.color7);
			config.put("colorMode.color.digit8", cm.color8);
			config.put("colorMode.color.digit9", cm.color9);
			config.put("colorMode.color.hmSeparator", cm.hmSeparator);
			config.put("colorMode.color.msSeparator", cm.msSeparator);
		}
		
		if (displayMode instanceof DigitalClockDisplayMode.Segmented) {
			config.put("displayMode", "SEGMENTED");
		} else if (displayMode instanceof DigitalClockDisplayMode.Pipped) {
			config.put("displayMode", "PIPPED");
		} else if (displayMode instanceof DigitalClockDisplayMode.Unary) {
			config.put("displayMode", "UNARY");
		}
	}
	
	@Override
	public String toString() {
		return name;
	}
}
