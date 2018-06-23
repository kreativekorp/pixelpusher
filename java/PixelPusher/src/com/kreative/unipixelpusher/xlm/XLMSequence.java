package com.kreative.unipixelpusher.xlm;

import com.kreative.unipixelpusher.ColorConstants;
import com.kreative.unipixelpusher.PixelSequence;
import com.kreative.unipixelpusher.PixelString;
import com.kreative.unipixelpusher.SequenceConfiguration;

public class XLMSequence implements PixelSequence {
	public static final String name = "Explorateria Light Machine (XLM)";
	
	private XLMChannel[] channels;
	
	public XLMSequence() {
		this.channels = new XLMChannel[] {
			new XLMChannel(ColorConstants.RED),
			new XLMChannel(ColorConstants.BLUE)
		};
	}
	
	public synchronized int getChannelCount() {
		return this.channels.length;
	}
	
	public synchronized void setChannelCount(int count) {
		if (count < 1) count = 1;
		XLMChannel[] channels = new XLMChannel[count];
		for (int i = 0; i < count; i++) {
			channels[i] = new XLMChannel(
				this.channels[i % this.channels.length]
			);
		}
		this.channels = channels;
	}
	
	public synchronized XLMChannel getChannel(int index) {
		return this.channels[index];
	}
	
	@Override
	public synchronized void update(PixelString ps, long tick) {
		for (XLMChannel ch : channels) ch.update(tick);
		for (int i = 0, n = ps.length(); i < n; i++) {
			XLMChannel ch = channels[i % channels.length];
			ps.setPixel(i, ch.getPixelColor(i / channels.length));
		}
		ps.push();
	}
	
	@Override
	public long getUpdateInterval() {
		return 20;
	}
	
	@Override
	public synchronized void loadConfiguration(SequenceConfiguration config) {
		int n = config.get("channels", 0);
		if (n > 0) {
			this.channels = new XLMChannel[n];
			for (int i = 0; i < n; i++) {
				this.channels[i] = new XLMChannel(-1);
				this.channels[i].loadConfiguration(config, "channels.ch" + i);
			}
		} else {
			this.channels = new XLMChannel[] {
				new XLMChannel(ColorConstants.RED),
				new XLMChannel(ColorConstants.BLUE)
			};
		}
	}
	
	@Override
	public synchronized void saveConfiguration(SequenceConfiguration config) {
		config.put("channels", channels.length);
		for (int i = 0; i < channels.length; i++) {
			channels[i].saveConfiguration(config, "channels.ch" + i);
		}
	}
	
	@Override
	public String toString() {
		return name;
	}
}
