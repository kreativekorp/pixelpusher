package com.kreative.pixelpusher.pixelset;

public class PixelSetVisualizerThread extends Thread {
	private PixelSetVisualizer<? extends PixelSet> visualizer;
	
	public PixelSetVisualizerThread(PixelSetVisualizer<? extends PixelSet> visualizer) {
		this.visualizer = visualizer;
	}
	
	@Override
	public void run() {
		while (!Thread.interrupted()) {
			visualizer.repaint();
			int ticks = visualizer.getPixelSet().getMsPerFrame() / 10;
			if (ticks < 20) ticks = 20;
			try {
				Thread.sleep(ticks);
			} catch (InterruptedException ie) {
				break;
			}
		}
	}
}
