package com.kreative.pixelpusher.animation;

import java.awt.Image;
import java.awt.Toolkit;
import com.kreative.pixelpusher.array.PixelArrayEditor;
import com.kreative.pixelpusher.array.PixelArrayFactory;
import com.kreative.pixelpusher.pixelset.PixelSetInfoSet;

public class AnimationFactory extends PixelArrayFactory<AnimationArray> {
	@Override
	public String getNameForMachines() {
		return "Animation";
	}
	
	@Override
	public String getNameForHumans() {
		return "Animation";
	}
	
	@Override
	public Image getIcon(int size) {
		return Toolkit.getDefaultToolkit().createImage(AnimationFactory.class.getResource("Animation" + size + ".png"));
	}
	
	@Override
	public Class<AnimationArray> getPixelSetClass() {
		return AnimationArray.class;
	}
	
	@Override
	public AnimationArray createPixelSet() {
		return new AnimationArray();
	}
	
	@Override
	public PixelArrayEditor<AnimationArray> createEditor(PixelSetInfoSet pixelSets, AnimationArray array) {
		return new AnimationEditor(array);
	}
}
