package com.kreative.pixelpusher.device;

import java.awt.Image;
import com.heroicrobot.dropbit.devices.pixelpusher.Pixel;
import com.heroicrobot.dropbit.devices.pixelpusher.PixelPusher;
import com.heroicrobot.dropbit.devices.pixelpusher.Strip;
import com.kreative.pixelpusher.array.ArrayOrdering;
import com.kreative.pixelpusher.array.PixelArrayToSequence;
import com.kreative.pixelpusher.pixelset.PixelSet;
import com.kreative.pixelpusher.sequence.PixelSequence;

public class StripInfo implements Comparable<StripInfo> {
	private String name;
	private String macAddress;
	private String ipAddress;
	private Integer groupOrdinal;
	private Integer controllerOrdinal;
	private Integer artnetUniverse;
	private Integer artnetChannel;
	private int strip;
	private StripType type;
	private GammaCorrector gamma;
	private ArrayOrdering ordering;
	private int rows;
	private int columns;
	private int length;
	private PixelSet pixelSet;
	private PixelSequence pixelSequence;
	private PixelArrayToSequence pixelArrayToSequence;
	private transient int[] colors;
	private transient Pixel[] pixels;
	
	private StripInfo() {}
	
	public static StripInfo forMacAddress(String macAddress, int strip, int length) {
		StripInfo info = new StripInfo();
		info.setName(null);
		info.setMacAddress(macAddress);
		info.setStripNumber(strip);
		info.setStripType(StripType.BEST_GUESS);
		info.setGammaCorrector(GammaCorrector.DEFAULT);
		info.setOrdering(ArrayOrdering.LTR_TTB);
		info.setRowCount(1);
		info.setColumnCount(length);
		info.setLength(length);
		info.setPixelSet(null);
		info.colors = new int[length];
		info.pixels = new Pixel[length];
		return info;
	}
	
	public static StripInfo forMacAddress(PixelPusher pusher, Strip strip) {
		return forMacAddress(pusher.getMacAddress(), strip.getStripNumber(), strip.getLength());
	}
	
	public static StripInfo forIpAddress(String ipAddress, int strip, int length) {
		StripInfo info = new StripInfo();
		info.setName(null);
		info.setIpAddress(ipAddress);
		info.setStripNumber(strip);
		info.setStripType(StripType.BEST_GUESS);
		info.setGammaCorrector(GammaCorrector.DEFAULT);
		info.setOrdering(ArrayOrdering.LTR_TTB);
		info.setRowCount(1);
		info.setColumnCount(length);
		info.setLength(length);
		info.setPixelSet(null);
		info.colors = new int[length];
		info.pixels = new Pixel[length];
		return info;
	}
	
	public static StripInfo forIpAddress(PixelPusher pusher, Strip strip) {
		return forIpAddress(pusher.getIp().getHostAddress(), strip.getStripNumber(), strip.getLength());
	}
	
	public static StripInfo forOrdinal(int groupOrdinal, int controllerOrdinal, int strip, int length) {
		StripInfo info = new StripInfo();
		info.setName(null);
		info.setOrdinal(groupOrdinal, controllerOrdinal);
		info.setStripNumber(strip);
		info.setStripType(StripType.BEST_GUESS);
		info.setGammaCorrector(GammaCorrector.DEFAULT);
		info.setOrdering(ArrayOrdering.LTR_TTB);
		info.setRowCount(1);
		info.setColumnCount(length);
		info.setLength(length);
		info.setPixelSet(null);
		info.colors = new int[length];
		info.pixels = new Pixel[length];
		return info;
	}
	
	public static StripInfo forOrdinal(PixelPusher pusher, Strip strip) {
		return forOrdinal(pusher.getGroupOrdinal(), pusher.getControllerOrdinal(), strip.getStripNumber(), strip.getLength());
	}
	
	public static StripInfo forArtnet(int artnetUniverse, int artnetChannel, int strip, int length) {
		StripInfo info = new StripInfo();
		info.setName(null);
		info.setArtnet(artnetUniverse, artnetChannel);
		info.setStripNumber(strip);
		info.setStripType(StripType.BEST_GUESS);
		info.setGammaCorrector(GammaCorrector.DEFAULT);
		info.setOrdering(ArrayOrdering.LTR_TTB);
		info.setRowCount(1);
		info.setColumnCount(length);
		info.setLength(length);
		info.setPixelSet(null);
		info.colors = new int[length];
		info.pixels = new Pixel[length];
		return info;
	}
	
