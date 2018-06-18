package com.kreative.unipixelpusher.device.rainbowduino;

import java.util.Arrays;
import java.util.Collections;
import com.kreative.unipixelpusher.AbstractPixelDevice;
import com.kreative.unipixelpusher.DeviceString;
import com.kreative.unipixelpusher.DeviceType;

public abstract class RainbowduinoDevice extends AbstractPixelDevice {
	protected String name;
	protected RainbowduinoProtocol protocol;
	
	protected RainbowduinoDevice(RainbowduinoDeviceRegistry parent) {
		super(parent);
	}
	
	protected void loadConfig(String id) {
		this.name = config.get(id, "name");
		this.protocol = RainbowduinoProtocol.fromString(
			config.get(id, "protocol"),
			RainbowduinoProtocol.RAINBOW_DASHBOARD_2
		);
	}
	
	public RainbowduinoProtocol getProtocol() {
		return this.protocol;
	}
	
	public void setProtocol(RainbowduinoProtocol protocol) {
		this.protocol = protocol;
		config.put(id(), "protocol", protocol.toString());
		pixelDeviceChanged();
	}
	
	public void setName(String name) {
		this.name = name;
		config.put(id(), "name", name);
		pixelDeviceChanged();
	}
	
	@Override
	public DeviceType type() {
		return DeviceType.MICROCONTROLLER_RAINBOWDUINO;
	}
	
	@Override
	public int getStringCount() {
		return 1;
	}
	
	@Override
	public Iterable<? extends DeviceString> getStrings() {
		return Collections.unmodifiableCollection(Arrays.asList(getString(0)));
	}
}
