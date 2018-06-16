package com.kreative.unipixelpusher.marquee;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class MarqueeItemString extends MarqueeItem implements List<MarqueeItem> {
	protected final List<MarqueeItem> items = new ArrayList<MarqueeItem>();
	protected BufferedImage image = null;
	protected long lastTick = 0;
	
	private final synchronized void update(long tick) {
		if (image != null && lastTick == tick) return;
		int w = 0;
		int h = 0;
		for (MarqueeItem item : items) {
			w += item.getOuterWidth(tick);
			h = Math.max(h, item.getOuterHeight(tick));
		}
		if (w <= 0 || h <= 0) {
			image = null;
		} else {
			image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = image.createGraphics();
			int x = 0;
			for (MarqueeItem item : items) {
				w = item.getOuterWidth(tick);
				item.paint(g, x, 0, w, h, tick);
				x += w;
			}
			g.dispose();
		}
		lastTick = tick;
	}
	
	@Override
	public final synchronized int getInnerWidth(long tick) {
		update(tick);
		return (image != null) ? image.getWidth() : 0;
	}
	
	@Override
	public final synchronized int getInnerHeight(long tick) {
		update(tick);
		return (image != null) ? image.getHeight() : 0;
	}
	
	@Override
	protected final synchronized void paintContent(Graphics2D g, int x, int y, long tick) {
		update(tick);
		if (image != null) g.drawImage(image, null, x, y);
	}
	
	@Override
	public long getUpdateInterval() {
		return 20;
	}
	
	@Override
	public final synchronized boolean add(MarqueeItem e) {
		return items.add(e);
	}
	
	@Override
	public final synchronized void add(int index, MarqueeItem element) {
		items.add(index, element);
	}
	
	@Override
	public final synchronized boolean addAll(Collection<? extends MarqueeItem> c) {
		return items.addAll(c);
	}
	
	@Override
	public final synchronized boolean addAll(int index, Collection<? extends MarqueeItem> c) {
		return items.addAll(index, c);
	}
	
	@Override
	public final synchronized void clear() {
		items.clear();
	}
	
	@Override
	public final synchronized boolean contains(Object o) {
		return items.contains(o);
	}
	
	@Override
	public final synchronized boolean containsAll(Collection<?> c) {
		return items.containsAll(c);
	}
	
	@Override
	public final synchronized MarqueeItem get(int index) {
		return items.get(index);
	}
	
	@Override
	public final synchronized int indexOf(Object o) {
		return items.indexOf(o);
	}
	
	@Override
	public final synchronized boolean isEmpty() {
		return items.isEmpty();
	}
	
	@Override
	public final synchronized Iterator<MarqueeItem> iterator() {
		return items.iterator();
	}
	
	@Override
	public final synchronized int lastIndexOf(Object o) {
		return items.lastIndexOf(o);
	}
	
	@Override
	public final synchronized ListIterator<MarqueeItem> listIterator() {
		return items.listIterator();
	}
	
	@Override
	public final synchronized ListIterator<MarqueeItem> listIterator(int index) {
		return items.listIterator(index);
	}
	
	@Override
	public final synchronized boolean remove(Object o) {
		return items.remove(o);
	}
	
	@Override
	public final synchronized MarqueeItem remove(int index) {
		return items.remove(index);
	}
	
	@Override
	public final synchronized boolean removeAll(Collection<?> c) {
		return items.removeAll(c);
	}
	
	@Override
	public final synchronized boolean retainAll(Collection<?> c) {
		return items.retainAll(c);
	}
	
	@Override
	public final synchronized MarqueeItem set(int index, MarqueeItem element) {
		return items.set(index, element);
	}
	
	@Override
	public final synchronized int size() {
		return items.size();
	}
	
	@Override
	public final synchronized List<MarqueeItem> subList(int fromIndex, int toIndex) {
		return items.subList(fromIndex, toIndex);
	}
	
	@Override
	public final synchronized Object[] toArray() {
		return items.toArray();
	}
	
	@Override
	public final synchronized <T> T[] toArray(T[] a) {
		return items.toArray(a);
	}
}
