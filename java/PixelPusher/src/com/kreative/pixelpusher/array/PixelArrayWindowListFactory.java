package com.kreative.pixelpusher.array;

import java.awt.Image;
import com.kreative.pixelpusher.pixelset.PixelSetInfoSet;
import com.kreative.pixelpusher.resources.Resources;

public class PixelArrayWindowListFactory extends PixelArrayFactory<PixelArrayWindowList> {
	@Override
	public String getNameForMachines() {
		return "Array-Windows";
	}
	
	@Override
	public String getNameForHumans() {
		return "Array Windows";
	}
	
	@Override
	public Image getIcon(int size) {
		return Resources.getImage("operation", "arraywindow", size);
	}
	
	@Override
	public Class<PixelArrayWindowList> getPixelSetClass() {
		return PixelArrayWindowList.class;
	}
	
	@Override
	public PixelArrayWindowList createPixelSet() {
		return new PixelArrayWindowList();
	}
	
	@Override
	public PixelArrayVisualizer createVisualizer(PixelArrayWindowList array) {
		return new PixelArrayWindowListVisualizer(array);
	}
	
	@Override
	public PixelArrayEditor<PixelArrayWindowList> createEditor(PixelSetInfoSet pixelSets, PixelArrayWindowList array) {
		return new PixelArrayWindowListEditor(pixelSets, array);
	}
}
