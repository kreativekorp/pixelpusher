package com.kreative.pixelpusher.moodlight;

import java.awt.Image;
import java.awt.Toolkit;
import com.kreative.pixelpusher.array.PixelArrayFactory;

public class MoodlightArrayFactory extends PixelArrayFactory<MoodlightArray> {
	@Override
	public String getNameForMachines() {
		return "Moodlight-Array";
	}
	
	@Override
	public String getNameForHumans() {
		return "Moodlight - Array";
	}
	
	@Override
	public Image getIcon(int size) {
		return Toolkit.getDefaultToolkit().createImage(MoodlightArrayFactory.class.getResource("Moodlight" + size + ".png"));
	}
	
	@Override
	public Class<MoodlightArray> getPixelSetClass() {
		return MoodlightArray.class;
	}
	
	@Override
	public MoodlightArray createPixelSet() {
		return new MoodlightArray();
	}
}
