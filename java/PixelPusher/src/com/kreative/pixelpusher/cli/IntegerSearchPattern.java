package com.kreative.pixelpusher.cli;

import java.util.ArrayList;
import java.util.List;
import com.heroicrobot.dropbit.devices.pixelpusher.Strip;
import com.kreative.pixelpusher.device.StripInfo;

public class IntegerSearchPattern {
	private final List<int[]> ranges;
	
	public IntegerSearchPattern(String s) {
		this.ranges = new ArrayList<int[]>();
		if (s.trim().equals("*")) {
			ranges.add(new int[]{0, Integer.MAX_VALUE});
		} else {
			for (String ss : s.split(",")) {
				if (ss.contains("-")) {
					String[] sss = ss.split("-");
					if (sss.length == 2) {
						try {
							int i = Integer.parseInt(sss[0].trim());
							int j = Integer.parseInt(sss[1].trim());
							if (i <= j) {
								ranges.add(new int[]{i, j});
							} else {
								throw new GeneralParseException("Invalid integer search pattern", s);
							}
						} catch (NumberFormatException nfe) {
							throw new GeneralParseException("Invalid integer search pattern", s, nfe);
						}
					} else {
						throw new GeneralParseException("Invalid integer search pattern", s);
					}
				} else {
					try {
						int i = Integer.parseInt(ss.trim());
						ranges.add(new int[]{i, i});
					} catch (NumberFormatException nfe) {
						throw new GeneralParseException("Invalid integer search pattern", s, nfe);
					}
				}
			}
		}
	}
	
	public boolean matches(int i) {
		for (int[] range : ranges) {
			if (range[0] <= i && i <= range[1]) {
				return true;
			}
		}
		return false;
	}
	
	public boolean matches(Strip strip) {
		return matches(strip.getStripNumber());
	}
	
	public boolean matches(StripInfo info) {
		return matches(info.getStripNumber());
	}
}
