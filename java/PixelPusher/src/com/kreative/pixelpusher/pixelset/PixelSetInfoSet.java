package com.kreative.pixelpusher.pixelset;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PixelSetInfoSet {
	private Set<PixelSetInfo<? extends PixelSet>> infoSet;
	
	public PixelSetInfoSet() {
		infoSet = new HashSet<PixelSetInfo<? extends PixelSet>>();
	}
	
	public <T extends PixelSet> PixelSetInfo<T> createPixelSetInfo(PixelSetFactory<T> factory) {
		PixelSetInfo<T> info = new PixelSetInfo<T>(factory.getPixelSetClass(), factory, factory.createPixelSet());
		infoSet.add(info);
		return info;
	}
	
	public <T extends PixelSet> PixelSetInfo<T> createPixelSetInfo(PixelSetFactory<T> factory, String name) {
		PixelSetInfo<T> info = new PixelSetInfo<T>(factory.getPixelSetClass(), factory, factory.createPixelSet(), name);
		infoSet.add(info);
		return info;
	}
	
	public PixelSetInfo<? extends PixelSet> getPixelSetInfo(PixelSet pixelSet) {
		for (PixelSetInfo<? extends PixelSet> info : infoSet) {
			if (info.getPixelSet().equals(pixelSet)) {
				return info;
			}
		}
		return null;
	}
	
	public <T extends PixelSet> PixelSetInfo<? extends PixelSet> getOrCreatePixelSetInfo(PixelSetFactory<T> factory, T pixelSet) {
		for (PixelSetInfo<? extends PixelSet> info : infoSet) {
			if (info.getPixelSet().equals(pixelSet)) {
				return info;
			}
		}
		PixelSetInfo<T> info = new PixelSetInfo<T>(factory.getPixelSetClass(), factory, pixelSet);
		infoSet.add(info);
		return info;
	}
	
	public List<PixelSetInfo<? extends PixelSet>> getPixelSetInfoList() {
		return getPixelSetInfoList(PixelSet.class);
	}
	
	public <T extends PixelSet> List<PixelSetInfo<? extends T>> getPixelSetInfoList(Class<T> type) {
		List<PixelSetInfo<? extends T>> list = new ArrayList<PixelSetInfo<? extends T>>();
		for (PixelSetInfo<? extends PixelSet> info : infoSet) {
			if (type.isAssignableFrom(info.getPixelSetClass())) {
				@SuppressWarnings("unchecked")
				PixelSetInfo<? extends T> infoT = (PixelSetInfo<? extends T>)info;
				list.add(infoT);
			}
		}
		Collections.sort(list);
		return list;
	}
	
	public boolean removePixelSetInfo(PixelSetInfo<? extends PixelSet> info) {
		return infoSet.remove(info);
	}
}
