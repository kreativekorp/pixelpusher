package com.kreative.pixelpusher.cli;

import java.io.PrintStream;
import java.util.Scanner;
import com.heroicrobot.dropbit.devices.pixelpusher.PixelPusher;
import com.heroicrobot.dropbit.devices.pixelpusher.Strip;
import com.kreative.pixelpusher.device.PixelPusherController;
import com.kreative.pixelpusher.device.StripInfo;

public class ResetDeviceCommand extends CLICommand {
	public ResetDeviceCommand() {
		super(
			new String[]{"r", "f", "reset", "forget"},
			"Reset the configuration of a connected or known device.",
			"reset [<device> [<strip>]]"
		);
	}
	
	@Override
	public void main(Scanner in, PrintStream out, PixelPusherController ctrl, String[] args) {
		try {
			StringSearchPattern dsp = (args.length > 1) ? new StringSearchPattern(args[1]) : null;
			IntegerSearchPattern ssp = (args.length > 2) ? new IntegerSearchPattern(args[2]) : null;
			if (dsp == null && ssp == null) {
				out.println();
				out.print("Are you sure you want to reset all devices? ");
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
					ctrl.getStripInfoSet().removeStripInfo(pusher, strip);
					count++;
				}
			}
			for (StripInfo info : devices.getDisconnectedStripInfos()) {
				ctrl.getStripInfoSet().removeStripInfo(info);
				count++;
			}
			printCount(out, "Reset", count, "device", "devices");
		} catch (GeneralParseException e) {
			printError(out, e.getMessage());
		}
	}
}
