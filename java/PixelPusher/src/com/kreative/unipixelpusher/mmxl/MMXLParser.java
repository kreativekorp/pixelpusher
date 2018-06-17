package com.kreative.unipixelpusher.mmxl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
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

public class MMXLParser {
	private final SortedMap<String, MMXLColorPattern> colorPatterns;
	private final SortedMap<String, MMXLBlinkPattern> blinkPatterns;
	
	public MMXLParser() {
		this.colorPatterns = new TreeMap<String, MMXLColorPattern>();
		this.blinkPatterns = new TreeMap<String, MMXLBlinkPattern>();
	}
	
	public String[] getColorPatternNames() {
		return colorPatterns.keySet().toArray(new String[colorPatterns.size()]);
	}
	
	public MMXLColorPattern getColorPattern(String name) {
		return colorPatterns.get(name);
	}
	
	public String[] getBlinkPatternNames() {
		return blinkPatterns.keySet().toArray(new String[blinkPatterns.size()]);
	}
	
	public MMXLBlinkPattern getBlinkPattern(String name) {
		return blinkPatterns.get(name);
	}
	
	public void parse() throws IOException {
		InputStream in = MMXLParser.class.getResourceAsStream("presets.mmxlx");
		parse("presets.mmxlx", in);
		in.close();
	}
	
	public void parse(File file) throws IOException {
		InputStream in = new FileInputStream(file);
		parse(file.getName(), in);
		in.close();
	}
	
	public void parse(String name, InputStream in) throws IOException {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(true); // make sure the XML is valid
			factory.setExpandEntityReferences(false); // don't allow custom entities
			DocumentBuilder builder = factory.newDocumentBuilder();
			builder.setEntityResolver(new MMXLEntityResolver());
			builder.setErrorHandler(new MMXLErrorHandler(name));
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
				if (ctype.equalsIgnoreCase("mmxl")) {
					if (child.hasAttributes() || child.hasChildNodes()) {
						parseMMXL(child);
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
	
	private void parseMMXL(Node node) throws IOException {
		String type = node.getNodeName();
		if (type.equalsIgnoreCase("mmxl")) {
			for (Node child : getChildren(node)) {
				String ctype = child.getNodeName();
				if (ctype.equalsIgnoreCase("color-pattern")) {
					MMXLColorPattern cp = parseColorPattern(child);
					colorPatterns.put(cp.toString(), cp);
				} else if (ctype.equalsIgnoreCase("blink-pattern")) {
					MMXLBlinkPattern bp = parseBlinkPattern(child);
					blinkPatterns.put(bp.toString(), bp);
				} else {
					throw new IOException("Unknown element: " + ctype);
				}
			}
		} else {
			throw new IOException("Unknown element: " + type);
		}
	}
	
	private static MMXLColorPattern parseColorPattern(Node node) throws IOException {
		String type = node.getNodeName();
		if (type.equalsIgnoreCase("color-pattern")) {
			NamedNodeMap attr = node.getAttributes();
			String name = parseString(attr, "name");
			List<Integer> colorList = new ArrayList<Integer>();
			for (Node child : getChildren(node)) {
				String ctype = child.getNodeName();
				if (ctype.equalsIgnoreCase("color")) {
					NamedNodeMap cattr = child.getAttributes();
					int r = parseInt(cattr, "r", 0); if (r < 0) r = 0; if (r > 255) r = 255;
					int g = parseInt(cattr, "g", 0); if (g < 0) g = 0; if (g > 255) g = 255;
					int b = parseInt(cattr, "b", 0); if (b < 0) b = 0; if (b > 255) b = 255;
					int a = parseInt(cattr, "a", 255); if (a < 0) a = 0; if (a > 255) a = 255;
					colorList.add((a << 24) | (r << 16) | (g << 8) | (b << 0));
				} else {
					throw new IOException("Unknown element: " + ctype);
				}
			}
			int length = colorList.size();
			int[] colors = new int[length];
			for (int i = 0; i < length; i++) colors[i] = colorList.get(i);
			return new MMXLColorPattern(name, colors);
		} else {
			throw new IOException("Unknown element: " + type);
		}
	}
	
	private static MMXLBlinkPattern parseBlinkPattern(Node node) throws IOException {
		String type = node.getNodeName();
		if (type.equalsIgnoreCase("blink-pattern")) {
			NamedNodeMap attr = node.getAttributes();
			String name = parseString(attr, "name");
			int frameDuration = parseInt(attr, "framedur", 1000);
			List<int[]> frameList = new ArrayList<int[]>();
			for (Node child : getChildren(node)) {
				String ctype = child.getNodeName();
				if (ctype.equalsIgnoreCase("frame")) {
					NamedNodeMap cattr = child.getAttributes();
					String levelsString = parseString(cattr, "levels");
					if (levelsString != null) {
						String[] levelStrings = levelsString.split("\\s+");
						int[] levels = new int[levelStrings.length];
						for (int i = 0; i < levelStrings.length; i++) {
							try {
								levels[i] = Integer.parseInt(levelStrings[i]);
								if (levels[i] < 0) levels[i] = 0;
								if (levels[i] > 255) levels[i] = 255;
							} catch (NumberFormatException nfe) {
								levels[i] = 255;
							}
						}
						frameList.add(levels);
					} else {
						frameList.add(new int[]{255});
					}
				} else {
					throw new IOException("Unknown element: " + ctype);
				}
			}
			int frames = frameList.size();
			int[][] levels = frameList.toArray(new int[frames][]);
			return new MMXLBlinkPattern(name, frameDuration, levels);
		} else {
			throw new IOException("Unknown element: " + type);
		}
	}
	
	private static int parseInt(NamedNodeMap attr, String key, int def) {
		if (attr == null) return def;
		Node node = attr.getNamedItem(key);
		if (node == null) return def;
		String text = node.getTextContent();
		if (text == null) return def;
		try { return Integer.parseInt(text.trim()); }
		catch (NumberFormatException nfe) { return def; }
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
	
	private static class MMXLEntityResolver implements EntityResolver {
		@Override
		public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
			if (publicId.contains("MoreMoreXmasLights") || systemId.contains("mmxlx.dtd")) {
				return new InputSource(MMXLParser.class.getResourceAsStream("mmxlx.dtd"));
			} else {
				return null;
			}
		}
	}
	
	private static class MMXLErrorHandler implements ErrorHandler {
		private final String name;
		public MMXLErrorHandler(String name) {
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
}
