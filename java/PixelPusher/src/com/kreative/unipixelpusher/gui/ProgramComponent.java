package com.kreative.unipixelpusher.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import com.kreative.unipixelpusher.DeviceLoader;
import com.kreative.unipixelpusher.DeviceType;
import com.kreative.unipixelpusher.PixelProgram;
import com.kreative.unipixelpusher.PixelProgramListener;
import com.kreative.unipixelpusher.PixelSequence;
import com.kreative.unipixelpusher.PusherThread;
import com.kreative.unipixelpusher.SequenceLoader;
import com.kreative.unipixelpusher.StringType;

public class ProgramComponent extends JComponent {
	private static final long serialVersionUID = 1L;
	
	private DeviceLoader deviceLoader;
	private SequenceLoader sequenceLoader;
	private PixelProgram program;
	private Object selection;
	private Map<Rectangle,PixelSequence> sequenceRects;
	private Map<Rectangle,PixelSequence> sequencePortRects;
	private Map<Rectangle,PixelProgram.DeviceInfo> deviceRects;
	private Map<Rectangle,PixelProgram.DeviceStringInfo> devicePortRects;
	private Map<Object,Point> portPoints;
	private Insets lastKnownInsets;
	private Dimension lastKnownSize;
	private int preferredWidth;
	private int sequencesHeight;
	private int devicesHeight;
	private Dimension calcSize;
	private Dimension prefSize;
	private Point tempWireStart;
	private Point tempWireEnd;
	private PixelSequence tempWireSequence;
	private PixelProgram.DeviceStringInfo tempWireDevice;
	
	public ProgramComponent(DeviceLoader deviceLoader, SequenceLoader sequenceLoader, PixelProgram progrem) {
		this.deviceLoader = deviceLoader;
		this.sequenceLoader = sequenceLoader;
		this.setProgram(progrem);
		this.addMouseListener(mouseListener);
		this.addMouseMotionListener(mouseListener);
	}
	
	public DeviceLoader getDeviceLoader() {
		return this.deviceLoader;
	}
	
	public SequenceLoader getSequenceLoader() {
		return this.sequenceLoader;
	}
	
	public PixelProgram getProgram() {
		return this.program;
	}
	
	public void setProgram(PixelProgram program) {
		if (this.program != null) this.program.removePixelProgramListener(this.programListener);
		this.program = program;
		if (this.program != null) this.program.addPixelProgramListener(this.programListener);
		this.update();
		this.repaint();
	}
	
	private final MyPixelProgramListener programListener = new MyPixelProgramListener();
	private class MyPixelProgramListener implements PixelProgramListener {
		@Override
		public void pixelProgramChanged(PixelProgram program) {
			update();
			repaint();
		}
		@Override
		public void pixelDevicesChanged(PixelProgram program) {
			update();
			repaint();
		}
	}
	
	public Object getSelectedItem() {
		return this.selection;
	}
	
	public void setSelectedItem(Object item) {
		this.selection = item;
		this.repaint();
	}
	
	public void update() {
		sequenceRects = new HashMap<Rectangle,PixelSequence>();
		sequencePortRects = new HashMap<Rectangle,PixelSequence>();
		deviceRects = new HashMap<Rectangle,PixelProgram.DeviceInfo>();
		devicePortRects = new HashMap<Rectangle,PixelProgram.DeviceStringInfo>();
		portPoints = new HashMap<Object,Point>();
		lastKnownInsets = (Insets)getInsets().clone();
		lastKnownSize = new Dimension(getWidth(), getHeight());
		preferredWidth = lastKnownInsets.left + lastKnownInsets.right + 600;
		sequencesHeight = lastKnownInsets.top;
		devicesHeight = lastKnownInsets.top;
		if (program != null) {
			for (PixelSequence s : program.getSequences()) {
				Rectangle r = new Rectangle(lastKnownInsets.left, sequencesHeight, 200, 32);
				sequenceRects.put(r, s);
				Rectangle pr = new Rectangle(r.x + r.width - 2, r.y + (r.height - 12) / 2, 12, 12);
				sequencePortRects.put(pr, s);
				Point pp = new Point(pr.x + 6, pr.y + 6);
				portPoints.put(s, pp);
				sequencesHeight = r.y + r.height;
			}
			for (PixelProgram.DeviceInfo d : program.getDevices()) {
				int x = lastKnownSize.width - lastKnownInsets.right - 200;
				int h = 32 + d.getStringCount() * 20;
				Rectangle r = new Rectangle(x, devicesHeight, 200, h);
				deviceRects.put(r, d);
				for (PixelProgram.DeviceStringInfo s : d.getStrings()) {
					Rectangle pr = new Rectangle(x - 10, devicesHeight + 30, 12, 12);
					devicePortRects.put(pr, s);
					Point pp = new Point(pr.x + 6, pr.y + 6);
					portPoints.put(s, pp);
					devicesHeight += 20;
				}
				devicesHeight = r.y + r.height;
			}
		}
		sequencesHeight += lastKnownInsets.bottom;
		devicesHeight += lastKnownInsets.bottom;
		int totalHeight = Math.max(sequencesHeight, devicesHeight);
		int minimumHeight = lastKnownInsets.top + lastKnownInsets.bottom + 400;
		int preferredHeight = Math.max(totalHeight, minimumHeight);
		calcSize = new Dimension(preferredWidth, preferredHeight);
	}
	
