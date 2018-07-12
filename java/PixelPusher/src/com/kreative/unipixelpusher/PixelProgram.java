package com.kreative.unipixelpusher;

import java.awt.Component;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

public class PixelProgram implements PixelDeviceListener {
	private final List<PixelSequence> sequences;
	private final List<DeviceInfo> devices;
	private final Map<String,DeviceInfo> deviceMap;
	private final PusherThreadPool threadPool;
	private final List<PixelProgramListener> listeners;
	
	public PixelProgram() {
		this.sequences = new ArrayList<PixelSequence>();
		this.devices = new ArrayList<DeviceInfo>();
		this.deviceMap = new HashMap<String,DeviceInfo>();
		this.threadPool = new PusherThreadPool();
		this.listeners = new ArrayList<PixelProgramListener>();
	}
	
	public synchronized void addPixelProgramListener(PixelProgramListener listener) {
		this.listeners.add(listener);
	}
	
	public synchronized void removePixelProgramListener(PixelProgramListener listener) {
		this.listeners.remove(listener);
	}
	
	public synchronized PixelProgramListener[] getPixelProgramListeners() {
		return listeners.toArray(new PixelProgramListener[listeners.size()]);
	}
	
	private synchronized void pixelProgramChanged() {
		for (PixelProgramListener listener : listeners) {
			listener.pixelProgramChanged(this);
		}
	}
	
	private synchronized void pixelDevicesChanged() {
		for (PixelProgramListener listener : listeners) {
			listener.pixelDevicesChanged(this);
		}
	}
	
	public synchronized void clear() {
		this.sequences.clear();
		this.devices.clear();
		this.deviceMap.clear();
		this.threadPool.disconnectAll();
		pixelProgramChanged();
	}
	
	public synchronized void addSequence(PixelSequence sequence) {
		if (sequences.contains(sequence)) return;
		sequences.add(sequence);
		pixelProgramChanged();
	}
	
	public synchronized List<PixelSequence> getSequences() {
		return Collections.unmodifiableList(sequences);
	}
	
	public synchronized void removeSequence(PixelSequence sequence) {
		sequences.remove(sequence);
		threadPool.disconnect(sequence);
		pixelProgramChanged();
	}
	
	public synchronized DeviceInfo addDevice(PixelDevice device) {
		String id = device.id();
		DeviceInfo info = deviceMap.get(id);
		if (info != null) {
			info.update(device);
		} else {
			info = new DeviceInfo(device);
			devices.add(info);
			deviceMap.put(id, info);
		}
		pixelProgramChanged();
		return info;
	}
	
	public synchronized List<DeviceInfo> getDevices() {
		return Collections.unmodifiableList(devices);
	}
	
	public synchronized void removeDevice(PixelDevice device) {
		removeDevice(device.id());
	}
	
	public synchronized void removeDevice(DeviceInfo info) {
		removeDevice(info.id);
	}
	
	public synchronized void removeDevice(String id) {
		DeviceInfo info = deviceMap.remove(id);
		if (info != null) {
			devices.remove(info);
			if (info.strings != null) {
				for (DeviceStringInfo string : info.strings) {
					threadPool.disconnect(string);
				}
			}
		}
		pixelProgramChanged();
	}
	
	public synchronized void connect(PixelSequence sequence, DeviceStringInfo string) {
		threadPool.connect(sequence, string);
		pixelProgramChanged();
	}
	
	public synchronized void disconnect(PixelSequence sequence, PixelString string) {
		threadPool.disconnect(sequence, string);
		pixelProgramChanged();
	}
	
	public synchronized void disconnect(PixelSequence sequence) {
		threadPool.disconnect(sequence);
		pixelProgramChanged();
	}
	
	public synchronized void disconnect(PixelString string) {
		threadPool.disconnect(string);
		pixelProgramChanged();
	}
	
	public synchronized void disconnectAll() {
		threadPool.disconnectAll();
		pixelProgramChanged();
	}
	
	public synchronized PusherThread getThread(PixelSequence sequence) {
		return threadPool.getThread(sequence);
	}
	
	public synchronized PusherThread getThread(PixelString string) {
		return threadPool.getThread(string);
	}
	
	public synchronized Set<PusherThread> threadSet() {
		return threadPool.threadSet();
	}
	
	public synchronized void update(DeviceLoader loader) {
		loader.removePixelDeviceListener(this);
		for (DeviceLoader.DeviceInfo dev : loader.getDevices()) {
			DeviceInfo info = deviceMap.get(dev.id());
			if (info != null) info.update(dev.device());
		}
		loader.addPixelDeviceListener(this);
		pixelDevicesChanged();
	}
	
	@Override
	public synchronized void pixelDeviceAppeared(PixelDevice dev) {
		String id = dev.id();
		DeviceInfo info = deviceMap.get(id);
		if (info != null) info.update(dev);
		pixelDevicesChanged();
	}
	
