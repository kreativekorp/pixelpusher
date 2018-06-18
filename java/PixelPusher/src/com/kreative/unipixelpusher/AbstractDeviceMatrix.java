package com.kreative.unipixelpusher;

import java.util.List;

public abstract class AbstractDeviceMatrix extends AbstractPixelMatrix implements DeviceString {
	protected final PixelDevice parent;
	protected final DeviceConfiguration config;
	protected final List<PixelDeviceListener> listeners;
	protected String name;
	protected GammaCurve gamma;
	
	protected AbstractDeviceMatrix(PixelDevice parent, DeviceConfiguration config, List<PixelDeviceListener> listeners) {
		this.parent = parent;
		this.config = config;
		this.listeners = listeners;
	}
	
	protected AbstractDeviceMatrix(AbstractPixelDevice parent) {
		this.parent = parent;
		this.config = parent.config;
		this.listeners = parent.listeners;
	}
	
	protected void loadConfig(String id) {
		this.windingOrder = config.get(id, "windingOrder", WindingOrder.class, WindingOrder.LTR_TTB);
		this.flipHorizontal = config.get(id, "flipHorizontal", false);
		this.flipVertical = config.get(id, "flipVertical", false);
		this.name = config.get(id, "name");
		this.gamma = config.get(id, "gamma", GammaCurve.LINEAR);
	}
	
	@Override
	public PixelDevice parent() {
		return this.parent;
	}
	
	@Override
	public void setWindingOrder(WindingOrder windingOrder) {
		super.setWindingOrder(windingOrder);
		config.put(id(), "windingOrder", windingOrder);
		pixelDeviceChanged();
	}
	
	@Override
	public void setFlipHorizontal(boolean flipHorizontal) {
		super.setFlipHorizontal(flipHorizontal);
		config.put(id(), "flipHorizontal", flipHorizontal);
		pixelDeviceChanged();
	}
	
	@Override
	public void setFlipVertical(boolean flipVertical) {
		super.setFlipVertical(flipVertical);
		config.put(id(), "flipVertical", flipVertical);
		pixelDeviceChanged();
	}
	
	public void setName(String name) {
		this.name = name;
		config.put(id(), "name", name);
		pixelDeviceChanged();
	}
	
	@Override
	public GammaCurve getGammaCurve() {
		return this.gamma;
	}
	
	@Override
	public void setGammaCurve(GammaCurve gamma) {
		this.gamma = gamma;
		config.put(id(), "gamma", gamma);
		pixelDeviceChanged();
	}
	
	protected int correct(int color) {
		return (gamma == null) ? color : gamma.correct(color);
	}
	
	protected void pixelDeviceChanged() {
		for (PixelDeviceListener listener : listeners) {
			listener.pixelDeviceChanged(this.parent);
		}
	}
}
