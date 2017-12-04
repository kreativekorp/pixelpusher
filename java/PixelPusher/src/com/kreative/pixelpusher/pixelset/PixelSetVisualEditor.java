package com.kreative.pixelpusher.pixelset;

public abstract class PixelSetVisualEditor<T extends PixelSet> extends PixelSetEditor<T> {
	private static final long serialVersionUID = 1L;
	
	protected PixelSetVisualizer<? super T> visualizer;
	protected PixelSetEditor<T> editor;
	protected PixelSetVisualizerThread thread;
	
	public PixelSetVisualEditor(T pixelSet, PixelSetVisualizer<? super T> visualizer, PixelSetEditor<T> editor) {
		super(pixelSet);
		this.visualizer = visualizer;
		this.editor = editor;
		this.thread = null;
	}
	
	@Override
	public void setPixelSet(T pixelSet) {
		super.setPixelSet(pixelSet);
		visualizer.setPixelSet(pixelSet);
		editor.setPixelSet(pixelSet);
	}
	
	public void start() {
		if (thread != null) thread.interrupt();
		thread = new PixelSetVisualizerThread(visualizer);
		thread.start();
	}
	
	public void stop() {
		if (thread != null) {
			thread.interrupt();
			thread = null;
		}
	}
}
