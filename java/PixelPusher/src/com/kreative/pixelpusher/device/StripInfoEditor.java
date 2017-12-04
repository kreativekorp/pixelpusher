package com.kreative.pixelpusher.device;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import com.heroicrobot.dropbit.devices.pixelpusher.PixelPusher;
import com.heroicrobot.dropbit.devices.pixelpusher.Strip;
import com.kreative.pixelpusher.array.ArrayOrderingPanel;
import com.kreative.pixelpusher.pixelset.PixelSet;
import com.kreative.pixelpusher.pixelset.PixelSetInfoSet;
import com.kreative.pixelpusher.pixelset.PixelSetPopupMenu;

public class StripInfoEditor extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private StripInfo info;
	private JCheckBox nameButton;
	private JTextField nameField;
	private JRadioButton trackByMacAddressButton;
	private JTextField trackByMacAddressField;
	private JRadioButton trackByIpAddressButton;
	private JTextField trackByIpAddressField;
	private JRadioButton trackByOrdinalButton;
	private SpinnerNumberModel trackByGroupOrdinalModel;
	private SpinnerNumberModel trackByControllerOrdinalModel;
	private JRadioButton trackByArtnetButton;
	private SpinnerNumberModel trackByArtnetUniverseModel;
	private SpinnerNumberModel trackByArtnetChannelModel;
	private SpinnerNumberModel stripModel;
	private JComboBox stripTypePopup;
	private JComboBox gammaPopup;
	private SpinnerNumberModel dimensionRowsModel;
	private SpinnerNumberModel dimensionColumnsModel;
	private ArrayOrderingPanel dimensionOrderingPanel;
	private PixelSetPopupMenu<PixelSet> pixelSetPopup;
	private JButton pixelSetButton;
	
	public StripInfoEditor(PixelSetInfoSet infoSet, PixelPusher pusher, Strip strip, StripInfo info) {
		this.info = info;
		
		String macAddress = ((pusher != null) ? pusher.getMacAddress() : info.hasMacAddress() ? info.getMacAddress() : "00:00:00:00:00:00");
		String ipAddress = ((pusher != null) ? pusher.getIp().getHostAddress() : info.hasIpAddress() ? info.getIpAddress() : "0.0.0.0");
		int groupOrdinal = ((pusher != null) ? pusher.getGroupOrdinal() : info.hasOrdinal() ? info.getGroupOrdinal() : 0);
		int controllerOrdinal = ((pusher != null) ? pusher.getControllerOrdinal() : info.hasOrdinal() ? info.getControllerOrdinal() : 0);
		int artnetUniverse = ((pusher != null) ? pusher.getArtnetUniverse() : info.hasArtnet() ? info.getArtnetUniverse() : 0);
		int artnetChannel = ((pusher != null) ? pusher.getArtnetChannel() : info.hasArtnet() ? info.getArtnetChannel() : 0);
		int stripNumber = ((strip != null) ? strip.getStripNumber() : info.getStripNumber());
		int numberOfStrips = ((pusher != null) ? pusher.getNumberOfStrips() : 8);
		int stripLength = ((strip != null) ? strip.getLength() : info.getLength());
		
		nameButton = new JCheckBox("Name:");
		nameButton.setSelected(info.hasName());
		nameField = new JTextField(info.getName());
		nameField.setEditable(info.hasName());
		JPanel namePanel = new JPanel(new BorderLayout(4,4));
		namePanel.add(nameButton, BorderLayout.LINE_START);
		namePanel.add(nameField, BorderLayout.CENTER);
		
		JLabel trackingLabel = new JLabel("Device Tracking:");
		trackByMacAddressButton = new JRadioButton("Track by MAC address:");
		trackByMacAddressButton.setSelected(info.hasMacAddress());
		trackByMacAddressField = new JTextField(macAddress, 16);
		JPanel trackByMacAddressPanel = makeLine(4, trackByMacAddressButton, trackByMacAddressField);
		trackByIpAddressButton = new JRadioButton("Track by IP address:");
		trackByIpAddressButton.setSelected(info.hasIpAddress());
		trackByIpAddressField = new JTextField(ipAddress, 15);
		JPanel trackByIpAddressPanel = makeLine(4, trackByIpAddressButton, trackByIpAddressField);
		trackByOrdinalButton = new JRadioButton("Track by group ordinal:");
		trackByGroupOrdinalModel = new SpinnerNumberModel(groupOrdinal, 0, 65535, 1);
		JSpinner trackByGroupOrdinalSpinner = new JSpinner(trackByGroupOrdinalModel);
		JLabel trackByOrdinalLabel = new JLabel(" controller ordinal:");
		trackByControllerOrdinalModel = new SpinnerNumberModel(controllerOrdinal, 0, 65535, 1);
		JSpinner trackByControllerOrdinalSpinner = new JSpinner(trackByControllerOrdinalModel);
		JPanel trackByOrdinalPanel = makeLine(4, trackByOrdinalButton, trackByGroupOrdinalSpinner, trackByOrdinalLabel, trackByControllerOrdinalSpinner);
		trackByArtnetButton = new JRadioButton("Track by ArtNet universe:");
		trackByArtnetUniverseModel = new SpinnerNumberModel(artnetUniverse, 0, 65535, 1);
		JSpinner trackByArtnetUniverseSpinner = new JSpinner(trackByArtnetUniverseModel);
		JLabel trackByArtnetLabel = new JLabel(" ArtNet channel:");
		trackByArtnetChannelModel = new SpinnerNumberModel(artnetChannel, 0, 65535, 1);
		JSpinner trackByArtnetChannelSpinner = new JSpinner(trackByArtnetChannelModel);
		JPanel trackByArtnetPanel = makeLine(4, trackByArtnetButton, trackByArtnetUniverseSpinner, trackByArtnetLabel, trackByArtnetChannelSpinner);
		makeButtonGroup(trackByMacAddressButton, trackByIpAddressButton, trackByOrdinalButton, trackByArtnetButton);
		JPanel trackingPanel = makePage(4, makeLine(4, trackingLabel), makePage(2, trackByMacAddressPanel, trackByIpAddressPanel, trackByOrdinalPanel, trackByArtnetPanel));
		
		JLabel stripLabel = new JLabel("Strip Number:");
		stripModel = new SpinnerNumberModel(stripNumber, 0, (numberOfStrips - 1), 1);
		JSpinner stripSpinner = new JSpinner(stripModel);
		JPanel stripPanel = makeLine(4, stripLabel, stripSpinner);
		
		JLabel stripTypeLabel = new JLabel("Strip Type:");
		stripTypePopup = new JComboBox(StripType.values());
		stripTypePopup.setEditable(false);
		stripTypePopup.setSelectedItem(info.getStripType());
		JPanel stripTypePanel = makeLine(4, stripTypeLabel, stripTypePopup);
		
		JLabel gammaLabel = new JLabel("Gamma Correction:");
		gammaPopup = new JComboBox(GammaCorrector.values());
		gammaPopup.setEditable(false);
		gammaPopup.setSelectedItem(info.getGammaCorrector());
		JPanel gammaPanel = makeLine(4, gammaLabel, gammaPopup);
		
		JLabel dimensionLabel = new JLabel("Dimensions:");
		JLabel dimensionLengthLabel = new JLabel("Strip Length:");
		String dimensionLengthString = Integer.toString(stripLength);
		JTextField dimensionLengthField = new JTextField(dimensionLengthString, dimensionLengthString.length());
		dimensionLengthField.setEditable(false);
		JLabel dimensionRowsLabel = new JLabel(" Rows:");
		dimensionRowsModel = new SpinnerNumberModel(info.getRowCount(), 1, stripLength, 1);
		JSpinner dimensionRowsSpinner = new JSpinner(dimensionRowsModel);
		JLabel dimensionColumnsLabel = new JLabel(" Columns:");
		dimensionColumnsModel = new SpinnerNumberModel(info.getColumnCount(), 1, stripLength, 1);
		JSpinner dimensionColumnsSpinner = new JSpinner(dimensionColumnsModel);
		JPanel dimensionRowsColumnsPanel = makeLine(4, dimensionLengthLabel, dimensionLengthField, dimensionRowsLabel, dimensionRowsSpinner, dimensionColumnsLabel, dimensionColumnsSpinner);
		dimensionOrderingPanel = new ArrayOrderingPanel();
		dimensionOrderingPanel.setSelectedValue(info.getOrdering());
		JPanel dimensionPanel = makePage(4, makeLine(4, dimensionLabel), dimensionRowsColumnsPanel, dimensionOrderingPanel);
		
		JLabel pixelSetLabel = new JLabel("Connected To:");
		pixelSetPopup = new PixelSetPopupMenu<PixelSet>(infoSet, PixelSet.class);
		pixelSetPopup.setSelectedPixelSet(info.getPixelSet());
		pixelSetButton = new JButton("Disconnect");
		JPanel pixelSetPanel = new JPanel(new BorderLayout(4,4));
		pixelSetPanel.add(pixelSetLabel, BorderLayout.LINE_START);
		pixelSetPanel.add(pixelSetPopup, BorderLayout.CENTER);
		pixelSetPanel.add(pixelSetButton, BorderLayout.LINE_END);
		
		JPanel mainPanel = makePage(12, namePanel, trackingPanel, stripPanel, stripTypePanel, gammaPanel, dimensionPanel, pixelSetPanel);
		setLayout(new BorderLayout());
		add(mainPanel, BorderLayout.CENTER);
		
		nameButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				StripInfoEditor.this.info.setName(null);
				nameField.setEditable(nameButton.isSelected());
				nameField.setText(nameButton.isSelected() ? "" : StripInfoEditor.this.info.getName());
				if (nameButton.isSelected()) nameField.requestFocusInWindow();
			}
		});
		nameField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent e) {
				if (nameField.isEditable()) {
					String s = nameField.getText();
					StripInfoEditor.this.info.setName((s == null || s.length() == 0) ? null : s);
				}
			}
			@Override
			public void insertUpdate(DocumentEvent e) {
				if (nameField.isEditable()) {
					String s = nameField.getText();
					StripInfoEditor.this.info.setName((s == null || s.length() == 0) ? null : s);
				}
			}
			@Override
			public void removeUpdate(DocumentEvent e) {
				if (nameField.isEditable()) {
					String s = nameField.getText();
					StripInfoEditor.this.info.setName((s == null || s.length() == 0) ? null : s);
				}
			}
		});
		trackByMacAddressButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				StripInfoEditor.this.info.setMacAddress(trackByMacAddressField.getText());
				if (!nameField.isEditable()) {
					nameField.setText(StripInfoEditor.this.info.getName());
				}
			}
		});
		trackByMacAddressField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent e) {
				trackByMacAddressButton.setSelected(true);
				StripInfoEditor.this.info.setMacAddress(trackByMacAddressField.getText());
				if (!nameField.isEditable()) {
					nameField.setText(StripInfoEditor.this.info.getName());
				}
			}
			@Override
			public void insertUpdate(DocumentEvent e) {
				trackByMacAddressButton.setSelected(true);
				StripInfoEditor.this.info.setMacAddress(trackByMacAddressField.getText());
				if (!nameField.isEditable()) {
					nameField.setText(StripInfoEditor.this.info.getName());
				}
			}
			@Override
			public void removeUpdate(DocumentEvent e) {
				trackByMacAddressButton.setSelected(true);
				StripInfoEditor.this.info.setMacAddress(trackByMacAddressField.getText());
				if (!nameField.isEditable()) {
					nameField.setText(StripInfoEditor.this.info.getName());
				}
			}
		});
		trackByIpAddressButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				StripInfoEditor.this.info.setIpAddress(trackByIpAddressField.getText());
				if (!nameField.isEditable()) {
					nameField.setText(StripInfoEditor.this.info.getName());
				}
			}
		});
		trackByIpAddressField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent e) {
				trackByIpAddressButton.setSelected(true);
				StripInfoEditor.this.info.setIpAddress(trackByIpAddressField.getText());
				if (!nameField.isEditable()) {
					nameField.setText(StripInfoEditor.this.info.getName());
				}
			}
			@Override
			public void insertUpdate(DocumentEvent e) {
				trackByIpAddressButton.setSelected(true);
				StripInfoEditor.this.info.setIpAddress(trackByIpAddressField.getText());
				if (!nameField.isEditable()) {
					nameField.setText(StripInfoEditor.this.info.getName());
				}
			}
			@Override
			public void removeUpdate(DocumentEvent e) {
				trackByIpAddressButton.setSelected(true);
				StripInfoEditor.this.info.setIpAddress(trackByIpAddressField.getText());
				if (!nameField.isEditable()) {
					nameField.setText(StripInfoEditor.this.info.getName());
				}
			}
		});
		trackByOrdinalButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				StripInfoEditor.this.info.setOrdinal(trackByGroupOrdinalModel.getNumber().intValue(), trackByControllerOrdinalModel.getNumber().intValue());
				if (!nameField.isEditable()) {
					nameField.setText(StripInfoEditor.this.info.getName());
				}
			}
		});
		trackByGroupOrdinalModel.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				trackByOrdinalButton.setSelected(true);
				StripInfoEditor.this.info.setOrdinal(trackByGroupOrdinalModel.getNumber().intValue(), trackByControllerOrdinalModel.getNumber().intValue());
				if (!nameField.isEditable()) {
					nameField.setText(StripInfoEditor.this.info.getName());
				}
			}
		});
		trackByControllerOrdinalModel.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				trackByOrdinalButton.setSelected(true);
				StripInfoEditor.this.info.setOrdinal(trackByGroupOrdinalModel.getNumber().intValue(), trackByControllerOrdinalModel.getNumber().intValue());
				if (!nameField.isEditable()) {
					nameField.setText(StripInfoEditor.this.info.getName());
				}
			}
		});
		trackByArtnetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				StripInfoEditor.this.info.setArtnet(trackByArtnetUniverseModel.getNumber().intValue(), trackByArtnetChannelModel.getNumber().intValue());
				if (!nameField.isEditable()) {
					nameField.setText(StripInfoEditor.this.info.getName());
				}
			}
		});
		trackByArtnetUniverseModel.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				trackByArtnetButton.setSelected(true);
				StripInfoEditor.this.info.setArtnet(trackByArtnetUniverseModel.getNumber().intValue(), trackByArtnetChannelModel.getNumber().intValue());
				if (!nameField.isEditable()) {
					nameField.setText(StripInfoEditor.this.info.getName());
				}
			}
		});
		trackByArtnetChannelModel.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				trackByArtnetButton.setSelected(true);
				StripInfoEditor.this.info.setArtnet(trackByArtnetUniverseModel.getNumber().intValue(), trackByArtnetChannelModel.getNumber().intValue());
				if (!nameField.isEditable()) {
					nameField.setText(StripInfoEditor.this.info.getName());
				}
			}
		});
		stripModel.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				StripInfoEditor.this.info.setStripNumber(stripModel.getNumber().intValue());
				if (!nameField.isEditable()) {
					nameField.setText(StripInfoEditor.this.info.getName());
				}
			}
		});
		stripTypePopup.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				StripInfoEditor.this.info.setStripType((StripType)stripTypePopup.getSelectedItem());
			}
		});
		gammaPopup.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				StripInfoEditor.this.info.setGammaCorrector((GammaCorrector)gammaPopup.getSelectedItem());
			}
		});
		dimensionRowsModel.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				StripInfoEditor.this.info.setRowCount(dimensionRowsModel.getNumber().intValue());
			}
		});
		dimensionColumnsModel.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				StripInfoEditor.this.info.setColumnCount(dimensionColumnsModel.getNumber().intValue());
			}
		});
		dimensionOrderingPanel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				StripInfoEditor.this.info.setOrdering(dimensionOrderingPanel.getSelectedValue());
			}
		});
		pixelSetPopup.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				StripInfoEditor.this.info.setPixelSet(pixelSetPopup.getSelectedPixelSet());
			}
		});
		pixelSetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pixelSetPopup.setSelectedPixelSet(null);
				StripInfoEditor.this.info.setPixelSet(null);
			}
		});
	}
	
	private static final JPanel makeLine(int hgap, JComponent... components) {
		JPanel p1 = new JPanel();
		p1.setLayout(new BoxLayout(p1, BoxLayout.LINE_AXIS));
		boolean first = true;
		for (JComponent c : components) {
			if (first) first = false;
			else p1.add(Box.createHorizontalStrut(hgap));
			p1.add(c);
		}
		JPanel p2 = new JPanel();
		p2.setLayout(new BorderLayout());
		p2.add(p1, BorderLayout.LINE_START);
		return p2;
	}
	
	private static final JPanel makePage(int vgap, JComponent... components) {
		JPanel p1 = new JPanel();
		p1.setLayout(new BoxLayout(p1, BoxLayout.PAGE_AXIS));
		boolean first = true;
		for (JComponent c : components) {
			if (first) first = false;
			else p1.add(Box.createVerticalStrut(vgap));
			p1.add(c);
		}
		JPanel p2 = new JPanel();
		p2.setLayout(new BorderLayout());
		p2.add(p1, BorderLayout.PAGE_START);
		return p2;
	}
	
	private static final void makeButtonGroup(AbstractButton... buttons) {
		ButtonGroup group = new ButtonGroup();
		for (AbstractButton button : buttons) {
			group.add(button);
		}
	}
}
