package com.kreative.pixelpusher.cli;

import java.io.PrintStream;
import java.util.Scanner;
import com.heroicrobot.dropbit.devices.pixelpusher.PixelPusher;
import com.heroicrobot.dropbit.devices.pixelpusher.Strip;
import com.kreative.pixelpusher.device.PixelPusherController;
import com.kreative.pixelpusher.device.PixelPusherIdentifier;
import com.kreative.pixelpusher.device.StripInfo;

public class IdentifyCommand extends CLICommand {
	public IdentifyCommand() {
		super(
			new String[]{"i", "id", "ident", "identify"},
			"Display identifying information on a connected device.",
			"identify [<device> [<strip>]]"
		);
	}
	
	@Override
	public void main(Scanner in, PrintStream out, PixelPusherController ctrl, String[] args) {
		try {
			StringSearchPattern dsp = (args.length > 1) ? new StringSearchPattern(args[1]) : null;
			IntegerSearchPattern ssp = (args.length > 2) ? new IntegerSearchPattern(args[2]) : null;
			StripSearchResults devices = StripSearchResults.forPattern(ctrl, dsp, ssp);
			int count = 0;
			for (PixelPusher pusher : devices.getPushers()) {
				for (Strip strip : devices.getStrips(pusher)) {
					StripInfo info = devices.getStripInfo(strip);
					if (info != null) info.setPixelSet(null);
					PixelPusherIdentifier id = new PixelPusherIdentifier();
					if (info != null) id.setGammaCorrector(info.getGammaCorrector());
					id.identifyDevice(pusher);
					id.identifyStrip(strip);
					id.displayIdentity(strip);
					count++;
				}
			}
			printCount(out, "Identified", count, "device", "devices");
		} catch (GeneralParseException e) {
			printError(out, e.getMessage());
		}
	}
}
