package com.kreative.unipixelpusher.device.rainbowduino;

import java.io.OutputStream;
import com.kreative.unipixelpusher.AbstractPixelMatrix;
import com.kreative.unipixelpusher.StringType;

public class RainbowduinoMatrix extends AbstractPixelMatrix.WithGammaCurve {
	private OutputStream out;
	private int[][] uppBuffer;
	private int[][] rbdBuffer;
	private RainbowduinoProtocol protocol;
	
	public RainbowduinoMatrix(OutputStream out, RainbowduinoProtocol protocol) {
		this.out = out;
		this.uppBuffer = new int[8][8];
		this.rbdBuffer = new int[8][8];
		this.protocol = protocol;
	}
	
	public RainbowduinoProtocol getProtocol() {
		return this.protocol;
	}
	
	public void setProtocol(RainbowduinoProtocol protocol) {
		this.protocol = protocol;
	}
	
	@Override
	public StringType type() {
		return StringType.LED_MATRIX_MODULE;
	}
	
	@Override
	public int getRowCount() {
		return 8;
	}
	
	@Override
	public int getColumnCount() {
		return 8;
	}
	
	@Override
	protected int getPixelImpl(int row, int col) {
		return (row >= 0 && row < 8 && col >= 0 && col < 8) ? uppBuffer[row][col] : 0;
	}
	
	@Override
	protected void setPixelImpl(int row, int col, int color) {
		if (row >= 0 && row < 8 && col >= 0 && col < 8) uppBuffer[row][col] = color;
	}
	
	@Override
	public void push() {
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				rbdBuffer[y][x] = correct(uppBuffer[y][x]);
			}
		}
		try {
			out.write(protocol.encodeFrame(rbdBuffer));
			Thread.sleep(protocol.getFrameDelay());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
