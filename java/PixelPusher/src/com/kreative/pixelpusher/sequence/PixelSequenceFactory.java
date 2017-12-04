package com.kreative.pixelpusher.sequence;

import java.awt.Image;
import com.kreative.pixelpusher.array.PixelArray;
import com.kreative.pixelpusher.array.PixelArrayFactory;
import com.kreative.pixelpusher.pixelset.PixelSetFactory;
import com.kreative.pixelpusher.pixelset.PixelSetInfoSet;

public abstract class PixelSequenceFactory<T extends PixelSequence> extends PixelSetFactory<T> {
	@Override public abstract String getNameForMachines();
	@Override public abstract String getNameForHumans();
	@Override public abstract Image getIcon(int size);
	@Override public final boolean isSequenceFactory() { return true; }
	@Override public final PixelSequenceFactory<T> sequenceFactory() { return this; }
	@Override public final boolean isArrayFactory() { return false; }
	@Override public final PixelArrayFactory<? extends PixelArray> arrayFactory() { return null; }
	@Override public abstract Class<T> getPixelSetClass();
	@Override public abstract T createPixelSet();
	
	@Override
	public PixelSequenceVisualizer createVisualizer(T sequence) {
		return new GenericSequenceVisualizer(sequence);
	}
	
	@Override
	public PixelSequenceEditor<T> createEditor(PixelSetInfoSet pixelSets, T sequence) {
		return new GenericSequenceEditor<T>(sequence);
	}
	
	@Override
	public PixelSequenceVisualEditor<T> createVisualEditor(PixelSetInfoSet pixelSets, T sequence) {
		return new GenericSequenceVisualEditor<T>(sequence, createVisualizer(sequence), createEditor(pixelSets, sequence));
	}
}
