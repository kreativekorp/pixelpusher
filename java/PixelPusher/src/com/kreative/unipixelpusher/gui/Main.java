package com.kreative.unipixelpusher.gui;

import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import com.kreative.unipixelpusher.DeviceConfiguration;
import com.kreative.unipixelpusher.DeviceLoader;
import com.kreative.unipixelpusher.SequenceLoader;

public class Main {
	public static void main(String[] args) {
		try { System.setProperty("com.apple.mrj.application.apple.menu.about.name", "UniPixelPusher"); } catch (Exception e) {}
		try { System.setProperty("apple.awt.graphics.UseQuartz", "false"); } catch (Exception e) {}
		try { System.setProperty("apple.laf.useScreenMenuBar", "true"); } catch (Exception e) {}
		try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}
		
		try {
			Method getModule = Class.class.getMethod("getModule");
			Object javaDesktop = getModule.invoke(Toolkit.getDefaultToolkit().getClass());
			Object allUnnamed = getModule.invoke(Main.class);
			Class<?> module = Class.forName("java.lang.Module");
			Method addOpens = module.getMethod("addOpens", String.class, module);
			addOpens.invoke(javaDesktop, "sun.awt.X11", allUnnamed);
		} catch (Exception e) {}
		
		try {
			Toolkit tk = Toolkit.getDefaultToolkit();
			Field aacn = tk.getClass().getDeclaredField("awtAppClassName");
			aacn.setAccessible(true);
			aacn.set(tk, "UniPixelPusher");
		} catch (Exception e) {}
		
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
		
		if (SwingUtils.IS_MAC_OS) {
			try {
				Class<?> cls = Class.forName("com.kreative.unipixelpusher.gui.mac.MyApplicationListener");
				cls.getConstructor(DeviceConfiguration.class, SaveManager.class).newInstance(dc, f.getSaveManager());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
