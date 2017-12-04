package com.kreative.pixelpusher.cli;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import com.heroicrobot.dropbit.devices.pixelpusher.PixelPusher;
import com.heroicrobot.dropbit.devices.pixelpusher.Strip;
import com.kreative.pixelpusher.device.PixelPusherController;
import com.kreative.pixelpusher.device.StripInfo;
import com.kreative.pixelpusher.pixelset.PixelSet;
import com.kreative.pixelpusher.pixelset.PixelSetFactory;
import com.kreative.pixelpusher.pixelset.PixelSetInfo;

public class AttachCommand extends CLICommand {
	public AttachCommand() {
		super(
			new String[]{"a", "attach", "assign"},
			"Attach devices to sequences.",
			"attach [<device> [<strip> [<sequence>]]]"
		);
	}
	
	@Override
	public void main(Scanner in, PrintStream out, PixelPusherController ctrl, String[] args) {
		try {
			StringSearchPattern dsp = (args.length > 1) ? new StringSearchPattern(args[1]) : null;
			IntegerSearchPattern ssp = (args.length > 2) ? new IntegerSearchPattern(args[2]) : null;
			StringSearchPattern fsp = (args.length > 3) ? new StringSearchPattern(args[3]) : null;
			
			StripSearchResults devices = StripSearchResults.forPattern(ctrl, dsp, ssp);
			FactorySearchResults factories = FactorySearchResults.forPattern(fsp);
			List<PixelSetInfo<? extends PixelSet>> infos = new ArrayList<PixelSetInfo<? extends PixelSet>>();
			for (PixelSetInfo<? extends PixelSet> info : ctrl.getPixelSetInfoSet().getPixelSetInfoList()) {
				if (fsp == null || fsp.matches(info)) {
					infos.add(info);
				}
			}
			PixelSet ps = selectPixelSet(in, out, ctrl, infos, factories.getFactories());
			if (ps == null) return;
			
			int count = 0;
			for (PixelPusher pusher : devices.getPushers()) {
				for (Strip strip : devices.getStrips(pusher)) {
					StripInfo info = devices.getStripInfo(strip);
					if (info == null) {
						info = ctrl.getStripInfoSet().getOrCreateStripInfo(pusher, strip);
					}
					info.setPixelSet(ps);
					count++;
				}
			}
			for (StripInfo info : devices.getDisconnectedStripInfos()) {
				info.setPixelSet(ps);
				count++;
			}
			printCount(out, "Attached", count, "device", "devices");
		} catch (GeneralParseException e) {
			printError(out, e.getMessage());
		}
	}
	
	private static PixelSet selectPixelSet(Scanner in, PrintStream out, PixelPusherController ctrl, List<PixelSetInfo<? extends PixelSet>> infos, List<PixelSetFactory<? extends PixelSet>> factories) {
		if (infos.isEmpty() && factories.isEmpty()) {
			printError(out, "No sequences or types found.");
			return null;
		} else if (infos.size() == 1 && factories.isEmpty()) {
			return infos.get(0).getPixelSet();
		} else if (infos.isEmpty() && factories.size() == 1) {
			return ctrl.getPixelSetInfoSet().createPixelSetInfo(factories.get(0)).getPixelSet();
		} else {
			out.println();
			if (!infos.isEmpty()) {
				out.println("Select a pixel sequence to attach:");
				for (int i = 0; i < infos.size(); i++) {
					out.println("\t" + (i+1) + ".\t" + infos.get(i).getName());
				}
			}
			if (!factories.isEmpty()) {
				if (infos.isEmpty()) {
					out.println("Select type of pixel sequence to attach:");
				} else {
					out.println("Or select type of pixel sequence to attach:");
				}
				for (int i = 0; i < factories.size(); i++) {
					out.println("\t" + (i+1+infos.size()) + ".\t" + factories.get(i).getNameForHumans());
				}
			}
			out.println();
			out.print("PixelPusher>Attach> ");
			if (in.hasNextLine()) {
				String line = in.nextLine().trim();
				try {
					int index = Integer.parseInt(line) - 1;
					if (index >= 0 && index < infos.size()) {
						return infos.get(index).getPixelSet();
					} else if (index >= infos.size() && index < infos.size() + factories.size()) {
						return ctrl.getPixelSetInfoSet().createPixelSetInfo(factories.get(index - infos.size())).getPixelSet();
					} else {
						printError(out, "Number out of range: " + line);
						return null;
					}
				} catch (NumberFormatException nfe) {
					printError(out, "Invalid number: " + line);
					return null;
				}
			} else {
				printError(out, "Unexpected end of input.");
				return null;
			}
		}
	}
}
