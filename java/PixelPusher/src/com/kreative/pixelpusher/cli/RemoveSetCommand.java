package com.kreative.pixelpusher.cli;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import com.kreative.pixelpusher.device.PixelPusherController;
import com.kreative.pixelpusher.pixelset.PixelSet;
import com.kreative.pixelpusher.pixelset.PixelSetInfo;

public class RemoveSetCommand extends CLICommand {
	public RemoveSetCommand() {
		super(
			new String[]{"rm", "remove"},
			"Remove an active pixel sequence.",
			"remove [<name>]"
		);
	}
	
	@Override
	public void main(Scanner in, PrintStream out, PixelPusherController ctrl, String[] args) {
		try {
			StringSearchPattern ssp = (args.length > 1) ? new StringSearchPattern(args[1]) : null;
			if (ssp == null) {
				out.println();
				out.print("Are you sure you want to remove all sequences? ");
				String line = in.hasNextLine() ? in.nextLine().trim() : "";
				if (!(line.equalsIgnoreCase("y") || line.equalsIgnoreCase("yes"))) {
					out.println();
					return;
				}
			}
			List<PixelSetInfo<? extends PixelSet>> infos = new ArrayList<PixelSetInfo<? extends PixelSet>>();
			for (PixelSetInfo<? extends PixelSet> info : ctrl.getPixelSetInfoSet().getPixelSetInfoList()) {
				if (ssp == null || ssp.matches(info)) {
					infos.add(info);
				}
			}
			for (PixelSetInfo<? extends PixelSet> info : infos) {
				ctrl.getPixelSetInfoSet().removePixelSetInfo(info);
			}
			printCount(out, "Removed", infos.size(), "sequence", "sequences");
		} catch (GeneralParseException e) {
			printError(out, e.getMessage());
		}
	}
}
