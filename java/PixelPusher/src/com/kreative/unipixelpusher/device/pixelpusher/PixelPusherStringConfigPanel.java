package com.kreative.unipixelpusher.device.pixelpusher;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import com.kreative.unipixelpusher.GammaCurve;
import com.kreative.unipixelpusher.StringType;
import com.kreative.unipixelpusher.WindingOrder;
import com.kreative.unipixelpusher.gui.EnumPopupMenu;
import com.kreative.unipixelpusher.gui.GammaPopupMenu;

public class PixelPusherStringConfigPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private final JTextField nameField;
	private final EnumPopupMenu typePopup;
	private final SpinnerNumberModel lengthModel;
	private final JCheckBox reversedCheckbox;
	private final SpinnerNumberModel rowCountModel;
	private final SpinnerNumberModel columnCountModel;
	private final EnumPopupMenu windingOrderPopup;
	private final GammaPopupMenu gammaPopup;
	
	public PixelPusherStringConfigPanel(final PixelPusherString string) {
		setLayout(new GridLayout(0, 1, 4, 4));
		add(nameField = new JTextField());
		add(typePopup = new EnumPopupMenu(StringType.class, StringType.values()));
		
		JPanel lengthPanel = new JPanel();
		lengthPanel.setLayout(new BoxLayout(lengthPanel, BoxLayout.LINE_AXIS));
		lengthPanel.add(new JSpinner(lengthModel = new SpinnerNumberModel(0, 0, 9999, 1)));
		lengthPanel.add(Box.createHorizontalStrut(8));
		lengthPanel.add(reversedCheckbox = new JCheckBox("Reversed"));
		JPanel lengthPanel2 = new JPanel(new BorderLayout());
		lengthPanel2.add(lengthPanel, BorderLayout.LINE_START);
		add(lengthPanel2);
		
		JPanel sizePanel = new JPanel();
		sizePanel.setLayout(new BoxLayout(sizePanel, BoxLayout.LINE_AXIS));
		sizePanel.add(new JLabel("Rows:"));
		sizePanel.add(Box.createHorizontalStrut(4));
		sizePanel.add(new JSpinner(rowCountModel = new SpinnerNumberModel(0, 0, 9999, 1)));
		sizePanel.add(Box.createHorizontalStrut(8));
		sizePanel.add(new JLabel("Cols:"));
		sizePanel.add(Box.createHorizontalStrut(4));
		sizePanel.add(new JSpinner(columnCountModel = new SpinnerNumberModel(0, 0, 9999, 1)));
		JPanel sizePanel2 = new JPanel(new BorderLayout());
		sizePanel2.add(sizePanel, BorderLayout.LINE_START);
		add(sizePanel2);
		
		add(windingOrderPopup = new EnumPopupMenu(WindingOrder.class, WindingOrder.values()));
		add(gammaPopup = new GammaPopupMenu());
		setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.gray),
			BorderFactory.createEmptyBorder(8, 8, 8, 8)
		));
		
		nameField.setText(string.name());
		typePopup.setSelectedItem(string.type());
		typePopup.setPreferredSize(new Dimension(1, 1));
		lengthModel.setValue(string.length());
		reversedCheckbox.setSelected(string.getReversed());
		rowCountModel.setValue(string.getRowCount());
		columnCountModel.setValue(string.getColumnCount());
		windingOrderPopup.setSelectedItem(string.getWindingOrder());
		windingOrderPopup.setPreferredSize(new Dimension(1, 1));
		gammaPopup.setSelectedItem(string.getGammaCurve());
		
		nameField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent e) {
				String newName = nameField.getText();
				if (newName == null || newName.length() == 0) {
					string.setName(null);
				} else {
					string.setName(newName);
				}
			}
			@Override public void insertUpdate(DocumentEvent e) { changedUpdate(e); }
			@Override public void removeUpdate(DocumentEvent e) { changedUpdate(e); }
		});
		typePopup.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				StringType newType = (StringType)typePopup.getSelectedItem();
				string.setType(newType);
			}
		});
		lengthModel.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				int newLength = lengthModel.getNumber().intValue();
				string.setLength(newLength);
			}
		});
		reversedCheckbox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean newReversed = reversedCheckbox.isSelected();
				string.setReversed(newReversed);
			}
		});
		rowCountModel.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				int newCount = rowCountModel.getNumber().intValue();
				string.setRowCount(newCount);
			}
		});
		columnCountModel.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				int newCount = columnCountModel.getNumber().intValue();
				string.setColumnCount(newCount);
			}
		});
		windingOrderPopup.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				WindingOrder newOrder = (WindingOrder)windingOrderPopup.getSelectedItem();
				string.setWindingOrder(newOrder);
			}
		});
		gammaPopup.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				GammaCurve newGamma = (GammaCurve)gammaPopup.getSelectedItem();
				string.setGammaCurve(newGamma);
			}
		});
	}
}
