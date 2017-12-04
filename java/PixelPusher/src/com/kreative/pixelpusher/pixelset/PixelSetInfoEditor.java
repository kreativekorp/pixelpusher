package com.kreative.pixelpusher.pixelset;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class PixelSetInfoEditor extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private PixelSetInfo<? extends PixelSet> info;
	private JCheckBox nameButton;
	private JTextField nameField;
	
	public PixelSetInfoEditor(PixelSetInfo<? extends PixelSet> info) {
		this.info = info;
		
		nameButton = new JCheckBox("Name:");
		nameButton.setSelected(info.hasName());
		nameField = new JTextField(info.getName());
		nameField.setEditable(info.hasName());
		JPanel namePanel = new JPanel(new BorderLayout(4,4));
		namePanel.add(nameButton, BorderLayout.LINE_START);
		namePanel.add(nameField, BorderLayout.CENTER);
		
		setLayout(new BorderLayout());
		add(namePanel, BorderLayout.CENTER);
		
		nameButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				PixelSetInfoEditor.this.info.setName(null);
				nameField.setEditable(nameButton.isSelected());
				nameField.setText(nameButton.isSelected() ? "" : PixelSetInfoEditor.this.info.getName());
				if (nameButton.isSelected()) nameField.requestFocusInWindow();
			}
		});
		nameField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent e) {
				if (nameField.isEditable()) {
					String s = nameField.getText();
					PixelSetInfoEditor.this.info.setName((s == null || s.length() == 0) ? null : s);
				}
			}
			@Override
			public void insertUpdate(DocumentEvent e) {
				if (nameField.isEditable()) {
					String s = nameField.getText();
					PixelSetInfoEditor.this.info.setName((s == null || s.length() == 0) ? null : s);
				}
			}
			@Override
			public void removeUpdate(DocumentEvent e) {
				if (nameField.isEditable()) {
					String s = nameField.getText();
					PixelSetInfoEditor.this.info.setName((s == null || s.length() == 0) ? null : s);
				}
			}
		});
	}
}
