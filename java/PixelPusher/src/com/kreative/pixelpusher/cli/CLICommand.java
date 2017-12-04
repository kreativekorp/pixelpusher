package com.kreative.pixelpusher.cli;

import java.io.PrintStream;
import java.util.Scanner;
import com.kreative.pixelpusher.device.PixelPusherController;

public abstract class CLICommand {
	private final String[] names;
	private final String description;
	private final String syntax;
	
	protected CLICommand(String name, String description, String syntax) {
		this.names = new String[]{name};
		this.description = description;
		this.syntax = syntax;
	}
	
	protected CLICommand(String[] names, String description, String syntax) {
		this.names = names;
		this.description = description;
		this.syntax = syntax;
	}
	
	public final String[] getNames() {
		return this.names;
	}
	
	public final String getDescription() {
		return this.description;
	}
	
	public final String getSyntax() {
		return this.syntax;
	}
	
	public abstract void main(Scanner in, PrintStream out, PixelPusherController ctrl, String[] args);
	
	protected static boolean equalsIgnoreCaseAny(String a, String... b) {
		for (String c : b) {
			if (c.equalsIgnoreCase(a)) {
				return true;
			}
		}
		return false;
	}
	
	protected static void printCount(PrintStream out, String action, int count, String singular, String plural) {
		out.println();
		out.println(action + " " + count + " " + ((count == 1) ? singular : plural) + ".");
		out.println();
	}
	
	protected static void printError(PrintStream out, String error) {
		out.println();
		out.println("Error: " + error);
		out.println();
	}
}
