package com.kreative.unipixelpusher.device.rainbowduino;

import java.io.OutputStream;
import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import com.kreative.unipixelpusher.PixelString;

public class RainbowduinoSerialDevice extends RainbowduinoDevice {
	private CommPortIdentifier portId;
	
	public RainbowduinoSerialDevice(RainbowduinoDeviceRegistry parent, CommPortIdentifier portId) {
		super(parent);
		this.portId = portId;
		duper();
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
	public PixelString getString(int i) {
		try {
			CommPort port = portId.open("UniPixelPusher", 1000);
			OutputStream out = port.getOutputStream();
			return new RainbowduinoMatrix(id(), out, protocol);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
