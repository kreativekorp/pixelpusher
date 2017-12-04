package com.kreative.pixelpusher.colorbars;

import java.awt.Image;
import java.awt.Toolkit;
import com.kreative.pixelpusher.array.PixelArrayEditor;
import com.kreative.pixelpusher.array.PixelArrayFactory;
import com.kreative.pixelpusher.pixelset.PixelSetInfoSet;

public class ColorBarsFactory extends PixelArrayFactory<ColorBarsArray> {
	@Override
	public String getNameForMachines() {
		return "Color-Bars";
	}
	
	@Override
	public String getNameForHumans() {
		return "Color Bars";
	}
	
	@Override
	public Image getIcon(int size) {
		return Toolkit.getDefaultToolkit().createImage(ColorBarsFactory.class.getResource("ColorBars" + size + ".png"));
	}
	
	@Override
	public Class<ColorBarsArray> getPixelSetClass() {
		return ColorBarsArray.class;
	}
	
	@Override
	public ColorBarsArray createPixelSet() {
		return new ColorBarsArray();
	}
	
	@Override
	public PixelArrayEditor<ColorBarsArray> createEditor(PixelSetInfoSet pixelSets, ColorBarsArray array) {
		return new ColorBarsEditor(array);
	}
}
