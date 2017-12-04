package com.kreative.pixelpusher.pixelset;

import java.awt.Image;

public class PixelSetInfo<T extends PixelSet> implements Comparable<PixelSetInfo<? extends PixelSet>> {
	private Class<T> type;
	private PixelSetFactory<T> factory;
	private T pixelSet;
	private String name;
	
	public PixelSetInfo(Class<T> type, PixelSetFactory<T> factory, T pixelSet) {
		this.type = type;
		this.factory = factory;
		this.pixelSet = pixelSet;
		this.name = null;
	}
	
	public PixelSetInfo(Class<T> type, PixelSetFactory<T> factory, T pixelSet, String name) {
		this.type = type;
		this.factory = factory;
		this.pixelSet = pixelSet;
		this.name = name;
	}
	
	public synchronized Class<T> getPixelSetClass() {
		return this.type;
	}
	
	public synchronized PixelSetFactory<T> getPixelSetFactory() {
		return this.factory;
	}
	
	public synchronized T getPixelSet() {
		return this.pixelSet;
	}
	
	public synchronized boolean hasName() {
		return this.name != null;
	}
	
	public synchronized String getName() {
		if (this.name != null) {
			return this.name;
		} else {
			return this.factory.getNameForHumans();
		}
	}
	
	public synchronized void setName(String name) {
		this.name = name;
	}
	
	public synchronized Image getIcon(int size) {
		return this.factory.getIcon(size);
	}
	
	public synchronized PixelSetEditor<T> createEditor(PixelSetInfoSet pixelSets) {
		return this.factory.createEditor(pixelSets, this.pixelSet);
	}
	
	public synchronized PixelSetVisualEditor<T> createVisualEditor(PixelSetInfoSet pixelSets) {
		return this.factory.createVisualEditor(pixelSets, this.pixelSet);
	}
	
	public synchronized PixelSetEditorFrame createEditorFrame(PixelSetInfoSet pixelSets) {
		return this.factory.createEditorFrame(pixelSets, this.pixelSet);
	}
	
	public synchronized PixelSetVisualEditorFrame createVisualEditorFrame(PixelSetInfoSet pixelSets) {
		return this.factory.createVisualEditorFrame(pixelSets, this.pixelSet);
	}
	
	@Override
	public int compareTo(PixelSetInfo<? extends PixelSet> other) {
		return this.getName().compareToIgnoreCase(other.getName());
	}
	
	@Override
	public String toString() {
		return this.getName();
	}
}
