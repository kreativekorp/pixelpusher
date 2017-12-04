package com.kreative.pixelpusher.moodlight;

import java.awt.Image;
import java.awt.Toolkit;
import com.kreative.pixelpusher.sequence.PixelSequenceFactory;

public class MoodlightSequenceFactory extends PixelSequenceFactory<MoodlightSequence> {
	@Override
	public String getNameForMachines() {
		return "Moodlight-Sequence";
	}
	
	@Override
	public String getNameForHumans() {
		return "Moodlight - Sequence";
	}
	
	@Override
	public Image getIcon(int size) {
		return Toolkit.getDefaultToolkit().createImage(MoodlightSequenceFactory.class.getResource("Moodlight" + size + ".png"));
	}
	
	@Override
	public Class<MoodlightSequence> getPixelSetClass() {
		return MoodlightSequence.class;
	}
	
	@Override
	public MoodlightSequence createPixelSet() {
		return new MoodlightSequence();
	}
}