	@Override
	public synchronized void pixelDeviceChanged(PixelDevice dev) {
		String id = dev.id();
		DeviceInfo info = deviceMap.get(id);
		if (info != null) info.update(dev);
		pixelDevicesChanged();
	}
	
	@Override
	public synchronized void pixelDeviceDisappeared(PixelDevice dev) {
		String id = dev.id();
		DeviceInfo info = deviceMap.get(id);
		if (info != null) info.update(null);
		pixelDevicesChanged();
	}
	
	public final class DeviceInfo implements PixelDevice {
		private PixelDeviceRegistry parent;
		private String id;
		private String name;
		private DeviceType type;
		private PixelDevice device;
		private List<DeviceStringInfo> strings;
		private Map<String,DeviceStringInfo> stringMap;
		
		private DeviceInfo(PixelDevice device) {
			update(device);
		}
		
		private synchronized void update(PixelDevice device) {
			if (device != null) {
				this.parent = device.parent();
				this.id = device.id();
				this.name = device.name();
				this.type = device.type();
				this.device = device;
				this.strings = new ArrayList<DeviceStringInfo>();
				for (DeviceString string : device.getStrings()) {
					if (stringMap != null) {
						DeviceStringInfo info = stringMap.remove(string.id());
						if (info != null) {
							info.update(string);
							this.strings.add(info);
							continue;
						}
					}
					this.strings.add(new DeviceStringInfo(this, string));
				}
				if (stringMap != null) {
					for (DeviceStringInfo string : stringMap.values()) {
						threadPool.disconnect(string);
					}
				}
				this.stringMap = new HashMap<String,DeviceStringInfo>();
				for (DeviceStringInfo string : strings) {
					this.stringMap.put(string.id(), string);
				}
			} else {
				this.device = null;
				if (strings != null) {
					for (DeviceStringInfo string : strings) {
						string.update(null);
					}
				}
			}
		}
		
		private DeviceInfo(Node node, Map<String,PixelDevice> dmap, Map<String,PixelString> smap) throws IOException {
			String type = node.getNodeName();
			if (type.equalsIgnoreCase("device")) {
				NamedNodeMap attr = node.getAttributes();
				String id = parseString(attr, "id");
				dmap.put(id, this);
				this.parent = null;
				this.id = parseString(attr, "device");
				this.name = parseString(attr, "name");
				this.type = parseEnum(attr, "type", DeviceType.class, DeviceType.UNKNOWN);
				this.device = null;
				this.strings = new ArrayList<DeviceStringInfo>();
				this.stringMap = new HashMap<String,DeviceStringInfo>();
				for (Node child : getChildren(node)) {
					DeviceStringInfo string = new DeviceStringInfo(this, child, smap);
					this.strings.add(string);
					this.stringMap.put(string.id(), string);
				}
			} else {
				throw new IOException("Unknown element: " + type);
			}
		}
		
		public synchronized boolean isActive() { return device != null; }
		@Override public synchronized PixelDeviceRegistry parent() { return (device != null) ? (parent = device.parent()) : parent; }
		@Override public synchronized String id() { return (device != null) ? (id = device.id()) : id; }
		@Override public synchronized String name() { return (device != null) ? (name = device.name()) : name; }
		@Override public synchronized DeviceType type() { return (device != null) ? (type = device.type()) : type; }
		@Override public synchronized int getStringCount() { return (strings != null) ? strings.size() : 0; }
		@Override public synchronized DeviceStringInfo getString(int i) { return (strings != null) ? strings.get(i) : null; }
		@Override public synchronized Iterable<DeviceStringInfo> getStrings() { return (strings != null) ? Collections.unmodifiableList(strings) : null; }
		@Override public synchronized boolean hasConfigurationPanel() { return (device != null) ? device.hasConfigurationPanel() : false; }
		@Override public synchronized Component createConfigurationPanel() { return (device != null) ? device.createConfigurationPanel() : null; }
	}
	
	public final class DeviceStringInfo implements DeviceString {
		private DeviceInfo parent;
		private String id;
		private String name;
		private StringType type;
		private int length;
		private int rowCount;
		private int columnCount;
		private DeviceString string;
		
		private DeviceStringInfo(DeviceInfo parent, DeviceString string) {
			this.parent = parent;
			update(string);
		}
		
		private synchronized void update(DeviceString string) {
			if (string != null) {
				this.id = string.id();
				this.name = string.name();
				this.type = string.type();
				this.length = string.length();
				this.rowCount = string.getRowCount();
				this.columnCount = string.getColumnCount();
				this.string = string;
			} else {
				this.string = null;
			}
		}
		
