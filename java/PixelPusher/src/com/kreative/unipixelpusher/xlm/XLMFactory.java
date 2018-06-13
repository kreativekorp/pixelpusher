package com.kreative.unipixelpusher.xlm;

import com.kreative.unipixelpusher.PixelSequence;
import com.kreative.unipixelpusher.PixelSequenceFactory;

public class XLMFactory implements PixelSequenceFactory {
	@Override
	public int size() {
		return 1;
	}
	
	@Override
	public String getName(int i) {
		return XLMSequence.name;
	}
	
	@Override
	public Class<? extends PixelSequence> getClass(int i) {
		return XLMSequence.class;
	}
	
	@Override
	public PixelSequence createInstance(int i) {
		return new XLMSequence();
	}
}
