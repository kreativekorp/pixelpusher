package com.kreative.unipixelpusher.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

public class ExitMenuItem extends JMenuItem {
	private static final long serialVersionUID = 1L;
	
	public ExitMenuItem(final SaveManager sm) {
		super("Exit");
		setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, SwingUtils.SHORTCUT_KEY));
		this.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sm.exit();
			}
		});
	}
}
