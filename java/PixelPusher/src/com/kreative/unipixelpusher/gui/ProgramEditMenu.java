package com.kreative.unipixelpusher.gui;

import javax.swing.JMenu;

public class ProgramEditMenu extends JMenu {
	private static final long serialVersionUID = 1L;
	
	public ProgramEditMenu(ProgramComponent pc) {
		super("Edit");
		add(new AddSequenceMenuItem(pc));
		add(new AddDeviceMenuItem(pc));
		addSeparator();
		add(new ConfigureItemMenuItem(pc));
		addSeparator();
		add(new RemoveItemMenuItem(pc));
	}
}
