package com.kreative.unipixelpusher.device.rainbowduino;

import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collections;
import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import com.kreative.unipixelpusher.DeviceType;
import com.kreative.unipixelpusher.PixelDevice;
import com.kreative.unipixelpusher.PixelString;

public class RainbowduinoSerialDevice implements PixelDevice {
	private String name;
	private CommPortIdentifier portId;
	private RainbowduinoProtocol protocol;
	
	public RainbowduinoSerialDevice(CommPortIdentifier portId) {
		this(portId, RainbowduinoProtocol.RAINBOW_DASHBOARD_2);
	}
	
	public RainbowduinoSerialDevice(CommPortIdentifier portId, RainbowduinoProtocol protocol) {
		this.name = null;
		this.portId = portId;
		this.protocol = protocol;
	}
	
	public RainbowduinoProtocol getProtocol() {
		return this.protocol;
	}
	
	public void setProtocol(RainbowduinoProtocol protocol) {
		this.protocol = protocol;
	}
	
	@Override
	public String id() {
		return "rainbowduino://" + portId.getName();
	}
	
	@Override
	public String name() {
		if (this.name != null) return this.name;
		return "Rainbowduino @ " + portId.getName();
	}
	
	public void setName(String name) {
		this.name = name;
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
	public PixelString getString(int i) {
		try {
			CommPort port = portId.open("UniPixelPusher", 1000);
			OutputStream out = port.getOutputStream();
			return new RainbowduinoMatrix(out, protocol);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public Iterable<? extends PixelString> getStrings() {
		return Collections.unmodifiableCollection(Arrays.asList(getString(0)));
	}
}
