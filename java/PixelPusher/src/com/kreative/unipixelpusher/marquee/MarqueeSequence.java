package com.kreative.unipixelpusher.marquee;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import com.kreative.imagetool.animation.Animation;
import com.kreative.imagetool.animation.AnimationIO;
import com.kreative.imagetool.gif.GIFFile;
import com.kreative.unipixelpusher.AbstractPixelSequence;
import com.kreative.unipixelpusher.PixelString;
import com.kreative.unipixelpusher.SequenceConfiguration;

public class MarqueeSequence extends AbstractPixelSequence {
	public static final String name = "Marquee";
	
	private static MarqueeItem createHelloWorld() {
		MarqueeItemString helloWorld = new MarqueeItemString();
		StaticTextItem hello = new StaticTextItem("Hello, world!");
		hello.setPadding(1, 4, 1, 4);
		helloWorld.add(hello);
		try {
			InputStream in = MarqueeSequence.class.getResourceAsStream("globe8.gif");
			GIFFile gif = new GIFFile();
			gif.read(new DataInputStream(in));
			in.close();
			Animation a = AnimationIO.fromGIFFile(gif);
			AnimationItem world = new AnimationItem(a);
			helloWorld.add(world);
		} catch (IOException e) {
			// Ignored.
		}
		return helloWorld;
	}
	
	private String itemName;
	private MarqueeItem item;
	
	public MarqueeSequence() {
		this.itemName = "Hello, world!";
		this.item = createHelloWorld();
	}
	
	public MarqueeSequence(String itemName, MarqueeItem item) {
		this.itemName = itemName;
		this.item = item;
	}
	
	public synchronized String getMarqueeItemName() {
		return this.itemName;
	}
	
	public synchronized MarqueeItem getMarqueeItem() {
		return this.item;
	}
	
	public synchronized void setMarqueeItem(String itemName, MarqueeItem item) {
		this.itemName = itemName;
		this.item = item;
	}
	
	private long tick;
	
	@Override
	public synchronized void update(PixelString ps, long tick) {
		super.update(ps, this.tick = tick);
	}
	
	@Override
	public synchronized void updateFrame(PixelString ps, long frame, boolean frameChanged) {
		int w = item.getOuterWidth(tick);
		int h = ps.getRowCount();
		if (w < 1 || h < 1) return;
		
		BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = image.createGraphics();
		item.paint(g, 0, 0, w, h, tick);
		g.dispose();
		
		int x = (int)(this.frame = (frame % w));
		int[] rgb = new int[h];
		for (int i = 0, n = ps.getColumnCount(); i < n; i++) {
			image.getRGB(x, 0, 1, h, rgb, 0, 1);
			for (int y = 0; y < h; y++) ps.setPixel(y, i, rgb[y]);
			x++; if (x >= w) x = 0;
		}
		
		ps.push();
	}
	
	@Override
	public synchronized long getFrameCount(PixelString ps) {
		return 0;
	}
	
	@Override
	public long getFrameDuration() {
		return 100;
	}
	
	@Override
	public long getUpdateInterval() {
		return 20;
	}
	
	@Override
	public synchronized void loadConfiguration(SequenceConfiguration config) {
		super.loadConfiguration(config);
		this.itemName = config.get("itemName");
		try {
			this.item = config.get("item", MarqueeItem.class).newInstance();
			this.item.loadConfiguration(config, "item");
		} catch (Exception e) {
			this.item = null;
		}
	}
	
	@Override
	public synchronized void saveConfiguration(SequenceConfiguration config) {
		super.saveConfiguration(config);
		config.put("itemName", itemName);
		if (item == null) {
			config.put("item", (String)null);
		} else {
			config.put("item", item.getClass());
			item.saveConfiguration(config, "item");
		}
	}
	
	@Override
	public String toString() {
		return name;
	}
}
