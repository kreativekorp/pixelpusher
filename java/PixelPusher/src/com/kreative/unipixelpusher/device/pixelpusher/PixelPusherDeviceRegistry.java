package com.kreative.unipixelpusher.device.pixelpusher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import com.heroicrobot.dropbit.devices.pixelpusher.PixelPusher;
import com.heroicrobot.dropbit.registry.DeviceRegistry;
import com.kreative.unipixelpusher.AbstractPixelDeviceRegistry;
import com.kreative.unipixelpusher.DeviceConfiguration;
import com.kreative.unipixelpusher.PixelDevice;

public class PixelPusherDeviceRegistry extends AbstractPixelDeviceRegistry {
	private static final PixelPusherDeviceIdentifier[] IDENTIFIERS = {
		PixelPusherDeviceIdentifier.MAC_ADDRESS,
		PixelPusherDeviceIdentifier.IP_ADDRESS,
		PixelPusherDeviceIdentifier.GROUP_CONTROLLER
	};
	
	private final List<PixelPusherDevice> deviceList;
	private final Map<String,PixelPusherDevice> deviceMap;
	private final DeviceRegistry registry;
	private final Observer observer;
	
	public PixelPusherDeviceRegistry(DeviceConfiguration config) {
		super(config);
		this.deviceList = new ArrayList<PixelPusherDevice>();
		this.deviceMap = new HashMap<String,PixelPusherDevice>();
		this.registry = new DeviceRegistry();
		this.registry.startPushing();
		this.registry.addObserver(
			this.observer = new Observer() {
				@Override
				public void update(Observable o1, Object o2) {
					PixelPusherDeviceRegistry.this.update();
				}
			}
		);
	}
	
	@Override
	public void finalize() {
		this.registry.deleteObserver(this.observer);
		this.registry.stopPushing();
	}
	
	@Override
	public synchronized void update() {
		Set<String> toRemove = new HashSet<String>();
		toRemove.addAll(deviceMap.keySet());
		for (PixelPusher pusher : registry.getPushers()) {
			for (PixelPusherDeviceIdentifier identifier : IDENTIFIERS) {
				String id = identifier.getId(pusher);
				if (!toRemove.remove(id)) {
					PixelPusherDevice device = new PixelPusherDevice(this, pusher, identifier);
					deviceList.add(device);
					deviceMap.put(id, device);
					pixelDeviceAppeared(device);
				}
			}
		}
		for (String id : toRemove) {
			PixelPusherDevice device = deviceMap.remove(id);
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
