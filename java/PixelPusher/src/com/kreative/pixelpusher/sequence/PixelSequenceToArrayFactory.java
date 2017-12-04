package com.kreative.pixelpusher.sequence;

import java.awt.Image;
import com.kreative.pixelpusher.array.PixelArrayEditor;
import com.kreative.pixelpusher.array.PixelArrayFactory;
import com.kreative.pixelpusher.pixelset.PixelSetInfoSet;
import com.kreative.pixelpusher.resources.Resources;

public class PixelSequenceToArrayFactory extends PixelArrayFactory<PixelSequenceToArray> {
	@Override
	public String getNameForMachines() {
		return "Sequence-to-Array";
	}
	
	@Override
	public String getNameForHumans() {
		return "Convert Sequence to Array";
	}
	
	@Override
	public Image getIcon(int size) {
		return Resources.getImage("operation", "sequencetoarray", size);
	}
	
	@Override
	public Class<PixelSequenceToArray> getPixelSetClass() {
		return PixelSequenceToArray.class;
	}
	
	@Override
	public PixelSequenceToArray createPixelSet() {
		return new PixelSequenceToArray();
	}
	
	@Override
	public PixelArrayEditor<PixelSequenceToArray> createEditor(PixelSetInfoSet pixelSets, PixelSequenceToArray sequence) {
		return new PixelSequenceToArrayEditor(pixelSets, sequence);
	}
}
