package com.kreative.pixelpusher.device;

import java.awt.Image;
import com.kreative.pixelpusher.resources.Resources;

public enum StripType {
	BEST_GUESS("Best Guess") {
		@Override
		public Image getIcon(int rows, int columns, int length, int size) {
			if (rows == 8 && columns == 8 && length == 64) {
				return Resources.getImage("device", "ledarray", size);
			} else if (length <= 32) {
				return Resources.getImage("device", "pixels", size);
			} else if (rows == 1 || columns == 1) {
				return Resources.getImage("device", "strip", size);
			} else {
				return Resources.getImage("device", "striparray", size);
			}
		}
	},
	PIXEL_STRIP("Pixel Strip") {
		@Override
		public Image getIcon(int rows, int columns, int length, int size) {
			return Resources.getImage("device", "pixels", size);
		}
	},
	LED_STRIP("LED Strip") {
		@Override
		public Image getIcon(int rows, int columns, int length, int size) {
			return Resources.getImage("device", "strip", size);
		}
	},
	LED_STRIP_ARRAY("LED Strip Array") {
		@Override
		public Image getIcon(int rows, int columns, int length, int size) {
			return Resources.getImage("device", "striparray", size);
		}
	},
	LED_ARRAY("LED Array") {
		@Override
		public Image getIcon(int rows, int columns, int length, int size) {
			return Resources.getImage("device", "ledarray", size);
		}
	},
	RAINBOWDUINO("Rainbowduino") {
		@Override
		public Image getIcon(int rows, int columns, int length, int size) {
			return Resources.getImage("device", "rainbowduino", size);
		}
	};
	
	private final String name;
	
	private StripType(String name) {
		this.name = name;
	}
	
	@Override
	public final String toString() {
		return this.name;
	}
	
	public final String getName() {
		return this.name;
	}
	
	public abstract Image getIcon(int rows, int columns, int length, int size);
}
