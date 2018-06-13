package com.kreative.unipixelpusher.clock;

public enum AnalogClockMode {
	POINT("Point", true) {
		@Override
		public boolean apply(int value, int index) {
			return index == value;
		}
	},
	LINE("Line", true) {
		@Override
		public boolean apply(int value, int index) {
			return index < value;
		}
	},
	BINARY("Binary", false) {
		@Override
		public boolean apply(int value, int index) {
			return index < 32 && (value & (1 << index)) != 0;
		}
	};
	
	private final String stringValue;
	private final boolean scaleHour;
	
	private AnalogClockMode(String stringValue, boolean scaleHour) {
		this.stringValue = stringValue;
		this.scaleHour = scaleHour;
	}
	
	@Override
	public String toString() {
		return stringValue;
	}
	
	public boolean scaleHour() {
		return scaleHour;
	}
	
	public abstract boolean apply(int value, int index);
}
