package com.kreative.pixelpusher.device;

import java.net.InetAddress;
import com.heroicrobot.dropbit.devices.pixelpusher.Pixel;
import com.heroicrobot.dropbit.devices.pixelpusher.PixelPusher;
import com.heroicrobot.dropbit.devices.pixelpusher.Strip;
import com.kreative.pixelpusher.common.ColorConstants;

public class PixelPusherIdentifier {
	private int[] colors = new int[240];
	private Pixel[] pixels = new Pixel[240];
	private GammaCorrector gamma = GammaCorrector.DEFAULT;
	private String devId1;
	private String devId2;
	private String devId3;
	private String stripId;
	
	public void setGammaCorrector(GammaCorrector gamma) {
		if (gamma == null) gamma = GammaCorrector.DEFAULT;
		this.gamma = gamma;
	}
	
	public void identifyDevice(PixelPusher pusher) {
		String mac = "NNN " + pusher.getMacAddress();
		String ipa = "MMM " + ipToString(pusher.getIp()) + ":" + intToString(pusher.getPort(),5);
		String dim = "VVV " + intToString(pusher.getNumberOfStrips(),1) + "*" + intToString(pusher.getPixelsPerStrip(),3);
		devId1 = mac + "   " + ipa + "   " + dim + "   ";
		String gco = "OOO " + intToString(pusher.getGroupOrdinal(),5) + "/" + intToString(pusher.getControllerOrdinal(),5);
		String ana = "GGG " + intToString(pusher.getArtnetUniverse(),5) + "/" + intToString(pusher.getArtnetChannel(),5);
		devId2 = "   " + gco + "   " + ana + "   ";
		String spp = "RRR " + intToString(pusher.getMaxStripsPerPacket(),1);
		String lsp = "VVV " + longToString(pusher.getLinkSpeed(),10);
		String pwr = "YYY " + longToString(pusher.getPowerTotal(),10);
		String upd = "OOO " + longToString(pusher.getUpdatePeriod(),5);
		String vid = "NNN " + intToString(pusher.getVendorId(),5);
		String pid = "UUU " + intToString(pusher.getProductId(),5);
		String hwv = "XXX " + intToString(pusher.getHardwareRevision(),5);
		String swv = "WWW " + intToString(pusher.getSoftwareRevision(),5);
		String ptv = "MMM " + intToString(pusher.getProtocolVersion(),5);
		devId3 = "   " + spp + "   " + lsp + "   " + pwr + "   " + upd + "   " + vid + "   " + pid + "   " + hwv + "   " + swv + "   " + ptv;
	}
	
	public void identifyStrip(Strip strip) {
		String stp = "WWW " + intToString(strip.getStripNumber(),1) + "/" + intToString(strip.getLength(),3);
		String sid = "XXX " + longToString(strip.getStripIdentifier(),10);
		stripId = devId1 + stp + devId2 + sid + devId3;
	}
	
	public String getIdentityString() {
		return stripId;
	}
	
	public void displayIdentity(Strip strip) {
		char[] ch = stripId.toCharArray();
		for (int i = 0; i < ch.length && i < colors.length; i++) {
			colors[i] = charToColor(ch[i]);
		}
		for (int i = ch.length; i < colors.length; i++) {
			colors[i] = 0;
		}
		gamma.setPixelColors(pixels, 0, colors, 0, colors.length);
		strip.setPixels(pixels);
	}
	
	private static final String intToString(int value, int length) {
		StringBuffer valueStringBuffer = new StringBuffer();
		for (int i = 0; i < length; i++) valueStringBuffer.append('0');
		valueStringBuffer.append(Integer.toString(Math.abs(value)));
		String valueString = valueStringBuffer.toString();
		valueString = valueString.substring(valueString.length() - length);
		if (value < 0) valueString = "-" + valueString.substring(1);
		return valueString;
	}
	
	private static final String longToString(long value, int length) {
		StringBuffer valueStringBuffer = new StringBuffer();
		for (int i = 0; i < length; i++) valueStringBuffer.append('0');
		valueStringBuffer.append(Long.toString(Math.abs(value)));
		String valueString = valueStringBuffer.toString();
		valueString = valueString.substring(valueString.length() - length);
		if (value < 0) valueString = "-" + valueString.substring(1);
		return valueString;
	}
	
	private static final String ipToString(InetAddress address) {
		byte[] ip = address.getAddress();
		return intToString(ip[0]&0xFF,3)
		  +"."+intToString(ip[1]&0xFF,3)
		  +"."+intToString(ip[2]&0xFF,3)
		  +"."+intToString(ip[3]&0xFF,3);
	}
	
	private static final int charToColor(char ch) {
		switch (ch) {
			case '+': return ColorConstants.CYAN;
			case '-': return ColorConstants.MAGENTA;
			case '0': return ColorConstants.BLACK;
			case '1': return ColorConstants.BROWN;
			case '2': return ColorConstants.RED;
			case '3': return ColorConstants.ORANGE;
			case '4': return ColorConstants.YELLOW;
			case '5': return ColorConstants.GREEN;
			case '6': return ColorConstants.BLUE;
			case '7': return ColorConstants.VIOLET;
			case '8': return ColorConstants.GRAY;
			case '9': return ColorConstants.WHITE;
			case 'A': case 'a': return ColorConstants.CORAL;
			case 'B': case 'b': return ColorConstants.CORANGE;
			case 'C': case 'c': return ColorConstants.LEMON;
			case 'D': case 'd': return ColorConstants.LIME;
			case 'E': case 'e': return ColorConstants.FROST;
			case 'F': case 'f': return ColorConstants.LAVENDER;
			case '.': case ',': return ColorConstants.CYAN;
			case ':': case ';': return ColorConstants.CYAN;
			case '/': case '*': return ColorConstants.CYAN;
			case 'G': case 'g': return ColorConstants.GREEN;
			case 'H': case 'h': return ColorConstants.CHARTREUSE;
			case 'I': case 'i': return ColorConstants.INDIGO;
			case 'J': case 'j': return ColorConstants.GOLD;
			case 'K': case 'k': return ColorConstants.BLACK;
			case 'L': case 'l': return ColorConstants.WARM_WHITE_LED;
			case 'M': case 'm': return ColorConstants.MAGENTA;
			case 'N': case 'n': return ColorConstants.BROWN;
			case 'O': case 'o': return ColorConstants.ORANGE;
			case 'P': case 'p': return ColorConstants.PURPLE;
			case 'Q': case 'q': return ColorConstants.AQUAMARINE;
			case 'R': case 'r': return ColorConstants.RED;
			case 'S': case 's': return ColorConstants.SCARLET;
			case 'T': case 't': return ColorConstants.CYAN;
			case 'U': case 'u': return ColorConstants.BLUE;
			case 'V': case 'v': return ColorConstants.VIOLET;
			case 'W': case 'w': return ColorConstants.WHITE;
			case 'X': case 'x': return ColorConstants.GRAY;
			case 'Y': case 'y': return ColorConstants.YELLOW;
			case 'Z': case 'z': return ColorConstants.AZURE;
			default: return 0;
		}
	}
}
