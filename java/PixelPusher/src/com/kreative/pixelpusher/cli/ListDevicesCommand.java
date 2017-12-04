package com.kreative.pixelpusher.cli;

import java.io.PrintStream;
import java.util.Scanner;
import com.heroicrobot.dropbit.devices.pixelpusher.PixelPusher;
import com.heroicrobot.dropbit.devices.pixelpusher.Strip;
import com.kreative.pixelpusher.device.PixelPusherController;
import com.kreative.pixelpusher.device.StripInfo;
import com.kreative.pixelpusher.pixelset.PixelSet;
import com.kreative.pixelpusher.pixelset.PixelSetInfo;

public class ListDevicesCommand extends CLICommand {
	public ListDevicesCommand() {
		super(
			new String[]{"/", "/d", "l", "d", "ld", "list", "devices", "listdevices"},
			"Print a list of connected and disconnected-but-known devices.",
			"listdevices [<device> [<strip>]]"
		);
	}
	
	@Override
	public void main(Scanner in, PrintStream out, PixelPusherController ctrl, String[] args) {
		try {
			StringSearchPattern dsp = (args.length > 1) ? new StringSearchPattern(args[1]) : null;
			IntegerSearchPattern ssp = (args.length > 2) ? new IntegerSearchPattern(args[2]) : null;
			StripSearchResults devices = StripSearchResults.forPattern(ctrl, dsp, ssp);
			printDevices(out, ctrl, devices);
		} catch (GeneralParseException e) {
			printError(out, e.getMessage());
		}
	}
	
	private static void printDevices(PrintStream out, PixelPusherController ctrl, StripSearchResults devices) {
		out.println();
		out.println("Connected Devices:");
		if (devices.getPushers().isEmpty()) {
			out.println("\t< No Devices >");
		} else for (PixelPusher pusher : devices.getPushers()) {
			out.println("\tMAC Address: " + pusher.getMacAddress());
			out.println("\tIP Address: " + pusher.getIp().getHostAddress() + " Port: " + pusher.getPort());
			out.println("\tGroup: " + pusher.getGroupOrdinal() + " Controller: " + pusher.getControllerOrdinal());
			out.println("\tArtNet Universe: " + pusher.getArtnetUniverse() + " Channel: " + pusher.getArtnetChannel());
			if (devices.getStrips(pusher).isEmpty()) {
				out.println("\t\t< No Strips >");
			} else for (Strip strip : devices.getStrips(pusher)) {
				out.println("\t\tStrip " + strip.getStripNumber() + ":");
				StripInfo info = devices.getStripInfo(strip);
				if (info == null) {
					out.println("\t\t\t< No Info >");
				} else {
					out.println("\t\t\tName: " + info.getName());
					out.println("\t\t\tMatched By: " + (info.hasMacAddress() ? "MAC Address" : info.hasIpAddress() ? "IP Address" : info.hasOrdinal() ? "Group/Controller Ordinal" : info.hasArtnet() ? "ArtNet Universe/Channel" : "Unknown"));
					out.println("\t\t\tGamma Correction: " + info.getGammaCorrector().getName());
					out.println("\t\t\tArray Ordering: " + info.getOrdering().toString());
					out.println("\t\t\tRows: " + info.getRowCount());
					out.println("\t\t\tColumns: " + info.getColumnCount());
					out.println("\t\t\tAttached To: " + getAttachmentString(ctrl, info));
				}
			}
		}
		out.println();
		out.println("Disconnected Devices:");
		if (devices.getDisconnectedStripInfos().isEmpty()) {
			out.println("\t< No Devices >");
		} else for (StripInfo info : devices.getDisconnectedStripInfos()) {
			out.println("\t" + info.getName() + ":");
			if (info.hasMacAddress()) out.println("\t\tMAC Address: " + info.getMacAddress());
			if (info.hasIpAddress()) out.println("\t\tIP Address: " + info.getIpAddress());
			if (info.hasOrdinal()) out.println("\t\tGroup: " + info.getGroupOrdinal() + " Controller: " + info.getControllerOrdinal());
			if (info.hasArtnet()) out.println("\t\tArtNet Universe: " + info.getArtnetUniverse() + " Channel: " + info.getArtnetChannel());
			out.println("\t\tGamma Correction: " + info.getGammaCorrector().getName());
			out.println("\t\tArray Ordering: " + info.getOrdering().toString());
			out.println("\t\tRows: " + info.getRowCount());
			out.println("\t\tColumns: " + info.getColumnCount());
			out.println("\t\tAttached To: " + getAttachmentString(ctrl, info));
		}
		out.println();
	}
	
	private static String getAttachmentString(PixelPusherController ctrl, StripInfo info) {
		PixelSet ps = info.getPixelSet();
		if (ps == null) {
			return "< No Attachment >";
		} else {
			PixelSetInfo<? extends PixelSet> psi = ctrl.getPixelSetInfoSet().getPixelSetInfo(ps);
			if (psi == null) {
				return "< " + getClassName(ps.getClass()) + " >";
			} else {
				return psi.getName();
			}
		}
	}
	
	private static String getClassName(Class<?> cls) {
		String name = cls.getName();
		if (name.startsWith("com.kreative.pixelpusher.")) {
			return "c.k.pp." + name.substring(25);
		} else if (name.startsWith("com.kreative.")) {
			return "c.k." + name.substring(13);
		} else {
			return name;
		}
	}
}
