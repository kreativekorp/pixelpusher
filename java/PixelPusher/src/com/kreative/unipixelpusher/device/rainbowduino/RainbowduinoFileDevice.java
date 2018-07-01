package com.kreative.unipixelpusher.device.rainbowduino;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import com.kreative.unipixelpusher.DeviceString;

public class RainbowduinoFileDevice extends RainbowduinoDevice {
	private File file;
	private RainbowduinoMatrix string;
	
	public RainbowduinoFileDevice(RainbowduinoDeviceRegistry parent, File file) {
		super(parent);
		this.file = file;
		this.string = null;
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
	protected void loadConfig(String id) {
		super.loadConfig(id);
		if (string != null) string.loadConfig(id);
	}
	
	@Override
	public DeviceString getString(int i) {
		if (this.string != null) {
			return this.string;
		} else try {
			OutputStream out = new FileOutputStream(file);
			this.string = new RainbowduinoMatrix(this, out);
			return this.string;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
