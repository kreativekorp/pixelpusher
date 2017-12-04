package com.kreative.pixelpusher.array;

import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class GenericArrayVisualEditor<T extends PixelArray> extends PixelArrayVisualEditor<T> {
	private static final long serialVersionUID = 1L;
	
	public GenericArrayVisualEditor(T array, PixelArrayVisualizer visualizer, PixelArrayEditor<T> editor) {
		super(array, visualizer, editor);
		JPanel mainPanel = new JPanel(new BorderLayout(16, 16));
		mainPanel.add(this.visualizer, BorderLayout.CENTER);
		mainPanel.add(this.editor, BorderLayout.PAGE_END);
		mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		setLayout(new BorderLayout());
		add(mainPanel, BorderLayout.CENTER);
	}
}
