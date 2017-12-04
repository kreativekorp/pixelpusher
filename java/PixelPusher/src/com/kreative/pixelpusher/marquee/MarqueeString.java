package com.kreative.pixelpusher.marquee;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import com.kreative.pixelpusher.common.MathUtilities;

public class MarqueeString extends MarqueeItem implements List<MarqueeItem> {
	private static final long serialVersionUID = 1L;
	
	protected List<MarqueeItem> items;
	protected transient BufferedImage image;
	
	public MarqueeString() {
		super();
		items = new ArrayList<MarqueeItem>();
		image = null;
	}
	
	public MarqueeString(MarqueeItem... items) {
		super();
		this.items = new ArrayList<MarqueeItem>();
		for (MarqueeItem item : items) {
			this.items.add(item.clone());
		}
		image = null;
	}
	
	public MarqueeString(Collection<? extends MarqueeItem> items) {
		super();
		this.items = new ArrayList<MarqueeItem>();
		for (MarqueeItem item : items) {
			this.items.add(item.clone());
		}
		image = null;
	}
	
	public MarqueeString(MarqueeString toClone) {
		super(toClone);
		this.items = new ArrayList<MarqueeItem>();
		for (MarqueeItem item : toClone.items) {
			this.items.add(item.clone());
		}
		image = null;
	}
	
	@Override
	public synchronized MarqueeString clone() {
		return new MarqueeString(this);
	}
	
	@Override
	public synchronized int getMsPerFrame() {
		if (!items.isEmpty()) {
			int msPerFrame = items.get(0).getMsPerFrame();
			if (msPerFrame <= 20) return 20;
			for (int i = 1; i < items.size(); i++) {
				msPerFrame = MathUtilities.gcd(msPerFrame, items.get(i).getMsPerFrame());
				if (msPerFrame <= 20) return 20;
			}
			return msPerFrame;
		} else {
			return 1000;
		}
	}
	
	@Override
	public synchronized void updatePixels(long tick) {
		int w = 0;
		int h = 0;
		for (MarqueeItem item : items) {
			item.updatePixels(tick);
			w += item.getOuterWidth();
			h = Math.max(h, item.getOuterHeight());
		}
		if (w <= 0 || h <= 0) {
			image = null;
		} else {
			image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = image.createGraphics();
			int x = 0;
			for (MarqueeItem item : items) {
				w = item.getOuterWidth();
				item.paint(g, x, 0, w, h);
				x += w;
			}
			g.dispose();
		}
	}
	
	@Override
	public synchronized int getInnerWidth() {
		if (image != null) {
			return image.getWidth();
		} else {
			return 0;
		}
	}
	
	@Override
	public synchronized int getInnerHeight() {
		if (image != null) {
			return image.getHeight();
		} else {
			return 0;
		}
	}
	
	@Override
	protected synchronized void paintContent(Graphics2D g, int x, int y) {
		if (image != null) {
			g.drawImage(image, null, x, y);
		}
	}
	
	@Override
	public synchronized boolean add(MarqueeItem e) {
		return items.add(e);
	}
	
	@Override
	public synchronized void add(int index, MarqueeItem element) {
		items.add(index, element);
	}
	
	@Override
	public synchronized boolean addAll(Collection<? extends MarqueeItem> c) {
		return items.addAll(c);
	}
	
	@Override
	public synchronized boolean addAll(int index, Collection<? extends MarqueeItem> c) {
		return items.addAll(index, c);
	}
	
	@Override
	public synchronized void clear() {
		items.clear();
	}
	
	@Override
	public synchronized boolean contains(Object o) {
		return items.contains(o);
	}
	
	@Override
	public synchronized boolean containsAll(Collection<?> c) {
		return items.containsAll(c);
	}
	
	@Override
	public synchronized MarqueeItem get(int index) {
		return items.get(index);
	}
	
	@Override
	public synchronized int indexOf(Object o) {
		return items.indexOf(o);
	}
	
	@Override
	public synchronized boolean isEmpty() {
		return items.isEmpty();
	}
	
	@Override
	public synchronized Iterator<MarqueeItem> iterator() {
		return items.iterator();
	}
	
	@Override
	public synchronized int lastIndexOf(Object o) {
		return items.lastIndexOf(o);
	}
	
	@Override
	public synchronized ListIterator<MarqueeItem> listIterator() {
		return items.listIterator();
	}
	
	@Override
	public synchronized ListIterator<MarqueeItem> listIterator(int index) {
		return items.listIterator(index);
	}
	
	@Override
	public synchronized boolean remove(Object o) {
		return items.remove(o);
	}
	
	@Override
	public synchronized MarqueeItem remove(int index) {
		return items.remove(index);
	}
	
	@Override
	public synchronized boolean removeAll(Collection<?> c) {
		return items.removeAll(c);
	}
	
	@Override
	public synchronized boolean retainAll(Collection<?> c) {
		return items.retainAll(c);
	}
	
	@Override
	public synchronized MarqueeItem set(int index, MarqueeItem element) {
		return items.set(index, element);
	}
	
	@Override
	public synchronized int size() {
		return items.size();
	}
	
	@Override
	public synchronized List<MarqueeItem> subList(int fromIndex, int toIndex) {
		return items.subList(fromIndex, toIndex);
	}
	
	@Override
	public synchronized Object[] toArray() {
		return items.toArray();
	}
	
	@Override
	public synchronized <T> T[] toArray(T[] a) {
		return items.toArray(a);
	}
}
