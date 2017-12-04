package com.kreative.pixelpusher.xlm;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import com.kreative.pixelpusher.sequence.PixelSequenceEditor;

public class XLMEditor extends PixelSequenceEditor<XLMSequence> {
	private static final long serialVersionUID = 1L;
	
	private JPanel container;
	private JButton button;
	private boolean advanced;
	
	public XLMEditor() {
		this(new XLMSequence());
	}
	
	public XLMEditor(XLMSequence sequence) {
		super(sequence);
		container = new JPanel(new BorderLayout());
		button = new JButton();
		advanced = false;
		
		JPanel buttonPanel = new JPanel(new FlowLayout());
		buttonPanel.add(button);
		JPanel mainPanel = new JPanel(new BorderLayout());
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
	
	@Override
	public void setPixelSet(XLMSequence sequence) {
		super.setPixelSet(sequence);
		setAdvancedMode(advanced);
	}
	
	private void setAdvancedMode(boolean advanced) {
		PixelSequenceEditor<XLMSequence> editor;
		if (advanced) {
			editor = new XLMAdvancedSequenceEditor(pixelSet);
		} else {
			editor = new XLMSimpleSequenceEditor(pixelSet);
		}
		container.removeAll();
		container.add(boxWrap(editor), BorderLayout.CENTER);
		
		if (advanced) {
			button.setText("Switch to Basic View");
		} else {
			button.setText("Switch to Advanced View");
		}
		
		this.advanced = advanced;
		revalidate();
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
