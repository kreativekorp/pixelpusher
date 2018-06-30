package com.kreative.unipixelpusher.gui;

import javax.swing.JComboBox;
import com.kreative.unipixelpusher.GammaCurve;

public class GammaPopupMenu extends JComboBox {
	private static final long serialVersionUID = 1L;
	
	public GammaPopupMenu() {
		super(GammaCurve.VALUES);
		this.setEditable(false);
		this.setMaximumRowCount(GammaCurve.VALUES.length);
		this.setSelectedIndex(0);
	}
}
