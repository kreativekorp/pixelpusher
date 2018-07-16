package com.kreative.unipixelpusher.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import com.kreative.unipixelpusher.DeviceConfiguration;

public class NewDeviceConfigurationMenuItem extends JMenuItem {
	private static final long serialVersionUID = 1L;
	
	public NewDeviceConfigurationMenuItem(final DeviceConfiguration dc) {
		super("Clear Device Configuration");
		setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, SwingUtils.SHORTCUT_KEY | KeyEvent.SHIFT_MASK));
		this.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dc.clear();
			}
		});
	}
}
