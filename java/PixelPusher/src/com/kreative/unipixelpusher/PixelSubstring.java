package com.kreative.unipixelpusher;

public class PixelSubstring extends AbstractPixelString {
	protected PixelString parent;
	protected int offset;
	protected int length;
	
	public PixelSubstring(PixelString parent, int offset, int length) {
		this.parent = parent;
		this.offset = offset;
		this.length = length;
	}
	
	public PixelString getParent() {
		return this.parent;
	}
	
	public void setParent(PixelString parent) {
		this.parent = parent;
	}
	
	public int getOffset() {
		return this.offset;
	}
	
	public void setOffset(int offset) {
		this.offset = offset;
	}
	
	public int getLength() {
		return this.length;
	}
	
	public void setLength(int length) {
		this.length = length;
	}
	
	@Override
	public int length() {
		return length;
	}
	
	@Override
	protected int getPixelImpl(int i) {
		if (i < 0 || i >= length) return 0;
		return parent.getPixel(offset + i);
	}
	
	@Override
	protected void setPixelImpl(int i, int color) {
		if (i < 0 || i >= length) return;
		parent.setPixel(offset + i, color);
	}
	
	@Override
	public void push() {
		parent.push();
	}
	
	@Override
	public String id() {
		return parent.id() + "#" + offset + ";" + length;
	}
	
	@Override
	public StringType type() {
		return parent.type();
	}
}
