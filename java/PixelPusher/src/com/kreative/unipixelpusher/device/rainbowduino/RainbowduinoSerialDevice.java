package com.kreative.unipixelpusher.device.rainbowduino;

import java.io.OutputStream;
import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import com.kreative.unipixelpusher.DeviceString;

public class RainbowduinoSerialDevice extends RainbowduinoDevice {
	private CommPortIdentifier portId;
	private RainbowduinoMatrix string;
	
	public RainbowduinoSerialDevice(RainbowduinoDeviceRegistry parent, CommPortIdentifier portId) {
		super(parent);
		this.portId = portId;
		this.string = null;
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
	protected void loadConfig(String id) {
		super.loadConfig(id);
		if (string != null) string.loadConfig(id);
	}
	
	@Override
	public DeviceString getString(int i) {
		if (this.string != null) {
			return this.string;
		} else try {
			CommPort port = portId.open("UniPixelPusher", 1000);
			OutputStream out = port.getOutputStream();
			this.string = new RainbowduinoMatrix(this, out);
			return this.string;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
