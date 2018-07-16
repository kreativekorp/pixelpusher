package com.kreative.unipixelpusher;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class DeviceConfiguration {
	private Map<String,Map<String,String>> config;
	
	public void clear() {
		this.config = null;
	}
	
	public boolean isEmpty() {
		return (config == null || config.isEmpty());
	}
	
	public String get(String id, String key) {
		if (config == null) return null;
		Map<String,String> subcon = config.get(id);
		if (subcon == null) return null;
		return subcon.get(key);
	}
	
	public String get(String id, String key, String def) {
		String value = get(id, key);
		if (value == null) return def;
		return value;
	}
	
	public int get(String id, String key, int def) {
		String value = get(id, key);
		if (value == null) return def;
		try { return Integer.parseInt(value); }
		catch (NumberFormatException e) { return def; }
	}
	
	public long get(String id, String key, long def) {
		String value = get(id, key);
		if (value == null) return def;
		try { return Long.parseLong(value); }
		catch (NumberFormatException e) { return def; }
	}
	
	public float get(String id, String key, float def) {
		String value = get(id, key);
		if (value == null) return def;
		try { return Float.parseFloat(value); }
		catch (NumberFormatException e) { return def; }
	}
	
	public double get(String id, String key, double def) {
		String value = get(id, key);
		if (value == null) return def;
		try { return Double.parseDouble(value); }
		catch (NumberFormatException e) { return def; }
	}
	
	public boolean get(String id, String key, boolean def) {
		String value = get(id, key);
		if (value == null) return def;
		if (value.equalsIgnoreCase("true")) return true;
		if (value.equalsIgnoreCase("false")) return false;
		return def;
	}
	
	public <E extends Enum<E>> E get(String id, String key, Class<E> enumType, E def) {
		String value = get(id, key);
		if (value == null) return def;
		try { return Enum.valueOf(enumType, value); }
		catch (IllegalArgumentException e) { return def; }
	}
	
	public GammaCurve get(String id, String key, GammaCurve def) {
		String value = get(id, key);
		if (value == null) return def;
		if (value.equalsIgnoreCase("linear")) return GammaCurve.LINEAR;
		if (value.equalsIgnoreCase("quadratic")) return GammaCurve.QUADRATIC;
		if (value.equalsIgnoreCase("cubic")) return GammaCurve.CUBIC;
		if (value.equalsIgnoreCase("quasicubic1")) return GammaCurve.QUASICUBIC_I;
		if (value.equalsIgnoreCase("quasicubic2")) return GammaCurve.QUASICUBIC_II;
		try {
			if (value.startsWith("quasipower")) {
				String[] params = value.substring(10).trim().split("\\s+");
				double p0 = Double.parseDouble(params[0]);
				double p1 = Double.parseDouble(params[1]);
				double p2 = Double.parseDouble(params[2]);
				return new GammaCurve.Quasipower(p0, p1, p2, value);
			} else {
				double p = Double.parseDouble(value);
				return new GammaCurve.Power(p, value);
			}
		} catch (Exception e) {
			return def;
		}
	}
	
	public void put(String id, String key, String value) {
		if (config == null) config = new TreeMap<String,Map<String,String>>();
		Map<String,String> subcon = config.get(id);
		if (subcon == null) config.put(id, subcon = new TreeMap<String,String>());
		subcon.put(key, value);
	}
	
	public void put(String id, String key, int value) {
		put(id, key, Integer.toString(value));
	}
	
	public void put(String id, String key, long value) {
		put(id, key, Long.toString(value));
	}
	
	public void put(String id, String key, float value) {
		put(id, key, Float.toString(value));
	}
	
	public void put(String id, String key, double value) {
		put(id, key, Double.toString(value));
	}
	
	public void put(String id, String key, boolean value) {
		put(id, key, value ? "true" : "false");
	}
	
	public void put(String id, String key, Enum<?> value) {
		put(id, key, (value != null) ? value.name() : null);
	}
	
	public void put(String id, String key, GammaCurve value) {
		if (value instanceof GammaCurve.Linear) {
			put(id, key, "linear");
		} else if (value instanceof GammaCurve.Power) {
			double p = ((GammaCurve.Power)value).power;
			if (p == 2.0) put(id, key, "quadratic");
			else if (p == 3.0) put(id, key, "cubic");
			else put(id, key, Double.toString(p));
		} else if (value instanceof GammaCurve.Quasipower) {
			double p0 = ((GammaCurve.Quasipower)value).power;
			double p1 = ((GammaCurve.Quasipower)value).p1;
			double p2 = ((GammaCurve.Quasipower)value).p2;
			if (p0 == 3.0 && p1 == 1.0 && p2 == 1.0) put(id, key, "quasicubic1");
			else if (p0 == 3.0 && p1 == 1.0 && p2 == 2.0) put(id, key, "quasicubic2");
			else put(id, key, "quasipower " + p0 + " " + p1 + " " + p2);
		} else {
			put(id, key, (String)null);
		}
	}
	
	public void remove(String id, String key) {
		if (config == null) return;
		Map<String,String> subcon = config.get(id);
		if (subcon == null) return;
		subcon.remove(key);
		if (!subcon.isEmpty()) return;
		config.remove(id);
		if (!config.isEmpty()) return;
		config = null;
	}
	
	public void removeAll(String id) {
		config.remove(id);
		if (!config.isEmpty()) return;
		config = null;
	}
	
	public void read() throws IOException {
		read(new File("DeviceConfiguration.ppdcx"));
	}
	
	public void read(File file) throws IOException {
		InputStream in = new FileInputStream(file);
		read(file.getName(), in);
		in.close();
	}
	
	public void read(String name, InputStream in) throws IOException {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(true); // make sure the XML is valid
			factory.setExpandEntityReferences(false); // don't allow custom entities
			DocumentBuilder builder = factory.newDocumentBuilder();
			builder.setEntityResolver(new DCEntityResolver());
			builder.setErrorHandler(new DCErrorHandler(name));
			Document document = builder.parse(new InputSource(in));
			parseDocument(document);
		} catch (ParserConfigurationException pce) {
			throw new IOException(pce);
		} catch (SAXException saxe) {
			throw new IOException(saxe);
		}
	}
	
	private void parseDocument(Node node) throws IOException {
		String type = node.getNodeName();
		if (type.equalsIgnoreCase("#document")) {
			for (Node child : getChildren(node)) {
				String ctype = child.getNodeName();
				if (ctype.equalsIgnoreCase("device-configuration")) {
					if (child.hasAttributes() || child.hasChildNodes()) {
						parseDC(child);
						return;
					}
				} else {
					throw new IOException("Unknown element: " + ctype);
				}
			}
			throw new IOException("Empty document.");
		} else {
			throw new IOException("Unknown element: " + type);
		}
	}
	
	private void parseDC(Node node) throws IOException {
		String type = node.getNodeName();
		if (type.equalsIgnoreCase("device-configuration")) {
			for (Node child : getChildren(node)) {
				String ctype = child.getNodeName();
				if (ctype.equalsIgnoreCase("device")) {
					NamedNodeMap cattr = child.getAttributes();
					String id = parseString(cattr, "id");
					for (Node gc : getChildren(child)) {
						String gctype = gc.getNodeName();
						if (gctype.equalsIgnoreCase("param")) {
							NamedNodeMap gcattr = gc.getAttributes();
							String key = parseString(gcattr, "key");
							String value = gc.getTextContent();
							put(id, key, value);
						} else {
							throw new IOException("Unknown element: " + gctype);
						}
					}
				} else {
					throw new IOException("Unknown element: " + ctype);
				}
			}
		} else {
			throw new IOException("Unknown element: " + type);
		}
	}
	
	private static String parseString(NamedNodeMap attr, String key) {
		if (attr == null) return null;
		Node node = attr.getNamedItem(key);
		if (node == null) return null;
		String text = node.getTextContent();
		if (text == null) return null;
		return text.trim();
	}
	
	private static List<Node> getChildren(Node node) {
		List<Node> list = new ArrayList<Node>();
		if (node != null) {
			NodeList children = node.getChildNodes();
			if (children != null) {
				int count = children.getLength();
				for (int i = 0; i < count; i++) {
					Node child = children.item(i);
					if (child != null) {
						String type = child.getNodeName();
						if (type.equalsIgnoreCase("#text") || type.equalsIgnoreCase("#comment")) {
							continue;
						} else {
							list.add(child);
						}
					}
				}
			}
		}
		return list;
	}
	
	private static class DCEntityResolver implements EntityResolver {
		@Override
		public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
			if (publicId.contains("PixelPusherDeviceConfiguration") || systemId.contains("ppdcx.dtd")) {
				return new InputSource(DeviceConfiguration.class.getResourceAsStream("ppdcx.dtd"));
			} else {
				return null;
			}
		}
	}
	
	private static class DCErrorHandler implements ErrorHandler {
		private final String name;
		public DCErrorHandler(String name) {
			this.name = name;
		}
		@Override
		public void error(SAXParseException e) throws SAXException {
			System.err.print("Warning: Failed to load " + name + ": ");
			System.err.println("ERROR on "+e.getLineNumber()+":"+e.getColumnNumber()+": "+e.getMessage());
		}
		@Override
		public void fatalError(SAXParseException e) throws SAXException {
			System.err.print("Warning: Failed to load " + name + ": ");
			System.err.println("FATAL ERROR on "+e.getLineNumber()+":"+e.getColumnNumber()+": "+e.getMessage());
		}
		@Override
		public void warning(SAXParseException e) throws SAXException {
			System.err.print("Warning: Failed to load " + name + ": ");
			System.err.println("WARNING on "+e.getLineNumber()+":"+e.getColumnNumber()+": "+e.getMessage());
		}
	}
	
	public void write() throws IOException {
		write(new File("DeviceConfiguration.ppdcx"));
	}
	
	public void write(File file) throws IOException {
		OutputStream out = new FileOutputStream(file);
		write(out);
		out.close();
	}
	
	public void write(OutputStream out) throws IOException {
		write(new OutputStreamWriter(out, "UTF-8"));
	}
	
	public void write(Writer out) throws IOException {
		write(new PrintWriter(out, true));
	}
	
	public void write(PrintWriter out) throws IOException {
		out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		out.println("<!DOCTYPE device-configuration PUBLIC \"-//Kreative//DTD PixelPusherDeviceConfiguration 1.0//EN\" \"http://www.kreativekorp.com/dtd/ppdcx.dtd\">");
		out.println("<device-configuration>");
		if (config != null) {
			for (Map.Entry<String,Map<String,String>> e : config.entrySet()) {
				out.println("\t<device id=\"" + xmls(e.getKey()) + "\">");
				for (Map.Entry<String,String> ee : e.getValue().entrySet()) {
					if (ee.getValue() == null) {
						out.println("\t\t<param key=\"" + xmls(ee.getKey()) + "\"/>");
					} else {
						out.println("\t\t<param key=\"" + xmls(ee.getKey()) + "\">" + xmls(ee.getValue()) + "</param>");
					}
				}
				out.println("\t</device>");
			}
		}
		out.println("</device-configuration>");
	}
	
	private static String xmls(String s) {
		return s.replaceAll("&", "&amp;")
		        .replaceAll("<", "&lt;")
		        .replaceAll(">", "&gt;")
		        .replaceAll("\"", "&quot;");
	}
}
