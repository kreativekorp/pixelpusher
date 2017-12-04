package com.kreative.pixelpusher.array;

import java.awt.Image;
import com.kreative.pixelpusher.pixelset.PixelSetInfoSet;
import com.kreative.pixelpusher.resources.Resources;
import com.kreative.pixelpusher.sequence.PixelSequenceEditor;
import com.kreative.pixelpusher.sequence.PixelSequenceFactory;

public class PixelArrayColumnFactory extends PixelSequenceFactory<PixelArrayColumn> {
	@Override
	public String getNameForMachines() {
		return "Array-Column";
	}
	
	@Override
	public String getNameForHumans() {
		return "Array Column";
	}
	
	@Override
	public Image getIcon(int size) {
		return Resources.getImage("operation", "arraycolumn", size);
	}
	
	@Override
	public Class<PixelArrayColumn> getPixelSetClass() {
		return PixelArrayColumn.class;
	}
	
	@Override
	public PixelArrayColumn createPixelSet() {
		return new PixelArrayColumn();
	}
	
	@Override
	public PixelSequenceEditor<PixelArrayColumn> createEditor(PixelSetInfoSet pixelSets, PixelArrayColumn sequence) {
		return new PixelArrayColumnEditor(pixelSets, sequence);
	}
}
