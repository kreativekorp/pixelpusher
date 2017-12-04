package com.kreative.pixelpusher.cli;

import java.io.PrintStream;
import java.util.Scanner;
import com.kreative.pixelpusher.device.PixelPusherController;
import com.kreative.pixelpusher.pixelset.PixelSet;
import com.kreative.pixelpusher.pixelset.PixelSetFactory;

public class ListTypesCommand extends CLICommand {
	public ListTypesCommand() {
		super(
			new String[]{"/t", "t", "lt", "types", "listtypes"},
			"Print a list of pixel sequence types.",
			"listtypes [<name>]"
		);
	}
	
	@Override
	public void main(Scanner in, PrintStream out, PixelPusherController ctrl, String[] args) {
		try {
			StringSearchPattern fsp = (args.length > 1) ? new StringSearchPattern(args[1]) : null;
			FactorySearchResults factories = FactorySearchResults.forPattern(fsp);
			out.println();
			out.println("Pixel Sequence Types:");
			if (factories.getFactories().isEmpty()) {
				out.println("\t< No Types >");
			} else for (PixelSetFactory<? extends PixelSet> factory : factories.getFactories()) {
				out.println("\t" + factory.getNameForHumans() + ":");
				out.println("\t\tName for Machines: " + factory.getNameForMachines());
				out.println("\t\tName for Humans: " + factory.getNameForHumans());
				out.println("\t\tFactory Class: " + getClassName(factory.getClass()));
				out.println("\t\tPixelSet Class: " + getClassName(factory.getPixelSetClass()));
			}
			out.println();
		} catch (GeneralParseException e) {
			printError(out, e.getMessage());
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
