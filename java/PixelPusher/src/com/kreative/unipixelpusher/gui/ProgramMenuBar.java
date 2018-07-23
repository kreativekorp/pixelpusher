package com.kreative.unipixelpusher.gui;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import com.kreative.unipixelpusher.DeviceConfiguration;

public class ProgramMenuBar extends JMenuBar {
	private static final long serialVersionUID = 1L;
	
	public ProgramMenuBar(SaveManager sm, JFrame frame, DeviceConfiguration dc, ProgramComponent pc) {
		add(new ProgramFileMenu(sm, frame, dc));
		add(new ProgramEditMenu(pc));
	}
}
