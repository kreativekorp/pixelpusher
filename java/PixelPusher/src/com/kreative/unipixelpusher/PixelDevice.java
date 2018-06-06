package com.kreative.unipixelpusher;

public interface PixelDevice {
	public int getStringCount();
	public PixelString getString(int i);
	public DeviceType type();
}
