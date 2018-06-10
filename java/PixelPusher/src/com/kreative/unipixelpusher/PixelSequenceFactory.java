package com.kreative.unipixelpusher;

public interface PixelSequenceFactory {
	public int size();
	public String getName(int i);
	public Class<? extends PixelSequence> getClass(int i);
	public PixelSequence createInstance(int i);
}
