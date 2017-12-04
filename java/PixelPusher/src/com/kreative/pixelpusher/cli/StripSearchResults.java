package com.kreative.pixelpusher.cli;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.heroicrobot.dropbit.devices.pixelpusher.PixelPusher;
import com.heroicrobot.dropbit.devices.pixelpusher.Strip;
import com.kreative.pixelpusher.device.PixelPusherController;
import com.kreative.pixelpusher.device.StripInfo;

public class StripSearchResults {
	private List<PixelPusher> pushers;
	private Map<PixelPusher, List<Strip>> strips;
	private Map<Strip, StripInfo> infos;
	private List<StripInfo> disconnectedInfos;
	
	private StripSearchResults() {
		pushers = new ArrayList<PixelPusher>();
		strips = new HashMap<PixelPusher, List<Strip>>();
		infos = new HashMap<Strip, StripInfo>();
		disconnectedInfos = new ArrayList<StripInfo>();
	}
	
	private void add(PixelPusher pusher, Strip strip, StripInfo info) {
		if (!pushers.contains(pusher)) pushers.add(pusher);
		if (!strips.containsKey(pusher)) strips.put(pusher, new ArrayList<Strip>());
		if (!strips.get(pusher).contains(strip)) strips.get(pusher).add(strip);
		if (info != null) infos.put(strip, info);
	}
	
	private void add(StripInfo info) {
		if (!disconnectedInfos.contains(info)) disconnectedInfos.add(info);
	}
	
	public List<PixelPusher> getPushers() {
		return pushers;
	}
	
	public List<Strip> getStrips(PixelPusher pusher) {
		return strips.get(pusher);
	}
	
	public StripInfo getStripInfo(Strip strip) {
		return infos.get(strip);
	}
	
	public List<StripInfo> getDisconnectedStripInfos() {
		return disconnectedInfos;
	}
	
	public static StripSearchResults forPattern(PixelPusherController ctrl, StringSearchPattern dsp, IntegerSearchPattern ssp) {
		StripSearchResults results = new StripSearchResults();
		
		Set<StripInfo> disconnectedInfos = new HashSet<StripInfo>();
		disconnectedInfos.addAll(ctrl.getStripInfoSet().asSet());
		for (PixelPusher pusher : ctrl.getDeviceRegistry().getPushers()) {
			for (Strip strip : pusher.getStrips()) {
				StripInfo info = ctrl.getStripInfoSet().getStripInfo(pusher, strip);
				if (info != null) disconnectedInfos.remove(info);
				
				boolean matchesStrip = (dsp == null || dsp.matches(pusher)) && (ssp == null || ssp.matches(strip));
				boolean matchesInfo = (info != null) && (dsp == null || dsp.matches(info)) && (ssp == null || ssp.matches(info));
				if (matchesStrip || matchesInfo) results.add(pusher, strip, info);
			}
		}
		for (StripInfo info : disconnectedInfos) {
			boolean matchesInfo = (dsp == null || dsp.matches(info)) && (ssp == null || ssp.matches(info));
			if (matchesInfo) results.add(info);
		}
		
		return results;
	}
}
