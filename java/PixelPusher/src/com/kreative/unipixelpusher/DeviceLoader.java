package com.kreative.unipixelpusher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

public class DeviceLoader {
	private final Set<PixelDeviceRegistry> registries;
	private final Map<PixelDevice,String> deviceNameMap;
	private final SortedMap<String,PixelDevice> nameDeviceMap;
	private final List<PixelDeviceListener> listeners;
	private final MyListener myListener;
	
	public DeviceLoader(DeviceConfiguration config) {
		this.registries = Collections.synchronizedSet(new HashSet<PixelDeviceRegistry>());
		this.deviceNameMap = Collections.synchronizedMap(new HashMap<PixelDevice,String>());
		this.nameDeviceMap = Collections.synchronizedSortedMap(new TreeMap<String,PixelDevice>());
		this.listeners = Collections.synchronizedList(new ArrayList<PixelDeviceListener>());
		this.myListener = new MyListener();
		
		for (PixelDeviceRegistryFactory f : ServiceLoader.load(PixelDeviceRegistryFactory.class)) {
			PixelDeviceRegistry r = f.createInstance(config);
			registries.add(r);
			for (PixelDevice d : r.getDevices()) {
				deviceNameMap.put(d, d.name());
				nameDeviceMap.put(d.name(), d);
			}
			r.addPixelDeviceListener(myListener);
			r.update();
		}
	}
	
	public Collection<PixelDevice> getDevices() {
		return Collections.unmodifiableCollection(deviceNameMap.keySet());
	}
	
	public Collection<String> getDeviceNames() {
		return Collections.unmodifiableCollection(nameDeviceMap.keySet());
	}
	
	public PixelDevice getDevice(String name) {
		return nameDeviceMap.get(name);
	}
	
	public void addPixelDeviceListener(PixelDeviceListener listener) {
		listeners.add(listener);
	}
	
	public void removePixelDeviceListener(PixelDeviceListener listener) {
		listeners.remove(listener);
	}
	
	public PixelDeviceListener[] getPixelDeviceListeners() {
		return listeners.toArray(new PixelDeviceListener[listeners.size()]);
	}
	
	private class MyListener implements PixelDeviceListener {
		@Override
		public synchronized void pixelDeviceAppeared(PixelDevice dev) {
			deviceNameMap.put(dev, dev.name());
			nameDeviceMap.put(dev.name(), dev);
			for (PixelDeviceListener listener : listeners) {
				listener.pixelDeviceAppeared(dev);
			}
		}
		@Override
		public synchronized void pixelDeviceChanged(PixelDevice dev) {
			String name = deviceNameMap.remove(dev);
			if (name != null) nameDeviceMap.remove(name);
			deviceNameMap.put(dev, dev.name());
			nameDeviceMap.put(dev.name(), dev);
			for (PixelDeviceListener listener : listeners) {
				listener.pixelDeviceChanged(dev);
			}
		}
		@Override
		public synchronized void pixelDeviceDisappeared(PixelDevice dev) {
			String name = deviceNameMap.remove(dev);
			if (name != null) nameDeviceMap.remove(name);
			for (PixelDeviceListener listener : listeners) {
				listener.pixelDeviceDisappeared(dev);
			}
		}
	}
}
