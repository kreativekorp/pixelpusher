package com.kreative.unipixelpusher.xlm;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;

public class XLMConfigPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private final XLMSequence sequence;
	private final JPanel container;
	private final JButton button;
	private boolean advanced;
	
	public XLMConfigPanel(XLMSequence sequence) {
		this.sequence = sequence;
		this.container = new JPanel(new BorderLayout());
		this.button = new JButton();
		this.advanced = false;
		
		JPanel buttonPanel = new JPanel(new FlowLayout());
		buttonPanel.add(button);
		JPanel mainPanel = new JPanel(new BorderLayout(16, 16));
		mainPanel.add(container, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.PAGE_END);
		setLayout(new BorderLayout());
		add(mainPanel, BorderLayout.CENTER);
		
		setAdvancedMode(false);
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setAdvancedMode(!advanced);
			}
		});
	}
	
	private void setAdvancedMode(boolean advanced) {
		JPanel editor = (advanced
			? new XLMAdvancedSequenceEditor(sequence)
			: new XLMSimpleSequenceEditor(sequence)
		);
		container.removeAll();
		container.add(boxWrap(editor), BorderLayout.CENTER);
		button.setText(advanced
			? "Switch to Basic View"
			: "Switch to Advanced View"
		);
		this.advanced = advanced;
		revalidate();
		
		Component c = getParent();
		while (c != null) {
			if (c instanceof Window) {
				((Window)c).pack();
				break;
			}
			c = c.getParent();
		}
	}
	
	private static JPanel boxWrap(Component c) {
		JPanel wrapper = new JPanel(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.anchor = GridBagConstraints.CENTER;
		wrapper.add(c, constraints);
		return wrapper;
	}
}
