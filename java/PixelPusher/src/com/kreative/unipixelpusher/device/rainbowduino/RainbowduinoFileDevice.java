package com.kreative.unipixelpusher.device.rainbowduino;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import com.kreative.unipixelpusher.DeviceString;

public class RainbowduinoFileDevice extends RainbowduinoDevice {
	private File file;
	
	public RainbowduinoFileDevice(RainbowduinoDeviceRegistry parent, File file) {
		super(parent);
		this.file = file;
		loadConfig(id());
	}
	
	@Override
	public String id() {
		return "rainbowduino://" + file.getAbsolutePath();
	}
	
	@Override
	public String name() {
		if (this.name != null) return this.name;
		return "Rainbowduino @ " + file.getAbsolutePath();
	}
	
	@Override
	public DeviceString getString(int i) {
		try {
			OutputStream out = new FileOutputStream(file);
			return new RainbowduinoMatrix(this, out);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
