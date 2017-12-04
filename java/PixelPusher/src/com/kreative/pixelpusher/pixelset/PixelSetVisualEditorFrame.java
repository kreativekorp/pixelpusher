package com.kreative.pixelpusher.pixelset;

public class PixelSetVisualEditorFrame extends PixelSetEditorFrame {
	private static final long serialVersionUID = 1L;
	
	private PixelSetVisualEditor<? extends PixelSet> editor;
	
	public PixelSetVisualEditorFrame(String title, PixelSetVisualEditor<? extends PixelSet> editor) {
		super(title, editor);
		this.editor = editor;
	}
	
	public void start() {
		editor.start();
	}
	
	public void stop() {
		editor.stop();
	}
}
