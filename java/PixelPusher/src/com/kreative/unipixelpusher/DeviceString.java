package com.kreative.unipixelpusher;

public interface DeviceString extends PixelString {
	public PixelDevice parent();
	public String id();
	public String name();
	public StringType type();
	public GammaCurve getGammaCurve();
	public void setGammaCurve(GammaCurve gamma);
}
