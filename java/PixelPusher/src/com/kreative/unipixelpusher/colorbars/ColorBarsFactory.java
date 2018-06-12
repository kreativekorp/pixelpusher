package com.kreative.unipixelpusher.colorbars;

import com.kreative.unipixelpusher.PixelSequence;
import com.kreative.unipixelpusher.PixelSequenceFactory;

public class ColorBarsFactory implements PixelSequenceFactory {
	@Override
	public int size() {
		return 1;
	}
	
	@Override
	public String getName(int i) {
		return ColorBarsSequence.name;
	}
	
	@Override
	public Class<? extends PixelSequence> getClass(int i) {
		return ColorBarsSequence.class;
	}
	
	@Override
	public PixelSequence createInstance(int i) {
		return new ColorBarsSequence();
	}
}
