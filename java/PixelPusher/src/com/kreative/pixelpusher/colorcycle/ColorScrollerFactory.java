package com.kreative.pixelpusher.colorcycle;

import java.awt.Image;
import java.awt.Toolkit;
import com.kreative.pixelpusher.pixelset.PixelSetInfoSet;
import com.kreative.pixelpusher.sequence.PixelSequenceEditor;
import com.kreative.pixelpusher.sequence.PixelSequenceFactory;

public class ColorScrollerFactory extends PixelSequenceFactory<ColorScrollerSequence> {
	@Override
	public String getNameForMachines() {
		return "ColorScroller";
	}
	
	@Override
	public String getNameForHumans() {
		return "ColorScroller";
	}
	
	@Override
	public Image getIcon(int size) {
		return Toolkit.getDefaultToolkit().createImage(ColorScrollerFactory.class.getResource("ColorCycle" + size + ".png"));
	}
	
	@Override
	public Class<ColorScrollerSequence> getPixelSetClass() {
		return ColorScrollerSequence.class;
	}
	
	@Override
	public ColorScrollerSequence createPixelSet() {
		return new ColorScrollerSequence();
	}
	
	@Override
	public PixelSequenceEditor<ColorScrollerSequence> createEditor(PixelSetInfoSet pixelSets, ColorScrollerSequence sequence) {
		return new ColorCycleEditor<ColorScrollerSequence>(sequence);
	}
}
