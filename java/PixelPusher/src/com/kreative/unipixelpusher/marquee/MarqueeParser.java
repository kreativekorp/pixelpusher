package com.kreative.unipixelpusher.marquee;

import java.awt.Font;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import javax.imageio.ImageIO;
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
import com.kreative.imagetool.animation.Animation;
import com.kreative.imagetool.animation.AnimationIO;
import com.kreative.imagetool.gif.GIFFile;
import com.kreative.unipixelpusher.ASCII85InputStream;
import com.kreative.unipixelpusher.Base64InputStream;
import com.kreative.unipixelpusher.ColorUtilities;

public class MarqueeParser {
	private static MarqueeParser instance = null;
	
	public static MarqueeParser getInstance() {
		if (instance == null) {
			instance = new MarqueeParser();
			try { instance.parse(); } catch (IOException e) {}
			try { instance.parse(new File(".")); } catch (IOException e) {}
		}
		return instance;
	}
	
	private final SortedMap<String,MarqueeItem> messages;
	
	public MarqueeParser() {
		this.messages = new TreeMap<String,MarqueeItem>();
	}
	
	public String[] getMessageNames() {
		return messages.keySet().toArray(new String[messages.size()]);
	}
	
	public MarqueeItem getMessage(String name) {
		return messages.get(name);
	}
	
	public void parse() throws IOException {
		InputStream in = MarqueeParser.class.getResourceAsStream("presets.ppmqx");
		parse(null, "presets.ppmqx", in);
		in.close();
	}
	
	public void parse(File file) throws IOException {
		if (file.isDirectory()) {
			for (File f : file.listFiles()) {
				String name = f.getName().toLowerCase();
				if (name.endsWith(".ppmqx") || (f.isDirectory() && !name.startsWith("."))) {
					try { parse(f); }
					catch (IOException e) { e.printStackTrace(); }
				}
			}
		} else {
			InputStream in = new FileInputStream(file);
			parse(file.getParentFile(), file.getName(), in);
			in.close();
		}
	}
	
