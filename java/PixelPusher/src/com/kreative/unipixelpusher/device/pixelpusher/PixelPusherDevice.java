package com.kreative.unipixelpusher.device.pixelpusher;

import java.util.Arrays;
import java.util.Collections;
import com.heroicrobot.dropbit.devices.pixelpusher.PixelPusher;
import com.kreative.unipixelpusher.AbstractPixelDevice;
import com.kreative.unipixelpusher.DeviceType;

public class PixelPusherDevice extends AbstractPixelDevice {
	private PixelPusher pusher;
	private PixelPusherDeviceIdentifier identifier;
	private String name;
	private int stringCount;
	private PixelPusherString[] strings;
	
	public PixelPusherDevice(PixelPusherDeviceRegistry parent, PixelPusher pusher, PixelPusherDeviceIdentifier identifier) {
		super(parent);
		this.pusher = pusher;
		this.identifier = identifier;
		this.name = config.get(id(), "name");
		this.stringCount = pusher.getNumberOfStrips();
		this.strings = new PixelPusherString[stringCount];
		for (int i = 0; i < stringCount; i++) {
			this.strings[i] = new PixelPusherString(this, pusher.getStrip(i));
		}
	}
	
	protected PixelPusher pusher() {
		return pusher;
	}
	
	@Override
	public String id() {
		return "pixelpusher://" + identifier.getId(pusher);
	}
	
	@Override
	public String name() {
		if (this.name != null) return this.name;
		return "PixelPusher @ " + identifier.getName(pusher);
	}
	
	public void setName(String name) {
		this.name = name;
		config.put(id(), "name", name);
		pixelDeviceChanged();
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
	public PixelPusherString getString(int i) {
		return strings[i];
	}
	
	@Override
	public Iterable<PixelPusherString> getStrings() {
		return Collections.unmodifiableCollection(Arrays.asList(strings));
	}
}
