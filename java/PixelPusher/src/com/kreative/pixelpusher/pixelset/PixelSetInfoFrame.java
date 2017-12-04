package com.kreative.pixelpusher.pixelset;

import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class PixelSetInfoFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private PixelSetInfoEditor editor;
	
	public PixelSetInfoFrame(PixelSetInfo<? extends PixelSet> info) {
		super(title(info.getPixelSet()));
		this.editor = new PixelSetInfoEditor(info);
		JPanel main = new JPanel(new BorderLayout());
		main.add(this.editor, BorderLayout.CENTER);
		main.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		setContentPane(main);
		pack();
		setResizable(false);
	}
	
	private static final String title(PixelSet pixelSet) {
		return pixelSet.isSequence() ? "Pixel Sequence Configuration"
		     : pixelSet.isArray() ? "Pixel Array Configuration"
		     : "Pixel Set Configuration";
	}
}
