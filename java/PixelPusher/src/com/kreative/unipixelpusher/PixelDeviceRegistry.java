package com.kreative.unipixelpusher;

import java.util.Collection;

public interface PixelDeviceRegistry {
	public void update();
	public int getDeviceCount();
	public PixelDevice getDevice(int i);
	public Collection<? extends PixelDevice> getDevices();
	public void addPixelDeviceListener(PixelDeviceListener listener);
	public void removePixelDeviceListener(PixelDeviceListener listener);
	public PixelDeviceListener[] getPixelDeviceListeners();
}
