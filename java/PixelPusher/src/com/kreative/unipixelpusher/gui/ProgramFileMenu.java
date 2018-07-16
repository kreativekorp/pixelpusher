package com.kreative.unipixelpusher.gui;

import javax.swing.JFrame;
import javax.swing.JMenu;
import com.kreative.unipixelpusher.DeviceConfiguration;

public class ProgramFileMenu extends JMenu {
	private static final long serialVersionUID = 1L;
	
	public ProgramFileMenu(SaveManager sm, JFrame frame, DeviceConfiguration dc) {
		super("File");
		add(new NewProgramMenuItem(sm));
		add(new OpenProgramMenuItem(sm));
		add(new SaveProgramMenuItem(sm));
		add(new SaveProgramAsMenuItem(sm));
		addSeparator();
		add(new NewDeviceConfigurationMenuItem(dc));
		add(new OpenDeviceConfigurationMenuItem(frame, dc));
		add(new SaveDeviceConfigurationMenuItem(frame, dc));
		add(new SaveDeviceConfigurationAsMenuItem(frame, dc));
		if (!SwingUtils.IS_MAC_OS) {
			addSeparator();
			add(new ExitMenuItem(sm));
		}
	}
}
