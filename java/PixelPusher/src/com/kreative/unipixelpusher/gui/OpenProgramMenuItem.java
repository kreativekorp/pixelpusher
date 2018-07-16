package com.kreative.unipixelpusher.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

public class OpenProgramMenuItem extends JMenuItem {
	private static final long serialVersionUID = 1L;
	
	public OpenProgramMenuItem(final SaveManager sm) {
		super("Open Program...");
		setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, SwingUtils.SHORTCUT_KEY));
		this.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sm.open();
			}
		});
	}
}
