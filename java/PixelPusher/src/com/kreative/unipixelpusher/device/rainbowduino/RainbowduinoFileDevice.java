package com.kreative.unipixelpusher.device.rainbowduino;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collections;
import com.kreative.unipixelpusher.DeviceType;
import com.kreative.unipixelpusher.PixelDevice;
import com.kreative.unipixelpusher.PixelString;

public class RainbowduinoFileDevice implements PixelDevice {
	private String name;
	private File file;
	private RainbowduinoProtocol protocol;
	
	public RainbowduinoFileDevice(File file) {
		this(file, RainbowduinoProtocol.RAINBOW_DASHBOARD_2);
	}
	
	public RainbowduinoFileDevice(File file, RainbowduinoProtocol protocol) {
		this.name = null;
		this.file = file;
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
		return "rainbowduino://" + file.getAbsolutePath();
	}
	
	@Override
	public String name() {
		if (this.name != null) return this.name;
		return "Rainbowduino @ " + file.getAbsolutePath();
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
			OutputStream out = new FileOutputStream(file);
			return new RainbowduinoMatrix(out, protocol);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public Iterable<? extends PixelString> getStrings() {
		return Collections.unmodifiableCollection(Arrays.asList(getString(0)));
	}
}
