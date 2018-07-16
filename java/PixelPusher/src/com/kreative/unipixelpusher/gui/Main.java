package com.kreative.unipixelpusher.gui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import com.kreative.unipixelpusher.DeviceConfiguration;
import com.kreative.unipixelpusher.DeviceLoader;
import com.kreative.unipixelpusher.SequenceLoader;

public class Main {
	public static void main(String[] args) {
		try { System.setProperty("com.apple.mrj.application.apple.menu.about.name", "PixelPusher"); } catch (Exception e) {}
		try { System.setProperty("apple.awt.graphics.UseQuartz", "false"); } catch (Exception e) {}
		try { System.setProperty("apple.laf.useScreenMenuBar", "true"); } catch (Exception e) {}
		try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}
		
		final DeviceConfiguration dc = new DeviceConfiguration();
		try { dc.read(); } catch (IOException ioe) {}
		DeviceLoader dl = new DeviceLoader(dc);
		SequenceLoader sl = new SequenceLoader();
		ProgramFrame f = new ProgramFrame(dc, dl, sl);
		f.setVisible(true);
		
		for (String arg : args) {
			File file = new File(arg);
			String lname = file.getName().toLowerCase();
			if (lname.endsWith(".ppgmx")) {
				if (f.getSaveManager().open(file)) {
					System.out.println("Loaded program: " + arg);
				} else {
					System.err.println("Failed to load program: " + arg);
				}
			} else if (lname.endsWith(".ppdcx")) {
				try {
					f.getDeviceConfiguration().read(file);
					System.out.println("Loaded device configuration: " + arg);
				} catch (IOException ioe) {
					JOptionPane.showMessageDialog(
						f, "An error occurred while reading the selected file.",
						"Load Device Configuration", JOptionPane.ERROR_MESSAGE
					);
					System.err.println("Failed to load device configuration: " + arg);
				}
			} else {
				System.err.println("Unknown file type: " + arg);
			}
		}
		
		f.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				try { dc.write(); } catch (IOException ioe) {}
				System.exit(0);
			}
		});
	}
}
