package com.kreative.unipixelpusher.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import com.kreative.unipixelpusher.PixelProgram;
import com.kreative.unipixelpusher.PixelSequence;

public class RemoveItemMenuItem extends JMenuItem {
	private static final long serialVersionUID = 1L;
	
	public RemoveItemMenuItem(final ProgramComponent pc) {
		super("Remove Item");
		setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, SwingUtils.SHORTCUT_KEY));
		this.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Object o = pc.getSelectedItem();
				if (o instanceof PixelSequence) {
					pc.getProgram().removeSequence((PixelSequence)o);
				} else if (o instanceof PixelProgram.DeviceInfo) {
					pc.getProgram().removeDevice((PixelProgram.DeviceInfo)o);
				}
			}
		});
	}
}