	@Override
	public Dimension getPreferredSize() {
		if (prefSize != null) return prefSize;
		if (
			lastKnownInsets == null || !lastKnownInsets.equals(getInsets()) ||
			lastKnownSize == null || !lastKnownSize.equals(getSize())
		) {
			update();
		}
		return calcSize;
	}
	
	@Override
	public void setPreferredSize(Dimension prefSize) {
		this.prefSize = prefSize;
	}
	
	@Override
	protected void paintComponent(Graphics graphics) {
		if (program == null) return;
		Insets i = getInsets();
		Dimension d = getSize();
		if (
			lastKnownInsets == null || !lastKnownInsets.equals(i) ||
			lastKnownSize == null || !lastKnownSize.equals(d)
		) {
			update();
		}
		BufferedImage buffer = new BufferedImage(d.width, d.height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = buffer.createGraphics();
		paintMe(g);
		g.dispose();
		graphics.drawImage(buffer, 0, 0, null);
	}
	
	private void paintMe(Graphics2D g) {
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
		for (Map.Entry<Rectangle,PixelSequence> e : sequenceRects.entrySet()) {
			Rectangle r = e.getKey();
			PixelSequence s = e.getValue();
			drawObjectBox(g, r.x, r.y, r.width, r.height, selection == s, true);
			drawObjectPort(g, r.x + 8, r.y + 8, r.width - 16, 16, Icons.getIcon(s.getClass(), 16), s.toString(), false, true);
		}
		for (Map.Entry<Rectangle,PixelProgram.DeviceInfo> e : deviceRects.entrySet()) {
			Rectangle r = e.getKey();
			PixelProgram.DeviceInfo d = e.getValue();
			drawObjectBox(g, r.x, r.y, r.width, r.height, selection == d, d.isActive());
			drawObjectPort(g, r.x + 8, r.y + 8, r.width - 16, 16, Icons.getIcon(DeviceType.class, d.type(), 16), d.name(), false, false);
			int portY = r.y + 28;
			for (PixelProgram.DeviceStringInfo s : d.getStrings()) {
				drawObjectPort(g, r.x + 8, portY, r.width - 16, 16, Icons.getIcon(StringType.class, s.type(), 16), s.name(), true, false);
				portY += 20;
			}
		}
		for (PusherThread pt : program.threadSet()) {
			Point p1 = portPoints.get(pt.sequence);
			Point p2 = portPoints.get(pt.string);
			if (p1 != null && p2 != null) {
				drawWire(g, p1.x, p1.y, p2.x, p2.y);
			}
		}
		if (tempWireStart != null && tempWireEnd != null) {
			drawWire(g, tempWireStart.x, tempWireStart.y, tempWireEnd.x, tempWireEnd.y);
		}
	}
	
	private static final Color BOX_TOP_COLOR_ACTIVE = new Color(0xFF666666, false);
	private static final Color BOX_BTM_COLOR_ACTIVE = new Color(0xFF333333, false);
	private static final Color BOX_TOP_COLOR_INACTIVE = new Color(0x80666666, true);
	private static final Color BOX_BTM_COLOR_INACTIVE = new Color(0x80333333, true);
	private static final Color BOX_STK_COLOR_ACTIVE = Color.black;
	private static final Color BOX_STK_COLOR_INACTIVE = Color.gray;
	private static final Color BOX_STK_COLOR_SELECTED = Color.orange;
	private static final BasicStroke BOX_STROKE = new BasicStroke(2);
	
	private static void drawObjectBox(Graphics2D g, int x, int y, int w, int h, boolean sel, boolean active) {
		Color top = active ? BOX_TOP_COLOR_ACTIVE : BOX_TOP_COLOR_INACTIVE;
		Color btm = active ? BOX_BTM_COLOR_ACTIVE : BOX_BTM_COLOR_INACTIVE;
		g.setPaint(new GradientPaint(x, y, top, x, y + h, btm));
		Shape box = new RoundRectangle2D.Float(x + 3, y + 3, w - 6, h - 6, 8, 8);
		g.fill(box);
		g.setPaint(active ? BOX_STK_COLOR_ACTIVE : BOX_STK_COLOR_INACTIVE);
		g.setStroke(BOX_STROKE);
		g.draw(box);
		if (sel) {
			g.setPaint(BOX_STK_COLOR_SELECTED);
			g.draw(new RoundRectangle2D.Float(x + 4, y + 4, w - 8, h - 8, 6, 6));
		}
	}
	
	private static final Color TEXT_COLOR = Color.white;
	private static final Image INPUT_ICON;
	private static final Image OUTPUT_ICON;
	static {
		Image input;
		Image output;
		try {
			input = ImageIO.read(ProgramComponent.class.getResource("input.png"));
			output = ImageIO.read(ProgramComponent.class.getResource("output.png"));
		} catch (IOException e) {
			input = null;
			output = null;
		}
		INPUT_ICON = input;
		OUTPUT_ICON = output;
	}
	
	private static void drawObjectPort(Graphics2D g, int x, int y, int w, int h, Image icon, String text, boolean isIn, boolean isOut) {
		Shape clip = g.getClip();
		g.clipRect(x, y, w, h);
		int textX = x;
		if (icon != null) {
			int iconHeight;
			do iconHeight = icon.getHeight(null);
			while (iconHeight < 0);
			g.drawImage(icon, x, y + (h - iconHeight) / 2, null);
			int iconWidth;
			do iconWidth = icon.getWidth(null);
			while (iconWidth < 0);
			textX += iconWidth + 4;
		}
		if (text != null) {
			FontMetrics fm = g.getFontMetrics();
			int textY = y + (h - fm.getHeight()) / 2 + fm.getAscent();
			g.setPaint(TEXT_COLOR);
			g.drawString(text, textX, textY);
		}
		g.setClip(clip);
		if (isIn && INPUT_ICON != null) {
			int iconWidth;
			do iconWidth = INPUT_ICON.getWidth(null);
			while (iconWidth < 0);
			int iconHeight;
			do iconHeight = INPUT_ICON.getHeight(null);
			while (iconHeight < 0);
			g.drawImage(INPUT_ICON, x - iconWidth - 6, y + (h - iconHeight) / 2, null);
		}
		if (isOut && OUTPUT_ICON != null) {
			int iconHeight;
			do iconHeight = OUTPUT_ICON.getHeight(null);
			while (iconHeight < 0);
			g.drawImage(OUTPUT_ICON, x + w + 6, y + (h - iconHeight) / 2, null);
		}
	}
	
	private static final Color WIRE_COLOR = Color.green;
	private static final BasicStroke WIRE_STROKE = new BasicStroke(2);
	
	private static void drawWire(Graphics2D g, int x1, int y1, int x2, int y2) {
		g.setPaint(WIRE_COLOR);
		g.setStroke(WIRE_STROKE);
		g.draw(new CubicCurve2D.Float(x1, y1, (x1 + x2) / 2f, y1, (x1 + x2) / 2f, y2, x2, y2));
	}
	
	private final MyMouseListener mouseListener = new MyMouseListener();
	private class MyMouseListener extends MouseAdapter {
		@Override
		public void mousePressed(MouseEvent e) {
			tempWireStart = null;
			tempWireEnd = null;
			tempWireSequence = null;
			tempWireDevice = null;
			for (Map.Entry<Rectangle,PixelProgram.DeviceStringInfo> entry : devicePortRects.entrySet()) {
				if (entry.getKey().contains(e.getPoint())) {
					PusherThread t = program.getThread(entry.getValue());
					if (t != null && portPoints.containsKey(t.sequence)) {
						tempWireStart = portPoints.get(t.sequence);
						tempWireEnd = portPoints.get(entry.getValue());
						tempWireSequence = t.sequence;
						program.disconnect(t.sequence);
					} else if (portPoints.containsKey(entry.getValue())) {
						tempWireStart = tempWireEnd = portPoints.get(entry.getValue());
						tempWireDevice = entry.getValue();
					}
					repaint();
					return;
				}
			}
			for (Map.Entry<Rectangle,PixelSequence> entry : sequencePortRects.entrySet()) {
				if (entry.getKey().contains(e.getPoint())) {
					PusherThread t = program.getThread(entry.getValue());
					if (t != null && t.string instanceof PixelProgram.DeviceStringInfo && portPoints.containsKey(t.string)) {
						tempWireStart = portPoints.get(t.string);
						tempWireEnd = portPoints.get(entry.getValue());
						tempWireDevice = (PixelProgram.DeviceStringInfo)t.string;
						program.disconnect(t.string);
					} else if (portPoints.containsKey(entry.getValue())) {
						tempWireStart = tempWireEnd = portPoints.get(entry.getValue());
						tempWireSequence = entry.getValue();
					}
					repaint();
					return;
				}
			}
			for (Map.Entry<Rectangle,PixelProgram.DeviceInfo> entry : deviceRects.entrySet()) {
				if (entry.getKey().contains(e.getPoint())) {
					selection = entry.getValue();
					repaint();
					return;
				}
			}
			for (Map.Entry<Rectangle,PixelSequence> entry : sequenceRects.entrySet()) {
				if (entry.getKey().contains(e.getPoint())) {
					selection = entry.getValue();
					repaint();
					return;
				}
			}
			selection = null;
			repaint();
		}
		@Override
		public void mouseDragged(MouseEvent e) {
			if (tempWireSequence != null) {
				for (Map.Entry<Rectangle,PixelProgram.DeviceStringInfo> entry : devicePortRects.entrySet()) {
					if (entry.getKey().contains(e.getPoint())) {
						tempWireEnd = portPoints.get(entry.getValue());
						repaint();
						return;
					}
				}
				tempWireEnd = e.getPoint();
				repaint();
			}
			if (tempWireDevice != null) {
				for (Map.Entry<Rectangle,PixelSequence> entry : sequencePortRects.entrySet()) {
					if (entry.getKey().contains(e.getPoint())) {
						tempWireEnd = portPoints.get(entry.getValue());
						repaint();
						return;
					}
				}
				tempWireEnd = e.getPoint();
				repaint();
			}
		}
		@Override
		public void mouseReleased(MouseEvent e) {
			if (tempWireSequence != null) {
				for (Map.Entry<Rectangle,PixelProgram.DeviceStringInfo> entry : devicePortRects.entrySet()) {
					if (entry.getKey().contains(e.getPoint())) {
						program.connect(tempWireSequence, entry.getValue());
						break;
					}
				}
			}
			if (tempWireDevice != null) {
				for (Map.Entry<Rectangle,PixelSequence> entry : sequencePortRects.entrySet()) {
					if (entry.getKey().contains(e.getPoint())) {
						program.connect(entry.getValue(), tempWireDevice);
						break;
					}
				}
			}
			tempWireStart = null;
			tempWireEnd = null;
			tempWireSequence = null;
			tempWireDevice = null;
			repaint();
		}
		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() > 1) {
				if (selection != null) {
					openConfiguration();
				} else {
					int x = e.getX() - lastKnownInsets.left;
					if (x >= lastKnownSize.width - 200) {
						openAddDevice();
					} else if (x < 200) {
						openAddSequence();
					}
				}
			}
		}
	}
	
	private AddSequenceFrame addSequenceFrame;
	private AddDeviceFrame addDeviceFrame;
	
	public void openAddSequence() {
		if (addSequenceFrame == null) {
			addSequenceFrame = new AddSequenceFrame(sequenceLoader, program);
			addSequenceFrame.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosed(WindowEvent e) {
					addSequenceFrame = null;
				}
			});
		}
		addSequenceFrame.setVisible(true);
	}
	
	public void openAddDevice() {
		if (addDeviceFrame == null) {
			addDeviceFrame = new AddDeviceFrame(deviceLoader, program);
			addDeviceFrame.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosed(WindowEvent e) {
					addDeviceFrame = null;
				}
			});
		}
		addDeviceFrame.setVisible(true);
	}
	
	public void openConfiguration() {
		if (selection instanceof PixelSequence) {
			if (SequenceConfigurationFrame.open((PixelSequence)selection) != null) {
				for (PixelProgramListener l : program.getPixelProgramListeners()) {
					l.pixelProgramChanged(program);
				}
			}
		} else if (selection instanceof PixelProgram.DeviceInfo) {
			DeviceConfigurationFrame.open((PixelProgram.DeviceInfo)selection);
		}
	}
	
	@Override
	public void finalize() {
		if (addSequenceFrame != null) addSequenceFrame.dispose();
		if (addDeviceFrame != null) addDeviceFrame.dispose();
	}
}
