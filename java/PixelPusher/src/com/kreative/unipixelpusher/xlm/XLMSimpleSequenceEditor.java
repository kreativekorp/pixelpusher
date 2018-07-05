package com.kreative.unipixelpusher.xlm;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class XLMSimpleSequenceEditor extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private final XLMSequence sequence;
	
	public XLMSimpleSequenceEditor(XLMSequence sequence) {
		this.sequence = sequence;
		setLayout(new BorderLayout());
		add(makeMainPanel(), BorderLayout.CENTER);
	}
	
	private JPanel makeMainPanel() {
		JPanel labelPanel = new JPanel(new GridLayout(0,1,16,16));
		JPanel mainPanel = new JPanel(new GridLayout(0,1,16,16));
		for (int i = 0; i < sequence.getChannelCount(); i++) {
			labelPanel.add(makeBig(center(new JLabel("CH " + (i+1)))));
			mainPanel.add(new XLMSimpleChannelEditor(sequence.getChannel(i)));
		}
		JPanel main = new JPanel(new BorderLayout(32,32));
		main.add(labelPanel, BorderLayout.LINE_START);
		main.add(mainPanel, BorderLayout.CENTER);
		return main;
	}
	
	private static <C extends JComponent> C makeBig(C c) {
		c.setFont(c.getFont().deriveFont(18.0f));
		c.putClientProperty("JButton.buttonType", "bevel");
		return c;
	}
	
	private static JLabel center(JLabel l) {
		l.setHorizontalAlignment(JLabel.CENTER);
		l.setVerticalAlignment(JLabel.CENTER);
		return l;
	}
}
