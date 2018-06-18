package com.kreative.unipixelpusher;

public class PusherThread extends Thread {
	public final PixelSequence sequence;
	public final PixelString string;
	
	public PusherThread(PixelSequence sequence, PixelString string) {
		this.sequence = sequence;
		this.string = string;
	}
	
	public void run() {
		while (!Thread.interrupted()) {
			long tick = System.currentTimeMillis();
			sequence.update(string, tick);
			tick += sequence.getUpdateInterval();
			tick -= System.currentTimeMillis();
			try { Thread.sleep((tick > 10) ? tick : 10); }
			catch (InterruptedException e) { break; }
		}
	}
}
