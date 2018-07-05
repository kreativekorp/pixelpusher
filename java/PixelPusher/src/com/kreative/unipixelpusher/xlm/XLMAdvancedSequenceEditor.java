package com.kreative.unipixelpusher.xlm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

public class XLMAdvancedSequenceEditor extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private final XLMSequence sequence;
	
	public XLMAdvancedSequenceEditor(XLMSequence sequence) {
		this.sequence = sequence;
		setLayout(new BorderLayout());
		add(makeMainPanel(), BorderLayout.CENTER);
	}
	
	private JPanel makeMainPanel() {
		final JPanel channelList = new JPanel();
		channelList.setLayout(new BoxLayout(channelList, BoxLayout.PAGE_AXIS));
		for (int i = 0; i < sequence.getChannelCount(); i++) {
			channelList.add(new XLMAdvancedChannelEditor(sequence.getChannel(i)));
		}
		channelList.setBorder(BorderFactory.createLineBorder(Color.gray));
		JPanel channelListWrapper = new JPanel(new BorderLayout());
		channelListWrapper.add(channelList, BorderLayout.PAGE_START);
		
		JButton add = squareOff(new JButton("+"), "Add Channel");
		JButton remove = squareOff(new JButton("\u2212"), "Remove Channel");
		
		JPanel buttonPanel1 = new JPanel(new GridLayout(0,1,2,2));
		buttonPanel1.add(add);
		buttonPanel1.add(remove);
		Dimension d = buttonPanel1.getPreferredSize();
		buttonPanel1.setMinimumSize(d);
		buttonPanel1.setPreferredSize(d);
		buttonPanel1.setMaximumSize(d);
		
		JPanel buttonPanel2 = new JPanel();
		buttonPanel2.setLayout(new BoxLayout(buttonPanel2, BoxLayout.PAGE_AXIS));
		buttonPanel2.add(Box.createVerticalGlue());
		buttonPanel2.add(buttonPanel1);
		buttonPanel2.add(Box.createVerticalGlue());
		
		JPanel main = new JPanel(new BorderLayout(4,4));
		main.add(channelListWrapper, BorderLayout.CENTER);
		main.add(buttonPanel2, BorderLayout.LINE_END);
		
		add.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int l = sequence.getChannelCount() + 1;
				sequence.setChannelCount(l);
				channelList.removeAll();
				for (int i = 0; i < l; i++) {
					channelList.add(new XLMAdvancedChannelEditor(sequence.getChannel(i)));
				}
				XLMAdvancedSequenceEditor.this.revalidate();
			}
		});
		remove.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int l = sequence.getChannelCount() - 1;
				if (l < 1) return;
				sequence.setChannelCount(l);
				channelList.removeAll();
				for (int i = 0; i < l; i++) {
					channelList.add(new XLMAdvancedChannelEditor(sequence.getChannel(i)));
				}
				XLMAdvancedSequenceEditor.this.revalidate();
			}
		});
		
		return main;
	}
	
	private static <C extends JComponent> C squareOff(C c, String tooltip) {
		c.putClientProperty("JButton.buttonType", "bevel");
		c.setToolTipText(tooltip);
		return c;
	}
}
