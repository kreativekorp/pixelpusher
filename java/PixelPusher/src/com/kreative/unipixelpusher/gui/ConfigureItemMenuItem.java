package com.kreative.unipixelpusher.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import com.kreative.unipixelpusher.PixelProgram;
import com.kreative.unipixelpusher.PixelProgramListener;
import com.kreative.unipixelpusher.PixelSequence;

public class ConfigureItemMenuItem extends JMenuItem {
	private static final long serialVersionUID = 1L;
	
	private ProgramComponent pc;
	
	public ConfigureItemMenuItem(ProgramComponent pc) {
		super("Configure Item");
		setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, SwingUtils.SHORTCUT_KEY));
		this.pc = pc;
		this.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doMenu();
			}
		});
	}
	
	public void doMenu() {
		Object o = pc.getSelectedItem();
		if (o instanceof PixelSequence) {
			if (SequenceConfigurationFrame.open((PixelSequence)o) != null) {
				PixelProgram pp = pc.getProgram();
				for (PixelProgramListener l : pp.getPixelProgramListeners()) {
					l.pixelProgramChanged(pp);
				}
			}
		} else if (o instanceof PixelProgram.DeviceInfo) {
			DeviceConfigurationFrame.open((PixelProgram.DeviceInfo)o);
		}
	}
}
