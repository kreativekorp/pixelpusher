package com.kreative.pixelpusher.xlm;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import com.kreative.pixelpusher.common.ColorConstants;
import com.kreative.pixelpusher.common.MathUtilities;
import com.kreative.pixelpusher.pixelset.PixelSet;
import com.kreative.pixelpusher.sequence.PixelSequence;

public class XLMSequence extends PixelSequence {
	private static final long serialVersionUID = 1L;
	
	private XLMChannel[] channels;
	
	public XLMSequence() {
		channels = new XLMChannel[]{
			new XLMChannel(ColorConstants.RED, 0),
			new XLMChannel(ColorConstants.BLUE, 0),
		};
	}
	
	public XLMSequence(XLMSequence source) {
		channels = new XLMChannel[source.channels.length];
		for (int i = 0; i < channels.length; i++) {
			channels[i] = new XLMChannel(source.channels[i]);
		}
	}
	
	@Override
	public synchronized XLMSequence clone() {
		return new XLMSequence(this);
	}
	
	public synchronized int getChannelCount() {
		return channels.length;
	}
	
	public synchronized void setChannelCount(int count) {
		if (count < 1) count = 1;
		XLMChannel[] channels = new XLMChannel[count];
		for (int i = 0; i < count; i++) {
			channels[i] = new XLMChannel(this.channels[i % this.channels.length]);
		}
		this.channels = channels;
	}
	
	public synchronized XLMChannel getChannel(int index) {
		return channels[index];
	}
	
	@Override
	public synchronized int getMsPerFrame() {
		int msPerFrame = channels[0].getMsPerFrame();
		if (msPerFrame <= 20) return 20;
		for (int i = 1; i < channels.length; i++) {
			msPerFrame = MathUtilities.gcd(msPerFrame, channels[i].getMsPerFrame());
			if (msPerFrame <= 20) return 20;
		}
		return msPerFrame;
	}
	
	@Override
	public synchronized void updatePixels(long tick) {
		for (XLMChannel channel : channels) {
			channel.updatePixels(tick);
		}
	}
	
	@Override
	public synchronized int getPixelColor(int index) {
		return channels[index % channels.length].getPixelColor(index / channels.length);
	}
	
	@Override
	public synchronized Collection<? extends PixelSet> getDependencies() {
		return Collections.unmodifiableCollection(Arrays.asList(channels));
	}
}
