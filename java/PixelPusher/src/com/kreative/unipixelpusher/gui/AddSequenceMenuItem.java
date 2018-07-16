package com.kreative.unipixelpusher.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import com.kreative.unipixelpusher.PixelProgram;
import com.kreative.unipixelpusher.SequenceLoader;

public class AddSequenceMenuItem extends JMenuItem {
	private static final long serialVersionUID = 1L;
	
	private SequenceLoader loader;
	private PixelProgram program;
	private AddSequenceFrame frame;
	
	public AddSequenceMenuItem(SequenceLoader loader, PixelProgram program) {
		super("Add Sequences...");
		setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_J, SwingUtils.SHORTCUT_KEY));
		this.loader = loader;
		this.program = program;
		this.frame = null;
		this.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doMenu();
			}
		});
	}
	
	public void doMenu() {
		if (frame == null) {
			frame = new AddSequenceFrame(loader, program);
			frame.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosed(WindowEvent e) {
					frame = null;
				}
			});
		}
		frame.setVisible(true);
	}
	
	@Override
	public void finalize() {
		if (frame != null) frame.dispose();
	}
}
