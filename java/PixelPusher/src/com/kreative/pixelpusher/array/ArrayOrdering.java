package com.kreative.pixelpusher.array;

public enum ArrayOrdering {
	LTR_TTB              (false, false, false, false),
	TTB_LTR              (false, false, true , false),
	RTL_TTB              (false, true , false, false),
	TTB_RTL              (false, true , true , false),
	LTR_BTT              (true , false, false, false),
	BTT_LTR              (true , false, true , false),
	RTL_BTT              (true , true , false, false),
	BTT_RTL              (true , true , true , false),
	LTR_TTB_BOUSTROPHEDON(false, false, false, true ),
	TTB_LTR_BOUSTROPHEDON(false, false, true , true ),
	RTL_TTB_BOUSTROPHEDON(false, true , false, true ),
	TTB_RTL_BOUSTROPHEDON(false, true , true , true ),
	LTR_BTT_BOUSTROPHEDON(true , false, false, true ),
	BTT_LTR_BOUSTROPHEDON(true , false, true , true ),
	RTL_BTT_BOUSTROPHEDON(true , true , false, true ),
	BTT_RTL_BOUSTROPHEDON(true , true , true , true );
	
	private final boolean bottomToTop;
	private final boolean rightToLeft;
	private final boolean vertical;
	private final boolean boustrophedon;
	
	private ArrayOrdering(
		boolean bottomToTop,
		boolean rightToLeft,
		boolean vertical,
		boolean boustrophedon
	) {
		this.bottomToTop = bottomToTop;
		this.rightToLeft = rightToLeft;
		this.vertical = vertical;
		this.boustrophedon = boustrophedon;
	}
	
	public int[] getYX(int rows, int columns, int index, int yx[]) {
		int y, x;
		if (vertical) {
			x = (index / rows) % columns;
			y = (index % rows);
			if (boustrophedon && ((x & 1) != 0)) {
				y = (rows-1) - y;
			}
		} else {
			y = (index / columns) % rows;
			x = (index % columns);
			if (boustrophedon && ((y & 1) != 0)) {
				x = (columns-1) - x;
			}
		}
		if (bottomToTop) {
			y = (rows-1) - y;
		}
		if (rightToLeft) {
			x = (columns-1) - x;
		}
		if (yx == null) {
			return new int[]{y,x};
		} else {
			yx[0] = y;
			yx[1] = x;
			return yx;
		}
	}
	
	public int getIndex(int rows, int columns, int y, int x) {
		y %= rows;
		x %= columns;
		if (bottomToTop) {
			y = (rows-1) - y;
		}
		if (rightToLeft) {
			x = (columns-1) - x;
		}
		if (vertical) {
			if (boustrophedon && ((x & 1) != 0)) {
				y = (rows-1) - y;
			}
			return x * rows + y;
		} else {
			if (boustrophedon && ((y & 1) != 0)) {
				x = (columns-1) - x;
			}
			return y * columns + x;
		}
	}
	
	@Override
	public String toString() {
		String verticalDirection = bottomToTop ? "Bottom to Top" : "Top to Bottom";
		String horizontalDirection = rightToLeft ? "Right to Left" : "Left to Right";
		String lineDirection = vertical ? verticalDirection : horizontalDirection;
		String pageDirection = vertical ? horizontalDirection : verticalDirection;
		String direction = lineDirection + ", " + pageDirection;
		if (boustrophedon) direction += ", Boustrophedon";
		return direction;
	}
}