		private DeviceStringInfo(DeviceInfo parent, Node node, Map<String,PixelString> map) throws IOException {
			String type = node.getNodeName();
			if (type.equalsIgnoreCase("string")) {
				NamedNodeMap attr = node.getAttributes();
				String id = parseString(attr, "id");
				map.put(id, this);
				this.parent = parent;
				this.id = parseString(attr, "string");
				this.name = parseString(attr, "name");
				this.type = parseEnum(attr, "type", StringType.class, StringType.UNKNOWN);
				this.length = parseInt(attr, "length", 0);
				this.rowCount = parseInt(attr, "row-count", 0);
				this.columnCount = parseInt(attr, "column-count", 0);
				this.string = null;
			} else {
				throw new IOException("Unknown element: " + type);
			}
		}
		
		@Override public synchronized DeviceInfo parent() { return parent; }
		@Override public synchronized String id() { return (string != null) ? (id = string.id()) : id; }
		@Override public synchronized String name() { return (string != null) ? (name = string.name()) : name; }
		@Override public synchronized StringType type() { return (string != null) ? (type = string.type()) : type; }
		@Override public synchronized int length() { return (string != null) ? (length = string.length()) : length; }
		@Override public synchronized int getRowCount() { return (string != null) ? (rowCount = string.getRowCount()) : rowCount; }
		@Override public synchronized int getColumnCount() { return (string != null) ? (columnCount = string.getColumnCount()) : columnCount; }
		@Override public synchronized int getPixel(int i) { return (string != null) ? string.getPixel(i) : 0; }
		@Override public synchronized int getPixel(int row, int col) { return (string != null) ? string.getPixel(row, col) : 0; }
		@Override public synchronized void setPixel(int i, int color) { if (string != null) string.setPixel(i, color); }
		@Override public synchronized void setPixel(int row, int col, int color) { if (string != null) string.setPixel(row, col, color); }
		@Override public synchronized void push() { if (string != null) string.push(); }
		@Override public synchronized GammaCurve getGammaCurve() { return (string != null) ? string.getGammaCurve() : null; }
		@Override public synchronized void setGammaCurve(GammaCurve gamma) { if (string != null) string.setGammaCurve(gamma); }
	}
	
	public synchronized void read(File file) throws IOException {
		InputStream in = new FileInputStream(file);
		read(file.getName(), in);
		in.close();
	}
	
