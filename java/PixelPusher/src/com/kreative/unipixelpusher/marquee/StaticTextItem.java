package com.kreative.unipixelpusher.marquee;

public class StaticTextItem extends TextItem {
	protected String text;
	
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
}
