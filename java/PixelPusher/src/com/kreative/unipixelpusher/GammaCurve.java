package com.kreative.unipixelpusher;

public interface GammaCurve {
	public static final GammaCurve LINEAR = new Linear();
	public static final GammaCurve QUADRATIC = new Power(2.0, "Quadratic");
	public static final GammaCurve CUBIC = new Power(3.0, "Cubic");
	public static final GammaCurve QUASICUBIC_I = new Quasipower(3.0, 1.0, 1.0, "Quasicubic I");
	public static final GammaCurve QUASICUBIC_II = new Quasipower(3.0, 1.0, 2.0, "Quasicubic II");
	
	public static final GammaCurve[] VALUES = {
		LINEAR, QUADRATIC,
		new Power(2.1, "2.1"),
		new Power(2.2, "2.2"),
		new Power(2.3, "2.3"),
		new Power(2.4, "2.4"),
		new Power(2.5, "2.5"),
		new Power(2.6, "2.6"),
		new Power(2.7, "2.7"),
		new Power(2.8, "2.8"),
		new Power(2.9, "2.9"),
		CUBIC, QUASICUBIC_I, QUASICUBIC_II
	};
	
	public abstract int correct(int color);
	
	public static final class Linear implements GammaCurve {
		@Override
		public int correct(int color) {
			return color;
		}
		
		@Override
		public String toString() {
			return "Linear";
		}
	}
	
	public static final class Power implements GammaCurve {
		public final double power;
		public final String name;
		
		public Power(double power, String name) {
			this.power = power;
			this.name = name;
		}
		
		@Override
		public int correct(int color) {
			int a = (int)Math.round(255.0 * Math.pow(((color >> 24) & 0xFF) / 255.0, power));
			int r = (int)Math.round(255.0 * Math.pow(((color >> 16) & 0xFF) / 255.0, power));
			int g = (int)Math.round(255.0 * Math.pow(((color >>  8) & 0xFF) / 255.0, power));
			int b = (int)Math.round(255.0 * Math.pow(((color >>  0) & 0xFF) / 255.0, power));
			return (a << 24) | (r << 16) | (g << 8) | (b << 0);
		}
		
		@Override
		public String toString() {
			return name;
		}
	}
	
	public static final class Quasipower implements GammaCurve {
		public final double power;
		public final double p1;
		public final double p2;
		public final String name;
		
		public Quasipower(double power, double p1, double p2, String name) {
			this.power = power;
			this.p1 = p1;
			this.p2 = p2;
			this.name = name;
		}
		
		@Override
		public int correct(int color) {
			int    a = ((color >> 24) & 0xFF);
			double r = ((color >> 16) & 0xFF) * a / 65025.0;
			double g = ((color >>  8) & 0xFF) * a / 65025.0;
			double b = ((color >>  0) & 0xFF) * a / 65025.0;
			
			// If someone could explain to me why this works the way it does please let me know.
			// I think it has to do with how blue LEDs require more forward voltage than red LEDs,
			// so when the blue LED is on it takes voltage away from the red LED. Am I close?
			b = Math.pow(b, power);
			g = Math.pow(g, power - p1 * b);
			r = Math.pow(r, power - p2 * Math.max(g, b));
			
			int ri = (int)Math.round(255.0 * r);
			int gi = (int)Math.round(255.0 * g);
			int bi = (int)Math.round(255.0 * b);
			return 0xFF000000 | (ri << 16) | (gi << 8) | (bi << 0);
		}
		
		@Override
		public String toString() {
			return name;
		}
	}
}
