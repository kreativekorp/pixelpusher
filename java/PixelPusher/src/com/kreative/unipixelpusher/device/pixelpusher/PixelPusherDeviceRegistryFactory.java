package com.kreative.unipixelpusher.device.pixelpusher;

import com.kreative.unipixelpusher.DeviceConfiguration;
import com.kreative.unipixelpusher.PixelDeviceRegistry;
import com.kreative.unipixelpusher.PixelDeviceRegistryFactory;

public class PixelPusherDeviceRegistryFactory implements PixelDeviceRegistryFactory {
	@Override
	public PixelDeviceRegistry createInstance(DeviceConfiguration config) {
		return new PixelPusherDeviceRegistry(config);
	}
}
