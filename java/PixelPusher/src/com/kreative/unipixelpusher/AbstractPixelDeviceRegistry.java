package com.kreative.unipixelpusher;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractPixelDeviceRegistry implements PixelDeviceRegistry {
	protected final DeviceConfiguration config;
	protected final List<PixelDeviceListener> listeners;
	
	protected AbstractPixelDeviceRegistry(DeviceConfiguration config) {
		this.config = config;
		this.listeners = new ArrayList<PixelDeviceListener>();
	}
	
	@Override
	public void addPixelDeviceListener(PixelDeviceListener listener) {
		listeners.add(listener);
	}
	
	@Override
	public void removePixelDeviceListener(PixelDeviceListener listener) {
		listeners.remove(listener);
	}
	
	@Override
	public PixelDeviceListener[] getPixelDeviceListeners() {
		return listeners.toArray(new PixelDeviceListener[listeners.size()]);
	}
	
	protected void pixelDeviceAppeared(PixelDevice dev) {
		for (PixelDeviceListener listener : listeners) {
			listener.pixelDeviceAppeared(dev);
		}
	}
	
	protected void pixelDeviceChanged(PixelDevice dev) {
		for (PixelDeviceListener listener : listeners) {
			listener.pixelDeviceChanged(dev);
		}
	}
	
	protected void pixelDeviceDisappeared(PixelDevice dev) {
		for (PixelDeviceListener listener : listeners) {
			listener.pixelDeviceDisappeared(dev);
		}
	}
}
