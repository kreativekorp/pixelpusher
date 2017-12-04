package com.kreative.pixelpusher.cli;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import com.kreative.pixelpusher.device.PixelPusherController;
import com.kreative.pixelpusher.device.StripInfoFrame;
import com.kreative.pixelpusher.pixelset.PixelSet;
import com.kreative.pixelpusher.pixelset.PixelSetInfo;
import com.kreative.pixelpusher.pixelset.PixelSetInfoFrame;

public class RenameSetCommand extends CLICommand {
	public RenameSetCommand() {
		super(
			new String[]{"n", "mv", "rn", "name", "rename"},
			"Change the name of an active pixel sequence.",
			"rename [<oldname> [<newname>]]"
		);
	}
	
	@Override
	public void main(Scanner in, PrintStream out, PixelPusherController ctrl, String[] args) {
		try {
			StringSearchPattern ssp = (args.length > 1) ? new StringSearchPattern(args[1]) : null;
			String newName = (args.length > 2 && args[2].length() > 0) ? args[2] : null;
			List<PixelSetInfo<? extends PixelSet>> infos = new ArrayList<PixelSetInfo<? extends PixelSet>>();
			for (PixelSetInfo<? extends PixelSet> info : ctrl.getPixelSetInfoSet().getPixelSetInfoList()) {
				if (ssp == null || ssp.matches(info)) {
					infos.add(info);
				}
			}
			if (infos.size() < 1) {
				printError(out, "No pixel sequences found.");
			} else if (infos.size() == 1) {
				main(out, infos.get(0), newName);
			} else {
				out.println();
				out.println("Select a pixel sequence to rename:");
				for (int i = 0; i < infos.size(); i++) {
					out.println("\t" + (i+1) + ".\t" + infos.get(i).getName());
				}
				out.println();
				out.print("PixelPusher>Rename> ");
				if (in.hasNextLine()) {
					String line = in.nextLine().trim();
					try {
						int index = Integer.parseInt(line) - 1;
						if (index >= 0 && index < infos.size()) {
							main(out, infos.get(index), newName);
						} else {
							printError(out, "Number out of range: " + line);
						}
					} catch (NumberFormatException nfe) {
						printError(out, "Invalid number: " + line);
					}
				} else {
					printError(out, "Unexpected end of input.");
				}
			}
		} catch (GeneralParseException e) {
			printError(out, e.getMessage());
		}
	}
	
	private void main(PrintStream out, PixelSetInfo<? extends PixelSet> info, String newName) {
		if (newName != null) {
			info.setName(newName);
			out.println();
			out.println("Renamed pixel sequence.");
			out.println();
		} else {
			PixelSetInfoFrame frame = new PixelSetInfoFrame(info);
			frame.pack();
			frame.setLocationRelativeTo(null);
			frame.setDefaultCloseOperation(StripInfoFrame.DISPOSE_ON_CLOSE);
			frame.setVisible(true);
		}
	}
}
