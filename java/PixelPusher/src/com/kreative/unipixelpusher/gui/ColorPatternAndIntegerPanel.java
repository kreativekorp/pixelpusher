package com.kreative.unipixelpusher.gui;

import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import com.kreative.unipixelpusher.PixelSequence;
import com.kreative.unipixelpusher.mmxl.MMXLParser;

public abstract class ColorPatternAndIntegerPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	public ColorPatternAndIntegerPanel(
		MMXLParser p,
		PixelSequence.ColorPattern s,
		String label, int min, int max
	) {
		ColorPatternPanel cpp = new ColorPatternPanel(p, new ColorListCellRenderer.LargeCircle(), s, true);
		final SpinnerNumberModel model = new SpinnerNumberModel(getValue(), min, max, 1);
		final JSpinner spinner = new JSpinner(model);
		final JPanel spinnerPanel = new JPanel(new BorderLayout());
		spinnerPanel.add(spinner, BorderLayout.LINE_START);
		JPanel innerPanel = new JPanel(new BorderLayout(8, 8));
		innerPanel.add(new JLabel(label), BorderLayout.LINE_START);
		innerPanel.add(spinnerPanel, BorderLayout.CENTER);
		JPanel bottomPanel = new JPanel(new BorderLayout(8, 8));
		bottomPanel.add(innerPanel, BorderLayout.CENTER);
		bottomPanel.add(new JSeparator(), BorderLayout.PAGE_START);
		setLayout(new BorderLayout(8, 8));
		add(cpp, BorderLayout.CENTER);
		add(bottomPanel, BorderLayout.PAGE_END);
		
		model.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				setValue(model.getNumber().intValue());
			}
		});
	}
	
	public abstract int getValue();
	public abstract void setValue(int value);
}
