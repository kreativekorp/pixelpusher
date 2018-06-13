package com.kreative.unipixelpusher.device.pixelpusher;

import com.heroicrobot.dropbit.devices.pixelpusher.Pixel;
import com.heroicrobot.dropbit.devices.pixelpusher.Strip;
import com.kreative.unipixelpusher.AbstractPixelString;
import com.kreative.unipixelpusher.StringType;

public class PixelPusherString extends AbstractPixelString.WithGammaCurve {
	private String id;
	private StringType type;
	private Strip strip;
	private int length;
	private int[] buffer;
	private Pixel[] pixels;
	
	public PixelPusherString(Strip strip) {
		this.id = "pixelpusher://" + strip.getMacAddress() + "/" + strip.getStripNumber();
		this.type = StringType.UNKNOWN;
		this.strip = strip;
		this.length = strip.getLength();
		this.buffer = new int[length];
		this.pixels = new Pixel[length];
		for (int i = 0; i < length; i++) {
			this.pixels[i] = new Pixel();
		}
	}
	
	@Override
	public String id() {
		return this.id;
	}
	
	@Override
	public StringType type() {
		return this.type;
	}
	
	public void setType(StringType type) {
		this.type = type;
	}
	
	@Override
	public int length() {
		return this.length;
	}
	
	public void setLength(int length) {
		this.length = length;
		this.buffer = new int[length];
		this.pixels = new Pixel[length];
		for (int i = 0; i < length; i++) {
			this.pixels[i] = new Pixel();
		}
	}
	
	@Override
	protected int getPixelImpl(int i) {
		return (i >= 0 && i < length) ? buffer[i] : 0;
	}
	
	@Override
	protected void setPixelImpl(int i, int color) {
		if (i >= 0 && i < length) buffer[i] = color;
	}
	
	@Override
	public void push() {
		for (int i = 0; i < length; i++) {
			int c = correct(buffer[i]);
			int a = (c >> 24) & 0xFF;
			pixels[i].red   = (byte)(((c >> 16) & 0xFF) * a / 255);
			pixels[i].green = (byte)(((c >>  8) & 0xFF) * a / 255);
			pixels[i].blue  = (byte)(((c >>  0) & 0xFF) * a / 255);
		}
		strip.setPixels(pixels);
	}
}
