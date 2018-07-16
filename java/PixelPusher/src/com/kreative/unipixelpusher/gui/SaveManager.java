package com.kreative.unipixelpusher.gui;

import java.awt.FileDialog;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import com.kreative.unipixelpusher.DeviceLoader;
import com.kreative.unipixelpusher.PixelProgram;
import com.kreative.unipixelpusher.PixelProgramListener;

public class SaveManager {
	private JFrame frame;
	private File file;
	private PixelProgram program;
	private DeviceLoader loader;
	private boolean changed;
	
	public SaveManager(JFrame frame, File file, PixelProgram program, DeviceLoader loader) {
		this.frame = frame;
		this.file = file;
		this.program = program;
		this.loader = loader;
		this.changed = false;
		updateWindow();
		
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (close()) e.getWindow().dispose();
			}
		});
		
		program.addPixelProgramListener(new PixelProgramListener() {
			@Override
			public void pixelProgramChanged(PixelProgram program) {
				setChanged();
			}
			@Override
			public void pixelDevicesChanged(PixelProgram program) {
				// ignored
			}
		});
	}
	
	public void setChanged() {
		this.changed = true;
		updateWindow();
	}
	
	public boolean clear() {
		if (!close()) return false;
		this.file = null;
		this.program.clear();
		this.changed = false;
		updateWindow();
		return true;
	}
	
	public boolean open(File file) {
		if (file == null) return open();
		this.file = file;
		return read();
	}
	
	public boolean open() {
		if (!clear()) return false;
		FileDialog fd = new FileDialog(frame, "Open Program", FileDialog.LOAD);
		fd.setVisible(true);
		String parent = fd.getDirectory();
		String name = fd.getFile();
		if (parent == null || name == null) return false;
		file = new File(parent, name);
		return read();
	}
	
	private boolean read() {
		try {
			program.read(file);
			program.update(loader);
			changed = false;
			updateWindow();
			return true;
		} catch (IOException ioe) {
			JOptionPane.showMessageDialog(
				frame, "An error occurred while reading the selected file.",
				"Open Program", JOptionPane.ERROR_MESSAGE
			);
			updateWindow();
			return false;
		}
	}
	
	public void exit() {
		if (close()) frame.dispose();
	}
	
	public boolean close() {
		if (!changed || (file == null && program.isEmpty())) return true;
		String fileName = (file != null) ? file.getName() : "Untitled";
		switch (new SaveChangesDialog(frame, fileName).showDialog()) {
			case SAVE: return save();
			case DONT_SAVE: return true;
			default: return false;
		}
	}
	
	public boolean save() {
		if (file == null) return saveAs();
		return write();
	}
	
	public boolean saveAs() {
		FileDialog fd = new FileDialog(frame, "Save Program", FileDialog.SAVE);
		fd.setVisible(true);
		String parent = fd.getDirectory();
		String name = fd.getFile();
		if (parent == null || name == null) return false;
		if (!name.toLowerCase().endsWith(".ppgmx")) name += ".ppgmx";
		file = new File(parent, name);
		return write();
	}
	
	private boolean write() {
		try {
			program.write(file);
			changed = false;
			updateWindow();
			return true;
		} catch (IOException ioe) {
			JOptionPane.showMessageDialog(
				frame, "An error occurred while saving this file.",
				"Save Program", JOptionPane.ERROR_MESSAGE
			);
			updateWindow();
			return false;
		}
	}
	
	private void updateWindow() {
		if (SwingUtils.IS_MAC_OS) {
			frame.getRootPane().putClientProperty("Window.documentFile", file);
			frame.getRootPane().putClientProperty("Window.documentModified", changed);
			frame.setTitle((file != null) ? file.getName() : "Untitled");
		} else {
			String fileName = (file != null) ? file.getName() : "Untitled";
			frame.setTitle(changed ? (fileName + " \u2022") : fileName);
		}
	}
}
