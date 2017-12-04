package com.kreative.pixelpusher.sequence;

import java.awt.BorderLayout;
import javax.swing.JLabel;

public class GenericSequenceEditor<T extends PixelSequence> extends PixelSequenceEditor<T> {
	private static final long serialVersionUID = 1L;
	
	public GenericSequenceEditor(T sequence) {
		super(sequence);
		setLayout(new BorderLayout());
		JLabel label = new JLabel("This type of sequence has no settings to edit.");
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setVerticalAlignment(JLabel.CENTER);
		add(label, BorderLayout.CENTER);
	}
}
