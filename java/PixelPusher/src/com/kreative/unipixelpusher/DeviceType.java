package com.kreative.unipixelpusher;

public enum DeviceType {
	UNKNOWN("Unknown"),
	MICROCONTROLLER_GENERIC("Microcontroller - Generic"),
	MICROCONTROLLER_ARDUINO("Microcontroller - Arduino"),
	MICROCONTROLLER_ARDUINO_MEGA("Microcontroller - Arduino Mega"),
	MICROCONTROLLER_COLORDUINO("Microcontroller - Colorduino"),
	MICROCONTROLLER_RAINBOWDUINO("Microcontroller - Rainbowduino"),
	SBC_GENERIC("Single-Board Computer - Generic"),
	SBC_ODROID("Single-Board Computer - ODROID"),
	SBC_RASPBERRY_PI("Single-Board Computer - Raspberry Pi"),
	PERSONAL_COMPUTER("Personal Computer"),
	SMART_LAMP("Smart Lamp"),
	SMART_LED_PANEL("Smart LED Panel"),
	PIXELPUSHER("PixelPusher"),
	OSCILLOSCOPE("Oscilloscope"),
	TEST_DEVICE("Test Device");
	
	private final String toString;
	private DeviceType(String toString) { this.toString = toString; }
	@Override public String toString() { return this.toString; }
}
