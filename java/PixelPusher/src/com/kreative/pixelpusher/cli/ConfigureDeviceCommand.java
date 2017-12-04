package com.kreative.pixelpusher.cli;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import com.heroicrobot.dropbit.devices.pixelpusher.PixelPusher;
import com.heroicrobot.dropbit.devices.pixelpusher.Strip;
import com.kreative.pixelpusher.device.PixelPusherController;
import com.kreative.pixelpusher.device.StripInfo;
import com.kreative.pixelpusher.device.StripInfoFrame;

public class ConfigureDeviceCommand extends CLICommand {
	public ConfigureDeviceCommand() {
		super(
			new String[]{"c", "conf", "config", "configure"},
			"Edit the configuration of a connected or known device.",
			"configure [<device> [<strip>]]"
		);
	}
	
	@Override
	public void main(Scanner in, PrintStream out, PixelPusherController ctrl, String[] args) {
		try {
			StringSearchPattern dsp = (args.length > 1) ? new StringSearchPattern(args[1]) : null;
			IntegerSearchPattern ssp = (args.length > 2) ? new IntegerSearchPattern(args[2]) : null;
			StripSearchResults devices = StripSearchResults.forPattern(ctrl, dsp, ssp);
			
			int count = 0;
			List<PixelPusher> pa = new ArrayList<PixelPusher>();
			List<Strip> sa = new ArrayList<Strip>();
			List<StripInfo> ia = new ArrayList<StripInfo>();
			for (PixelPusher pusher : devices.getPushers()) {
				for (Strip strip : devices.getStrips(pusher)) {
					count++;
					pa.add(pusher);
					sa.add(strip);
					ia.add(devices.getStripInfo(strip));
				}
			}
			for (StripInfo info : devices.getDisconnectedStripInfos()) {
				count++;
				pa.add(null);
				sa.add(null);
				ia.add(info);
			}
			
			if (count < 1) {
				printError(out, "No devices found.");
			} else if (count > 1) {
				printError(out, "Cannot configure multiple devices.");
			} else {
				PixelPusher pusher = pa.get(0);
				Strip strip = sa.get(0);
				StripInfo info = ia.get(0);
				if (info == null) {
					info = ctrl.getStripInfoSet().getOrCreateStripInfo(pusher, strip);
				}
				StripInfoFrame frame = new StripInfoFrame(ctrl.getPixelSetInfoSet(), pusher, strip, info);
				frame.pack();
				frame.setLocationRelativeTo(null);
				frame.setDefaultCloseOperation(StripInfoFrame.DISPOSE_ON_CLOSE);
				frame.setVisible(true);
			}
		} catch (GeneralParseException e) {
			printError(out, e.getMessage());
		}
	}
}
