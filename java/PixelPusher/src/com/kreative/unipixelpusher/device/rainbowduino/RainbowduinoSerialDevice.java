package com.kreative.unipixelpusher.device.rainbowduino;

import java.io.OutputStream;
import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import com.kreative.unipixelpusher.DeviceString;

public class RainbowduinoSerialDevice extends RainbowduinoDevice {
	private CommPortIdentifier portId;
	
	public RainbowduinoSerialDevice(RainbowduinoDeviceRegistry parent, CommPortIdentifier portId) {
		super(parent);
		this.portId = portId;
		loadConfig(id());
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
	
	@Override
	public DeviceString getString(int i) {
		try {
			CommPort port = portId.open("UniPixelPusher", 1000);
			OutputStream out = port.getOutputStream();
			return new RainbowduinoMatrix(this, out);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
