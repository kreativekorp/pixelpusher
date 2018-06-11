package com.kreative.unipixelpusher;

public interface PixelDeviceRegistry {
	public int getDeviceCount();
	public PixelDevice getDevice(int i);
	public void addPixelDeviceListener(PixelDeviceListener listener);
	public void removePixelDeviceListener(PixelDeviceListener listener);
	public PixelDeviceListener[] getPixelDeviceListeners();
}
