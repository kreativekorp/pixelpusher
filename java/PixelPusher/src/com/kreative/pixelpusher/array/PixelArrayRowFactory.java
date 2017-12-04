package com.kreative.pixelpusher.array;

import java.awt.Image;
import com.kreative.pixelpusher.pixelset.PixelSetInfoSet;
import com.kreative.pixelpusher.resources.Resources;
import com.kreative.pixelpusher.sequence.PixelSequenceEditor;
import com.kreative.pixelpusher.sequence.PixelSequenceFactory;

public class PixelArrayRowFactory extends PixelSequenceFactory<PixelArrayRow> {
	@Override
	public String getNameForMachines() {
		return "Array-Row";
	}
	
	@Override
	public String getNameForHumans() {
		return "Array Row";
	}
	
	@Override
	public Image getIcon(int size) {
		return Resources.getImage("operation", "arrayrow", size);
	}
	
	@Override
	public Class<PixelArrayRow> getPixelSetClass() {
		return PixelArrayRow.class;
	}
	
	@Override
	public PixelArrayRow createPixelSet() {
		return new PixelArrayRow();
	}
	
	@Override
	public PixelSequenceEditor<PixelArrayRow> createEditor(PixelSetInfoSet pixelSets, PixelArrayRow sequence) {
		return new PixelArrayRowEditor(pixelSets, sequence);
	}
}
