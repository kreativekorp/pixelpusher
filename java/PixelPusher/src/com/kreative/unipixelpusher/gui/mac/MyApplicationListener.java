package com.kreative.unipixelpusher.gui.mac;

import java.awt.desktop.OpenFilesEvent;
import java.awt.desktop.OpenFilesHandler;
import java.awt.desktop.PrintFilesEvent;
import java.awt.desktop.PrintFilesHandler;
import java.awt.desktop.QuitEvent;
import java.awt.desktop.QuitHandler;
import java.awt.desktop.QuitResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import javax.swing.JOptionPane;
import com.kreative.unipixelpusher.DeviceConfiguration;
import com.kreative.unipixelpusher.gui.SaveManager;
import com.kreative.unipixelpusher.marquee.MarqueeParser;
import com.kreative.unipixelpusher.mmxl.MMXLParser;

public class MyApplicationListener {
	private static final String[][] classAndMethodNames = {
		{ "java.awt.Desktop", "getDesktop" },
		{ "com.kreative.ual.eawt.NewApplicationAdapter", "getInstance" },
		{ "com.kreative.ual.eawt.OldApplicationAdapter", "getInstance" },
	};
	
	private final DeviceConfiguration dc;
	private final SaveManager sm;
	
	public MyApplicationListener(final DeviceConfiguration dc, final SaveManager sm) {
		this.dc = dc;
		this.sm = sm;
		for (String[] classAndMethodName : classAndMethodNames) {
			try {
				Class<?> cls = Class.forName(classAndMethodName[0]);
				Method getInstance = cls.getMethod(classAndMethodName[1]);
				Object instance = getInstance.invoke(null);
				cls.getMethod("setOpenFileHandler", OpenFilesHandler.class).invoke(instance, open);
				cls.getMethod("setPrintFileHandler", PrintFilesHandler.class).invoke(instance, print);
				cls.getMethod("setQuitHandler", QuitHandler.class).invoke(instance, quit);
				System.out.println("Registered app event handlers through " + classAndMethodName[0]);
				return;
			} catch (Exception e) {
				System.out.println("Failed to register app event handlers through " + classAndMethodName[0] + ": " + e);
			}
		}
	}
	
	private void readFile(final File f) {
		String name = f.getName().toLowerCase();
		if (name.endsWith(".ppgmx")) {
			sm.open(f);
		} else if (name.endsWith(".ppdcx")) {
			try {
				dc.read(f);
			} catch (IOException ioe) {
				JOptionPane.showMessageDialog(
					null, "An error occurred while reading the selected file.",
					"Load Device Configuration", JOptionPane.ERROR_MESSAGE
				);
			}
		} else if (name.endsWith(".mmxlx")) {
			try {
				MMXLParser.getInstance().parse(f);
			} catch (IOException ioe) {
				JOptionPane.showMessageDialog(
					null, "An error occurred while reading the selected file.",
					"Load MMXL Presets", JOptionPane.ERROR_MESSAGE
				);
			}
		} else if (name.endsWith(".ppmqx")) {
			try {
				MarqueeParser.getInstance().parse(f);
			} catch (IOException ioe) {
				JOptionPane.showMessageDialog(
					null, "An error occurred while reading the selected file.",
					"Load Marquee Presets", JOptionPane.ERROR_MESSAGE
				);
			}
		}
	}
	
	private final OpenFilesHandler open = new OpenFilesHandler() {
		@Override
		public void openFiles(final OpenFilesEvent e) {
			for (Object o : e.getFiles()) {
				readFile((File)o);
			}
		}
	};
	
	private final PrintFilesHandler print = new PrintFilesHandler() {
		@Override
		public void printFiles(final PrintFilesEvent e) {
			for (Object o : e.getFiles()) {
				readFile((File)o);
			}
		}
	};
	
	private final QuitHandler quit = new QuitHandler() {
		@Override
		public void handleQuitRequestWith(final QuitEvent e, final QuitResponse r) {
			if (sm.close()) r.performQuit();
			else r.cancelQuit();
		}
	};
}
