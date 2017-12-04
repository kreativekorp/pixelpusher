package com.kreative.pixelpusher.cli;

import java.io.PrintStream;
import java.util.Scanner;
import com.heroicrobot.dropbit.devices.pixelpusher.PixelPusher;
import com.heroicrobot.dropbit.devices.pixelpusher.Strip;
import com.kreative.pixelpusher.device.PixelPusherController;
import com.kreative.pixelpusher.device.StripInfo;

public class UnattachCommand extends CLICommand {
	public UnattachCommand() {
		super(
			new String[]{"u", "k", "unattach", "unassign", "kill"},
			"Unattach devices from sequences.",
			"unattach [<device> [<strip>]]"
		);
	}
	
	@Override
	public void main(Scanner in, PrintStream out, PixelPusherController ctrl, String[] args) {
		try {
			StringSearchPattern dsp = (args.length > 1) ? new StringSearchPattern(args[1]) : null;
			IntegerSearchPattern ssp = (args.length > 2) ? new IntegerSearchPattern(args[2]) : null;
			if (dsp == null && ssp == null) {
				out.println();
				out.print("Are you sure you want to unattach all devices? ");
				String line = in.hasNextLine() ? in.nextLine().trim() : "";
				if (!(line.equalsIgnoreCase("y") || line.equalsIgnoreCase("yes"))) {
					out.println();
					return;
				}
			}
			StripSearchResults devices = StripSearchResults.forPattern(ctrl, dsp, ssp);
			int count = 0;
			for (PixelPusher pusher : devices.getPushers()) {
				for (Strip strip : devices.getStrips(pusher)) {
					StripInfo info = devices.getStripInfo(strip);
					if (info != null) info.setPixelSet(null);
					count++;
				}
			}
			for (StripInfo info : devices.getDisconnectedStripInfos()) {
				info.setPixelSet(null);
				count++;
			}
			printCount(out, "Unattached", count, "device", "devices");
		} catch (GeneralParseException e) {
			printError(out, e.getMessage());
		}
	}
}
