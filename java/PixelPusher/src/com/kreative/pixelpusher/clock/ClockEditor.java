package com.kreative.pixelpusher.clock;

import java.awt.BorderLayout;
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
import com.kreative.pixelpusher.common.ColorPopupMenu;
import com.kreative.pixelpusher.sequence.PixelSequenceEditor;

public class ClockEditor extends PixelSequenceEditor<ClockSequence> {
	private static final long serialVersionUID = 1L;
	
	private JCheckBox is24Hour;
	private ColorPopupMenu hourColor;
	private ColorPopupMenu minuteColor;
	private ColorPopupMenu secondColor;
	private JComboBox mode;
	
	public ClockEditor() {
		this(new ClockSequence());
	}
	
	public ClockEditor(ClockSequence sequence) {
		super(sequence);
		
		this.is24Hour = new JCheckBox("24-Hour");
		this.hourColor = new ColorPopupMenu();
		this.minuteColor = new ColorPopupMenu();
		this.secondColor = new ColorPopupMenu();
		this.mode = new JComboBox(ClockMode.values());
		this.mode.setEditable(false);
		
		this.is24Hour.setSelected(sequence.is24Hour);
		this.is24Hour.setEnabled(!sequence.mode.scaleHour());
		this.hourColor.setSelectedItem(sequence.hourColor);
		this.minuteColor.setSelectedItem(sequence.minuteColor);
		this.secondColor.setSelectedItem(sequence.secondColor);
		this.mode.setSelectedItem(sequence.mode);
		
		JPanel checkboxPanel = new JPanel(new BorderLayout());
		checkboxPanel.add(is24Hour, BorderLayout.LINE_START);
		
		JPanel labelPanel = new JPanel(new GridLayout(0, 1, 4, 4));
		labelPanel.add(new JLabel("Hour Color:"));
		labelPanel.add(new JLabel("Minute Color:"));
		labelPanel.add(new JLabel("Second Color:"));
		labelPanel.add(new JLabel("Mode:"));
		labelPanel.add(new JLabel(" "));
		
		JPanel controlPanel = new JPanel(new GridLayout(0, 1, 4, 4));
		controlPanel.add(hourColor);
		controlPanel.add(minuteColor);
		controlPanel.add(secondColor);
		controlPanel.add(mode);
		controlPanel.add(checkboxPanel);
		
		JPanel mainPanel = new JPanel(new BorderLayout(8, 8));
		mainPanel.add(labelPanel, BorderLayout.LINE_START);
		mainPanel.add(controlPanel, BorderLayout.CENTER);
		
		setLayout(new BorderLayout());
		add(mainPanel, BorderLayout.CENTER);
		fixHeight(this);
		
		is24Hour.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pixelSet.is24Hour = is24Hour.isSelected();
			}
		});
		hourColor.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				pixelSet.hourColor = ((Number)hourColor.getSelectedItem()).intValue();
			}
		});
		minuteColor.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				pixelSet.minuteColor = ((Number)minuteColor.getSelectedItem()).intValue();
			}
		});
		secondColor.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				pixelSet.secondColor = ((Number)secondColor.getSelectedItem()).intValue();
			}
		});
		mode.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				pixelSet.mode = (ClockMode)mode.getSelectedItem();
				is24Hour.setEnabled(!pixelSet.mode.scaleHour());
			}
		});
	}
	
	@Override
	public void setPixelSet(ClockSequence sequence) {
		super.setPixelSet(sequence);
		this.is24Hour.setSelected(sequence.is24Hour);
		this.is24Hour.setEnabled(!sequence.mode.scaleHour());
		this.hourColor.setSelectedItem(sequence.hourColor);
		this.minuteColor.setSelectedItem(sequence.minuteColor);
		this.secondColor.setSelectedItem(sequence.secondColor);
		this.mode.setSelectedItem(sequence.mode);
	}
	
	private static <C extends Component> C fixHeight(C c) {
		int h = c.getPreferredSize().height;
		c.setMinimumSize(new Dimension(c.getMinimumSize().width, h));
		c.setPreferredSize(new Dimension(c.getPreferredSize().width, h));
		c.setMaximumSize(new Dimension(c.getMaximumSize().width, h));
		return c;
	}
}
