package com.kreative.unipixelpusher.device.pixelpusher;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class PixelPusherConfigPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private final JTextField nameField;
	
	public PixelPusherConfigPanel(final PixelPusherDevice device) {
		JPanel deviceLabelPanel = new JPanel(new GridLayout(0, 1, 4, 4));
		deviceLabelPanel.add(new JLabel("ID:"));
		deviceLabelPanel.add(new JLabel("MAC Address:"));
		deviceLabelPanel.add(new JLabel("IP Address:"));
		deviceLabelPanel.add(new JLabel("PixelPusher Address:"));
		deviceLabelPanel.add(new JLabel("Art-Net Address:"));
		deviceLabelPanel.add(new JLabel("Name:"));
		
		JPanel deviceControlPanel = new JPanel(new GridLayout(0, 1, 4, 4));
		deviceControlPanel.add(new JLabel(device.id()));
		deviceControlPanel.add(new JLabel(PixelPusherDeviceIdentifier.MAC_ADDRESS.getName(device.pusher())));
		deviceControlPanel.add(new JLabel(PixelPusherDeviceIdentifier.IP_ADDRESS.getName(device.pusher())));
		deviceControlPanel.add(new JLabel(PixelPusherDeviceIdentifier.GROUP_CONTROLLER.getName(device.pusher())));
		deviceControlPanel.add(new JLabel(PixelPusherDeviceIdentifier.ARTNET_ADDRESS.getName(device.pusher())));
		deviceControlPanel.add(nameField = new JTextField());
		
		JPanel devicePanel = new JPanel(new BorderLayout(8, 8));
		devicePanel.add(deviceLabelPanel, BorderLayout.LINE_START);
		devicePanel.add(deviceControlPanel, BorderLayout.CENTER);
		
		JPanel stringLabelPanel = new JPanel(new GridLayout(2, 1));
		stringLabelPanel.add(new PixelPusherStringLabelPanel());
		stringLabelPanel.add(new PixelPusherStringLabelPanel());
		
		JPanel stringLeftPanel = new JPanel(new BorderLayout(8, 8));
		stringLeftPanel.add(new JLabel(" "), BorderLayout.PAGE_START);
		stringLeftPanel.add(stringLabelPanel, BorderLayout.CENTER);
		stringLeftPanel.add(new JLabel(" "), BorderLayout.PAGE_END);
		
		JPanel stringControlPanel = new JPanel(new GridLayout(2, 4));
		for (int i = 0; i < device.getStringCount(); i++) {
			stringControlPanel.add(new PixelPusherStringConfigPanel(device.getString(i)));
		}
		for (int i = device.getStringCount(); i < 8; i++) {
			stringControlPanel.add(new PixelPusherStringDummyPanel(i + 1));
		}
		
		JPanel stringTopPanel = new JPanel(new GridLayout(1, 4));
		stringTopPanel.add(center(new JLabel("Strip 1")));
		stringTopPanel.add(center(new JLabel("Strip 2")));
		stringTopPanel.add(center(new JLabel("Strip 3")));
		stringTopPanel.add(center(new JLabel("Strip 4")));
		
		JPanel stringBottomPanel = new JPanel(new GridLayout(1, 4));
		stringBottomPanel.add(center(new JLabel("Strip 5")));
		stringBottomPanel.add(center(new JLabel("Strip 6")));
		stringBottomPanel.add(center(new JLabel("Strip 7")));
		stringBottomPanel.add(center(new JLabel("Strip 8")));
		
		JPanel stringRightPanel = new JPanel(new BorderLayout(8, 8));
		stringRightPanel.add(stringTopPanel, BorderLayout.PAGE_START);
		stringRightPanel.add(stringControlPanel, BorderLayout.CENTER);
		stringRightPanel.add(stringBottomPanel, BorderLayout.PAGE_END);
		
		JPanel stringPanel = new JPanel(new BorderLayout(8, 8));
		stringPanel.add(stringLeftPanel, BorderLayout.LINE_START);
		stringPanel.add(stringRightPanel, BorderLayout.CENTER);
		
		setLayout(new BorderLayout(20, 20));
		add(devicePanel, BorderLayout.PAGE_START);
		add(stringPanel, BorderLayout.CENTER);
		
		nameField.setText(device.name());
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
	}
	
	private static JLabel center(JLabel l) {
		l.setHorizontalAlignment(JLabel.CENTER);
		return l;
	}
}
