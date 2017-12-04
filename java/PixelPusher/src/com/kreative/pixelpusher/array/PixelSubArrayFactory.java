package com.kreative.pixelpusher.array;

import java.awt.Image;
import com.kreative.pixelpusher.pixelset.PixelSetInfoSet;
import com.kreative.pixelpusher.resources.Resources;

public class PixelSubArrayFactory extends PixelArrayFactory<PixelSubArray> {
	@Override
	public String getNameForMachines() {
		return "Subarray";
	}
	
	@Override
	public String getNameForHumans() {
		return "Subarray";
	}
	
	@Override
	public Image getIcon(int size) {
		return Resources.getImage("operation", "subarray", size);
	}
	
	@Override
	public Class<PixelSubArray> getPixelSetClass() {
		return PixelSubArray.class;
	}
	
	@Override
	public PixelSubArray createPixelSet() {
		return new PixelSubArray();
	}
	
	@Override
	public PixelArrayEditor<PixelSubArray> createEditor(PixelSetInfoSet pixelSets, PixelSubArray array) {
		return new PixelSubArrayEditor(pixelSets, array);
	}
}
