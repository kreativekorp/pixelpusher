package com.kreative.pixelpusher.colorcycle;

import java.awt.Image;
import java.awt.Toolkit;
import com.kreative.pixelpusher.pixelset.PixelSetInfoSet;
import com.kreative.pixelpusher.sequence.PixelSequenceEditor;
import com.kreative.pixelpusher.sequence.PixelSequenceFactory;

public class ColorCycleFactory extends PixelSequenceFactory<ColorCycleSequence> {
	@Override
	public String getNameForMachines() {
		return "ColorCycle";
	}
	
	@Override
	public String getNameForHumans() {
		return "ColorCycle";
	}
	
	@Override
	public Image getIcon(int size) {
		return Toolkit.getDefaultToolkit().createImage(ColorCycleFactory.class.getResource("ColorCycle" + size + ".png"));
	}
	
	@Override
	public Class<ColorCycleSequence> getPixelSetClass() {
		return ColorCycleSequence.class;
	}
	
	@Override
	public ColorCycleSequence createPixelSet() {
		return new ColorCycleSequence();
	}
	
	@Override
	public PixelSequenceEditor<ColorCycleSequence> createEditor(PixelSetInfoSet pixelSets, ColorCycleSequence sequence) {
		return new ColorCycleEditor<ColorCycleSequence>(sequence);
	}
}
