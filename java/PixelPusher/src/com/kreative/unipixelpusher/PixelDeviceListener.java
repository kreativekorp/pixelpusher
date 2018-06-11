package com.kreative.unipixelpusher;

public interface PixelDeviceListener {
	public void pixelDeviceAppeared(PixelDevice dev);
	public void pixelDeviceChanged(PixelDevice dev);
	public void pixelDeviceDisappeared(PixelDevice dev);
}
