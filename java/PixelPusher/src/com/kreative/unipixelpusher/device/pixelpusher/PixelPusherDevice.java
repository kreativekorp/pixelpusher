package com.kreative.unipixelpusher.device.pixelpusher;

import java.util.Arrays;
import java.util.Collections;
import com.heroicrobot.dropbit.devices.pixelpusher.PixelPusher;
import com.kreative.unipixelpusher.DeviceType;
import com.kreative.unipixelpusher.PixelDevice;
import com.kreative.unipixelpusher.PixelString;

public class PixelPusherDevice implements PixelDevice {
	private String name;
	private PixelPusher pusher;
	private int stringCount;
	private PixelPusherString[] strings;
	
	public PixelPusherDevice(PixelPusher pusher) {
		this.name = null;
		this.pusher = pusher;
		this.stringCount = pusher.getNumberOfStrips();
		this.strings = new PixelPusherString[stringCount];
		for (int i = 0; i < stringCount; i++) {
			this.strings[i] = new PixelPusherString(pusher.getStrip(i));
		}
	}
	
	@Override
	public String id() {
		return "pixelpusher://" + pusher.getMacAddress();
	}
	
	@Override
	public String name() {
		if (this.name != null) return this.name;
		return "PixelPusher @ " + pusher.getMacAddress();
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public DeviceType type() {
		return DeviceType.PIXELPUSHER;
	}
	
	@Override
	public int getStringCount() {
		return stringCount;
	}
	
	@Override
	public PixelString getString(int i) {
		return strings[i];
	}
	
	@Override
	public Iterable<? extends PixelString> getStrings() {
		return Collections.unmodifiableCollection(Arrays.asList(strings));
	}
}
