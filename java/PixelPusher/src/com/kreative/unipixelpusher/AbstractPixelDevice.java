package com.kreative.unipixelpusher;

import java.util.List;

public abstract class AbstractPixelDevice implements PixelDevice {
	protected final PixelDeviceRegistry parent;
	protected final DeviceConfiguration config;
	protected final List<PixelDeviceListener> listeners;
	
	protected AbstractPixelDevice(PixelDeviceRegistry parent, DeviceConfiguration config, List<PixelDeviceListener> listeners) {
		this.parent = parent;
		this.config = config;
		this.listeners = listeners;
	}
	
	protected AbstractPixelDevice(AbstractPixelDeviceRegistry parent) {
		this.parent = parent;
		this.config = parent.config;
		this.listeners = parent.listeners;
	}
	
	@Override
	public PixelDeviceRegistry parent() {
		return this.parent;
	}
	
	protected void pixelDeviceChanged() {
		for (PixelDeviceListener listener : listeners) {
			listener.pixelDeviceChanged(this);
		}
	}
}
