package com.kreative.pixelpusher.array;

import java.awt.Image;
import com.kreative.pixelpusher.pixelset.PixelSetFactory;
import com.kreative.pixelpusher.pixelset.PixelSetInfoSet;
import com.kreative.pixelpusher.sequence.PixelSequence;
import com.kreative.pixelpusher.sequence.PixelSequenceFactory;

public abstract class PixelArrayFactory<T extends PixelArray> extends PixelSetFactory<T> {
	@Override public abstract String getNameForMachines();
	@Override public abstract String getNameForHumans();
	@Override public abstract Image getIcon(int size);
	@Override public final boolean isSequenceFactory() { return false; }
	@Override public final PixelSequenceFactory<? extends PixelSequence> sequenceFactory() { return null; }
	@Override public final boolean isArrayFactory() { return true; }
	@Override public final PixelArrayFactory<T> arrayFactory() { return this; }
	@Override public abstract Class<T> getPixelSetClass();
	@Override public abstract T createPixelSet();
	
	@Override
	public PixelArrayVisualizer createVisualizer(T array) {
		return new GenericArrayVisualizer(array);
	}
	
	@Override
	public PixelArrayEditor<T> createEditor(PixelSetInfoSet pixelSets, T array) {
		return new GenericArrayEditor<T>(array);
	}
	
	@Override
	public PixelArrayVisualEditor<T> createVisualEditor(PixelSetInfoSet pixelSets, T array) {
		return new GenericArrayVisualEditor<T>(array, createVisualizer(array), createEditor(pixelSets, array));
	}
}
