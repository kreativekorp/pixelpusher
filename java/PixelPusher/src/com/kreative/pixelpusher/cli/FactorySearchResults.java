package com.kreative.pixelpusher.cli;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;
import com.kreative.pixelpusher.pixelset.PixelSet;
import com.kreative.pixelpusher.pixelset.PixelSetFactory;

public class FactorySearchResults {
	private List<PixelSetFactory<? extends PixelSet>> factories;
	
	private FactorySearchResults() {
		factories = new ArrayList<PixelSetFactory<? extends PixelSet>>();
	}
	
	private void add(PixelSetFactory<? extends PixelSet> factory) {
		if (!factories.contains(factory)) {
			factories.add(factory);
		}
	}
	
	private void sort() {
		Collections.sort(factories, new Comparator<PixelSetFactory<? extends PixelSet>>() {
			@Override
			public int compare(PixelSetFactory<? extends PixelSet> a, PixelSetFactory<? extends PixelSet> b) {
				return a.getNameForHumans().compareToIgnoreCase(b.getNameForHumans());
			}
		});
	}
	
	public List<PixelSetFactory<? extends PixelSet>> getFactories() {
		return factories;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static FactorySearchResults forPattern(StringSearchPattern fsp) {
		FactorySearchResults results = new FactorySearchResults();
		ServiceLoader<PixelSetFactory> loader = ServiceLoader.load(PixelSetFactory.class);
		Iterator<PixelSetFactory> factoryIterator = loader.iterator();
		while (factoryIterator.hasNext()) {
			PixelSetFactory factory = factoryIterator.next();
			if (fsp == null || fsp.matches(factory)) {
				results.add(factory);
			}
		}
		results.sort();
		return results;
	}
}