	public static StripInfo forArtnet(PixelPusher pusher, Strip strip) {
		return forArtnet(pusher.getArtnetUniverse(), pusher.getArtnetChannel(), strip.getStripNumber(), strip.getLength());
	}
	
	public synchronized boolean hasName() {
		return this.name != null;
	}
	
	public synchronized String getName() {
		if (this.name != null) {
			return this.name;
		} else if (this.macAddress != null) {
			return "MAC@" + this.macAddress + " S#" + this.strip;
		} else if (this.ipAddress != null) {
			return "IP@" + this.ipAddress + " S#" + this.strip;
		} else if (this.groupOrdinal != null && this.controllerOrdinal != null) {
			return "G#" + this.groupOrdinal + " C#" + this.controllerOrdinal + " S#" + this.strip;
		} else if (this.artnetUniverse != null && this.artnetChannel != null) {
			return "U#" + this.artnetUniverse + " Ch#" + this.artnetChannel + " S#" + this.strip;
		} else {
			return super.toString();
		}
	}
	
	public synchronized void setName(String name) {
		this.name = name;
	}
	
	public synchronized Image getIcon(int size) {
		return this.type.getIcon(rows, columns, length, size);
	}
	
	public synchronized boolean hasMacAddress() {
		return this.macAddress != null;
	}
	
	public synchronized String getMacAddress() {
		return this.macAddress;
	}
	
