package com.kreative.pixelpusher.array;

import java.awt.Image;
import com.kreative.pixelpusher.pixelset.PixelSetInfoSet;
import com.kreative.pixelpusher.resources.Resources;
import com.kreative.pixelpusher.sequence.PixelSequenceEditor;
import com.kreative.pixelpusher.sequence.PixelSequenceFactory;

public class PixelArrayToSequenceFactory extends PixelSequenceFactory<PixelArrayToSequence> {
	@Override
	public String getNameForMachines() {
		return "Array-to-Sequence";
	}
	
	@Override
	public String getNameForHumans() {
		return "Convert Array to Sequence";
	}
	
	@Override
	public Image getIcon(int size) {
		return Resources.getImage("operation", "arraytosequence", size);
	}
	
	@Override
	public Class<PixelArrayToSequence> getPixelSetClass() {
		return PixelArrayToSequence.class;
	}
	
	@Override
	public PixelArrayToSequence createPixelSet() {
		return new PixelArrayToSequence();
	}
	
	@Override
	public PixelSequenceEditor<PixelArrayToSequence> createEditor(PixelSetInfoSet pixelSets, PixelArrayToSequence sequence) {
		return new PixelArrayToSequenceEditor(pixelSets, sequence);
	}
}
