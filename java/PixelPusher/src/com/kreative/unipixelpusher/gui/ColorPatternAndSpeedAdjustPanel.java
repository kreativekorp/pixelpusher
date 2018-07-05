package com.kreative.unipixelpusher.gui;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import com.kreative.unipixelpusher.PixelSequence;
import com.kreative.unipixelpusher.mmxl.MMXLParser;

public class ColorPatternAndSpeedAdjustPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	public ColorPatternAndSpeedAdjustPanel(MMXLParser p, PixelSequence s) {
		ColorPatternPanel cpp = new ColorPatternPanel(
			p, new ColorListCellRenderer.LargeCircle(),
			(PixelSequence.ColorPattern)s, true
		);
		SpeedAdjustPanel sap = new SpeedAdjustPanel((PixelSequence.SpeedAdjust)s, true);
		JPanel bottomPanel = new JPanel(new BorderLayout(8, 8));
		bottomPanel.add(sap, BorderLayout.CENTER);
		bottomPanel.add(new JSeparator(), BorderLayout.PAGE_START);
		setLayout(new BorderLayout(8, 8));
		add(cpp, BorderLayout.CENTER);
		add(bottomPanel, BorderLayout.PAGE_END);
	}
}
