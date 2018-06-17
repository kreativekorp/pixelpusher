package com.kreative.unipixelpusher;

public interface PixelDevice {
	public PixelDeviceRegistry parent();
	public String id();
	public String name();
	public DeviceType type();
	public int getStringCount();
	public PixelString getString(int i);
	public Iterable<? extends PixelString> getStrings();
}
