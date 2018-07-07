package com.kreative.unipixelpusher.gui;

import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import com.kreative.unipixelpusher.PixelSequence;

public class SequenceConfigurationFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	
	public SequenceConfigurationFrame(PixelSequence sequence) {
		super(sequence.toString());
		JPanel content = new JPanel(new BorderLayout());
		content.add(sequence.createConfigurationPanel(), BorderLayout.CENTER);
		content.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		setContentPane(content);
		setResizable(false);
		pack();
		setLocationRelativeTo(null);
	}
}
