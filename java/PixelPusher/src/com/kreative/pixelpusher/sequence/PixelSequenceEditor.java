package com.kreative.pixelpusher.sequence;

import com.kreative.pixelpusher.pixelset.PixelSetEditor;

public abstract class PixelSequenceEditor<T extends PixelSequence> extends PixelSetEditor<T> {
	private static final long serialVersionUID = 1L;
	
	public PixelSequenceEditor(T sequence) {
		super(sequence);
	}
}
