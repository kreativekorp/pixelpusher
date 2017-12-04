package com.kreative.pixelpusher.audiospectrum;

import java.awt.Image;
import java.awt.Toolkit;
import com.kreative.pixelpusher.array.PixelArrayEditor;
import com.kreative.pixelpusher.array.PixelArrayFactory;
import com.kreative.pixelpusher.pixelset.PixelSetInfoSet;

public class AudioSpectrumFactory extends PixelArrayFactory<AudioSpectrumArray> {
	@Override
	public String getNameForMachines() {
		return "Audio-Spectrum";
	}
	
	@Override
	public String getNameForHumans() {
		return "Audio Spectrum";
	}
	
	@Override
	public Image getIcon(int size) {
		return Toolkit.getDefaultToolkit().createImage(AudioSpectrumFactory.class.getResource("AudioSpectrum" + size + ".png"));
	}
	
	@Override
	public Class<AudioSpectrumArray> getPixelSetClass() {
		return AudioSpectrumArray.class;
	}
	
	@Override
	public AudioSpectrumArray createPixelSet() {
		return new AudioSpectrumArray();
	}
	
	@Override
	public PixelArrayEditor<AudioSpectrumArray> createEditor(PixelSetInfoSet pixelSets, AudioSpectrumArray array) {
		return new AudioSpectrumEditor(array);
	}
}
