package com.kreative.pixelpusher.pixelset;

import javax.swing.JPanel;

public abstract class PixelSetEditor<T extends PixelSet> extends JPanel {
	private static final long serialVersionUID = 1L;
	
	protected T pixelSet;
	
	public PixelSetEditor(T pixelSet) {
		this.pixelSet = pixelSet;
	}
	
	public T getPixelSet() {
		return pixelSet;
	}
	
	public void setPixelSet(T pixelSet) {
		this.pixelSet = pixelSet;
	}
}
