package com.kreative.pixelpusher.array;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Area;
import javax.swing.SwingConstants;

public class PixelArrayWindowListVisualizer extends GenericArrayVisualizer {
	private static final long serialVersionUID = 1L;
	
	public PixelArrayWindowListVisualizer(PixelArray array) {
		super(array);
		WindowMouseListener listener = new WindowMouseListener();
		addMouseListener(listener);
		addMouseMotionListener(listener);
	}
	
	@Override
	public void paintPixelSet(Graphics g, int x, int y, int w, int h, PixelArray array) {
		super.paintPixelSet(g, x, y, w, h, array);
		if (array instanceof PixelArrayWindowList) {
			PixelArrayWindowList windows = (PixelArrayWindowList)array;
			Shape oldClip = g.getClip();
			Area newClip = new Area(new Rectangle(x, y, w & ~0x7, h & ~0x7));
			g.setClip(newClip);
			g.setColor(Color.black);
			for (PixelArrayWindow window : windows) {
				g.drawRect(
					x + window.getLocationX() * 8,
					y + window.getLocationY() * 8,
					window.getWidth() * 8 - 1,
					window.getHeight() * 8 - 1
				);
				newClip.subtract(new Area(new Rectangle(
					x + window.getLocationX() * 8,
					y + window.getLocationY() * 8,
					window.getWidth() * 8,
					window.getHeight() * 8
				)));
				g.setClip(newClip);
			}
			g.setClip(oldClip);
		}
	}
	
	private class WindowMouseListener implements MouseListener, MouseMotionListener {
		private Cursor getCursor(MouseEvent e) {
			if (pixelSet instanceof PixelArrayWindowList) {
				PixelArrayWindowList windows = (PixelArrayWindowList)pixelSet;
				int x = e.getX() / 8;
				int y = e.getY() / 8;
				for (PixelArrayWindow window : windows) {
					if (window.contains(x, y)) {
						if (y == window.getLocationY() + window.getHeight() - 1) {
							if (x == window.getLocationX() + window.getWidth() - 1) {
								return Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR);
							} else if (x == window.getLocationX()) {
								return Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR);
							} else {
								return Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR);
							}
						} else if (y == window.getLocationY()) {
							if (x == window.getLocationX() + window.getWidth() - 1) {
								return Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR);
							} else if (x == window.getLocationX()) {
								return Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR);
							} else {
								return Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR);
							}
						} else {
							if (x == window.getLocationX() + window.getWidth() - 1) {
								return Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR);
							} else if (x == window.getLocationX()) {
								return Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR);
							} else {
								return Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR);
							}
						}
					}
				}
			}
			return null;
		}
		@Override public void mouseEntered(MouseEvent e) { setCursor(getCursor(e)); }
		@Override public void mouseMoved(MouseEvent e) { setCursor(getCursor(e)); }
		@Override public void mouseExited(MouseEvent e) { setCursor(getCursor(e)); }
		
		private PixelArrayWindow windowClicked = null;
		private int windowRegion = SwingConstants.CENTER;
		private int xdiff, ydiff, wdiff, hdiff, wdiff2, hdiff2;
		@Override public void mousePressed(MouseEvent e) {
			windowClicked = null;
			windowRegion = SwingConstants.CENTER;
			if (pixelSet instanceof PixelArrayWindowList) {
				PixelArrayWindowList windows = (PixelArrayWindowList)pixelSet;
				int x = e.getX() / 8;
				int y = e.getY() / 8;
				for (PixelArrayWindow window : windows) {
					if (window.contains(x, y)) {
						windowClicked = window;
						if (y == window.getLocationY() + window.getHeight() - 1) {
							if (x == window.getLocationX() + window.getWidth() - 1) {
								windowRegion = SwingConstants.SOUTH_EAST;
							} else if (x == window.getLocationX()) {
								windowRegion = SwingConstants.SOUTH_WEST;
							} else {
								windowRegion = SwingConstants.SOUTH;
							}
						} else if (y == window.getLocationY()) {
							if (x == window.getLocationX() + window.getWidth() - 1) {
								windowRegion = SwingConstants.NORTH_EAST;
							} else if (x == window.getLocationX()) {
								windowRegion = SwingConstants.NORTH_WEST;
							} else {
								windowRegion = SwingConstants.NORTH;
							}
						} else {
							if (x == window.getLocationX() + window.getWidth() - 1) {
								windowRegion = SwingConstants.EAST;
							} else if (x == window.getLocationX()) {
								windowRegion = SwingConstants.WEST;
							} else {
								windowRegion = SwingConstants.CENTER;
							}
						}
						xdiff = x - window.getLocationX();
						ydiff = y - window.getLocationY();
						wdiff = x - window.getWidth();
						hdiff = y - window.getHeight();
						wdiff2 = x + window.getWidth();
						hdiff2 = y + window.getHeight();
						return;
					}
				}
			}
		}
		@Override public void mouseDragged(MouseEvent e) {
			if (windowClicked != null) {
				int x = e.getX() / 8;
				int y = e.getY() / 8;
				switch (windowRegion) {
					case SwingConstants.CENTER:
						windowClicked.setLocation(Math.max(0, x - xdiff), Math.max(0, y - ydiff));
						break;
					case SwingConstants.EAST:
						windowClicked.setWidth(Math.max(2, x - wdiff));
						break;
					case SwingConstants.SOUTH:
						windowClicked.setHeight(Math.max(2, y - hdiff));
						break;
					case SwingConstants.SOUTH_EAST:
						windowClicked.setSize(Math.max(2, x - wdiff), Math.max(2, y - hdiff));
						break;
					case SwingConstants.WEST:
						if (x < 0) x = 0;
						if (wdiff2 - x < 2) x = wdiff2 - 2;
						windowClicked.setLocationX(x - xdiff);
						windowClicked.setWidth(wdiff2 - x);
						break;
					case SwingConstants.NORTH:
						if (y < 0) y = 0;
						if (hdiff2 - y < 2) y = hdiff2 - 2;
						windowClicked.setLocationY(y - ydiff);
						windowClicked.setHeight(hdiff2 - y);
						break;
					case SwingConstants.NORTH_WEST:
						if (x < 0) x = 0;
						if (y < 0) y = 0;
						if (wdiff2 - x < 2) x = wdiff2 - 2;
						if (hdiff2 - y < 2) y = hdiff2 - 2;
						windowClicked.setLocation(x - xdiff, y - ydiff);
						windowClicked.setSize(wdiff2 - x, hdiff2 - y);
						break;
					case SwingConstants.SOUTH_WEST:
						if (x < 0) x = 0;
						if (wdiff2 - x < 2) x = wdiff2 - 2;
						windowClicked.setLocationX(x - xdiff);
						windowClicked.setSize(wdiff2 - x, Math.max(2, y - hdiff));
						break;
					case SwingConstants.NORTH_EAST:
						if (y < 0) y = 0;
						if (hdiff2 - y < 2) y = hdiff2 - 2;
						windowClicked.setLocationY(y - ydiff);
						windowClicked.setSize(Math.max(2, x - wdiff), hdiff2 - y);
						break;
				}
			}
		}
		@Override public void mouseReleased(MouseEvent e) { mouseDragged(e); }
		@Override public void mouseClicked(MouseEvent e) {}
	}
}
