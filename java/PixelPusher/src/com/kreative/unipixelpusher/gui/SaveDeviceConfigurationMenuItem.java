package com.kreative.unipixelpusher.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import com.kreative.unipixelpusher.DeviceConfiguration;

public class SaveDeviceConfigurationMenuItem extends JMenuItem {
	private static final long serialVersionUID = 1L;
	
	public SaveDeviceConfigurationMenuItem(final JFrame frame, final DeviceConfiguration dc) {
		super("Save Device Configuration");
		setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, SwingUtils.SHORTCUT_KEY));
		this.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					dc.write();
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
