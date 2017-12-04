package com.kreative.pixelpusher.device;

import com.heroicrobot.dropbit.registry.DeviceRegistry;
import com.kreative.pixelpusher.pixelset.PixelSetInfoSet;

public class PixelPusherController {
	private DeviceRegistry registry;
	private PixelSetInfoSet pixelSets;
	private StripInfoSet strips;
	private PixelPusherThread thread;
	
	public PixelPusherController() {
		this.registry = new DeviceRegistry();
		this.pixelSets = new PixelSetInfoSet();
		this.strips = new StripInfoSet();
		this.thread = null;
	}
	
	public DeviceRegistry getDeviceRegistry() {
		return this.registry;
	}
	
	public PixelSetInfoSet getPixelSetInfoSet() {
		return this.pixelSets;
	}
	
	public StripInfoSet getStripInfoSet() {
		return this.strips;
	}
	
	public boolean isRunning() {
		return this.thread != null;
	}
	
	public void start() {
		this.registry.startPushing();
		if (this.thread != null) {
			this.thread.interrupt();
			try { this.thread.join(); }
			catch (InterruptedException ie) {}
		}
		this.thread = new PixelPusherThread(registry, strips);
		this.thread.start();
	}
	
	public void stop() {
		if (this.thread != null) {
			this.thread.interrupt();
			try { this.thread.join(); }
			catch (InterruptedException ie) {}
			this.thread = null;
		}
		this.registry.stopPushing();
	}
}
