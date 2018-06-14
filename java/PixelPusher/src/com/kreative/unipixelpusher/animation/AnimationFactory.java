package com.kreative.unipixelpusher.animation;

import com.kreative.unipixelpusher.PixelSequence;
import com.kreative.unipixelpusher.PixelSequenceFactory;

public class AnimationFactory implements PixelSequenceFactory {
	@Override
	public int size() {
		return 1;
	}
	
	@Override
	public String getName(int i) {
		return AnimationSequence.name;
	}
	
	@Override
	public Class<? extends PixelSequence> getClass(int i) {
		return AnimationSequence.class;
	}
	
	@Override
	public PixelSequence createInstance(int i) {
		return new AnimationSequence();
	}
}
