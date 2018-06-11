package com.kreative.unipixelpusher;

public abstract class AbstractPixelMatrix implements PixelString {
	protected WindingOrder windingOrder = WindingOrder.LTR_TTB;
	protected boolean flipHorizontal = false;
	protected boolean flipVertical = false;
	
	@Override
	public int length() {
		return this.getRowCount() * this.getColumnCount();
	}
	
	public WindingOrder getWindingOrder() {
		return this.windingOrder;
	}
	
	public void setWindingOrder(WindingOrder windingOrder) {
		this.windingOrder = windingOrder;
	}
	
	public boolean getFlipHorizontal() {
		return this.flipHorizontal;
	}
	
	public void setFlipHorizontal(boolean flipHorizontal) {
		this.flipHorizontal = flipHorizontal;
	}
	
	public boolean getFlipVertical() {
		return this.flipVertical;
	}
	
	public void setFlipVertical(boolean flipVertical) {
		this.flipVertical = flipVertical;
	}
	
	@Override
	public int getPixel(int i) {
		if (i < 0) return 0;
		int rows = getRowCount(), cols = getColumnCount();
		if (i >= rows * cols) return 0;
		int[] yx = windingOrder.getYX(rows, cols, i, null);
		return getPixel(yx[0], yx[1]);
	}
	
	@Override
	public void setPixel(int i, int color) {
		if (i < 0) return;
		int rows = getRowCount(), cols = getColumnCount();
		if (i >= rows * cols) return;
		int[] yx = windingOrder.getYX(rows, cols, i, null);
		setPixel(yx[0], yx[1], color);
	}
	
	@Override
	public int getPixel(int row, int col) {
		return getPixelImpl(
			flipVertical ? (getRowCount() - row - 1) : row,
			flipHorizontal ? (getColumnCount() - col - 1) : col
		);
	}
	
	@Override
	public void setPixel(int row, int col, int color) {
		setPixelImpl(
			flipVertical ? (getRowCount() - row - 1) : row,
			flipHorizontal ? (getColumnCount() - col - 1) : col,
			color
		);
	}
	
	protected abstract int getPixelImpl(int row, int col);
	protected abstract void setPixelImpl(int row, int col, int color);
	
	public abstract class WithGammaCurve extends AbstractPixelMatrix implements PixelString.WithGammaCurve {
		protected GammaCurve gamma = GammaCurve.LINEAR;
		
		@Override
		public GammaCurve getGammaCurve() {
			return this.gamma;
		}
		
		@Override
		public void setGammaCurve(GammaCurve gamma) {
			this.gamma = gamma;
		}
		
		protected int correct(int color) {
			return (gamma == null) ? color : gamma.correct(color);
		}
	}
}
