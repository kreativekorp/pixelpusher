package com.kreative.pixelpusher.xlm;

import java.awt.Image;
import java.awt.Toolkit;
import com.kreative.pixelpusher.pixelset.PixelSetInfoSet;
import com.kreative.pixelpusher.sequence.PixelSequenceEditor;
import com.kreative.pixelpusher.sequence.PixelSequenceFactory;
import com.kreative.pixelpusher.sequence.PixelSequenceVisualEditor;
import com.kreative.pixelpusher.sequence.PixelSequenceVisualizer;

public class XLMFactory extends PixelSequenceFactory<XLMSequence> {
	@Override
	public String getNameForMachines() {
		return "XLM";
	}
	
	@Override
	public String getNameForHumans() {
		return "Explorateria Light Machine (XLM)";
	}
	
	@Override
	public Image getIcon(int size) {
		return Toolkit.getDefaultToolkit().createImage(XLMFactory.class.getResource("XLM" + size + ".png"));
	}
	
	@Override
	public Class<XLMSequence> getPixelSetClass() {
		return XLMSequence.class;
	}
	
	@Override
	public XLMSequence createPixelSet() {
		return new XLMSequence();
	}
	
	@Override
	public PixelSequenceVisualizer createVisualizer(XLMSequence sequence) {
		return new XLMVisualizer(sequence);
	}
	
	@Override
	public PixelSequenceEditor<XLMSequence> createEditor(PixelSetInfoSet pixelSets, XLMSequence sequence) {
		return new XLMEditor(sequence);
	}
	
	@Override
	public PixelSequenceVisualEditor<XLMSequence> createVisualEditor(PixelSetInfoSet pixelSets, XLMSequence sequence) {
		return new XLMVisualEditor(sequence);
	}
}
