package com.kreative.unipixelpusher.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import com.kreative.unipixelpusher.DeviceLoader;
import com.kreative.unipixelpusher.PixelProgram;

public class AddDeviceMenuItem extends JMenuItem {
	private static final long serialVersionUID = 1L;
	
	private DeviceLoader loader;
	private PixelProgram program;
	private AddDeviceFrame frame;
	
	public AddDeviceMenuItem(DeviceLoader loader, PixelProgram program) {
		super("Add Devices...");
		setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, SwingUtils.SHORTCUT_KEY));
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
			frame = new AddDeviceFrame(loader, program);
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
