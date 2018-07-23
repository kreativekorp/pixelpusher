package com.kreative.unipixelpusher.gui.mac;

import java.io.File;
import java.io.IOException;
import javax.swing.JOptionPane;
import com.apple.eawt.Application;
import com.apple.eawt.ApplicationEvent;
import com.apple.eawt.ApplicationListener;
import com.kreative.unipixelpusher.DeviceConfiguration;
import com.kreative.unipixelpusher.gui.SaveManager;
import com.kreative.unipixelpusher.marquee.MarqueeParser;
import com.kreative.unipixelpusher.mmxl.MMXLParser;

@SuppressWarnings("deprecation")
public class MyApplicationListener implements ApplicationListener {
	private final DeviceConfiguration dc;
	private final SaveManager sm;
	
	public MyApplicationListener(DeviceConfiguration dc, SaveManager sm) {
		this.dc = dc;
		this.sm = sm;
		Application a = Application.getApplication();
		a.addApplicationListener(this);
	}
	
	public void handleOpenFile(ApplicationEvent e) {
		File f = new File(e.getFilename());
		String name = f.getName().toLowerCase();
		if (name.endsWith(".ppgmx")) {
			e.setHandled(sm.open(f));
		} else if (name.endsWith(".ppdcx")) {
			try {
				dc.read(f);
				e.setHandled(true);
			} catch (IOException ioe) {
				JOptionPane.showMessageDialog(
					null, "An error occurred while reading the selected file.",
					"Load Device Configuration", JOptionPane.ERROR_MESSAGE
				);
			}
		} else if (name.endsWith(".mmxlx")) {
			try {
				MMXLParser.getInstance().parse(f);
				e.setHandled(true);
			} catch (IOException ioe) {
				JOptionPane.showMessageDialog(
					null, "An error occurred while reading the selected file.",
					"Load MMXL Presets", JOptionPane.ERROR_MESSAGE
				);
			}
		} else if (name.endsWith(".ppmqx")) {
			try {
				MarqueeParser.getInstance().parse(f);
				e.setHandled(true);
			} catch (IOException ioe) {
				JOptionPane.showMessageDialog(
					null, "An error occurred while reading the selected file.",
					"Load Marquee Presets", JOptionPane.ERROR_MESSAGE
				);
			}
		}
	}
	
	public void handlePrintFile(ApplicationEvent e) {
		handleOpenFile(e);
	}
	
	public void handleQuit(ApplicationEvent e) {
		e.setHandled(sm.close());
	}
	
	public void handleAbout(ApplicationEvent e) {}
	public void handleOpenApplication(ApplicationEvent e) {}
	public void handlePreferences(ApplicationEvent e) {}
	public void handleReOpenApplication(ApplicationEvent e) {}
}
