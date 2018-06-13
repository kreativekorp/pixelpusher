package com.kreative.unipixelpusher.clock;

import com.kreative.unipixelpusher.PixelSequence;
import com.kreative.unipixelpusher.PixelSequenceFactory;

public class ClockFactory implements PixelSequenceFactory {
	@Override
	public int size() {
		return 2;
	}
	
	@Override
	public String getName(int i) {
		if (i <= 0) return AnalogClockSequence.name;
		else return DigitalClockSequence.name;
	}
	
	@Override
	public Class<? extends PixelSequence> getClass(int i) {
		if (i <= 0) return AnalogClockSequence.class;
		else return DigitalClockSequence.class;
	}
	
	@Override
	public PixelSequence createInstance(int i) {
		if (i <= 0) return new AnalogClockSequence();
		else return new DigitalClockSequence();
	}
}
