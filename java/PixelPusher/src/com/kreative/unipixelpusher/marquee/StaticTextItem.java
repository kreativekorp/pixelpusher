package com.kreative.unipixelpusher.marquee;

import com.kreative.unipixelpusher.SequenceConfiguration;

public class StaticTextItem extends TextItem {
	protected String text;
	
	public StaticTextItem() {
		this.text = null;
	}
	
	public StaticTextItem(String text) {
		this.text = text;
	}
	
	public synchronized String getText() {
		return this.text;
	}
	
	public synchronized void setText(String text) {
		this.text = text;
	}
	
	@Override
	public synchronized String getText(long tick) {
		return text;
	}
	
	@Override
	public long getUpdateInterval() {
		return 1000;
	}
	
	@Override
	public synchronized void loadConfiguration(SequenceConfiguration config, String prefix) {
		super.loadConfiguration(config, prefix);
		this.text = config.get(prefix + ".text");
	}
	
	@Override
	public synchronized void saveConfiguration(SequenceConfiguration config, String prefix) {
		super.saveConfiguration(config, prefix);
		config.put(prefix + ".text", text);
	}
}
