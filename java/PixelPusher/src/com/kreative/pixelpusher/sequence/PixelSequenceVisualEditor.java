package com.kreative.pixelpusher.sequence;

import com.kreative.pixelpusher.pixelset.PixelSetVisualEditor;

public abstract class PixelSequenceVisualEditor<T extends PixelSequence> extends PixelSetVisualEditor<T> {
	private static final long serialVersionUID = 1L;
	
	public PixelSequenceVisualEditor(T sequence, PixelSequenceVisualizer visualizer, PixelSequenceEditor<T> editor) {
		super(sequence, visualizer, editor);
	}
}
