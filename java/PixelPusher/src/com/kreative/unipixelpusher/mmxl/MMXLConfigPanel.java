package com.kreative.unipixelpusher.mmxl;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import com.kreative.unipixelpusher.gui.ColorListCellRenderer;
import com.kreative.unipixelpusher.gui.ColorPatternPanel;
import com.kreative.unipixelpusher.gui.SpeedAdjustPanel;

public class MMXLConfigPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	public MMXLConfigPanel(MMXLParser p, MMXLSequence s) {
		ColorPatternPanel cpp = new ColorPatternPanel(p, new ColorListCellRenderer.MMXL(), s, true);
		BlinkPatternPanel bpp = new BlinkPatternPanel(p, s, true);
		SpeedAdjustPanel sap = new SpeedAdjustPanel(s, true);
		JPanel topPanel = new JPanel(new BorderLayout(8, 8));
		topPanel.add(cpp, BorderLayout.CENTER);
		topPanel.add(new JSeparator(), BorderLayout.PAGE_END);
		JPanel bottomPanel = new JPanel(new BorderLayout(8, 8));
		bottomPanel.add(sap, BorderLayout.CENTER);
		bottomPanel.add(new JSeparator(), BorderLayout.PAGE_START);
		setLayout(new BorderLayout(8, 8));
		add(bpp, BorderLayout.CENTER);
		add(topPanel, BorderLayout.PAGE_START);
		add(bottomPanel, BorderLayout.PAGE_END);
	}
}
