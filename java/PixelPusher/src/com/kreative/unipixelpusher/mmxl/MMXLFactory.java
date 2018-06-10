package com.kreative.unipixelpusher.mmxl;

import com.kreative.unipixelpusher.PixelSequence;
import com.kreative.unipixelpusher.PixelSequenceFactory;

public class MMXLFactory implements PixelSequenceFactory {
	@Override
	public int size() {
		return 1;
	}
	
	@Override
	public String getName(int i) {
		return MMXLSequence.name;
	}
	
	@Override
	public Class<? extends PixelSequence> getClass(int i) {
		return MMXLSequence.class;
	}
	
	@Override
	public PixelSequence createInstance(int i) {
		return new MMXLSequence();
	}
}
