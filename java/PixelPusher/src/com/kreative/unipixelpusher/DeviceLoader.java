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
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

public class DeviceLoader {
	private final Set<PixelDeviceRegistry> registries;
	private final SortedSet<DeviceInfo> deviceSet;
	private final SortedMap<String,DeviceInfo> idMap;
	private final SortedMap<String,DeviceInfo> nameMap;
	private final Map<PixelDevice,DeviceInfo> deviceMap;
	private final List<PixelDeviceListener> listeners;
	private final MyListener myListener;
	
	public DeviceLoader(DeviceConfiguration config) {
		this.registries = Collections.synchronizedSet(new HashSet<PixelDeviceRegistry>());
		this.deviceSet = Collections.synchronizedSortedSet(new TreeSet<DeviceInfo>());
		this.idMap = Collections.synchronizedSortedMap(new TreeMap<String,DeviceInfo>());
		this.nameMap = Collections.synchronizedSortedMap(new TreeMap<String,DeviceInfo>());
		this.deviceMap = Collections.synchronizedMap(new HashMap<PixelDevice,DeviceInfo>());
		this.listeners = Collections.synchronizedList(new ArrayList<PixelDeviceListener>());
		this.myListener = new MyListener();
		
		for (PixelDeviceRegistryFactory f : ServiceLoader.load(PixelDeviceRegistryFactory.class)) {
			PixelDeviceRegistry r = f.createInstance(config);
			registries.add(r);
			for (PixelDevice d : r.getDevices()) {
				DeviceInfo info = new DeviceInfo(d);
				add(info);
			}
			r.addPixelDeviceListener(myListener);
			r.update();
		}
	}
	
	public Collection<DeviceInfo> getDevices() {
		return Collections.unmodifiableCollection(deviceSet);
	}
	
	public Collection<String> getDeviceIds() {
		return Collections.unmodifiableCollection(idMap.keySet());
	}
	
	public Collection<String> getDeviceNames() {
		return Collections.unmodifiableCollection(nameMap.keySet());
	}
	
	public DeviceInfo getDeviceById(String id) {
		return idMap.get(id);
	}
	
	public DeviceInfo getDeviceByName(String name) {
		return nameMap.get(name);
	}
	
	public void update() {
		for (PixelDeviceRegistry r : registries) {
			r.update();
		}
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
	
	private synchronized void add(DeviceInfo info) {
		deviceSet.add(info);
		idMap.put(info.id, info);
		nameMap.put(info.name, info);
		deviceMap.put(info.device, info);
	}
	
	private synchronized void remove(DeviceInfo info) {
		deviceSet.remove(info);
		idMap.remove(info.id);
		nameMap.remove(info.name);
		deviceMap.remove(info.device);
	}
	
	private class MyListener implements PixelDeviceListener {
		@Override
		public synchronized void pixelDeviceAppeared(PixelDevice dev) {
			DeviceInfo info = deviceMap.get(dev);
			if (info != null) { remove(info); info.update(dev); }
			else { info = new DeviceInfo(dev); }
			add(info);
			for (PixelDeviceListener listener : listeners) {
				listener.pixelDeviceAppeared(dev);
			}
		}
		@Override
		public synchronized void pixelDeviceChanged(PixelDevice dev) {
			DeviceInfo info = deviceMap.get(dev);
			if (info != null) { remove(info); info.update(dev); }
			else { info = new DeviceInfo(dev); }
			add(info);
			for (PixelDeviceListener listener : listeners) {
				listener.pixelDeviceChanged(dev);
			}
		}
		@Override
		public synchronized void pixelDeviceDisappeared(PixelDevice dev) {
			DeviceInfo info = deviceMap.get(dev);
			if (info != null) { remove(info); }
			for (PixelDeviceListener listener : listeners) {
				listener.pixelDeviceDisappeared(dev);
			}
		}
	}
	
	public static final class DeviceInfo implements Comparable<DeviceInfo> {
		private String id;
		private String name;
		private PixelDevice device;
		
		private DeviceInfo(PixelDevice dev) {
			this.id = dev.id();
			this.name = dev.name();
			this.device = dev;
		}
		
		private void update(PixelDevice dev) {
			this.id = dev.id();
			this.name = dev.name();
			this.device = dev;
		}
		
		public String id() { return id; }
		public String name() { return name; }
		public PixelDevice device() { return device; }
		
		@Override
		public int compareTo(DeviceInfo that) {
			return this.name.compareToIgnoreCase(that.name);
		}
		
		@Override
		public String toString() {
			return this.name;
		}
	}
}
