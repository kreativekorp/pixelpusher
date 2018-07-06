package com.kreative.unipixelpusher.clock;

import java.awt.BorderLayout;
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

public class AnalogClockConfigPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private final AnalogClockSequence sequence;
	private final ColorPopupMenu hourPopup;
	private final ColorPopupMenu minutePopup;
	private final ColorPopupMenu secondPopup;
	private final JComboBox modePopup;
	private final JCheckBox hourCheckbox;
	
	public AnalogClockConfigPanel(AnalogClockSequence seq) {
		this.sequence = seq;
		this.hourPopup = new ColorPopupMenu();
		this.hourPopup.setSelectedItem(seq.hourColor);
		this.minutePopup = new ColorPopupMenu();
		this.minutePopup.setSelectedItem(seq.minuteColor);
		this.secondPopup = new ColorPopupMenu();
		this.secondPopup.setSelectedItem(seq.secondColor);
		AnalogClockMode[] modes = AnalogClockMode.values();
		this.modePopup = new JComboBox(modes);
		this.modePopup.setEditable(false);
		this.modePopup.setMaximumRowCount(modes.length);
		this.modePopup.setSelectedItem(seq.mode);
		this.hourCheckbox = new JCheckBox("24-Hour");
		this.hourCheckbox.setSelected(seq.is24Hour);
		
		JPanel labelPanel = new JPanel(new GridLayout(0,1,4,4));
		labelPanel.add(new JLabel("Hour Color:"));
		labelPanel.add(new JLabel("Minute Color:"));
		labelPanel.add(new JLabel("Second Color:"));
		labelPanel.add(new JLabel("Clock Mode:"));
		labelPanel.add(new JLabel());
		JPanel controlPanel = new JPanel(new GridLayout(0,1,4,4));
		controlPanel.add(hourPopup);
		controlPanel.add(minutePopup);
		controlPanel.add(secondPopup);
		controlPanel.add(modePopup);
		controlPanel.add(hourCheckbox);
		setLayout(new BorderLayout(8,8));
		add(labelPanel, BorderLayout.LINE_START);
		add(controlPanel, BorderLayout.CENTER);
		
		hourPopup.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				Object c = hourPopup.getSelectedItem();
				if (c instanceof Number) {
					sequence.hourColor = ((Number)c).intValue();
				}
			}
		});
		minutePopup.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				Object c = minutePopup.getSelectedItem();
				if (c instanceof Number) {
					sequence.minuteColor = ((Number)c).intValue();
				}
			}
		});
		secondPopup.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				Object c = secondPopup.getSelectedItem();
				if (c instanceof Number) {
					sequence.secondColor = ((Number)c).intValue();
				}
			}
		});
		modePopup.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				Object c = modePopup.getSelectedItem();
				if (c instanceof AnalogClockMode) {
					sequence.mode = (AnalogClockMode)c;
				}
			}
		});
		hourCheckbox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sequence.is24Hour = hourCheckbox.isSelected();
			}
		});
	}
}
