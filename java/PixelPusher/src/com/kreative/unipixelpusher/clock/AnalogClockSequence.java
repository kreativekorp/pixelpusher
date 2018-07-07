package com.kreative.unipixelpusher.clock;

import java.awt.Component;
import java.util.Calendar;
import java.util.GregorianCalendar;
import com.kreative.unipixelpusher.ColorConstants;
import com.kreative.unipixelpusher.PixelSequence;
import com.kreative.unipixelpusher.PixelString;
import com.kreative.unipixelpusher.SequenceConfiguration;

public class AnalogClockSequence implements PixelSequence {
	public static final String name = "Analog Clock";
	
	public boolean is24Hour = false;
	public int hourColor = ColorConstants.RED;
	public int minuteColor = ColorConstants.GREEN;
	public int secondColor = ColorConstants.BLUE;
	public AnalogClockMode mode = AnalogClockMode.POINT;
	
	@Override
	public void update(PixelString ps, long tick) {
		GregorianCalendar now = new GregorianCalendar();
		int h = now.get(Calendar.HOUR_OF_DAY);
		int m = now.get(Calendar.MINUTE);
		int s = now.get(Calendar.SECOND);
		if (mode.scaleHour()) {
			h *= 5; // scale hour from 24 up to 120
			h += m / 12; // add minute scaled from 60 down to 5
			if (h >= 60) h -= 60; // modulus 60
		} else if (!is24Hour) {
			if (h <  1) h += 12;
			if (h > 12) h -= 12;
		}
		for (int i = 0; i < 60; i++) {
			int a = 0, r = 0, g = 0, b = 0;
			if (mode.apply(h, i)) {
				a = Math.max(a, (hourColor >> 24) & 0xFF);
				r = Math.max(r, (hourColor >> 16) & 0xFF);
				g = Math.max(g, (hourColor >>  8) & 0xFF);
				b = Math.max(b, (hourColor >>  0) & 0xFF);
			}
			if (mode.apply(m, i)) {
				a = Math.max(a, (minuteColor >> 24) & 0xFF);
				r = Math.max(r, (minuteColor >> 16) & 0xFF);
				g = Math.max(g, (minuteColor >>  8) & 0xFF);
				b = Math.max(b, (minuteColor >>  0) & 0xFF);
			}
			if (mode.apply(s, i)) {
				a = Math.max(a, (secondColor >> 24) & 0xFF);
				r = Math.max(r, (secondColor >> 16) & 0xFF);
				g = Math.max(g, (secondColor >>  8) & 0xFF);
				b = Math.max(b, (secondColor >>  0) & 0xFF);
			}
			ps.setPixel(i, (a << 24) | (r << 16) | (g << 8) | (b << 0));
		}
		ps.push();
	}
	
	@Override
	public long getUpdateInterval() {
		return 250;
	}
	
	@Override
	public synchronized void loadConfiguration(SequenceConfiguration config) {
		this.is24Hour = config.get("is24Hour", false);
		this.hourColor = config.get("hourColor", ColorConstants.RED);
		this.minuteColor = config.get("minuteColor", ColorConstants.GREEN);
		this.secondColor = config.get("secondColor", ColorConstants.BLUE);
		this.mode = config.get("mode", AnalogClockMode.class, AnalogClockMode.POINT);
	}
	
	@Override
	public synchronized void saveConfiguration(SequenceConfiguration config) {
		config.put("is24Hour", is24Hour);
		config.put("hourColor", hourColor);
		config.put("minuteColor", minuteColor);
		config.put("secondColor", secondColor);
		config.put("mode", mode);
	}
	
	@Override
	public boolean hasConfigurationPanel() {
		return true;
	}
	
	@Override
	public Component createConfigurationPanel() {
		return new AnalogClockConfigPanel(this);
	}
	
	@Override
	public String toString() {
		return name;
	}
}
