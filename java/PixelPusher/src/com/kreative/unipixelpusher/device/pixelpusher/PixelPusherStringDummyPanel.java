package com.kreative.unipixelpusher.device.pixelpusher;

import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PixelPusherStringDummyPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	public PixelPusherStringDummyPanel(int stripNumber) {
		JLabel label = new JLabel("Strip " + stripNumber + " Not Installed");
		label.setForeground(Color.gray);
		
		setLayout(new GridLayout(1, 1));
		add(label);
		setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.gray),
			BorderFactory.createEmptyBorder(8, 8, 8, 8)
		));
	}
}
