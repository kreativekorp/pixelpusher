package com.kreative.pixelpusher.marquee;

import java.awt.Image;
import java.awt.Toolkit;
import com.kreative.pixelpusher.array.PixelArrayEditor;
import com.kreative.pixelpusher.array.PixelArrayFactory;
import com.kreative.pixelpusher.pixelset.PixelSetInfoSet;

public class MarqueeFactory extends PixelArrayFactory<MarqueeArray> {
	@Override
	public String getNameForMachines() {
		return "Marquee";
	}
	
	@Override
	public String getNameForHumans() {
		return "Marquee";
	}
	
	@Override
	public Image getIcon(int size) {
		return Toolkit.getDefaultToolkit().createImage(MarqueeFactory.class.getResource("Marquee" + size + ".png"));
	}
	
	@Override
	public Class<MarqueeArray> getPixelSetClass() {
		return MarqueeArray.class;
	}
	
	@Override
	public MarqueeArray createPixelSet() {
		return new MarqueeArray();
	}
	
	@Override
	public PixelArrayEditor<MarqueeArray> createEditor(PixelSetInfoSet pixelSets, MarqueeArray array) {
		return new MarqueeEditor(array);
	}
}
