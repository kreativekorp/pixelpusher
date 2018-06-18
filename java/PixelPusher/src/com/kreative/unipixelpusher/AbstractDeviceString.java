package com.kreative.unipixelpusher;

import java.util.List;

public abstract class AbstractDeviceString extends AbstractPixelString implements DeviceString {
	protected final PixelDevice parent;
	protected final DeviceConfiguration config;
	protected final List<PixelDeviceListener> listeners;
	protected String name;
	protected GammaCurve gamma;
	
	protected AbstractDeviceString(PixelDevice parent, DeviceConfiguration config, List<PixelDeviceListener> listeners) {
		this.parent = parent;
		this.config = config;
		this.listeners = listeners;
	}
	
	protected AbstractDeviceString(AbstractPixelDevice parent) {
		this.parent = parent;
		this.config = parent.config;
		this.listeners = parent.listeners;
	}
	
	protected void loadConfig(String id) {
		this.rowCount = config.get(id, "rowCount", 0);
		this.columnCount = config.get(id, "columnCount", 0);
		this.windingOrder = config.get(id, "windingOrder", WindingOrder.class, WindingOrder.LTR_TTB);
		this.reversed = config.get(id, "reversed", false);
		this.name = config.get(id, "name");
		this.gamma = config.get(id, "gamma", GammaCurve.LINEAR);
	}
	
	@Override
	public PixelDevice parent() {
		return this.parent;
	}
	
	@Override
	public void setRowCount(int rowCount) {
		super.setRowCount(rowCount);
		config.put(id(), "rowCount", rowCount);
		pixelDeviceChanged();
	}
	
	@Override
	public void setColumnCount(int columnCount) {
		super.setColumnCount(columnCount);
		config.put(id(), "columnCount", columnCount);
		pixelDeviceChanged();
	}
	
	@Override
	public void setWindingOrder(WindingOrder windingOrder) {
		super.setWindingOrder(windingOrder);
		config.put(id(), "windingOrder", windingOrder);
		pixelDeviceChanged();
	}
	
	@Override
	public void setReversed(boolean reversed) {
		super.setReversed(reversed);
		config.put(id(), "reversed", reversed);
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
