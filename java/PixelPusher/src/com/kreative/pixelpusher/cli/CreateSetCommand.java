package com.kreative.pixelpusher.cli;

import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;
import com.kreative.pixelpusher.device.PixelPusherController;
import com.kreative.pixelpusher.pixelset.PixelSet;
import com.kreative.pixelpusher.pixelset.PixelSetFactory;

public class CreateSetCommand extends CLICommand {
	public CreateSetCommand() {
		super(
			new String[]{"m", "mk", "cr", "make", "create"},
			"Create a new pixel sequence.",
			"create [<name> [<type>]]"
		);
	}
	
	@Override
	public void main(Scanner in, PrintStream out, PixelPusherController ctrl, String[] args) {
		try {
			String name = (args.length > 1 && args[1].length() > 0) ? args[1] : null;
			StringSearchPattern fsp = (args.length > 2) ? new StringSearchPattern(args[2]) : null;
			FactorySearchResults factories = FactorySearchResults.forPattern(fsp);
			PixelSetFactory<? extends PixelSet> factory = selectFactory(in, out, factories.getFactories());
			if (factory == null) return;
			ctrl.getPixelSetInfoSet().createPixelSetInfo(factory, name);
			out.println();
			out.println("Created new pixel sequence.");
			out.println();
		} catch (GeneralParseException e) {
			printError(out, e.getMessage());
		}
	}
	
	private static PixelSetFactory<? extends PixelSet> selectFactory(Scanner in, PrintStream out, List<PixelSetFactory<? extends PixelSet>> factories) {
		if (factories.isEmpty()) {
			printError(out, "No sequence types found.");
			return null;
		} else if (factories.size() == 1) {
			return factories.get(0);
		} else {
			out.println();
			out.println("Select type of pixel sequence to create:");
			for (int i = 0; i < factories.size(); i++) {
				out.println("\t" + (i+1) + ".\t" + factories.get(i).getNameForHumans());
			}
			out.println();
			out.print("PixelPusher>Create> ");
			if (in.hasNextLine()) {
				String line = in.nextLine().trim();
				try {
					int index = Integer.parseInt(line) - 1;
					if (index >= 0 && index < factories.size()) {
						return factories.get(index);
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
