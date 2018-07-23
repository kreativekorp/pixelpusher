package com.kreative.unipixelpusher.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

public class ConfigureItemMenuItem extends JMenuItem {
	private static final long serialVersionUID = 1L;
	
	public ConfigureItemMenuItem(final ProgramComponent pc) {
		super("Configure Item");
		setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, SwingUtils.SHORTCUT_KEY));
		this.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pc.openConfiguration();
			}
		});
	}
}
