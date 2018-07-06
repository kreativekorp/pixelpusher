package com.kreative.unipixelpusher.marquee;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import com.kreative.unipixelpusher.gui.SpeedAdjustPanel;

public class MarqueeConfigPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private final MarqueeSequence sequence;
	private final String[] messageNames;
	private final MarqueeItem[] messages;
	private final JComboBox messagePopup;
	private final SpeedAdjustPanel speedPanel;
	
	public MarqueeConfigPanel(MarqueeParser mp, MarqueeSequence seq) {
		this.sequence = seq;
		this.messageNames = mp.getMessageNames();
		this.messages = new MarqueeItem[messageNames.length];
		this.messagePopup = new JComboBox(messageNames);
		this.messagePopup.setEditable(false);
		this.messagePopup.setMaximumRowCount(Math.max(messageNames.length / 2, 16));
		this.messagePopup.setSelectedIndex(-1);
		String messageName = seq.getMarqueeItemName();
		for (int i = 0; i < messageNames.length; i++) {
			messages[i] = mp.getMessage(messageNames[i]);
			if (messageNames[i].equals(messageName)) {
				messagePopup.setSelectedIndex(i);
			}
		}
		this.speedPanel = new SpeedAdjustPanel(seq, false);
		
		JPanel labelPanel = new JPanel(new GridLayout(0, 1, 4, 4));
		labelPanel.add(new JLabel("Message:"));
		labelPanel.add(new JLabel("Speed:"));
		JPanel messagePanel = new JPanel(new BorderLayout());
		messagePanel.add(messagePopup, BorderLayout.LINE_START);
		JPanel controlPanel = new JPanel(new GridLayout(0, 1, 4, 4));
		controlPanel.add(messagePanel);
		controlPanel.add(speedPanel);
		setLayout(new BorderLayout(8, 8));
		add(labelPanel, BorderLayout.LINE_START);
		add(controlPanel, BorderLayout.CENTER);
		
		messagePopup.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				int i = messagePopup.getSelectedIndex();
				if (i >= 0) sequence.setMarqueeItem(messageNames[i], messages[i]);
			}
		});
	}
}
