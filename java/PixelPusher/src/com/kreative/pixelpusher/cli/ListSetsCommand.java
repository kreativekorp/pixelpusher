package com.kreative.pixelpusher.cli;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import com.kreative.pixelpusher.device.PixelPusherController;
import com.kreative.pixelpusher.pixelset.PixelSet;
import com.kreative.pixelpusher.pixelset.PixelSetInfo;

public class ListSetsCommand extends CLICommand {
	public ListSetsCommand() {
		super(
			new String[]{"/s", "s", "ls", "sets", "listsets", "sequences", "listsequences"},
			"Print a list of active pixel sequences.",
			"listsequences [<name>]"
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
			out.println();
			out.println("Active Pixel Sequences:");
			if (infos.isEmpty()) {
				out.println("\t< No Sequences >");
			} else for (PixelSetInfo<? extends PixelSet> info : infos) {
				out.println("\t" + info.getName() + ":");
				out.println("\t\tType Name for Machines: " + info.getPixelSetFactory().getNameForMachines());
				out.println("\t\tType Name for Humans: " + info.getPixelSetFactory().getNameForHumans());
				out.println("\t\tFactory Class: " + getClassName(info.getPixelSetFactory().getClass()));
				out.println("\t\tPixelSet Class: " + getClassName(info.getPixelSet().getClass()));
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
