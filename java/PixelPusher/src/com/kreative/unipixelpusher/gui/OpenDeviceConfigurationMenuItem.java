package com.kreative.unipixelpusher.gui;

import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import com.kreative.unipixelpusher.DeviceConfiguration;

public class OpenDeviceConfigurationMenuItem extends JMenuItem {
	private static final long serialVersionUID = 1L;
	
	public OpenDeviceConfigurationMenuItem(final JFrame frame, final DeviceConfiguration dc) {
		super("Load Device Configuration...");
		setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, SwingUtils.SHORTCUT_KEY | KeyEvent.SHIFT_MASK));
		this.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				FileDialog fd = new FileDialog(frame, "Load Device Configuration", FileDialog.LOAD);
				if (SaveManager.lastOpenDirectory != null) fd.setDirectory(SaveManager.lastOpenDirectory);
				fd.setVisible(true);
				String parent = fd.getDirectory();
				String name = fd.getFile();
				fd.dispose();
				if (parent == null || name == null) return;
				File file = new File((SaveManager.lastOpenDirectory = parent), name);
				try {
					dc.read(file);
				} catch (IOException ioe) {
					JOptionPane.showMessageDialog(
						frame, "An error occurred while reading the selected file.",
						"Load Device Configuration", JOptionPane.ERROR_MESSAGE
					);
				}
			}
		});
	}
}
