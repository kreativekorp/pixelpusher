package com.kreative.pixelpusher.array;

import com.kreative.pixelpusher.pixelset.PixelSetVisualEditor;

public abstract class PixelArrayVisualEditor<T extends PixelArray> extends PixelSetVisualEditor<T> {
	private static final long serialVersionUID = 1L;
	
	public PixelArrayVisualEditor(T array, PixelArrayVisualizer visualizer, PixelArrayEditor<T> editor) {
		super(array, visualizer, editor);
	}
}
