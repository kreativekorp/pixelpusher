package com.kreative.unipixelpusher.device.rainbowduino;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import com.kreative.unipixelpusher.DeviceConfiguration;
import com.kreative.unipixelpusher.GammaCurve;
import com.kreative.unipixelpusher.WindingOrder;
import com.kreative.unipixelpusher.gui.EnumPopupMenu;
import com.kreative.unipixelpusher.gui.GammaPopupMenu;
import com.kreative.unipixelpusher.gui.ObjectPopupMenu;

public class RainbowduinoConfigPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private final JTextField nameField;
	private final ObjectPopupMenu protocolPopup;
	private final EnumPopupMenu windingOrderPopup;
	private final JCheckBox flipHorizontalCheckbox;
	private final JCheckBox flipVerticalCheckbox;
	private final GammaPopupMenu gammaPopup;
	
	public RainbowduinoConfigPanel(final DeviceConfiguration config, final RainbowduinoDevice device) {
		JPanel labelPanel = new JPanel(new GridLayout(0, 1, 4, 4));
		labelPanel.add(new JLabel("ID:"));
		labelPanel.add(new JLabel("Name:"));
		labelPanel.add(new JLabel("Protocol:"));
		labelPanel.add(new JLabel("Winding Order:"));
		labelPanel.add(new JLabel());
		labelPanel.add(new JLabel("Gamma Curve:"));
		
		JPanel controlPanel = new JPanel(new GridLayout(0, 1, 4, 4));
		controlPanel.add(new JLabel(device.id()));
		controlPanel.add(nameField = new JTextField());
		controlPanel.add(protocolPopup = new ObjectPopupMenu(RainbowduinoProtocol.PROTOCOLS));
		controlPanel.add(windingOrderPopup = new EnumPopupMenu(WindingOrder.class, WindingOrder.values()));
		JPanel checkboxPanel1 = new JPanel(new GridLayout(1, 0, 4, 4));
		checkboxPanel1.add(flipHorizontalCheckbox = new JCheckBox("Flip Horizontal"));
		checkboxPanel1.add(flipVerticalCheckbox = new JCheckBox("Flip Vertical"));
		JPanel checkboxPanel2 = new JPanel(new BorderLayout());
		checkboxPanel2.add(checkboxPanel1, BorderLayout.LINE_START);
		controlPanel.add(checkboxPanel2);
		controlPanel.add(gammaPopup = new GammaPopupMenu());
		
		setLayout(new BorderLayout(8, 8));
		add(labelPanel, BorderLayout.LINE_START);
		add(controlPanel, BorderLayout.CENTER);
		
		nameField.setText(device.name());
		protocolPopup.setSelectedItem(device.getProtocol());
		windingOrderPopup.setSelectedItem(config.get(device.id(), "windingOrder", WindingOrder.class, WindingOrder.LTR_TTB));
		flipHorizontalCheckbox.setSelected(config.get(device.id(), "flipHorizontal", false));
		flipVerticalCheckbox.setSelected(config.get(device.id(), "flipVertical", false));
		gammaPopup.setSelectedItem(config.get(device.id(), "gamma", GammaCurve.LINEAR));
		
		nameField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent e) {
				String newName = nameField.getText();
				if (newName == null || newName.length() == 0) {
					device.setName(null);
				} else {
					device.setName(newName);
				}
			}
			@Override public void insertUpdate(DocumentEvent e) { changedUpdate(e); }
			@Override public void removeUpdate(DocumentEvent e) { changedUpdate(e); }
		});
		protocolPopup.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				RainbowduinoProtocol newProto = (RainbowduinoProtocol)protocolPopup.getSelectedItem();
				device.setProtocol(newProto);
			}
		});
		windingOrderPopup.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				WindingOrder newOrder = (WindingOrder)windingOrderPopup.getSelectedItem();
				config.put(device.id(), "windingOrder", newOrder);
				device.loadConfig(device.id());
			}
		});
		flipHorizontalCheckbox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean newFlip = flipHorizontalCheckbox.isSelected();
				config.put(device.id(), "flipHorizontal", newFlip);
				device.loadConfig(device.id());
			}
		});
		flipVerticalCheckbox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean newFlip = flipVerticalCheckbox.isSelected();
				config.put(device.id(), "flipVertical", newFlip);
				device.loadConfig(device.id());
			}
		});
		gammaPopup.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				GammaCurve newGamma = (GammaCurve)gammaPopup.getSelectedItem();
				config.put(device.id(), "gamma", newGamma);
				device.loadConfig(device.id());
			}
		});
	}
}
