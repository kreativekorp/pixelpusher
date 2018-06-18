package com.kreative.unipixelpusher;

public interface PixelDevice {
	public PixelDeviceRegistry parent();
	public String id();
	public String name();
	public DeviceType type();
	public int getStringCount();
	public DeviceString getString(int i);
	public Iterable<? extends DeviceString> getStrings();
}
