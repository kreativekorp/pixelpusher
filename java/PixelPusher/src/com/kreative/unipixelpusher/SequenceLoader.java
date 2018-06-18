package com.kreative.unipixelpusher;

import java.util.Collection;
import java.util.Collections;
import java.util.ServiceLoader;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

public class SequenceLoader {
	private SortedSet<SequenceInfo> infoSet;
	private SortedMap<String,SequenceInfo> infoMap;
	
	public SequenceLoader() {
		this.infoSet = new TreeSet<SequenceInfo>();
		this.infoMap = new TreeMap<String,SequenceInfo>();
		for (PixelSequenceFactory f : ServiceLoader.load(PixelSequenceFactory.class)) {
			for (int i = 0, n = f.size(); i < n; i++) {
				SequenceInfo info = new SequenceInfo(f, i);
				infoSet.add(info);
				infoMap.put(info.name, info);
			}
		}
	}
	
	public Collection<SequenceInfo> getSequences() {
		return Collections.unmodifiableCollection(infoSet);
	}
	
	public Collection<String> getSequenceNames() {
		return Collections.unmodifiableCollection(infoMap.keySet());
	}
	
	public SequenceInfo getSequence(String name) {
		return infoMap.get(name);
	}
	
	public static final class SequenceInfo implements Comparable<SequenceInfo> {
		private final String name;
		private final Class<? extends PixelSequence> cls;
		private final PixelSequenceFactory factory;
		private final int index;
		
		private SequenceInfo(PixelSequenceFactory f, int i) {
			this.name = f.getName(i);
			this.cls = f.getClass(i);
			this.factory = f;
			this.index = i;
		}
		
		public String getSequenceName() {
			return this.name;
		}
		
		public Class<? extends PixelSequence> getSequenceClass() {
			return this.cls;
		}
		
		public PixelSequence createInstance() {
			return this.factory.createInstance(this.index);
		}
		
		@Override
		public int compareTo(SequenceInfo that) {
			return this.name.compareToIgnoreCase(that.name);
		}
		
		@Override
		public String toString() {
			return this.name;
		}
	}
	
	public static void main(String[] args) {
		SequenceLoader loader = new SequenceLoader();
		for (String name : loader.getSequenceNames()) {
			System.out.println(name);
		}
	}
}
