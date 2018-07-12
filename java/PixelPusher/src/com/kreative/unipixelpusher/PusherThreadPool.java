package com.kreative.unipixelpusher;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PusherThreadPool {
	private Map<PixelSequence,PusherThread> sequenceThreads;
	private Map<PixelString,PusherThread> stringThreads;
	private Set<PusherThread> threads;
	
	public PusherThreadPool() {
		this.sequenceThreads = new HashMap<PixelSequence,PusherThread>();
		this.stringThreads = new HashMap<PixelString,PusherThread>();
		this.threads = new HashSet<PusherThread>();
	}
	
	public synchronized void connect(PixelSequence sequence, PixelString string) {
		disconnect(sequence);
		disconnect(string);
		PusherThread thread = new PusherThread(sequence, string);
		sequenceThreads.put(sequence, thread);
		stringThreads.put(string, thread);
		threads.add(thread);
		thread.start();
	}
	
	public synchronized void disconnect(PixelSequence sequence, PixelString string) {
		disconnect(sequence);
		disconnect(string);
	}
	
	public synchronized void disconnect(PixelSequence sequence) {
		PusherThread thread = sequenceThreads.remove(sequence);
		if (thread != null) {
			stringThreads.remove(thread.string);
			threads.remove(thread);
			thread.interrupt();
		}
	}
	
	public synchronized void disconnect(PixelString string) {
		PusherThread thread = stringThreads.remove(string);
		if (thread != null) {
			sequenceThreads.remove(thread.sequence);
			threads.remove(thread);
			thread.interrupt();
		}
	}
	
	public synchronized void disconnectAll() {
		for (PusherThread thread : threads) thread.interrupt();
		this.sequenceThreads = new HashMap<PixelSequence,PusherThread>();
		this.stringThreads = new HashMap<PixelString,PusherThread>();
		this.threads = new HashSet<PusherThread>();
	}
	
	public synchronized PusherThread getThread(PixelSequence sequence) {
		return sequenceThreads.get(sequence);
	}
	
	public synchronized PusherThread getThread(PixelString string) {
		return stringThreads.get(string);
	}
	
	public synchronized Set<PusherThread> threadSet() {
		return Collections.unmodifiableSet(new HashSet<PusherThread>(threads));
	}
}
