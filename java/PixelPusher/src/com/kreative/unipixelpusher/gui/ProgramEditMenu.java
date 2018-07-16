package com.kreative.unipixelpusher.gui;

import javax.swing.JMenu;
import com.kreative.unipixelpusher.DeviceLoader;
import com.kreative.unipixelpusher.PixelProgram;
import com.kreative.unipixelpusher.SequenceLoader;

public class ProgramEditMenu extends JMenu {
	private static final long serialVersionUID = 1L;
	
	public ProgramEditMenu(SequenceLoader sl, DeviceLoader dl, PixelProgram pp, ProgramComponent pc) {
		super("Edit");
		add(new AddSequenceMenuItem(sl, pp));
		add(new AddDeviceMenuItem(dl, pp));
		addSeparator();
		add(new ConfigureItemMenuItem(pc));
		addSeparator();
		add(new RemoveItemMenuItem(pc));
	}
}