	public synchronized void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
		this.ipAddress = null;
		this.groupOrdinal = null;
		this.controllerOrdinal = null;
		this.artnetUniverse = null;
		this.artnetChannel = null;
	}
	
	public synchronized void setMacAddress(PixelPusher pusher) {
		setMacAddress(pusher.getMacAddress());
	}
	
	public synchronized boolean hasIpAddress() {
		return this.ipAddress != null;
	}
	
	public synchronized String getIpAddress() {
		return this.ipAddress;
	}
	
	public synchronized void setIpAddress(String ipAddress) {
		this.macAddress = null;
		this.ipAddress = ipAddress;
		this.groupOrdinal = null;
		this.controllerOrdinal = null;
		this.artnetUniverse = null;
		this.artnetChannel = null;
	}
	
	public synchronized void setIpAddress(PixelPusher pusher) {
		setIpAddress(pusher.getIp().getHostAddress());
	}
	
	public synchronized boolean hasOrdinal() {
		return (this.groupOrdinal != null) && (this.controllerOrdinal != null);
	}
	
	public synchronized int getGroupOrdinal() {
		return (this.groupOrdinal != null) ? this.groupOrdinal : (-1);
	}
	
	public synchronized int getControllerOrdinal() {
		return (this.controllerOrdinal != null) ? this.controllerOrdinal : (-1);
	}
	
	public synchronized void setOrdinal(int groupOrdinal, int controllerOrdinal) {
		this.macAddress = null;
		this.ipAddress = null;
		this.groupOrdinal = groupOrdinal;
		this.controllerOrdinal = controllerOrdinal;
		this.artnetUniverse = null;
		this.artnetChannel = null;
	}
	
	public synchronized void setOrdinal(PixelPusher pusher) {
		setOrdinal(pusher.getGroupOrdinal(), pusher.getControllerOrdinal());
	}
	
	public synchronized boolean hasArtnet() {
		return (this.artnetUniverse != null) && (this.artnetChannel != null);
	}
	
	public synchronized int getArtnetUniverse() {
		return (this.artnetUniverse != null) ? this.artnetUniverse : (-1);
	}
	
	public synchronized int getArtnetChannel() {
		return (this.artnetChannel != null) ? this.artnetChannel : (-1);
	}
	
	public synchronized void setArtnet(int artnetUniverse, int artnetChannel) {
		this.macAddress = null;
		this.ipAddress = null;
		this.groupOrdinal = null;
		this.controllerOrdinal = null;
		this.artnetUniverse = artnetUniverse;
		this.artnetChannel = artnetChannel;
	}
	
	public synchronized void setArtnet(PixelPusher pusher) {
		setArtnet(pusher.getArtnetUniverse(), pusher.getArtnetChannel());
	}
	
	public synchronized int getStripNumber() {
		return this.strip;
	}
	
	public synchronized void setStripNumber(int strip) {
		this.strip = strip;
	}
	
	public synchronized void setStripNumber(Strip strip) {
		setStripNumber(strip.getStripNumber());
	}
	
	public synchronized boolean matches(PixelPusher pusher) {
		if (this.hasMacAddress() && !(
			this.getMacAddress().equalsIgnoreCase(pusher.getMacAddress())
		)) return false;
		
		if (this.hasIpAddress() && !(
			this.getIpAddress().equalsIgnoreCase(pusher.getIp().getHostAddress())
		)) return false;
		
		if (this.hasOrdinal() && !(
			this.getGroupOrdinal() == pusher.getGroupOrdinal() &&
			this.getControllerOrdinal() == pusher.getControllerOrdinal()
		)) return false;
		
		if (this.hasArtnet() && !(
			this.getArtnetUniverse() == pusher.getArtnetUniverse() &&
			this.getArtnetChannel() == pusher.getArtnetChannel()
		)) return false;
		
		return true;
	}
	
	public synchronized boolean matches(PixelPusher pusher, Strip strip) {
		return matches(pusher) && (this.getStripNumber() == strip.getStripNumber());
	}
	
	public synchronized StripType getStripType() {
		return this.type;
	}
	
	public synchronized void setStripType(StripType type) {
		if (type == null) type = StripType.BEST_GUESS;
		this.type = type;
	}
	
	public synchronized GammaCorrector getGammaCorrector() {
		return this.gamma;
	}
	
	public synchronized void setGammaCorrector(GammaCorrector gamma) {
		if (gamma == null) gamma = GammaCorrector.DEFAULT;
		this.gamma = gamma;
	}
	
	public synchronized ArrayOrdering getOrdering() {
		return ordering;
	}
	
	public synchronized void setOrdering(ArrayOrdering ordering) {
		if (ordering == null) ordering = ArrayOrdering.LTR_TTB;
		this.ordering = ordering;
		if (this.pixelArrayToSequence != null) {
			this.pixelArrayToSequence.setOrdering(ordering);
		}
	}
	
	public synchronized int getRowCount() {
		return rows;
	}
	
	public synchronized void setRowCount(int rowCount) {
		if (rowCount < 1) rowCount = 1;
		this.rows = rowCount;
		if (this.pixelArrayToSequence != null) {
			this.pixelArrayToSequence.setRowCount(rowCount);
		}
	}
	
	public synchronized int getColumnCount() {
		return columns;
	}
	
	public synchronized void setColumnCount(int columnCount) {
		if (columnCount < 1) columnCount = 1;
		this.columns = columnCount;
		if (this.pixelArrayToSequence != null) {
			this.pixelArrayToSequence.setColumnCount(columnCount);
		}
	}
	
	public synchronized int getLength() {
		return length;
	}
	
	private synchronized void setLength(int length) {
		if (length < 1) length = 1;
		this.length = length;
	}
	
	public synchronized boolean hasPixelSet() {
		return this.pixelSet != null;
	}
	
	public synchronized boolean hasPixelSequence() {
		return this.pixelSequence != null;
	}
	
	public synchronized PixelSet getPixelSet() {
		return this.pixelSet;
	}
	
	public synchronized PixelSequence getPixelSequence() {
		return this.pixelSequence;
	}
	
	public synchronized void setPixelSet(PixelSet pixelSet) {
		if (pixelSet == null) {
			this.pixelSet = null;
			this.pixelSequence = null;
			this.pixelArrayToSequence = null;
		} else if (pixelSet.isSequence()) {
			this.pixelSet = pixelSet;
			this.pixelSequence = pixelSet.sequence();
			this.pixelArrayToSequence = null;
		} else if (pixelSet.isArray()) {
			this.pixelSet = pixelSet;
			PixelArrayToSequence pats = new PixelArrayToSequence(pixelSet.array(), ordering, rows, columns);
			this.pixelSequence = pats;
			this.pixelArrayToSequence = pats;
		} else {
			this.pixelSet = pixelSet;
			this.pixelSequence = null;
			this.pixelArrayToSequence = null;
		}
	}
	
	public synchronized void push(Strip strip, long tick) {
		if (pixelSequence != null) {
			int length = strip.getLength();
			if (colors == null || colors.length < length) colors = new int[length];
			if (pixels == null || pixels.length < length) pixels = new Pixel[length];
			pixelSequence.updatePixels(tick);
			pixelSequence.getPixelColors(0, colors, 0, length);
			gamma.setPixelColors(pixels, 0, colors, 0, length);
			strip.setPixels(pixels);
		}
	}
	
	@Override
	public synchronized int compareTo(StripInfo other) {
		return this.getName().compareToIgnoreCase(other.getName());
	}
}
