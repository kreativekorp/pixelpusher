package com.kreative.unipixelpusher.marquee;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Insets;
import javax.swing.SwingConstants;

public abstract class MarqueeItem implements SwingConstants {
	protected int topPadding = 0;
	protected int leftPadding = 0;
	protected int bottomPadding = 0;
	protected int rightPadding = 0;
	protected int horizontalAlignment = CENTER;
	protected int verticalAlignment = CENTER;
	protected int backgroundColor = 0;
	
	public abstract int getInnerWidth(long tick);
	public abstract int getInnerHeight(long tick);
	
	public final synchronized Dimension getInnerSize(long tick) {
		return new Dimension(getInnerWidth(tick), getInnerHeight(tick));
	}
	
	public final synchronized int getTopPadding() { return topPadding; }
	public final synchronized int getLeftPadding() { return leftPadding; }
	public final synchronized int getBottomPadding() { return bottomPadding; }
	public final synchronized int getRightPadding() { return rightPadding; }
	
	public final synchronized void setTopPadding(int top) { topPadding = (top > 0) ? top : 0; }
	public final synchronized void setLeftPadding(int left) { leftPadding = (left > 0) ? left : 0; }
	public final synchronized void setBottomPadding(int bottom) { bottomPadding = (bottom > 0) ? bottom : 0; }
	public final synchronized void setRightPadding(int right) { rightPadding = (right > 0) ? right : 0; }
	
	public final synchronized Insets getPadding() {
		return new Insets(topPadding, leftPadding, bottomPadding, rightPadding);
	}
	
	public final synchronized void setPadding(Insets insets) {
		topPadding = (insets.top > 0) ? insets.top : 0;
		leftPadding = (insets.left > 0) ? insets.left : 0;
		bottomPadding = (insets.bottom > 0) ? insets.bottom : 0;
		rightPadding = (insets.right > 0) ? insets.right : 0;
	}
	
	public final synchronized void setPadding(int top, int left, int bottom, int right) {
		topPadding = (top > 0) ? top : 0;
		leftPadding = (left > 0) ? left : 0;
		bottomPadding = (bottom > 0) ? bottom : 0;
		rightPadding = (right > 0) ? right : 0;
	}
	
	public final synchronized int getOuterWidth(long tick) {
		return getInnerWidth(tick) + leftPadding + rightPadding;
	}
	
	public final synchronized int getOuterHeight(long tick) {
		return getInnerHeight(tick) + topPadding + bottomPadding;
	}
	
	public final synchronized Dimension getOuterSize(long tick) {
		return new Dimension(getOuterWidth(tick), getOuterHeight(tick));
	}
	
	public final synchronized int getHorizontalAlignment() { return horizontalAlignment; }
	public final synchronized int getVerticalAlignment() { return verticalAlignment; }
	
	public final synchronized void setHorizontalAlignment(int halign) { horizontalAlignment = halign; }
	public final synchronized void setVerticalAlignment(int valign) { verticalAlignment = valign; }
	
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
			case NORTH_WEST: case WEST: case SOUTH_WEST: horizontalAlignment = LEFT; break;
			case NORTH_EAST: case EAST: case SOUTH_EAST: horizontalAlignment = RIGHT; break;
			default: horizontalAlignment = CENTER; break;
		}
		switch (alignment) {
			case NORTH_WEST: case NORTH: case NORTH_EAST: verticalAlignment = TOP; break;
			case SOUTH_WEST: case SOUTH: case SOUTH_EAST: verticalAlignment = BOTTOM; break;
			default: verticalAlignment = CENTER; break;
		}
	}
	
	public final synchronized void setAlignment(int halign, int valign) {
		horizontalAlignment = halign;
		verticalAlignment = valign;
	}
	
	public final synchronized int getBackgroundColor() { return backgroundColor; }
	public final synchronized void setBackgroundColor(int bgcolor) { backgroundColor = bgcolor; }
	
	protected abstract void paintContent(Graphics2D g, int x, int y, long tick);
	
	public final synchronized void paint(Graphics2D g, int x, int y, int width, int height, long tick) {
		g.setColor(new Color(backgroundColor, true));
		g.fillRect(x, y, width, height);
		switch (horizontalAlignment) {
			case LEFT: x += leftPadding; break;
			case RIGHT: x += leftPadding + (width - getOuterWidth(tick)); break;
			default: x += leftPadding + (width - getOuterWidth(tick)) / 2; break;
		}
		switch (verticalAlignment) {
			case TOP: y += topPadding; break;
			case BOTTOM: y += topPadding + (height - getOuterHeight(tick)); break;
			default: y += topPadding + (height - getOuterHeight(tick)) / 2; break;
		}
		paintContent(g, x, y, tick);
	}
	
	public abstract long getUpdateInterval();
}
