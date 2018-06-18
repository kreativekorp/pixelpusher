package com.kreative.unipixelpusher.device.rainbowduino;

import com.kreative.unipixelpusher.DeviceConfiguration;
import com.kreative.unipixelpusher.PixelDeviceRegistry;
import com.kreative.unipixelpusher.PixelDeviceRegistryFactory;

public class RainbowduinoDeviceRegistryFactory implements PixelDeviceRegistryFactory {
	@Override
	public PixelDeviceRegistry createInstance(DeviceConfiguration config) {
		return new RainbowduinoDeviceRegistry(config);
	}
}