	public void parse(File parentDir, String name, InputStream in) throws IOException {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(true); // make sure the XML is valid
			factory.setExpandEntityReferences(false); // don't allow custom entities
			DocumentBuilder builder = factory.newDocumentBuilder();
			builder.setEntityResolver(new MarqueeEntityResolver());
			builder.setErrorHandler(new MarqueeErrorHandler(name));
			Document document = builder.parse(new InputSource(in));
			parseDocument(parentDir, document);
		} catch (ParserConfigurationException pce) {
			throw new IOException(pce);
		} catch (SAXException saxe) {
			throw new IOException(saxe);
		}
	}
	
	private void parseDocument(File parentDir, Node node) throws IOException {
		String type = node.getNodeName();
		if (type.equalsIgnoreCase("#document")) {
			for (Node child : getChildren(node)) {
				String ctype = child.getNodeName();
				if (ctype.equalsIgnoreCase("messages")) {
					if (child.hasAttributes() || child.hasChildNodes()) {
						parseMessages(parentDir, child);
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
	
	private void parseMessages(File parentDir, Node node) throws IOException {
		String type = node.getNodeName();
		if (type.equalsIgnoreCase("messages")) {
			for (Node child : getChildren(node)) {
				String ctype = child.getNodeName();
				if (ctype.equalsIgnoreCase("message")) {
					NamedNodeMap cattr = child.getAttributes();
					String name = parseString(cattr, "name");
					if (name == null || name.trim().length() == 0) {
						name = child.getTextContent();
						if (name == null || name.trim().length() == 0) {
							name = "Message " + System.currentTimeMillis();
						} else {
							name = name.replaceAll("\\s+", " ").trim();
						}
					}
					MarqueeItemString str = parseMessage(parentDir, child);
					messages.put(name, str);
				} else {
					throw new IOException("Unknown element: " + ctype);
				}
			}
		} else {
			throw new IOException("Unknown element: " + type);
		}
	}
	
	private static MarqueeItemString parseMessage(File parentDir, Node node) throws IOException {
		String type = node.getNodeName();
		if (type.equalsIgnoreCase("message")) {
			MarqueeItemString str = new MarqueeItemString();
			for (Node child : getChildren(node)) {
				str.add(parseMarqueeItem(parentDir, child));
			}
			return str;
		} else {
			throw new IOException("Unknown element: " + type);
		}
	}
	
	private static MarqueeItem parseMarqueeItem(File parentDir, Node node) throws IOException {
		String type = node.getNodeName();
		NamedNodeMap attr = node.getAttributes();
		if (type.equalsIgnoreCase("animation")) {
			AnimationItem item = new AnimationItem(null);
			parseItemBase(attr, item);
			InputStream in = parseInputStream(parentDir, node, attr);
			if (in != null) {
				try {
					GIFFile gif = new GIFFile();
					gif.read(new DataInputStream(in));
					Animation a = AnimationIO.fromGIFFile(gif);
					item.setAnimation(a);
				} catch (IOException ioe) {
					ioe.printStackTrace();
				} finally {
					in.close();
				}
			}
			return item;
		} else if (type.equalsIgnoreCase("image")) {
			ImageItem item = new ImageItem(null);
			parseItemBase(attr, item);
			InputStream in = parseInputStream(parentDir, node, attr);
			if (in != null) {
				try { item.setImage(ImageIO.read(in)); }
				catch (IOException ioe) { ioe.printStackTrace(); }
				finally { in.close(); }
			}
			return item;
		} else if (type.equalsIgnoreCase("text")) {
			StaticTextItem item = new StaticTextItem(null);
			parseItemBase(attr, item);
			parseTextItemBase(attr, item);
			String text = node.getTextContent();
			if (text != null) item.setText(text.trim());
			return item;
		} else if (type.equalsIgnoreCase("datetime")) {
			DateTimeItem item = new DateTimeItem();
			parseItemBase(attr, item);
			parseTextItemBase(attr, item);
			String text = node.getTextContent();
			if (text != null) item.setFormat(text.trim());
			return item;
		} else if (type.equalsIgnoreCase("div")) {
			MarqueeItemString item = new MarqueeItemString();
			parseItemBase(attr, item);
			for (Node child : getChildren(node)) {
				item.add(parseMarqueeItem(parentDir, child));
			}
			return item;
		} else {
			throw new IOException("Unknown element: " + type);
		}
	}
	
	private static void parseItemBase(NamedNodeMap attr, MarqueeItem item) {
		int[] pad = parseInts(attr, "padding", 0);
		if (pad == null || pad.length == 0) item.setPadding(0, 0, 0, 0);
		else if (pad.length == 1) item.setPadding(pad[0], pad[0], pad[0], pad[0]);
		else if (pad.length == 2) item.setPadding(pad[0], pad[1], pad[0], pad[1]);
		else if (pad.length == 3) item.setPadding(pad[0], pad[1], pad[2], pad[1]);
		else item.setPadding(pad[0], pad[3], pad[2], pad[1]);
		item.setHorizontalAlignment(parseSwingConstant(attr, "align", MarqueeItem.CENTER));
		item.setVerticalAlignment(parseSwingConstant(attr, "valign", MarqueeItem.CENTER));
		item.setBackgroundColor(parseColor(attr, "bgcolor", 0));
	}
	
	@SuppressWarnings("resource")
	private static InputStream parseInputStream(File parentDir, Node node, NamedNodeMap attr) {
		InputStream in = null;
		if (in == null) {
			String src = parseString(attr, "src");
			if (src != null && src.trim().length() > 0) {
				try { in = new FileInputStream(new File(parentDir, src)); }
				catch (IOException ioe) {}
			}
		}
		if (in == null) {
			String data = node.getTextContent();
			if (data != null && data.trim().length() > 0) {
				try { in = new ByteArrayInputStream(data.getBytes("UTF-8")); }
				catch (IOException ioe) {}
			}
		}
		if (in != null) {
			String enc = parseString(attr, "enc");
			if (enc != null) {
				if (enc.equalsIgnoreCase("base64")) in = new Base64InputStream(in);
				if (enc.equalsIgnoreCase("ascii85")) in = new ASCII85InputStream(in);
			}
		}
		return in;
	}
	
	private static void parseTextItemBase(NamedNodeMap attr, TextItem item) {
		item.setForegroundColor(parseColor(attr, "color", -1));
		
		String face = parseString(attr, "face");
		if (face == null || face.trim().length() == 0);
		else if (face.equalsIgnoreCase("4x5")) item.setFont(TextItem.FIXED_4x5);
		else if (face.equalsIgnoreCase("4x7")) item.setFont(TextItem.FIXED_4x7);
		else if (face.equalsIgnoreCase("4")) item.setFont(TextItem.PROPORTIONAL_4);
		else if (face.equalsIgnoreCase("7")) item.setFont(TextItem.PROPORTIONAL_7);
		else if (face.equalsIgnoreCase("9")) item.setFont(TextItem.PROPORTIONAL_9);
		else item.setFont(new Font(face, Font.PLAIN, 12));
		
		int size = parseInt(attr, "size", 0);
		if (size > 0) item.setFontSize(size);
		
		String style = parseString(attr, "style");
		if (style == null || style.trim().length() == 0);
		else if (style.equalsIgnoreCase("normal")) item.setFontStyle(Font.PLAIN);
		else if (style.equalsIgnoreCase("bold")) item.setFontStyle(Font.BOLD);
		else if (style.equalsIgnoreCase("italic")) item.setFontStyle(Font.ITALIC);
		else if (style.equalsIgnoreCase("bolditalic")) item.setFontStyle(Font.BOLD | Font.ITALIC);
		
		item.setFontWeight(parseInt(attr, "weight", 1));
		item.setFontSpacing(parseInt(attr, "spacing", 0));
	}
	
	private static int parseColor(NamedNodeMap attr, String key, int def) {
		if (attr == null) return def;
		Node node = attr.getNamedItem(key);
		if (node == null) return def;
		String text = node.getTextContent();
		if (text == null) return def;
		try { return ColorUtilities.parse(text.trim()); }
		catch (NumberFormatException nfe) { return def; }
	}
	
	private static int parseSwingConstant(NamedNodeMap attr, String key, int def) {
		if (attr == null) return def;
		Node node = attr.getNamedItem(key);
		if (node == null) return def;
		String text = node.getTextContent();
		if (text == null) return def;
		if (text.equalsIgnoreCase("top")) return MarqueeItem.TOP;
		if (text.equalsIgnoreCase("left")) return MarqueeItem.LEFT;
		if (text.equalsIgnoreCase("right")) return MarqueeItem.RIGHT;
		if (text.equalsIgnoreCase("bottom")) return MarqueeItem.BOTTOM;
		if (text.equalsIgnoreCase("center")) return MarqueeItem.CENTER;
		return def;
	}
	
	private static int[] parseInts(NamedNodeMap attr, String key, int def) {
		if (attr == null) return null;
		Node node = attr.getNamedItem(key);
		if (node == null) return null;
		String text = node.getTextContent();
		if (text == null) return null;
		String[] pieces = text.trim().split("\\s+");
		int[] ints = new int[pieces.length];
		for (int i = 0; i < pieces.length; i++) {
			try { ints[i] = Integer.parseInt(pieces[i]); }
			catch (NumberFormatException nfe) { ints[i] = def; }
		}
		return ints;
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
	
	private static class MarqueeEntityResolver implements EntityResolver {
		@Override
		public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
			if (publicId.contains("PixelPusherMarquee") || systemId.contains("ppmqx.dtd")) {
				return new InputSource(MarqueeParser.class.getResourceAsStream("ppmqx.dtd"));
			} else {
				return null;
			}
		}
	}
	
	private static class MarqueeErrorHandler implements ErrorHandler {
		private final String name;
		public MarqueeErrorHandler(String name) {
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
