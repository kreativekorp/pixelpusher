package com.kreative.pixelpusher.colorcycle;

import java.awt.Image;
import java.awt.Toolkit;
import com.kreative.pixelpusher.pixelset.PixelSetInfoSet;
import com.kreative.pixelpusher.sequence.PixelSequenceEditor;
import com.kreative.pixelpusher.sequence.PixelSequenceFactory;

public class ColorFaderFactory extends PixelSequenceFactory<ColorFaderSequence> {
	@Override
	public String getNameForMachines() {
		return "ColorFader";
	}
	
	@Override
	public String getNameForHumans() {
		return "ColorFader";
	}
	
	@Override
	public Image getIcon(int size) {
		return Toolkit.getDefaultToolkit().createImage(ColorFaderFactory.class.getResource("ColorCycle" + size + ".png"));
	}
	
	@Override
	public Class<ColorFaderSequence> getPixelSetClass() {
		return ColorFaderSequence.class;
	}
	
	@Override
	public ColorFaderSequence createPixelSet() {
		return new ColorFaderSequence();
	}
	
	@Override
	public PixelSequenceEditor<ColorFaderSequence> createEditor(PixelSetInfoSet pixelSets, ColorFaderSequence sequence) {
		return new ColorCycleEditor<ColorFaderSequence>(sequence);
	}
}
