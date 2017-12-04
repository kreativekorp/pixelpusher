package com.kreative.pixelpusher.pixelset;

import javax.swing.JComboBox;

public class PixelSetPopupMenu<T extends PixelSet> extends JComboBox {
	private static final long serialVersionUID = 1L;
	
	private PixelSetInfoSet infoSet;
	
	public PixelSetPopupMenu(PixelSetInfoSet infoSet, Class<T> type) {
		super(infoSet.getPixelSetInfoList(type).toArray());
		this.infoSet = infoSet;
		this.setEditable(false);
		this.setSelectedItem(null);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public T getSelectedPixelSet() {
		Object o = this.getSelectedItem();
		if (o instanceof PixelSetInfo) {
			return (T)((PixelSetInfo)o).getPixelSet();
		} else {
			return null;
		}
	}
	
	public void setSelectedPixelSet(T pixelSet) {
		if (pixelSet == null) {
			this.setSelectedItem(null);
		} else {
			this.setSelectedItem(infoSet.getPixelSetInfo(pixelSet));
		}
	}
}
