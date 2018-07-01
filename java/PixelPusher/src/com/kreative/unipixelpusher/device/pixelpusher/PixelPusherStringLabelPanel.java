package com.kreative.unipixelpusher.device.pixelpusher;

import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PixelPusherStringLabelPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	public PixelPusherStringLabelPanel() {
		setLayout(new GridLayout(0, 1, 4, 4));
		add(new JLabel("Name:"));
		add(new JLabel("Type:"));
		add(new JLabel("Length:"));
		add(new JLabel("Matrix Size:"));
		add(new JLabel("Winding Order:"));
		add(new JLabel("Gamma Curve:"));
		setBorder(BorderFactory.createEmptyBorder(9, 0, 9, 0));
	}
}
