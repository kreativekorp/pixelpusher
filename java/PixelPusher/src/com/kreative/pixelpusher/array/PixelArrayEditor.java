package com.kreative.pixelpusher.array;

import com.kreative.pixelpusher.pixelset.PixelSetEditor;

public abstract class PixelArrayEditor<T extends PixelArray> extends PixelSetEditor<T> {
	private static final long serialVersionUID = 1L;
	
	public PixelArrayEditor(T array) {
		super(array);
	}
}
