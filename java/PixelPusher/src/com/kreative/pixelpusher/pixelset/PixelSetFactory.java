package com.kreative.pixelpusher.pixelset;

import java.awt.Image;
import com.kreative.pixelpusher.array.PixelArray;
import com.kreative.pixelpusher.array.PixelArrayFactory;
import com.kreative.pixelpusher.sequence.PixelSequence;
import com.kreative.pixelpusher.sequence.PixelSequenceFactory;

public abstract class PixelSetFactory<T extends PixelSet> {
	public abstract String getNameForMachines();
	public abstract String getNameForHumans();
	public abstract Image getIcon(int size);
	public abstract boolean isSequenceFactory();
	public abstract PixelSequenceFactory<? extends PixelSequence> sequenceFactory();
	public abstract boolean isArrayFactory();
	public abstract PixelArrayFactory<? extends PixelArray> arrayFactory();
	public abstract Class<T> getPixelSetClass();
	public abstract T createPixelSet();
	public abstract PixelSetVisualizer<? super T> createVisualizer(T pixelSet);
	public abstract PixelSetEditor<T> createEditor(PixelSetInfoSet pixelSets, T pixelSet);
	public abstract PixelSetVisualEditor<T> createVisualEditor(PixelSetInfoSet pixelSets, T pixelSet);
	
	public final PixelSetEditorFrame createEditorFrame(PixelSetInfoSet pixelSets, T pixelSet) {
		return new PixelSetEditorFrame(getNameForHumans(), createEditor(pixelSets, pixelSet));
	}
	
	public final PixelSetVisualEditorFrame createVisualEditorFrame(PixelSetInfoSet pixelSets, T pixelSet) {
		return new PixelSetVisualEditorFrame(getNameForHumans(), createVisualEditor(pixelSets, pixelSet));
	}
}
