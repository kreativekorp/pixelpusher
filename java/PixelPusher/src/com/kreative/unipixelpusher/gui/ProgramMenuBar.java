package com.kreative.unipixelpusher.gui;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import com.kreative.unipixelpusher.DeviceConfiguration;
import com.kreative.unipixelpusher.DeviceLoader;
import com.kreative.unipixelpusher.PixelProgram;
import com.kreative.unipixelpusher.SequenceLoader;

public class ProgramMenuBar extends JMenuBar {
	private static final long serialVersionUID = 1L;
	
	public ProgramMenuBar(
		SaveManager sm, JFrame frame, DeviceConfiguration dc,
		SequenceLoader sl, DeviceLoader dl, PixelProgram pp, ProgramComponent pc
	) {
		add(new ProgramFileMenu(sm, frame, dc));
		add(new ProgramEditMenu(sl, dl, pp, pc));
	}
}
