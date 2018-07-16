package com.kreative.unipixelpusher.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

public class SaveProgramAsMenuItem extends JMenuItem {
	private static final long serialVersionUID = 1L;
	
	public SaveProgramAsMenuItem(final SaveManager sm) {
		super("Save Program As...");
		setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, SwingUtils.SHORTCUT_KEY | KeyEvent.SHIFT_MASK));
		this.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sm.saveAs();
			}
		});
	}
}
