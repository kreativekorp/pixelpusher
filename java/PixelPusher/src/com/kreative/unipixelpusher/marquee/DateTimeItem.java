package com.kreative.unipixelpusher.marquee;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import com.kreative.unipixelpusher.SequenceConfiguration;

public class DateTimeItem extends TextItem {
	protected DateFormat format;
	protected GregorianCalendar calendar;
	protected long lastTick;
	
	public DateTimeItem() {
		this.format = DateFormat.getInstance();
		this.calendar = null;
		this.lastTick = 0;
	}
	
	public DateTimeItem(String format) {
		this.format = new SimpleDateFormat(format);
		this.calendar = null;
		this.lastTick = 0;
	}
	
	public DateTimeItem(DateFormat format) {
		this.format = (format != null) ? format : DateFormat.getInstance();
		this.calendar = null;
		this.lastTick = 0;
	}
	
	public synchronized DateFormat getFormat() {
		return this.format;
	}
	
	public synchronized void setFormat(String format) {
		this.format = new SimpleDateFormat(format);
	}
	
	public synchronized void setFormat(DateFormat format) {
		this.format = (format != null) ? format : DateFormat.getInstance();
	}
	
	@Override
	public synchronized String getText(long tick) {
		if (calendar == null || lastTick != tick) {
			calendar = new GregorianCalendar();
			lastTick = tick;
		}
		return format.format(calendar.getTime());
	}
	
	@Override
	public long getUpdateInterval() {
		return 250;
	}
	
	@Override
	public synchronized void loadConfiguration(SequenceConfiguration config, String prefix) {
		super.loadConfiguration(config, prefix);
		String fmt = config.get(prefix + ".format");
		this.format = (fmt != null) ? new SimpleDateFormat(fmt) : DateFormat.getInstance();
		this.calendar = null;
		this.lastTick = 0;
	}
	
	@Override
	public synchronized void saveConfiguration(SequenceConfiguration config, String prefix) {
		super.saveConfiguration(config, prefix);
		if (format instanceof SimpleDateFormat) {
			SimpleDateFormat fmt = (SimpleDateFormat)format;
			config.put(prefix + ".format", fmt.toPattern());
		}
	}
}
