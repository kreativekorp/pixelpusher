package com.kreative.pixelpusher.device;

import java.io.Serializable;
import com.heroicrobot.dropbit.devices.pixelpusher.Pixel;

public abstract class GammaCorrector implements Cloneable, Serializable {
	private static final long serialVersionUID = 1L;
	
	@Override public abstract GammaCorrector clone();
	public abstract String getName();
	public abstract void setPixelColor(Pixel pixel, double r, double g, double b);
	
	public void setPixelColor(Pixel pixel, double r, double g, double b, double a) {
		setPixelColor(pixel, r * a, g * a, b * a);
	}
	
	public void setPixelColor(Pixel pixel, int r, int g, int b) {
		setPixelColor(pixel, r / 255.0, g / 255.0, b / 255.0);
	}
	
	public void setPixelColor(Pixel pixel, int r, int g, int b, int a) {
		setPixelColor(pixel, (r * a) / 65025.0, (g * a) / 65025.0, (b * a) / 65025.0);
	}
	
	public void setPixelColor(Pixel pixel, int color) {
		int a = ((color >> 24) & 0xFF);
		int r = ((color >> 16) & 0xFF);
		int g = ((color >>  8) & 0xFF);
		int b = ((color >>  0) & 0xFF);
		setPixelColor(pixel, (r * a) / 65025.0, (g * a) / 65025.0, (b * a) / 65025.0);
	}
	
	public final void setPixelColors(Pixel[] pixels, int pIndex, int[] colors, int cIndex, int count) {
		while (count > 0) {
			if (pixels[pIndex] == null) pixels[pIndex] = new Pixel();
			setPixelColor(pixels[pIndex++], colors[cIndex++]);
			count--;
		}
	}
	
	@Override
	public final String toString() {
		return getName();
	}
	
	public static final GammaCorrector UNCORRECTED = new Uncorrected();
	private static class Uncorrected extends GammaCorrector {
		private static final long serialVersionUID = 1L;
		@Override
		public Uncorrected clone() {
			return new Uncorrected();
		}
		@Override
		public String getName() {
			return "Uncorrected";
		}
		@Override
		public void setPixelColor(Pixel pixel, double r, double g, double b) {
			pixel.blue  = (byte)Math.round(255.0 * b);
			pixel.green = (byte)Math.round(255.0 * g);
			pixel.red   = (byte)Math.round(255.0 * r);
		}
		@Override
		public void setPixelColor(Pixel pixel, double r, double g, double b, double a) {
			pixel.blue  = (byte)Math.round(255.0 * b * a);
			pixel.green = (byte)Math.round(255.0 * g * a);
			pixel.red   = (byte)Math.round(255.0 * r * a);
		}
		@Override
		public void setPixelColor(Pixel pixel, int r, int g, int b) {
			pixel.blue  = (byte)b;
			pixel.green = (byte)g;
			pixel.red   = (byte)r;
		}
		@Override
		public void setPixelColor(Pixel pixel, int r, int g, int b, int a) {
			pixel.blue  = (byte)(b * a / 255);
			pixel.green = (byte)(g * a / 255);
			pixel.red   = (byte)(r * a / 255);
		}
		@Override
		public void setPixelColor(Pixel pixel, int color) {
			int a = ((color >> 24) & 0xFF);
			int r = ((color >> 16) & 0xFF);
			int g = ((color >>  8) & 0xFF);
			int b = ((color >>  0) & 0xFF);
			pixel.blue  = (byte)(b * a / 255);
			pixel.green = (byte)(g * a / 255);
			pixel.red   = (byte)(r * a / 255);
		}
	}
	
	public static final GammaCorrector QUADRATIC = new Quadratic();
	private static class Quadratic extends GammaCorrector {
		private static final long serialVersionUID = 1L;
		@Override
		public Quadratic clone() {
			return new Quadratic();
		}
		@Override
		public String getName() {
			return "Quadratic";
		}
		@Override
		public void setPixelColor(Pixel pixel, double r, double g, double b) {
			pixel.blue  = (byte)Math.round(255.0 * b * b);
			pixel.green = (byte)Math.round(255.0 * g * g);
			pixel.red   = (byte)Math.round(255.0 * r * r);
		}
	}
	
	public static final GammaCorrector CUBIC = new Cubic();
	private static class Cubic extends GammaCorrector {
		private static final long serialVersionUID = 1L;
		@Override
		public Cubic clone() {
			return new Cubic();
		}
		@Override
		public String getName() {
			return "Cubic";
		}
		@Override
		public void setPixelColor(Pixel pixel, double r, double g, double b) {
			pixel.blue  = (byte)Math.round(255.0 * b * b * b);
			pixel.green = (byte)Math.round(255.0 * g * g * g);
			pixel.red   = (byte)Math.round(255.0 * r * r * r);
		}
	}
	
	public static final GammaCorrector QUASICUBIC1 = new Quasicubic1();
	private static class Quasicubic1 extends GammaCorrector {
		private static final long serialVersionUID = 1L;
		@Override
		public Quasicubic1 clone() {
			return new Quasicubic1();
		}
		@Override
		public String getName() {
			return "Quasicubic I";
		}
		@Override
		public void setPixelColor(Pixel pixel, double r, double g, double b) {
			// If someone could explain to me why this works the way it does please let me know.
			// I think it has to do with how blue LEDs require more forward voltage than red LEDs,
			// so when the blue LED is on it takes voltage away from the red LED. Am I close?
			b = Math.pow(b, 3.0);
			g = Math.pow(g, 3.0 - b);
			r = Math.pow(r, 3.0 - Math.max(g, b));
			pixel.blue  = (byte)Math.round(255.0 * b);
			pixel.green = (byte)Math.round(255.0 * g);
			pixel.red   = (byte)Math.round(255.0 * r);
		}
	}
	
	public static final GammaCorrector QUASICUBIC2 = new Quasicubic2();
	private static class Quasicubic2 extends GammaCorrector {
		private static final long serialVersionUID = 1L;
		@Override
		public Quasicubic2 clone() {
			return new Quasicubic2();
		}
		@Override
		public String getName() {
			return "Quasicubic II";
		}
		@Override
		public void setPixelColor(Pixel pixel, double r, double g, double b) {
			// If someone could explain to me why this works the way it does please let me know.
			// I think it has to do with how blue LEDs require more forward voltage than red LEDs,
			// so when the blue LED is on it takes voltage away from the red LED. Am I close?
			b = Math.pow(b, 3.0);
			g = Math.pow(g, 3.0 - b);
			r = Math.pow(r, 3.0 - 2.0 * Math.max(g, b));
			pixel.blue  = (byte)Math.round(255.0 * b);
			pixel.green = (byte)Math.round(255.0 * g);
			pixel.red   = (byte)Math.round(255.0 * r);
		}
	}
	
	public static final GammaCorrector DEFAULT = QUASICUBIC1;
	public static final GammaCorrector[] values() {
		return new GammaCorrector[]{
			UNCORRECTED, QUADRATIC, CUBIC, QUASICUBIC1, QUASICUBIC2
		};
	}
}
