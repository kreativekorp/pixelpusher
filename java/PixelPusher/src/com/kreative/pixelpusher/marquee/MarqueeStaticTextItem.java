package com.kreative.pixelpusher.marquee;

public class MarqueeStaticTextItem extends MarqueeTextItem {
	private static final long serialVersionUID = 1L;
	
	protected String text;
	
	public MarqueeStaticTextItem() {
		super();
		text = "";
	}
	
	public MarqueeStaticTextItem(String text) {
		super();
		if (text == null) text = "";
		this.text = text;
	}
	
	public MarqueeStaticTextItem(MarqueeStaticTextItem toClone) {
		super(toClone);
		text = toClone.text;
	}
	
	@Override
	public synchronized MarqueeStaticTextItem clone() {
		return new MarqueeStaticTextItem(this);
	}
	
	@Override
	public synchronized int getMsPerFrame() {
		return 1000;
	}
	
	@Override
	public synchronized String getText(long tick) {
		return text;
	}
	
	public synchronized String getText() {
		return text;
	}
	
	public synchronized void setText(String text) {
		if (text == null) text = "";
		this.text = text;
	}
}
