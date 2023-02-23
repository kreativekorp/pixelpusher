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

public class SaveDeviceConfigurationAsMenuItem extends JMenuItem {
	private static final long serialVersionUID = 1L;
	
	public SaveDeviceConfigurationAsMenuItem(final JFrame frame, final DeviceConfiguration dc) {
		super("Save Device Configuration As...");
		setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, SwingUtils.SHORTCUT_KEY | KeyEvent.SHIFT_MASK));
		this.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				FileDialog fd = new FileDialog(frame, "Save Device Configuration", FileDialog.SAVE);
				if (SaveManager.lastSaveDirectory != null) fd.setDirectory(SaveManager.lastSaveDirectory);
				fd.setVisible(true);
				String parent = fd.getDirectory();
				String name = fd.getFile();
				fd.dispose();
				if (parent == null || name == null) return;
				if (!name.toLowerCase().endsWith(".ppdcx")) name += ".ppdcx";
				File file = new File((SaveManager.lastSaveDirectory = parent), name);
				try {
					dc.write(file);
				} catch (IOException ioe) {
					JOptionPane.showMessageDialog(
						frame, "An error occurred while saving this file.",
						"Save Device Configuration", JOptionPane.ERROR_MESSAGE
					);
				}
			}
		});
	}
}
