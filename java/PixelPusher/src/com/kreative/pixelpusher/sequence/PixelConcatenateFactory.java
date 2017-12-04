package com.kreative.pixelpusher.sequence;

import java.awt.Image;
import com.kreative.pixelpusher.pixelset.PixelSetInfoSet;
import com.kreative.pixelpusher.resources.Resources;

public class PixelConcatenateFactory extends PixelSequenceFactory<PixelConcatenate> {
	@Override
	public String getNameForMachines() {
		return "Concatenate";
	}
	
	@Override
	public String getNameForHumans() {
		return "Concatenate";
	}
	
	@Override
	public Image getIcon(int size) {
		return Resources.getImage("operation", "concatenate", size);
	}
	
	@Override
	public Class<PixelConcatenate> getPixelSetClass() {
		return PixelConcatenate.class;
	}
	
	@Override
	public PixelConcatenate createPixelSet() {
		return new PixelConcatenate();
	}
	
	@Override
	public PixelSequenceEditor<PixelConcatenate> createEditor(PixelSetInfoSet pixelSets, PixelConcatenate sequence) {
		return new PixelConcatenateEditor(pixelSets, sequence);
	}
}
