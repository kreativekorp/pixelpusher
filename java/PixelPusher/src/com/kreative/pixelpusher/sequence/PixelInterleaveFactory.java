package com.kreative.pixelpusher.sequence;

import java.awt.Image;
import com.kreative.pixelpusher.pixelset.PixelSetInfoSet;
import com.kreative.pixelpusher.resources.Resources;

public class PixelInterleaveFactory extends PixelSequenceFactory<PixelInterleave> {
	@Override
	public String getNameForMachines() {
		return "Interleave";
	}
	
	@Override
	public String getNameForHumans() {
		return "Interleave";
	}
	
	@Override
	public Image getIcon(int size) {
		return Resources.getImage("operation", "interleave", size);
	}
	
	@Override
	public Class<PixelInterleave> getPixelSetClass() {
		return PixelInterleave.class;
	}
	
	@Override
	public PixelInterleave createPixelSet() {
		return new PixelInterleave();
	}
	
	@Override
	public PixelSequenceEditor<PixelInterleave> createEditor(PixelSetInfoSet pixelSets, PixelInterleave sequence) {
		return new PixelInterleaveEditor(pixelSets, sequence);
	}
}
