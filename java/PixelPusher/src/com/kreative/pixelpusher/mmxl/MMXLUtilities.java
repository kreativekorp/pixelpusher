package com.kreative.pixelpusher.mmxl;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.SwingConstants;

public class MMXLUtilities {
	private MMXLUtilities() {}
	
	public static void drawBulb(Graphics g, int x, int y, int position, int color) {
		int[] sx;
		int[] sy;
		
		switch (position) {
		case SwingConstants.NORTH_WEST:
			x--;
			y--;
			break;
		case SwingConstants.WEST:
		case SwingConstants.SOUTH_WEST:
			x--;
			break;
		case SwingConstants.NORTH:
		case SwingConstants.NORTH_EAST:
			y--;
			break;
		}
		
		switch (position) {
		case SwingConstants.NORTH:
		case SwingConstants.SOUTH:
			sx = new int[]{x+16, x+19, x+19, x+16, x+13, x+13, x+16};
			sy = new int[]{y+5, y+8, y+24, y+27, y+24, y+8, y+5};
			break;
		case SwingConstants.EAST:
		case SwingConstants.WEST:
			sx = new int[]{x+5, x+8, x+24, x+27, x+24, x+8, x+5};
			sy = new int[]{y+16, y+13, y+13, y+16, y+19, y+19, y+16};
			break;
		case SwingConstants.NORTH_WEST:
		case SwingConstants.SOUTH_EAST:
			sx = new int[]{x+7, x+11, x+25, x+25, x+21, x+7, x+7};
			sy = new int[]{y+7, y+7, y+21, y+25, y+25, y+11, y+7};
			break;
		case SwingConstants.NORTH_EAST:
		case SwingConstants.SOUTH_WEST:
			sx = new int[]{x+25, x+25, x+11, x+7, x+7, x+21, x+25};
			sy = new int[]{y+7, y+11, y+25, y+25, y+21, y+7, y+7};
			break;
		default:
			sx = new int[]{x+8, x+24, x+24, x+8, x+8};
			sy = new int[]{y+8, y+8, y+24, y+24, y+8};
			break;
		}
		
		g.setColor(new Color(color, true));
		g.fillPolygon(sx, sy, sx.length);
		int na = 255 - (255 - ((color >> 24) & 0xFF)) * 3 / 4;
		int nr = ((color >> 16) & 0xFF) * 3 / 4;
		int ng = ((color >> 8) & 0xFF) * 3 / 4;
		int nb = ((color >> 0) & 0xFF) * 3 / 4;
		int nc = (na << 24) | (nr << 16) | (ng << 8) | (nb << 0);
		g.setColor(new Color(nc, true));
		g.drawPolygon(sx, sy, sx.length);
		
		switch (position) {
		case SwingConstants.NORTH:
			sx = new int[]{x+12, x+20, x+20, x+12, x+12};
			sy = new int[]{y, y, y+8, y+8, y};
			break;
		case SwingConstants.SOUTH:
			sx = new int[]{x+12, x+20, x+20, x+12, x+12};
			sy = new int[]{y+24, y+24, y+32, y+32, y+24};
			break;
		case SwingConstants.WEST:
			sx = new int[]{x, x+8, x+8, x, x};
			sy = new int[]{y+12, y+12, y+20, y+20, y+12};
			break;
		case SwingConstants.EAST:
			sx = new int[]{x+24, x+32, x+32, x+24, x+24};
			sy = new int[]{y+12, y+12, y+20, y+20, y+12};
			break;
		case SwingConstants.NORTH_WEST:
			sx = new int[]{x, x+6, x+14, x+8, x, x};
			sy = new int[]{y, y, y+8, y+14, y+6, y};
			break;
		case SwingConstants.NORTH_EAST:
			sx = new int[]{x+32, x+32, x+24, x+18, x+26, x+32};
			sy = new int[]{y, y+6, y+14, y+8, y, y};
			break;
		case SwingConstants.SOUTH_WEST:
			sx = new int[]{x, x, x+8, x+14, x+6, x};
			sy = new int[]{y+32, y+26, y+18, y+24, y+32, y+32};
			break;
		case SwingConstants.SOUTH_EAST:
			sx = new int[]{x+32, x+26, x+18, x+24, x+32, x+32};
			sy = new int[]{y+32, y+32, y+24, y+18, y+26, y+32};
			break;
		default:
			return;
		}
		
		g.setColor(new Color(0xFF009933));
		g.fillPolygon(sx, sy, sx.length);
		g.setColor(new Color(0xFF006600));
		g.drawPolygon(sx, sy, sx.length);
	}
}
