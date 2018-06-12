package com.kreative.unipixelpusher.device.rainbowduino;

import java.io.ByteArrayOutputStream;

public interface RainbowduinoProtocol {
	public static final CommandMode COMMAND_MODE = new CommandMode();
	public static final DirectMode2 DIRECT_MODE_2 = new DirectMode2();
	public static final DirectMode3 DIRECT_MODE_3 = new DirectMode3();
	public static final RainbowDashboard2 RAINBOW_DASHBOARD_2 = new RainbowDashboard2();
	public static final RainbowDashboard3 RAINBOW_DASHBOARD_3 = new RainbowDashboard3();
	public static final RainbowduinoProtocol[] PROTOCOLS = {
		COMMAND_MODE,
		DIRECT_MODE_2,
		DIRECT_MODE_3,
		RAINBOW_DASHBOARD_2,
		RAINBOW_DASHBOARD_3,
	};
	
	public byte[] encodeFrame(int[][] pixels);
	public long getFrameDelay();
	
	public static class CommandMode implements RainbowduinoProtocol {
		@Override
		public byte[] encodeFrame(int[][] pixels) {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			for (int y = 0; y < 8; y++) {
				for (int x = 0; x < 8; x++) {
					int c = pixels[y][x];
					int a = ((c >> 24) & 0xFF);
					int r = ((c >> 16) & 0xFF) * a / 4335;
					int g = ((c >>  8) & 0xFF) * a / 4335;
					int b = ((c >>  0) & 0xFF) * a / 4335;
					out.write('R');
					out.write(4);
					out.write((x << 4) | r);
					out.write((g << 4) | b);
					out.write(y);
				}
			}
			return out.toByteArray();
		}
		@Override
		public long getFrameDelay() {
			return 280;
		}
		@Override
		public String toString() {
			return "CommandMode";
		}
	}
	
	public static class DirectMode2 implements RainbowduinoProtocol {
		@Override
		public byte[] encodeFrame(int[][] pixels) {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			for (int y = 0; y < 8; y++) {
				for (int x = 0; x < 8; x += 2) {
					int c1 = pixels[y][x];
					int a1 = ((c1 >> 24) & 0xFF);
					int g1 = ((c1 >>  8) & 0xFF) * a1 / 4335;
					int c2 = pixels[y][x + 1];
					int a2 = ((c2 >> 24) & 0xFF);
					int g2 = ((c2 >>  8) & 0xFF) * a2 / 4335;
					out.write((g1 << 4) | g2);
				}
			}
			for (int y = 0; y < 8; y++) {
				for (int x = 0; x < 8; x += 2) {
					int c1 = pixels[y][x];
					int a1 = ((c1 >> 24) & 0xFF);
					int r1 = ((c1 >> 16) & 0xFF) * a1 / 4335;
					int c2 = pixels[y][x + 1];
					int a2 = ((c2 >> 24) & 0xFF);
					int r2 = ((c2 >> 16) & 0xFF) * a2 / 4335;
					out.write((r1 << 4) | r2);
				}
			}
			for (int y = 0; y < 8; y++) {
				for (int x = 0; x < 8; x += 2) {
					int c1 = pixels[y][x];
					int a1 = ((c1 >> 24) & 0xFF);
					int b1 = ((c1 >>  0) & 0xFF) * a1 / 4335;
					int c2 = pixels[y][x + 1];
					int a2 = ((c2 >> 24) & 0xFF);
					int b2 = ((c2 >>  0) & 0xFF) * a2 / 4335;
					out.write((b1 << 4) | b2);
				}
			}
			return out.toByteArray();
		}
		@Override
		public long getFrameDelay() {
			return 100;
		}
		@Override
		public String toString() {
			return "DirectMode 2.0";
		}
	}
	
