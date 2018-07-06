package com.kreative.unipixelpusher;

public enum StringType {
	UNKNOWN("Unknown"),
	SINGLE_LAMP("Single Lamp"),
	STANDARD_LAMP_STRING("Standard Lamp String"),
	ADDRESSABLE_LAMP_STRING("Individually Addressable Lamp String"),
	SINGLE_LED("Single LED"),
	STANDARD_LED_STRING("Standard LED String"),
	STANDARD_LED_STRIP("Standard LED Strip"),
	ADDRESSABLE_LED_STRING("Individually Addressable LED String"),
	ADDRESSABLE_LED_STRIP("Individually Addressable LED Strip"),
	LED_STICK("LED Stick"),
	LED_ARC("LED Arc"),
	LED_RING("LED Ring"),
	LED_DISC("LED Disc"),
	LED_MATRIX_MODULE("LED Matrix (Module)"),
	LED_MATRIX_ON_FLEX("LED Matrix on Flex"),
	LED_MATRIX_ON_PCB("LED Matrix on PCB"),
	LED_MATRIX_FROM_STRIPS("LED Matrix from Strips"),
	LED_CUBE("LED Cube"),
	LED_BAR_GRAPH("LED Bar Graph"),
	SEGMENT_DISPLAY_7("7-Segment Display"),
	SEGMENT_DISPLAY_9("9-Segment Display"),
	SEGMENT_DISPLAY_14("14-Segment Display"),
	SEGMENT_DISPLAY_16("16-Segment Display"),
	CHARACTER_LCD("Character LCD"),
	GRAPHIC_LCD("Graphic LCD"),
	NIXIE_TUBE("Nixie Tube"),
	OSCILLOSCOPE("Oscilloscope"),
	VECTOR_CRT("Vector CRT"),
	RASTER_CRT("Raster CRT"),
	EL_WIRE("EL Wire"),
	EL_TAPE("EL Tape"),
	EL_PANEL("EL Panel"),
	BACKLIGHT("Backlight"),
	STANDARD_MOTOR("Standard Motor"),
	STEPPING_MOTOR("Stepping Motor"),
	SERVO_MOTOR("Servo Motor"),
	LINEAR_ACTUATOR("Linear Actuator"),
	TEST_DEVICE("Test Device");
	
	private final String toString;
	private StringType(String toString) { this.toString = toString; }
	@Override public String toString() { return this.toString; }
}
