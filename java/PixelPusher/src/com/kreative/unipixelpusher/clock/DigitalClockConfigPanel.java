package com.kreative.unipixelpusher.clock;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import com.kreative.unipixelpusher.gui.ColorPopupMenu;

public class DigitalClockConfigPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private final DigitalClockSequence sequence;
	private final String[] colorModeNames;
	private final JComboBox colorModePopup;
	private final ColorPopupMenu monochromePopup;
	private final ColorPopupMenu hourTensPopup;
	private final ColorPopupMenu hourOnesPopup;
	private final ColorPopupMenu hmSeparatorPopup;
	private final ColorPopupMenu minuteTensPopup;
	private final ColorPopupMenu minuteOnesPopup;
	private final ColorPopupMenu msSeparatorPopup;
	private final ColorPopupMenu secondTensPopup;
	private final ColorPopupMenu secondOnesPopup;
	private final ColorPopupMenu digit0Popup;
	private final ColorPopupMenu digit1Popup;
	private final ColorPopupMenu digit2Popup;
	private final ColorPopupMenu digit3Popup;
	private final ColorPopupMenu digit4Popup;
	private final ColorPopupMenu digit5Popup;
	private final ColorPopupMenu digit6Popup;
	private final ColorPopupMenu digit7Popup;
	private final ColorPopupMenu digit8Popup;
	private final ColorPopupMenu digit9Popup;
	private final ColorPopupMenu hmColonPopup;
	private final ColorPopupMenu msColonPopup;
	private final JComboBox displayModePopup;
	private final JCheckBox hourCheckbox;
	private final JCheckBox showSeparatorsCheckbox;
	private final JCheckBox flashSeparatorsCheckbox;
	private final JCheckBox showSecondsCheckbox;
	
	public DigitalClockConfigPanel(DigitalClockSequence seq) {
		this.sequence = seq;
		this.colorModeNames = new String[]{"Monochrome", "Positional", "Synaesthetic"};
		this.colorModePopup = new JComboBox(colorModeNames);
		this.colorModePopup.setEditable(false);
		this.colorModePopup.setMaximumRowCount(colorModeNames.length);
		this.monochromePopup = new ColorPopupMenu();
		this.hourTensPopup = new ColorPopupMenu();
		this.hourOnesPopup = new ColorPopupMenu();
		this.hmSeparatorPopup = new ColorPopupMenu();
		this.minuteTensPopup = new ColorPopupMenu();
		this.minuteOnesPopup = new ColorPopupMenu();
		this.msSeparatorPopup = new ColorPopupMenu();
		this.secondTensPopup = new ColorPopupMenu();
		this.secondOnesPopup = new ColorPopupMenu();
		this.digit0Popup = new ColorPopupMenu();
		this.digit1Popup = new ColorPopupMenu();
		this.digit2Popup = new ColorPopupMenu();
		this.digit3Popup = new ColorPopupMenu();
		this.digit4Popup = new ColorPopupMenu();
		this.digit5Popup = new ColorPopupMenu();
		this.digit6Popup = new ColorPopupMenu();
		this.digit7Popup = new ColorPopupMenu();
		this.digit8Popup = new ColorPopupMenu();
		this.digit9Popup = new ColorPopupMenu();
		this.hmColonPopup = new ColorPopupMenu();
		this.msColonPopup = new ColorPopupMenu();
		DigitalClockDisplayMode[] modes = DigitalClockDisplayMode.MODES;
		this.displayModePopup = new JComboBox(modes);
		this.displayModePopup.setEditable(false);
		this.displayModePopup.setMaximumRowCount(modes.length);
		this.hourCheckbox = new JCheckBox("24-Hour");
		this.showSeparatorsCheckbox = new JCheckBox("Show Separators");
		this.flashSeparatorsCheckbox = new JCheckBox("Flash Separators");
		this.showSecondsCheckbox = new JCheckBox("Show Seconds");
		
		DigitalClockColorMode cm = seq.getColorMode();
		DigitalClockColorMode.Monochrome cmm;
		DigitalClockColorMode.Positional cmp;
		DigitalClockColorMode.Synaesthetic cms;
		if (cm instanceof DigitalClockColorMode.Monochrome) {
			colorModePopup.setSelectedItem("Monochrome");
			cmm = (DigitalClockColorMode.Monochrome)cm;
		} else {
			cmm = new DigitalClockColorMode.Monochrome();
		}
		monochromePopup.setSelectedItem(cmm.color);
		if (cm instanceof DigitalClockColorMode.Positional) {
			colorModePopup.setSelectedItem("Positional");
			cmp = (DigitalClockColorMode.Positional)cm;
		} else {
			cmp = new DigitalClockColorMode.Positional();
		}
		hourTensPopup.setSelectedItem(cmp.hourTens);
		hourOnesPopup.setSelectedItem(cmp.hourOnes);
		hmSeparatorPopup.setSelectedItem(cmp.hmSeparator);
		minuteTensPopup.setSelectedItem(cmp.minuteTens);
		minuteOnesPopup.setSelectedItem(cmp.minuteOnes);
		msSeparatorPopup.setSelectedItem(cmp.msSeparator);
		secondTensPopup.setSelectedItem(cmp.secondTens);
		secondOnesPopup.setSelectedItem(cmp.secondOnes);
		if (cm instanceof DigitalClockColorMode.Synaesthetic) {
			colorModePopup.setSelectedItem("Synaesthetic");
			cms = (DigitalClockColorMode.Synaesthetic)cm;
		} else {
			cms = new DigitalClockColorMode.Synaesthetic();
		}
		digit0Popup.setSelectedItem(cms.color0);
		digit1Popup.setSelectedItem(cms.color1);
		digit2Popup.setSelectedItem(cms.color2);
		digit3Popup.setSelectedItem(cms.color3);
		digit4Popup.setSelectedItem(cms.color4);
		digit5Popup.setSelectedItem(cms.color5);
		digit6Popup.setSelectedItem(cms.color6);
		digit7Popup.setSelectedItem(cms.color7);
		digit8Popup.setSelectedItem(cms.color8);
		digit9Popup.setSelectedItem(cms.color9);
		hmColonPopup.setSelectedItem(cms.hmSeparator);
		msColonPopup.setSelectedItem(cms.msSeparator);
		displayModePopup.setSelectedItem(seq.getDisplayMode());
		hourCheckbox.setSelected(seq.getIs24Hour());
		showSeparatorsCheckbox.setSelected(seq.getShowSeparators());
		flashSeparatorsCheckbox.setSelected(seq.getFlashSeparators());
		showSecondsCheckbox.setSelected(seq.getShowSeconds());
		
		JPanel topPanel = createLCPanel(createLabel("Color Mode:"), colorModePopup);
		JPanel monochromePanel = createAlignTopPanel(createLCPanel(createLabel("Color:"), monochromePopup));
		JPanel positionalPanel = createAlignTopPanel(createLCPanel(createLabelPanel(
			"Hour Tens Place:", "Hour Ones Place:", "Hour/Minute Separator:",
			"Minute Tens Place:", "Minute Ones Place:", "Minute/Second Separator:",
			"Second Tens Place:", "Second Ones Place:"
		), createControlPanel(
			hourTensPopup, hourOnesPopup, hmSeparatorPopup,
			minuteTensPopup, minuteOnesPopup, msSeparatorPopup,
			secondTensPopup, secondOnesPopup
		)));
		JPanel synaestheticPanel = createAlignTopPanel(createLCPanel(createLabelPanel(
			"Digit 0:", "Digit 1:", "Digit 2:", "Digit 3:", "Digit 4:",
			"Digit 5:", "Digit 6:", "Digit 7:", "Digit 8:", "Digit 9:",
			"Hour/Minute Separator:", "Minute/Second Separator:"
		), createControlPanel(
			digit0Popup, digit1Popup, digit2Popup, digit3Popup, digit4Popup,
			digit5Popup, digit6Popup, digit7Popup, digit8Popup, digit9Popup,
			hmColonPopup, msColonPopup
		)));
		final CardLayout cmLayout = new CardLayout();
		final JPanel cmPanel = new JPanel(cmLayout);
		cmPanel.add(monochromePanel, "Monochrome");
		cmPanel.add(positionalPanel, "Positional");
		cmPanel.add(synaestheticPanel, "Synaesthetic");
		Object o = colorModePopup.getSelectedItem();
		if (o != null) cmLayout.show(cmPanel, o.toString());
		JPanel checkboxPanel = new JPanel(new GridLayout(1, 0, 8, 8));
		checkboxPanel.add(hourCheckbox);
		checkboxPanel.add(showSeparatorsCheckbox);
		checkboxPanel.add(flashSeparatorsCheckbox);
		checkboxPanel.add(showSecondsCheckbox);
		JPanel checkboxPanel2 = new JPanel(new BorderLayout());
		checkboxPanel2.add(checkboxPanel, BorderLayout.LINE_START);
		JPanel bottomPanel = createLCPanel(
			createLabelPanel("Display Mode:", ""),
			createControlPanel(displayModePopup, checkboxPanel2)
		);
		setLayout(new BorderLayout(4, 4));
		add(topPanel, BorderLayout.PAGE_START);
		add(cmPanel, BorderLayout.CENTER);
		add(bottomPanel, BorderLayout.PAGE_END);
		
		colorModePopup.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				Object o = colorModePopup.getSelectedItem();
				if (o != null) {
					String s = o.toString();
					cmLayout.show(cmPanel, s);
					pushColorMode(s);
				}
			}
		});
		monochromePopup.addItemListener(new PushColorModeListener());
		hourTensPopup.addItemListener(new PushColorModeListener());
		hourOnesPopup.addItemListener(new PushColorModeListener());
		hmSeparatorPopup.addItemListener(new PushColorModeListener());
		minuteTensPopup.addItemListener(new PushColorModeListener());
		minuteOnesPopup.addItemListener(new PushColorModeListener());
		msSeparatorPopup.addItemListener(new PushColorModeListener());
		secondTensPopup.addItemListener(new PushColorModeListener());
		secondOnesPopup.addItemListener(new PushColorModeListener());
		digit0Popup.addItemListener(new PushColorModeListener());
		digit1Popup.addItemListener(new PushColorModeListener());
		digit2Popup.addItemListener(new PushColorModeListener());
		digit3Popup.addItemListener(new PushColorModeListener());
		digit4Popup.addItemListener(new PushColorModeListener());
		digit5Popup.addItemListener(new PushColorModeListener());
		digit6Popup.addItemListener(new PushColorModeListener());
		digit7Popup.addItemListener(new PushColorModeListener());
		digit8Popup.addItemListener(new PushColorModeListener());
		digit9Popup.addItemListener(new PushColorModeListener());
		hmColonPopup.addItemListener(new PushColorModeListener());
		msColonPopup.addItemListener(new PushColorModeListener());
		displayModePopup.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				Object o = displayModePopup.getSelectedItem();
				if (o instanceof DigitalClockDisplayMode) {
					sequence.setDisplayMode((DigitalClockDisplayMode)o);
				}
			}
		});
		hourCheckbox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sequence.setIs24Hour(hourCheckbox.isSelected());
			}
		});
		showSeparatorsCheckbox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sequence.setShowSeparators(showSeparatorsCheckbox.isSelected());
			}
		});
		flashSeparatorsCheckbox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sequence.setFlashSeparators(flashSeparatorsCheckbox.isSelected());
			}
		});
		showSecondsCheckbox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sequence.setShowSeconds(showSecondsCheckbox.isSelected());
			}
		});
	}
	
	private JLabel createLabel(String text) {
		JLabel label = new JLabel(text);
		Dimension d = label.getPreferredSize();
		d.width = 180;
		label.setMinimumSize(d);
		label.setPreferredSize(d);
		label.setMaximumSize(d);
		return label;
	}
	
	private JPanel createLabelPanel(String... labels) {
		JPanel panel = new JPanel(new GridLayout(0, 1, 4, 4));
		for (String label : labels) panel.add(createLabel(label));
		return panel;
	}
	
	private JPanel createControlPanel(Component... components) {
		JPanel panel = new JPanel(new GridLayout(0, 1, 4, 4));
		for (Component component : components) panel.add(component);
		return panel;
	}
	
	private JPanel createLCPanel(Component labels, Component controls) {
		JPanel panel = new JPanel(new BorderLayout(8, 8));
		panel.add(labels, BorderLayout.LINE_START);
		panel.add(controls, BorderLayout.CENTER);
		return panel;
	}
	
	private JPanel createAlignTopPanel(Component c) {
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(c, BorderLayout.PAGE_START);
		return panel;
	}
	
	private class PushColorModeListener implements ItemListener {
		@Override
		public void itemStateChanged(ItemEvent e) {
			Object o = colorModePopup.getSelectedItem();
			if (o != null) pushColorMode(o.toString());
		}
	}
	
	private void pushColorMode(String cms) {
		DigitalClockColorMode cm = null;
		if (cms.equalsIgnoreCase("Monochrome")) {
			cm = new DigitalClockColorMode.Monochrome(
				((Number)monochromePopup.getSelectedItem()).intValue()
			);
		} else if (cms.equalsIgnoreCase("Positional")) {
			cm = new DigitalClockColorMode.Positional(
				((Number)hourTensPopup.getSelectedItem()).intValue(),
				((Number)hourOnesPopup.getSelectedItem()).intValue(),
				((Number)hmSeparatorPopup.getSelectedItem()).intValue(),
				((Number)minuteTensPopup.getSelectedItem()).intValue(),
				((Number)minuteOnesPopup.getSelectedItem()).intValue(),
				((Number)msSeparatorPopup.getSelectedItem()).intValue(),
				((Number)secondTensPopup.getSelectedItem()).intValue(),
				((Number)secondOnesPopup.getSelectedItem()).intValue()
			);
		} else if (cms.equalsIgnoreCase("Synaesthetic")) {
			cm = new DigitalClockColorMode.Synaesthetic(
				((Number)digit0Popup.getSelectedItem()).intValue(),
				((Number)digit1Popup.getSelectedItem()).intValue(),
				((Number)digit2Popup.getSelectedItem()).intValue(),
				((Number)digit3Popup.getSelectedItem()).intValue(),
				((Number)digit4Popup.getSelectedItem()).intValue(),
				((Number)digit5Popup.getSelectedItem()).intValue(),
				((Number)digit6Popup.getSelectedItem()).intValue(),
				((Number)digit7Popup.getSelectedItem()).intValue(),
				((Number)digit8Popup.getSelectedItem()).intValue(),
				((Number)digit9Popup.getSelectedItem()).intValue(),
				((Number)hmColonPopup.getSelectedItem()).intValue(),
				((Number)msColonPopup.getSelectedItem()).intValue()
			);
		}
		if (cm != null) sequence.setColorMode(cm);
	}
}
