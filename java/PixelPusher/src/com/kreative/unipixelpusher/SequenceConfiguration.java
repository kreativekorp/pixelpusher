package com.kreative.unipixelpusher;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.imageio.ImageIO;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import com.kreative.imagetool.animation.Animation;
import com.kreative.imagetool.animation.AnimationFrame;

public class SequenceConfiguration {
	private final Map<String,String> config;
	
	public SequenceConfiguration() {
		config = new TreeMap<String,String>();
	}
	
	public String get(String key) {
		return config.get(key);
	}
	
	public String get(String key, String def) {
		String value = config.get(key);
		if (value == null) return def;
		return value;
	}
	
	public int get(String key, int def) {
		String value = config.get(key);
		if (value == null) return def;
		try { return Integer.parseInt(value); }
		catch (NumberFormatException e) { return def; }
	}
	
	public long get(String key, long def) {
		String value = config.get(key);
		if (value == null) return def;
		try { return Long.parseLong(value); }
		catch (NumberFormatException e) { return def; }
	}
	
	public float get(String key, float def) {
		String value = config.get(key);
		if (value == null) return def;
		try { return Float.parseFloat(value); }
		catch (NumberFormatException e) { return def; }
	}
	
	public double get(String key, double def) {
		String value = config.get(key);
		if (value == null) return def;
		try { return Double.parseDouble(value); }
		catch (NumberFormatException e) { return def; }
	}
	
	public boolean get(String key, boolean def) {
		String value = config.get(key);
		if (value == null) return def;
		if (value.equalsIgnoreCase("true")) return true;
		if (value.equalsIgnoreCase("false")) return false;
		return def;
	}
	
	public <E extends Enum<E>> E get(String key, Class<E> enumType, E def) {
		String value = config.get(key);
		if (value == null) return def;
		try { return Enum.valueOf(enumType, value); }
		catch (IllegalArgumentException e) { return def; }
	}
	
	public <C> Class<? extends C> get(String key, Class<C> cls) {
		String value = config.get(key);
		if (value == null) return cls;
		try { return Class.forName(value).asSubclass(cls); }
		catch (Exception e) { return cls; }
	}
	
	public int[] get(String key, int[] def) {
		String value = config.get(key);
		if (value == null) {
			return def;
		} else if (value.length() == 0) {
			return new int[0];
		} else try {
			String[] strings = value.split(",");
			int[] ints = new int[strings.length];
			for (int i = 0; i < strings.length; i++) {
				ints[i] = Integer.parseInt(strings[i].trim());
			}
			return ints;
		} catch (NumberFormatException e) {
			return def;
		}
	}
	
	public int[][] get(String key, int[][] def) {
		String value = config.get(key);
		if (value == null) {
			return def;
		} else if (value.length() == 0) {
			return new int[0][0];
		} else try {
			String[] rowStrings = value.split(";");
			int[][] rows = new int[rowStrings.length][];
			for (int row = 0; row < rowStrings.length; row++) {
				String[] colStrings = rowStrings[row].split(",");
				int[] cols = new int[colStrings.length];
				for (int col = 0; col < colStrings.length; col++) {
					cols[col] = Integer.parseInt(colStrings[col].trim());
				}
				rows[row] = cols;
			}
			return rows;
		} catch (NumberFormatException e) {
			return def;
		}
	}
	
	public byte[] get(String key, byte[] def) {
		String value = config.get(key);
		if (value == null) {
			return def;
		} else if (value.length() == 0) {
			return new byte[0];
		} else try {
			ASCII85InputStream in = new ASCII85InputStream(value);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			byte[] buf = new byte[65536]; int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			in.close();
			out.close();
			return out.toByteArray();
		} catch (IOException e) {
			return def;
		}
	}
	
	public BufferedImage get(String key, BufferedImage def) {
		byte[] data = get(key, (byte[])null);
		if (data == null) {
			return def;
		} else try {
			ByteArrayInputStream in = new ByteArrayInputStream(data);
			BufferedImage image = ImageIO.read(in);
			in.close();
			if (image == null) return def;
			return image;
		} catch (IOException e) {
			return def;
		}
	}
	
