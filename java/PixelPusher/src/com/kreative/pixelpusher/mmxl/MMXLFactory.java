package com.kreative.pixelpusher.mmxl;

import java.awt.Image;
import java.awt.Toolkit;
import com.kreative.pixelpusher.pixelset.PixelSetInfoSet;
import com.kreative.pixelpusher.sequence.PixelSequenceEditor;
import com.kreative.pixelpusher.sequence.PixelSequenceFactory;
import com.kreative.pixelpusher.sequence.PixelSequenceVisualEditor;
import com.kreative.pixelpusher.sequence.PixelSequenceVisualizer;

public class MMXLFactory extends PixelSequenceFactory<MMXLSequence> {
	@Override
	public String getNameForMachines() {
		return "MMXL";
	}
	
	@Override
	public String getNameForHumans() {
		return "MoreMore Christmas Lights (MMXL)";
	}
	
	@Override
	public Image getIcon(int size) {
		return Toolkit.getDefaultToolkit().createImage(MMXLFactory.class.getResource("MMXL" + size + ".png"));
	}
	
	@Override
	public Class<MMXLSequence> getPixelSetClass() {
		return MMXLSequence.class;
	}
	
	@Override
	public MMXLSequence createPixelSet() {
		return new MMXLSequence();
	}
	
	@Override
	public PixelSequenceVisualizer createVisualizer(MMXLSequence sequence) {
		return new MMXLVisualizer(sequence);
	}
	
	@Override
	public PixelSequenceEditor<MMXLSequence> createEditor(PixelSetInfoSet pixelSets, MMXLSequence sequence) {
		return new MMXLEditor(sequence);
	}
	
	@Override
	public PixelSequenceVisualEditor<MMXLSequence> createVisualEditor(PixelSetInfoSet pixelSets, MMXLSequence sequence) {
		return new MMXLVisualEditor(sequence);
	}
}
