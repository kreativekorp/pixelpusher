package com.kreative.pixelpusher.marquee;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.io.Serializable;
import javax.swing.SwingConstants;

public abstract class MarqueeItem implements Cloneable, Serializable, SwingConstants {
	private static final long serialVersionUID = 1L;
	
	protected int topPadding;
	protected int leftPadding;
	protected int bottomPadding;
	protected int rightPadding;
	protected int horizontalAlignment;
	protected int verticalAlignment;
	protected int backgroundColor;
	
	protected MarqueeItem() {
		topPadding = 0;
		leftPadding = 0;
		bottomPadding = 0;
		rightPadding = 0;
		horizontalAlignment = CENTER;
		verticalAlignment = CENTER;
		backgroundColor = 0;
	}
	
	protected MarqueeItem(MarqueeItem toClone) {
		topPadding = toClone.topPadding;
		leftPadding = toClone.leftPadding;
		bottomPadding = toClone.bottomPadding;
		rightPadding = toClone.rightPadding;
		horizontalAlignment = toClone.horizontalAlignment;
		verticalAlignment = toClone.verticalAlignment;
		backgroundColor = toClone.backgroundColor;
	}
	
	@Override public abstract MarqueeItem clone();
	public abstract int getMsPerFrame();
	public abstract void updatePixels(long tick);
	
	public abstract int getInnerWidth();
	public abstract int getInnerHeight();
	
	public final synchronized Dimension getInnerSize() {
		return new Dimension(getInnerWidth(), getInnerHeight());
	}
	
	public final synchronized int getTopPadding() {
		return topPadding;
	}
	
	public final synchronized void setTopPadding(int top) {
		if (top < 0) top = 0;
		topPadding = top;
	}
	
	public final synchronized int getLeftPadding() {
		return leftPadding;
	}
	
	public final synchronized void setLeftPadding(int left) {
		if (left < 0) left = 0;
		leftPadding = left;
	}
	
	public final synchronized int getBottomPadding() {
		return bottomPadding;
	}
	
	public final synchronized void setBottomPadding(int bottom) {
		if (bottom < 0) bottom = 0;
		bottomPadding = bottom;
	}
	
	public final synchronized int getRightPadding() {
		return rightPadding;
	}
	
	public final synchronized void setRightPadding(int right) {
		if (right < 0) right = 0;
		rightPadding = right;
	}
	
	public final synchronized Insets getPadding() {
		return new Insets(topPadding, leftPadding, bottomPadding, rightPadding);
	}
	
	public final synchronized void setPadding(Insets insets) {
		setPadding(insets.top, insets.left, insets.bottom, insets.right);
	}
	
	public final synchronized void setPadding(int top, int left, int bottom, int right) {
		if (top < 0) top = 0;
		if (left < 0) left = 0;
		if (bottom < 0) bottom = 0;
		if (right < 0) right = 0;
		topPadding = top;
		leftPadding = left;
		bottomPadding = bottom;
		rightPadding = right;
	}
	
	public final synchronized int getOuterWidth() {
		return getInnerWidth() + leftPadding + rightPadding;
	}
	
	public final synchronized int getOuterHeight() {
		return getInnerHeight() + topPadding + bottomPadding;
	}
	
	public final synchronized Dimension getOuterSize() {
		return new Dimension(getOuterWidth(), getOuterHeight());
	}
	
	public final synchronized int getHorizontalAlignment() {
		return horizontalAlignment;
	}
	
	public final synchronized void setHorizontalAlignment(int horizontal) {
		horizontalAlignment = horizontal;
	}
	
	public final synchronized int getVerticalAlignment() {
		return verticalAlignment;
	}
	
	public final synchronized void setVerticalAlignment(int vertical) {
		verticalAlignment = vertical;
	}
	
	public final synchronized int getAlignment() {
		switch (verticalAlignment) {
			case TOP:
				switch (horizontalAlignment) {
					case LEFT: return NORTH_WEST;
					case RIGHT: return NORTH_EAST;
					default: return NORTH;
				}
			case BOTTOM:
				switch (horizontalAlignment) {
					case LEFT: return SOUTH_WEST;
					case RIGHT: return SOUTH_EAST;
					default: return SOUTH;
				}
			default:
				switch (horizontalAlignment) {
					case LEFT: return WEST;
					case RIGHT: return EAST;
					default: return CENTER;
				}
		}
	}
	
	public final synchronized void setAlignment(int alignment) {
		switch (alignment) {
			case NORTH_WEST:
			case WEST:
			case SOUTH_WEST:
				horizontalAlignment = LEFT;
				break;
			case NORTH_EAST:
			case EAST:
			case SOUTH_EAST:
				horizontalAlignment = RIGHT;
				break;
			default:
				horizontalAlignment = CENTER;
				break;
		}
		switch (alignment) {
			case NORTH_WEST:
			case NORTH:
			case NORTH_EAST:
				verticalAlignment = TOP;
				break;
			case SOUTH_WEST:
			case SOUTH:
			case SOUTH_EAST:
				verticalAlignment = BOTTOM;
				break;
			default:
				verticalAlignment = CENTER;
				break;
		}
	}
	
	public final synchronized void setAlignment(int horizontal, int vertical) {
		horizontalAlignment = horizontal;
		verticalAlignment = vertical;
	}
	
	public final synchronized int getBackgroundColor() {
		return backgroundColor;
	}
	
	public final synchronized void setBackgroundColor(int background) {
		backgroundColor = background;
	}
	
	protected abstract void paintContent(Graphics2D g, int x, int y);
	
	public final synchronized void paint(Graphics2D g, int x, int y, int width, int height) {
		g.setColor(new Color(backgroundColor, true));
		g.fillRect(x, y, width, height);
		
		int px;
		switch (horizontalAlignment) {
			case LEFT:
				px = x + leftPadding;
				break;
			case RIGHT:
				px = x + leftPadding + (width - getOuterWidth());
				break;
			default:
				px = x + leftPadding + (width - getOuterWidth())/2;
				break;
		}
		
		int py;
		switch (verticalAlignment) {
			case TOP:
				py = y + topPadding;
				break;
			case BOTTOM:
				py = y + topPadding + (height - getOuterHeight());
				break;
			default:
				py = y + topPadding + (height - getOuterHeight())/2;
				break;
		}
		
		paintContent(g, px, py);
	}
}