	public static class DirectMode3 implements RainbowduinoProtocol {
		@Override
		public byte[] encodeFrame(int[][] pixels) {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			for (int y = 0; y < 8; y++) {
				for (int x = 0; x < 8; x++) {
					int c = pixels[y][x];
					int a = ((c >> 24) & 0xFF);
					int b = ((c >>  0) & 0xFF) * a / 255;
					out.write(b);
				}
			}
			for (int y = 0; y < 8; y++) {
				for (int x = 0; x < 8; x++) {
					int c = pixels[y][x];
					int a = ((c >> 24) & 0xFF);
					int g = ((c >>  8) & 0xFF) * a / 255;
					out.write(g);
				}
			}
			for (int y = 0; y < 8; y++) {
				for (int x = 0; x < 8; x++) {
					int c = pixels[y][x];
					int a = ((c >> 24) & 0xFF);
					int r = ((c >> 16) & 0xFF) * a / 255;
					out.write(r);
				}
			}
			return out.toByteArray();
		}
		@Override
		public long getFrameDelay() {
			return 180;
		}
		@Override
		public String toString() {
			return "DirectMode 3.0";
		}
	}
	
	public static class RainbowDashboard2 implements RainbowduinoProtocol {
		@Override
		public byte[] encodeFrame(int[][] pixels) {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			out.write('D');
			for (int y = 0; y < 8; y++) {
				for (int x = 0; x < 8; x += 2) {
					int c1 = pixels[y][x];
					int a1 = ((c1 >> 24) & 0xFF);
					int g1 = ((c1 >>  8) & 0xFF) * a1 / 4335;
					int c2 = pixels[y][x + 1];
					int a2 = ((c2 >> 24) & 0xFF);
					int g2 = ((c2 >>  8) & 0xFF) * a2 / 4335;
					out.write((g1 << 4) | g2);
				}
			}
			for (int y = 0; y < 8; y++) {
				for (int x = 0; x < 8; x += 2) {
					int c1 = pixels[y][x];
					int a1 = ((c1 >> 24) & 0xFF);
					int r1 = ((c1 >> 16) & 0xFF) * a1 / 4335;
					int c2 = pixels[y][x + 1];
					int a2 = ((c2 >> 24) & 0xFF);
					int r2 = ((c2 >> 16) & 0xFF) * a2 / 4335;
					out.write((r1 << 4) | r2);
				}
			}
			for (int y = 0; y < 8; y++) {
				for (int x = 0; x < 8; x += 2) {
					int c1 = pixels[y][x];
					int a1 = ((c1 >> 24) & 0xFF);
					int b1 = ((c1 >>  0) & 0xFF) * a1 / 4335;
					int c2 = pixels[y][x + 1];
					int a2 = ((c2 >> 24) & 0xFF);
					int b2 = ((c2 >>  0) & 0xFF) * a2 / 4335;
					out.write((b1 << 4) | b2);
				}
			}
			return out.toByteArray();
		}
		@Override
		public long getFrameDelay() {
			return 100;
		}
		@Override
		public String toString() {
			return "RainbowDashboard 2.0";
		}
	}
	
	public static class RainbowDashboard3 implements RainbowduinoProtocol {
		@Override
		public byte[] encodeFrame(int[][] pixels) {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			out.write('3');
			for (int y = 0; y < 8; y++) {
				for (int x = 0; x < 8; x++) {
					int c = pixels[y][x];
					int a = ((c >> 24) & 0xFF);
					int b = ((c >>  0) & 0xFF) * a / 255;
					out.write(b);
				}
			}
			for (int y = 0; y < 8; y++) {
				for (int x = 0; x < 8; x++) {
					int c = pixels[y][x];
					int a = ((c >> 24) & 0xFF);
					int g = ((c >>  8) & 0xFF) * a / 255;
					out.write(g);
				}
			}
			for (int y = 0; y < 8; y++) {
				for (int x = 0; x < 8; x++) {
					int c = pixels[y][x];
					int a = ((c >> 24) & 0xFF);
					int r = ((c >> 16) & 0xFF) * a / 255;
					out.write(r);
				}
			}
			return out.toByteArray();
		}
		@Override
		public long getFrameDelay() {
			return 180;
		}
		@Override
		public String toString() {
			return "RainbowDashboard 3.0";
		}
	}
}
