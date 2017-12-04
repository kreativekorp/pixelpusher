package com.kreative.pixelpusher.array;

import java.awt.BorderLayout;
import javax.swing.JLabel;

public class GenericArrayEditor<T extends PixelArray> extends PixelArrayEditor<T> {
	private static final long serialVersionUID = 1L;
	
	public GenericArrayEditor(T array) {
		super(array);
		setLayout(new BorderLayout());
		JLabel label = new JLabel("This type of array has no settings to edit.");
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setVerticalAlignment(JLabel.CENTER);
		add(label, BorderLayout.CENTER);
	}
}