	public Animation get(String key, Animation def) {
		int[] header = get(key, (int[])null);
		if (header == null || header.length != 3) {
			return def;
		} else {
			Animation a = new Animation(header[0], header[1]);
			for (int i = 0; i < header[2]; i++) {
				String is = Integer.toString(i);
				while (is.length() < 6) is = "0" + is;
				BufferedImage image = get(key + ".f" + is, (BufferedImage)null);
				double duration = get(key + ".d" + is, 1.0);
				a.frames.add(new AnimationFrame(image, duration));
			}
			return a;
		}
	}
	
	public void put(String key, String value) {
		config.put(key, value);
	}
	
	public void put(String key, int value) {
		config.put(key, Integer.toString(value));
	}
	
	public void put(String key, long value) {
		config.put(key, Long.toString(value));
	}
	
	public void put(String key, float value) {
		config.put(key, Float.toString(value));
	}
	
	public void put(String key, double value) {
		config.put(key, Double.toString(value));
	}
	
	public void put(String key, boolean value) {
		config.put(key, value ? "true" : "false");
	}
	
	public void put(String key, Enum<?> value) {
		config.put(key, (value != null) ? value.name() : null);
	}
	
	public void put(String key, Class<?> value) {
		config.put(key, (value != null) ? value.getName() : null);
	}
	
	public void put(String key, int[] value) {
		if (value == null) {
			config.put(key, null);
		} else if (value.length == 0) {
			config.put(key, "");
		} else {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < value.length; i++) {
				if (i > 0) sb.append(", ");
				sb.append(value[i]);
			}
			config.put(key, sb.toString());
		}
	}
	
	public void put(String key, int[][] value) {
		if (value == null) {
			config.put(key, null);
		} else if (value.length == 0) {
			config.put(key, "");
		} else {
			StringBuffer sb = new StringBuffer();
			for (int row = 0; row < value.length; row++) {
				if (row > 0) sb.append("; ");
				for (int col = 0; col < value[row].length; col++) {
					if (col > 0) sb.append(", ");
					sb.append(value[row][col]);
				}
			}
			config.put(key, sb.toString());
		}
	}
	
	public void put(String key, byte[] value) {
		if (value == null) {
			config.put(key, null);
		} else if (value.length == 0) {
			config.put(key, "");
		} else {
			StringBuffer sb = new StringBuffer();
			try {
				ASCII85OutputStream out = new ASCII85OutputStream(sb, true, true, true, true);
				out.write(value);
				out.close();
			} catch (IOException e) {
				// ignored
			}
			config.put(key, sb.toString());
		}
	}
	
	public void put(String key, BufferedImage value) {
		if (value == null) {
			config.put(key, null);
		} else try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ImageIO.write(value, "png", out);
			out.close();
			put(key, out.toByteArray());
		} catch (IOException e) {
			config.put(key, null);
		}
	}
	
	public void put(String key, Animation value) {
		if (value == null) {
			config.put(key, null);
		} else {
			int w = value.width;
			int h = value.height;
			int n = value.frames.size();
			put(key, new int[]{ w, h, n });
			for (int i = 0; i < n; i++) {
				AnimationFrame f = value.frames.get(i);
				String is = Integer.toString(i);
				while (is.length() < 6) is = "0" + is;
				put(key + ".f" + is, f.image);
				put(key + ".d" + is, f.duration);
			}
		}
	}
	
	public void read(List<Node> children) throws IOException {
		for (Node child : children) {
			String type = child.getNodeName();
			if (type.equalsIgnoreCase("param")) {
				NamedNodeMap attr = child.getAttributes();
				if (attr == null) continue;
				Node node = attr.getNamedItem("key");
				if (node == null) continue;
				String text = node.getTextContent();
				if (text == null) continue;
				String key = text.trim();
				String value = child.getTextContent();
				config.put(key, value);
			} else {
				throw new IOException("Unknown element: " + type);
			}
		}
	}
	
	public void write(PrintWriter out, String prefix) throws IOException {
		for (Map.Entry<String,String> e : config.entrySet()) {
			if (e.getValue() == null) {
				out.println(prefix + "<param key=\"" + xmls(e.getKey()) + "\"/>");
			} else {
				out.println(prefix + "<param key=\"" + xmls(e.getKey()) + "\">" + xmls(e.getValue()) + "</param>");
			}
		}
	}
	
	private static String xmls(String s) {
		return s.replaceAll("&", "&amp;")
		        .replaceAll("<", "&lt;")
		        .replaceAll(">", "&gt;")
		        .replaceAll("\"", "&quot;");
	}
}
