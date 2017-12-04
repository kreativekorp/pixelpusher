package com.kreative.pixelpusher.sequence;

import java.awt.Image;
import com.kreative.pixelpusher.pixelset.PixelSetInfoSet;
import com.kreative.pixelpusher.resources.Resources;

public class PixelSubSequenceFactory extends PixelSequenceFactory<PixelSubSequence> {
	@Override
	public String getNameForMachines() {
		return "Subsequence";
	}
	
	@Override
	public String getNameForHumans() {
		return "Subsequence";
	}
	
	@Override
	public Image getIcon(int size) {
		return Resources.getImage("operation", "subsequence", size);
	}
	
	@Override
	public Class<PixelSubSequence> getPixelSetClass() {
		return PixelSubSequence.class;
	}
	
	@Override
	public PixelSubSequence createPixelSet() {
		return new PixelSubSequence();
	}
	
	@Override
	public PixelSequenceEditor<PixelSubSequence> createEditor(PixelSetInfoSet pixelSets, PixelSubSequence sequence) {
		return new PixelSubSequenceEditor(pixelSets, sequence);
	}
}
