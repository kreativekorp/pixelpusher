package com.kreative.pixelpusher.cli;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import com.kreative.pixelpusher.device.PixelPusherController;
import com.kreative.pixelpusher.device.StripInfoFrame;
import com.kreative.pixelpusher.pixelset.PixelSet;
import com.kreative.pixelpusher.pixelset.PixelSetInfo;
import com.kreative.pixelpusher.pixelset.PixelSetVisualEditorFrame;

public class EditSetCommand extends CLICommand {
	public EditSetCommand() {
		super(
			new String[]{"e", "ed", "edit"},
			"Edit an active pixel sequence.",
			"edit [<name>]"
		);
	}
	
	@Override
	public void main(Scanner in, PrintStream out, PixelPusherController ctrl, String[] args) {
		try {
			StringSearchPattern ssp = (args.length > 1) ? new StringSearchPattern(args[1]) : null;
			List<PixelSetInfo<? extends PixelSet>> infos = new ArrayList<PixelSetInfo<? extends PixelSet>>();
			for (PixelSetInfo<? extends PixelSet> info : ctrl.getPixelSetInfoSet().getPixelSetInfoList()) {
				if (ssp == null || ssp.matches(info)) {
					infos.add(info);
				}
			}
			if (infos.size() < 1) {
				printError(out, "No pixel sequences found.");
			} else if (infos.size() == 1) {
				main(ctrl, infos.get(0));
			} else {
				out.println();
				out.println("Select a pixel sequence to edit:");
				for (int i = 0; i < infos.size(); i++) {
					out.println("\t" + (i+1) + ".\t" + infos.get(i).getName());
				}
				out.println();
				out.print("PixelPusher>Edit> ");
				if (in.hasNextLine()) {
					String line = in.nextLine().trim();
					try {
						int index = Integer.parseInt(line) - 1;
						if (index >= 0 && index < infos.size()) {
							main(ctrl, infos.get(index));
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
	
	private void main(PixelPusherController ctrl, PixelSetInfo<? extends PixelSet> info) {
		final PixelSetVisualEditorFrame frame = info.createVisualEditorFrame(ctrl.getPixelSetInfoSet());
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(StripInfoFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
		frame.start();
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				frame.stop();
			}
		});
	}
}