	public synchronized void read(String name, InputStream in) throws IOException {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(true); // make sure the XML is valid
			factory.setExpandEntityReferences(false); // don't allow custom entities
			DocumentBuilder builder = factory.newDocumentBuilder();
			builder.setEntityResolver(new ProgramEntityResolver());
			builder.setErrorHandler(new ProgramErrorHandler(name));
			Document document = builder.parse(new InputSource(in));
			parseDocument(document);
		} catch (ParserConfigurationException pce) {
			throw new IOException(pce);
		} catch (SAXException saxe) {
			throw new IOException(saxe);
		}
	}
	
	private synchronized void parseDocument(Node node) throws IOException {
		String type = node.getNodeName();
		if (type.equalsIgnoreCase("#document")) {
			for (Node child : getChildren(node)) {
				String ctype = child.getNodeName();
				if (ctype.equalsIgnoreCase("program")) {
					if (child.hasAttributes() || child.hasChildNodes()) {
						parseProgram(child);
						pixelProgramChanged();
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
	
	private synchronized void parseProgram(Node node) throws IOException {
		String type = node.getNodeName();
		if (type.equalsIgnoreCase("program")) {
			Map<String,PixelSequence> sequences = new HashMap<String,PixelSequence>();
			Map<String,PixelDevice> devices = new HashMap<String,PixelDevice>();
			Map<String,PixelString> strings = new HashMap<String,PixelString>();
			this.clear();
			for (Node child : getChildren(node)) {
				String ctype = child.getNodeName();
				if (ctype.equalsIgnoreCase("sequence")) {
					PixelSequence sequence = parseSequence(child, sequences);
					this.sequences.add(sequence);
				} else if (ctype.equalsIgnoreCase("device")) {
					DeviceInfo info = new DeviceInfo(child, devices, strings);
					this.devices.add(info);
					this.deviceMap.put(info.id(), info);
				} else if (ctype.equalsIgnoreCase("connection")) {
					NamedNodeMap cattr = child.getAttributes();
					String sequenceId = parseString(cattr, "sequence");
					String stringId = parseString(cattr, "string");
					if (sequences.containsKey(sequenceId) && strings.containsKey(stringId)) {
						PixelSequence sequence = sequences.get(sequenceId);
						PixelString string = strings.get(stringId);
						this.threadPool.connect(sequence, string);
					}
				} else {
					throw new IOException("Unknown element: " + ctype);
				}
			}
		} else {
			throw new IOException("Unknown element: " + type);
		}
	}
	
	private static PixelSequence parseSequence(Node node, Map<String,PixelSequence> map) throws IOException {
		String type = node.getNodeName();
		if (type.equalsIgnoreCase("sequence")) {
			NamedNodeMap attr = node.getAttributes();
			String id = parseString(attr, "id");
			String clsName = parseString(attr, "class");
			SequenceConfiguration config = new SequenceConfiguration();
			config.read(getChildren(node));
			try {
				Class<?> cls = Class.forName(clsName);
				Class<? extends PixelSequence> scls = cls.asSubclass(PixelSequence.class);
				PixelSequence sequence = scls.newInstance();
				sequence.loadConfiguration(config);
				map.put(id, sequence);
				return sequence;
			} catch (Exception e) {
				throw new IOException("Unknown sequence class: " + clsName);
			}
		} else {
			throw new IOException("Unknown element: " + type);
		}
	}
	
	private static <E extends Enum<E>> E parseEnum(NamedNodeMap attr, String key, Class<E> enumType, E def) {
		if (attr == null) return def;
		Node node = attr.getNamedItem(key);
		if (node == null) return def;
		String text = node.getTextContent();
		if (text == null) return def;
		try { return Enum.valueOf(enumType, text.trim()); }
		catch (IllegalArgumentException e) { return def; }
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
	
	private static class ProgramEntityResolver implements EntityResolver {
		@Override
		public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
			if (publicId.contains("PixelPusherProgram") || systemId.contains("ppgmx.dtd")) {
				return new InputSource(PixelProgram.class.getResourceAsStream("ppgmx.dtd"));
			} else {
				return null;
			}
		}
	}
	
	private static class ProgramErrorHandler implements ErrorHandler {
		private final String name;
		public ProgramErrorHandler(String name) {
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
	
	public synchronized void write(File file) throws IOException {
		OutputStream out = new FileOutputStream(file);
		write(out);
		out.close();
	}
	
	public synchronized void write(OutputStream out) throws IOException {
		write(new OutputStreamWriter(out, "UTF-8"));
	}
	
	public synchronized void write(Writer out) throws IOException {
		write(new PrintWriter(out, true));
	}
	
	public synchronized void write(PrintWriter out) throws IOException {
		int nextId = 1;
		Map<PixelSequence,String> sequenceIds = new HashMap<PixelSequence,String>();
		Map<PixelDevice,String> deviceIds = new HashMap<PixelDevice,String>();
		Map<PixelString,String> stringIds = new HashMap<PixelString,String>();
		
		out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		out.println("<!DOCTYPE program PUBLIC \"-//Kreative//DTD PixelPusherProgram 1.0//EN\" \"http://www.kreativekorp.com/dtd/ppgmx.dtd\">");
		out.println("<program>");
		
		for (PixelSequence sequence : sequences) {
			String id = ("q" + Integer.toString(nextId++));
			sequenceIds.put(sequence, id);
			out.println("\t<sequence id=\"" + id + "\" class=\"" + sequence.getClass().getName() + "\">");
			SequenceConfiguration config = new SequenceConfiguration();
			sequence.saveConfiguration(config);
			config.write(out, "\t\t");
			out.println("\t</sequence>");
		}
		
		for (DeviceInfo device : devices) {
			String id = ("d" + Integer.toString(nextId++));
			deviceIds.put(device, id);
			out.println("\t<device id=\"" + id + "\" device=\"" + xmls(device.id()) + "\" name=\"" + xmls(device.name()) + "\" type=\"" + device.type().name() + "\">");
			for (DeviceStringInfo string : device.getStrings()) {
				id = ("s" + Integer.toString(nextId++));
				stringIds.put(string, id);
				out.println("\t\t<string id=\"" + id + "\" string=\"" + xmls(string.id()) + "\" name=\"" + xmls(string.name()) + "\" type=\"" + string.type().name() + "\"");
				out.println("\t\t        length=\"" + string.length() + "\" row-count=\"" + string.getRowCount() + "\" column-count=\"" + string.getColumnCount() + "\">");
				out.println("\t\t</string>");
			}
			out.println("\t</device>");
		}
		
		for (PusherThread thread : threadPool.threadSet()) {
			if (sequenceIds.containsKey(thread.sequence) && stringIds.containsKey(thread.string)) {
				String sequenceId = sequenceIds.get(thread.sequence);
				String stringId = stringIds.get(thread.string);
				out.println("\t<connection sequence=\"" + sequenceId + "\" string=\"" + stringId + "\"/>");
			}
		}
		
		out.println("</program>");
	}
	
	private static String xmls(String s) {
		return s.replaceAll("&", "&amp;")
		        .replaceAll("<", "&lt;")
		        .replaceAll(">", "&gt;")
		        .replaceAll("\"", "&quot;");
	}
}
