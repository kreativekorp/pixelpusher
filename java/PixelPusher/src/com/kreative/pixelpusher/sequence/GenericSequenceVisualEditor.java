package com.kreative.pixelpusher.sequence;

import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class GenericSequenceVisualEditor<T extends PixelSequence> extends PixelSequenceVisualEditor<T> {
	private static final long serialVersionUID = 1L;
	
	public GenericSequenceVisualEditor(T sequence, PixelSequenceVisualizer visualizer, PixelSequenceEditor<T> editor) {
		super(sequence, visualizer, editor);
		JPanel mainPanel = new JPanel(new BorderLayout(16, 16));
		mainPanel.add(this.visualizer, BorderLayout.PAGE_START);
		mainPanel.add(this.editor, BorderLayout.CENTER);
		mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		setLayout(new BorderLayout());
		add(mainPanel, BorderLayout.CENTER);
	}
}
