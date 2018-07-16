package com.kreative.unipixelpusher.gui;

import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import com.kreative.unipixelpusher.DeviceConfiguration;
import com.kreative.unipixelpusher.DeviceLoader;
import com.kreative.unipixelpusher.PixelProgram;
import com.kreative.unipixelpusher.SequenceLoader;

public class ProgramFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private final DeviceConfiguration dc;
	private final DeviceLoader dl;
	private final SequenceLoader sl;
	private final PixelProgram pp;
	private final ProgramComponent pc;
	private final SaveManager sm;
	
	public ProgramFrame(DeviceConfiguration dc, DeviceLoader dl, SequenceLoader sl) {
		super("Untitled");
		this.dc = dc;
		this.dl = dl;
		this.sl = sl;
		this.pp = new PixelProgram();
		this.pc = new ProgramComponent(pp);
		this.sm = new SaveManager(this, null, pp, dl);
		
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.add(pc, BorderLayout.CENTER);
		mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		setContentPane(mainPanel);
		setJMenuBar(new ProgramMenuBar(sm, this, dc, sl, dl, pp, pc));
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setResizable(true);
		pack();
		setLocationRelativeTo(null);
		
		pp.update(dl);
	}
	
	public DeviceConfiguration getDeviceConfiguration() {
		return this.dc;
	}
	
	public DeviceLoader getDeviceLoader() {
		return this.dl;
	}
	
	public SequenceLoader getSequenceLoader() {
		return this.sl;
	}
	
	public PixelProgram getProgram() {
		return this.pp;
	}
	
	public ProgramComponent getProgramComponent() {
		return this.pc;
	}
	
	public SaveManager getSaveManager() {
		return this.sm;
	}
}
