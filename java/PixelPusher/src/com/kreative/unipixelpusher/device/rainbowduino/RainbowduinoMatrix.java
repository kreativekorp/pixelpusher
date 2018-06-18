package com.kreative.unipixelpusher.device.rainbowduino;

import java.io.IOException;
import java.io.OutputStream;
import com.kreative.unipixelpusher.AbstractDeviceMatrix;
import com.kreative.unipixelpusher.StringType;

public class RainbowduinoMatrix extends AbstractDeviceMatrix {
	private RainbowduinoDevice rbd;
	private OutputStream out;
	private int[][] uppBuffer;
	private int[][] rbdBuffer;
	
	public RainbowduinoMatrix(RainbowduinoDevice parent, OutputStream out) {
		super(parent);
		this.rbd = parent;
		this.out = out;
		this.uppBuffer = new int[8][8];
		this.rbdBuffer = new int[8][8];
		loadConfig(parent.id());
	}
	
	@Override
	public void finalize() {
		try { out.close(); }
		catch (IOException e) {}
	}
	
	@Override
	public String id() {
		return rbd.id();
	}
	
	@Override
	public String name() {
		return rbd.name();
	}
	
	@Override
	public void setName(String name) {
		rbd.setName(name);
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
			RainbowduinoProtocol protocol = rbd.getProtocol();
			out.write(protocol.encodeFrame(rbdBuffer));
			Thread.sleep(protocol.getFrameDelay());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
