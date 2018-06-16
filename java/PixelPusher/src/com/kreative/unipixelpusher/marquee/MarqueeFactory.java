package com.kreative.unipixelpusher.marquee;

import com.kreative.unipixelpusher.PixelSequence;
import com.kreative.unipixelpusher.PixelSequenceFactory;

public class MarqueeFactory implements PixelSequenceFactory {
	@Override
	public int size() {
		return 1;
	}
	
	@Override
	public String getName(int i) {
		return MarqueeSequence.name;
	}
	
	@Override
	public Class<? extends PixelSequence> getClass(int i) {
		return MarqueeSequence.class;
	}
	
	@Override
	public PixelSequence createInstance(int i) {
		return new MarqueeSequence();
	}
}
