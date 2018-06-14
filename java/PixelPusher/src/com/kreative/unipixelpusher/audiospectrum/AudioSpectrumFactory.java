package com.kreative.unipixelpusher.audiospectrum;

import com.kreative.unipixelpusher.PixelSequence;
import com.kreative.unipixelpusher.PixelSequenceFactory;

public class AudioSpectrumFactory implements PixelSequenceFactory {
	@Override
	public int size() {
		return 1;
	}
	
	@Override
	public String getName(int i) {
		return AudioSpectrumSequence.name;
	}
	
	@Override
	public Class<? extends PixelSequence> getClass(int i) {
		return AudioSpectrumSequence.class;
	}
	
	@Override
	public PixelSequence createInstance(int i) {
		return new AudioSpectrumSequence();
	}
}
