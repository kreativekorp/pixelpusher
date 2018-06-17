package com.kreative.unipixelpusher.device.rainbowduino;

import gnu.io.CommPortIdentifier;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.kreative.unipixelpusher.AbstractPixelDeviceRegistry;
import com.kreative.unipixelpusher.DeviceConfiguration;
import com.kreative.unipixelpusher.PixelDevice;

public class RainbowduinoDeviceRegistry extends AbstractPixelDeviceRegistry {
	private final List<PixelDevice> deviceList;
	private final Map<String,PixelDevice> deviceMap;
	private final File pipe;
	
	public RainbowduinoDeviceRegistry(DeviceConfiguration config) {
		super(config);
		this.deviceList = new ArrayList<PixelDevice>();
		this.deviceMap = new HashMap<String,PixelDevice>();
		File pipe;
		try {
			String tmp = System.getenv("RAINBOWD_PIPE");
			if (tmp != null && tmp.length() > 0) pipe = new File(tmp);
			else pipe = new File("/tmp/rainbowduino");
		} catch (Exception e) {
			pipe = new File("/tmp/rainbowduino");
		}
		this.pipe = pipe;
		update();
	}
	
	@Override
	public synchronized void update() {
		Set<String> toRemove = new HashSet<String>();
		toRemove.addAll(deviceMap.keySet());
		if (pipe.exists()) {
			if (!toRemove.remove(pipe.getAbsolutePath())) {
				RainbowduinoFileDevice device = new RainbowduinoFileDevice(this, pipe);
				deviceList.add(device);
				deviceMap.put(pipe.getAbsolutePath(), device);
				pixelDeviceAppeared(device);
			}
		}
		Enumeration<?> en = CommPortIdentifier.getPortIdentifiers();
		while (en.hasMoreElements()) {
			CommPortIdentifier portId = (CommPortIdentifier)en.nextElement();
			if (!toRemove.remove(portId.getName())) {
				RainbowduinoSerialDevice device = new RainbowduinoSerialDevice(this, portId);
				deviceList.add(device);
				deviceMap.put(portId.getName(), device);
				pixelDeviceAppeared(device);
			}
		}
		for (String id : toRemove) {
			PixelDevice device = deviceMap.remove(id);
			deviceList.remove(device);
			pixelDeviceDisappeared(device);
		}
	}
	
	@Override
	public synchronized int getDeviceCount() {
		return deviceList.size();
	}
	
	@Override
	public synchronized PixelDevice getDevice(int i) {
		return deviceList.get(i);
	}
	
	@Override
	public synchronized Collection<? extends PixelDevice> getDevices() {
		return Collections.unmodifiableCollection(deviceList);
	}
}
