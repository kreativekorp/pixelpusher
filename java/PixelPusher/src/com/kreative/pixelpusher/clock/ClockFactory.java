package com.kreative.pixelpusher.clock;

import java.awt.Image;
import java.awt.Toolkit;
import com.kreative.pixelpusher.pixelset.PixelSetInfoSet;
import com.kreative.pixelpusher.sequence.PixelSequenceEditor;
import com.kreative.pixelpusher.sequence.PixelSequenceFactory;
import com.kreative.pixelpusher.sequence.PixelSequenceVisualizer;

public class ClockFactory extends PixelSequenceFactory<ClockSequence> {
	@Override
	public String getNameForMachines() {
		return "Clock";
	}
	
	@Override
	public String getNameForHumans() {
		return "Clock";
	}
	
	@Override
	public Image getIcon(int size) {
		return Toolkit.getDefaultToolkit().createImage(ClockFactory.class.getResource("Clock" + size + ".png"));
	}
	
	@Override
	public Class<ClockSequence> getPixelSetClass() {
		return ClockSequence.class;
	}
	
	@Override
	public ClockSequence createPixelSet() {
		return new ClockSequence();
	}
	
	@Override
	public PixelSequenceVisualizer createVisualizer(ClockSequence sequence) {
		return new ClockVisualizer(sequence);
	}
	
	@Override
	public PixelSequenceEditor<ClockSequence> createEditor(PixelSetInfoSet pixelSets, ClockSequence sequence) {
		return new ClockEditor(sequence);
	}
}
