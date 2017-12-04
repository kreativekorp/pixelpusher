package com.kreative.pixelpusher.device;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import com.heroicrobot.dropbit.devices.pixelpusher.PixelPusher;
import com.heroicrobot.dropbit.devices.pixelpusher.Strip;

public class StripInfoSet {
	private Set<StripInfo> infoSet;
	
	public StripInfoSet() {
		infoSet = new HashSet<StripInfo>();
	}
	
	public StripInfo getStripInfo(PixelPusher pusher, Strip strip) {
		for (StripInfo info : infoSet) {
			if (info.matches(pusher, strip)) {
				return info;
			}
		}
		return null;
	}
	
	public StripInfo getOrCreateStripInfo(PixelPusher pusher, Strip strip) {
		for (StripInfo info : infoSet) {
			if (info.matches(pusher, strip)) {
				return info;
			}
		}
		StripInfo info = StripInfo.forMacAddress(pusher, strip);
		infoSet.add(info);
		return info;
	}
	
	public boolean removeStripInfo(StripInfo info) {
		return infoSet.remove(info);
	}
	
	public boolean removeStripInfo(PixelPusher pusher, Strip strip) {
		for (StripInfo info : infoSet) {
			if (info.matches(pusher, strip)) {
				return infoSet.remove(info);
			}
		}
		return false;
	}
	
	public Set<StripInfo> asSet() {
		return Collections.unmodifiableSet(infoSet);
	}
}
