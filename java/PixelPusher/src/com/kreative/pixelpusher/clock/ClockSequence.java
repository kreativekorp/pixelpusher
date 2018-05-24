package com.kreative.pixelpusher.clock;

import java.util.Calendar;
import java.util.GregorianCalendar;
import com.kreative.pixelpusher.common.ColorConstants;
import com.kreative.pixelpusher.sequence.PixelSequence;

public class ClockSequence extends PixelSequence {
	private static final long serialVersionUID = 1L;
	
	public boolean is24Hour;
	public int hourColor;
	public int minuteColor;
	public int secondColor;
	public ClockMode mode;
	private transient Calendar now;
	
	public ClockSequence() {
		this.is24Hour = false;
		this.hourColor = ColorConstants.RED;
		this.minuteColor = ColorConstants.GREEN;
		this.secondColor = ColorConstants.BLUE;
		this.mode = ClockMode.POINT;
		this.now = new GregorianCalendar();
	}
	
	public ClockSequence(ClockSequence source) {
		this.is24Hour = source.is24Hour;
		this.hourColor = source.hourColor;
		this.minuteColor = source.minuteColor;
		this.secondColor = source.secondColor;
		this.mode = source.mode;
		this.now = new GregorianCalendar();
	}
	
	@Override
	public ClockSequence clone() {
		return new ClockSequence(this);
	}
	
	@Override
	public int getMsPerFrame() {
		return 500;
	}
	
	@Override
	public void updatePixels(long tick) {
		now = new GregorianCalendar();
	}
	
	@Override
	public int getPixelColor(int index) {
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
		
		int a = 0, r = 0, g = 0, b = 0;
		if (mode.apply(h, index)) {
			a = Math.max(a, (hourColor >> 24) & 0xFF);
			r = Math.max(r, (hourColor >> 16) & 0xFF);
			g = Math.max(g, (hourColor >>  8) & 0xFF);
			b = Math.max(b, (hourColor >>  0) & 0xFF);
		}
		if (mode.apply(m, index)) {
			a = Math.max(a, (minuteColor >> 24) & 0xFF);
			r = Math.max(r, (minuteColor >> 16) & 0xFF);
			g = Math.max(g, (minuteColor >>  8) & 0xFF);
			b = Math.max(b, (minuteColor >>  0) & 0xFF);
		}
		if (mode.apply(s, index)) {
			a = Math.max(a, (secondColor >> 24) & 0xFF);
			r = Math.max(r, (secondColor >> 16) & 0xFF);
			g = Math.max(g, (secondColor >>  8) & 0xFF);
			b = Math.max(b, (secondColor >>  0) & 0xFF);
		}
		return (a << 24) | (r << 16) | (g << 8) | (b << 0);
	}
}
