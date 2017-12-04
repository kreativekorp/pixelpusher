package com.kreative.pixelpusher.cli;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import com.kreative.pixelpusher.device.PixelPusherController;

public class PixelPusherCLI {
	public static void main(String[] args) {
		final PrintStream stdout = System.out;
		if (!Arrays.asList(args).contains("--debug")) {
			final PrintStream devnull = new PrintStream(new OutputStream() {
				@Override public void write(int ch) throws IOException {}
				@Override public void write(byte[] b) throws IOException {}
				@Override public void write(byte[] b, int off, int len) throws IOException {}
			});
			System.setOut(devnull);
			System.setErr(devnull);
		}
		
		final List<CLICommand> commands = new ArrayList<CLICommand>();
		commands.add(new ListDevicesCommand());
		commands.add(new ConfigureDeviceCommand());
		commands.add(new ResetDeviceCommand());
		commands.add(new IdentifyCommand());
		commands.add(new ColorCommand());
		commands.add(new ListSetsCommand());
		commands.add(new CreateSetCommand());
		commands.add(new RenameSetCommand());
		commands.add(new EditSetCommand());
		commands.add(new RemoveSetCommand());
		commands.add(new ListTypesCommand());
		commands.add(new AttachCommand());
		commands.add(new UnattachCommand());
		
		final Scanner scanner = new Scanner(System.in);
		final PixelPusherController controller = new PixelPusherController();
		controller.start();
		
		stdout.println("PixelPusherCLI 1.0 (c) Kreative Software");
		stdout.println("Type \"list\" to list all devices or \"help\" for more information.");
		while (true) {
			stdout.print("PixelPusher> ");
			String line = scanner.hasNextLine() ? scanner.nextLine().trim() : ".";
			if (line.length() > 0) {
				String[] inargs = line.split("\\s+");
				if (equalsIgnoreCaseAny(inargs[0], ".", "q", "quit", "x", "exit", "bye")) {
					break;
				} else if (equalsIgnoreCaseAny(inargs[0], "?", "h", "help")) {
					stdout.println();
					stdout.println("Available Commands:");
					for (CLICommand command : commands) {
						String[] names = command.getNames();
						stdout.print("\t");
						for (int i = 0; i < names.length; i++) {
							if (i > 0) stdout.print(", ");
							stdout.print(names[i]);
						}
						stdout.println(":");
						stdout.print("\t\t");
						stdout.println(command.getDescription());
						stdout.print("\t\tSyntax: ");
						stdout.println(command.getSyntax());
					}
					stdout.println("\t?, h, help:");
					stdout.println("\t\tPrint a list of available commands.");
					stdout.println("\t., q, quit, x, exit, bye:");
					stdout.println("\t\tQuit the PixelPusher CLI.");
					stdout.println();
				} else {
					boolean executed = false;
					for (CLICommand command : commands) {
						if (equalsIgnoreCaseAny(inargs[0], command.getNames())) {
							command.main(scanner, stdout, controller, inargs);
							executed = true;
							break;
						}
					}
					if (!executed) {
						stdout.println();
						stdout.println("Unknown command: " + inargs[0]);
						stdout.println();
					}
				}
			}
		}
		
		controller.stop();
		System.exit(0);
	}
	
	private static boolean equalsIgnoreCaseAny(String a, String... b) {
		for (String c : b) {
			if (c.equalsIgnoreCase(a)) {
				return true;
			}
		}
		return false;
	}
}
