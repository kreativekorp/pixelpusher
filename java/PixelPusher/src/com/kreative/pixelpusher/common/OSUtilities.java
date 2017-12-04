package com.kreative.pixelpusher.common;

import java.io.File;

public class OSUtilities {
	private OSUtilities() {}
	
	private static String osName;
	private static String osVersion;
	private static boolean isMacOS;
	private static boolean isWindows;
	private static File homeDir;
	static {
		try {
			osName = System.getProperty("os.name");
			osVersion = System.getProperty("os.version");
			isMacOS = osName.toUpperCase().contains("MAC OS");
			isWindows = osName.toUpperCase().contains("WINDOWS");
			homeDir = new File(System.getProperty("user.home"));
		} catch (Exception e) {
			osName = "";
			osVersion = "";
			isMacOS = false;
			isWindows = false;
			homeDir = null;
		}
	}
	
	public static String getOSName() {
		return osName;
	}
	
	public static String getOSVersion() {
		return osVersion;
	}
	
	public static boolean isMacOS() {
		return isMacOS;
	}
	
	public static boolean isWindows() {
		return isWindows;
	}
	
	public static File getHomeDir() {
		return homeDir;
	}
	
	public static File getPreferencesDir() {
		if (homeDir == null) {
			return null;
		} else if (isMacOS) {
			File libDir = new File(homeDir, "Library");
			File prefDir = new File(libDir, "Preferences");
			File appDir = new File(prefDir, "com.kreative.pixelpusher");
			if (!appDir.exists()) appDir.mkdirs();
			return appDir;
		} else if (isWindows) {
			File appDataDir = new File(homeDir, "Application Data");
			File kkDir = new File(appDataDir, "Kreative");
			File appDir = new File(kkDir, "PixelPusher");
			if (!appDir.exists()) appDir.mkdirs();
			return appDir;
		} else {
			File appDir = new File(homeDir, ".com.kreative.pixelpusher");
			if (!appDir.exists()) appDir.mkdirs();
			return appDir;
		}
	}
	
	public static File getPreferencesFile(String name) {
		if (homeDir == null) {
			return null;
		} else {
			return new File(getPreferencesDir(), name);
		}
	}
}
