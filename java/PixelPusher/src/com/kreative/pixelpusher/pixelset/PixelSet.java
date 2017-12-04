package com.kreative.pixelpusher.pixelset;

import java.io.Serializable;
import java.util.Collection;
import com.kreative.pixelpusher.array.PixelArray;
import com.kreative.pixelpusher.sequence.PixelSequence;

public abstract class PixelSet implements Cloneable, Serializable {
	private static final long serialVersionUID = 1L;
	
	@Override public abstract PixelSet clone();
	public abstract boolean isSequence();
	public abstract PixelSequence sequence();
	public abstract boolean isArray();
	public abstract PixelArray array();
	public abstract int getMsPerFrame();
	public abstract void updatePixels(long tick);
	
	public synchronized Collection<? extends PixelSet> getDependencies() {
		return null;
	}
	
	public final synchronized boolean dependsOn(PixelSet pixelSet) {
		if (pixelSet == null) return false;
		if (pixelSet == this) return true;
		Collection<? extends PixelSet> deps = getDependencies();
		if (deps == null || deps.isEmpty()) return false;
		if (deps.contains(pixelSet)) return true;
		for (PixelSet dep : deps) {
			if (dep == null) continue;
			if (dep.dependsOn(pixelSet)) return true;
		}
		return false;
	}
	
	protected final synchronized void checkForCycles(PixelSet pixelSet) {
		if (pixelSet != null && pixelSet.dependsOn(this)) {
			throw new IllegalStateException("Cycle detected in sequence graph.");
		}
	}
}
