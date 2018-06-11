package com.kreative.unipixelpusher;

public interface PixelDevice {
	public String id();
	public String name();
	public DeviceType type();
	public int getStringCount();
	public PixelString getString(int i);
	public Iterable<? extends PixelString> getStrings();
}
