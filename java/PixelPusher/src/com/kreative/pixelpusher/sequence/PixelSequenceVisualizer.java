package com.kreative.pixelpusher.sequence;

import com.kreative.pixelpusher.pixelset.PixelSetVisualizer;

public abstract class PixelSequenceVisualizer extends PixelSetVisualizer<PixelSequence> {
	private static final long serialVersionUID = 1L;
	
	public PixelSequenceVisualizer(PixelSequence sequence) {
		super(sequence);
	}
}
