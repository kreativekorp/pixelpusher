package com.kreative.pixelpusher.sequence;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import com.kreative.pixelpusher.pixelset.PixelSetInfoSet;

public class PixelInterleaveEditor extends PixelSequenceEditor<PixelInterleave> {
	private static final long serialVersionUID = 1L;
	
	private final PixelSetInfoSet infoSet;
	
	public PixelInterleaveEditor(PixelSetInfoSet infoSet, PixelInterleave sequence) {
		super(sequence);
		this.infoSet = infoSet;
		setLayout(new BorderLayout());
		add(makeMainPanel(), BorderLayout.CENTER);
	}
	
	@Override
	public void setPixelSet(PixelInterleave sequence) {
		super.setPixelSet(sequence);
		removeAll();
		add(makeMainPanel(), BorderLayout.CENTER);
		pack();
	}
	
	private JPanel sequencePanel;
	private JPanel buttonPanel;
	
	private JPanel makeMainPanel() {
		sequencePanel = new JPanel(new GridLayout(0,1,2,2));
		buttonPanel = new JPanel(new GridLayout(0,1,2,2));
		
		JPanel sequencePanel2 = new JPanel(new BorderLayout(2,2));
		sequencePanel2.add(new JLabel("Sequence:"), BorderLayout.PAGE_START);
		sequencePanel2.add(sequencePanel, BorderLayout.CENTER);
		
		JPanel buttonPanel2 = new JPanel(new BorderLayout(2,2));
		buttonPanel2.add(new JLabel(" "), BorderLayout.PAGE_START);
		buttonPanel2.add(buttonPanel, BorderLayout.CENTER);
		
		JPanel centerPanel = new JPanel(new BorderLayout(8,8));
		centerPanel.add(buttonPanel2, BorderLayout.LINE_END);
		centerPanel.add(sequencePanel2, BorderLayout.CENTER);
		
		addPixelSequences();
		return centerPanel;
	}
	
	private void addPixelSequences() {
		sequencePanel.removeAll();
		buttonPanel.removeAll();
		
		for (int i = 0; i < pixelSet.size(); i++) {
			final int j = i;
			
			final PixelSequencePopupMenu sequencePopup = new PixelSequencePopupMenu(infoSet);
			sequencePopup.setSelectedPixelSet(pixelSet.get(j));
			sequencePanel.add(sequencePopup);
			sequencePopup.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() != ItemEvent.SELECTED) return;
					PixelSequence oldSequence = pixelSet.get(j);
					PixelSequence newSequence = sequencePopup.getSelectedPixelSet();
					if (newSequence != null) {
						if (newSequence.dependsOn(pixelSet)) {
							JOptionPane.showMessageDialog(
								PixelInterleaveEditor.this,
								"The selected sequence would create a circular dependency.",
								"Interleave",
								JOptionPane.WARNING_MESSAGE
							);
							sequencePopup.setSelectedPixelSet(oldSequence);
						} else {
							pixelSet.set(j, newSequence);
						}
					}
				}
			});
			
			JButton removeButton = squareOff(new JButton("\u2212"), "Remove");
			JButton upButton = squareOff(new JButton("\u25B2"), "Move Up");
			JButton downButton = squareOff(new JButton("\u25BC"), "Move Down");
			JPanel buttonPanel2 = new JPanel(new GridLayout(1,0,2,2));
			buttonPanel2.add(removeButton);
			buttonPanel2.add(upButton);
			buttonPanel2.add(downButton);
			buttonPanel.add(buttonPanel2);
			removeButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					pixelSet.remove(j);
					addPixelSequences();
					pack();
				}
			});
			upButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (j > 0) {
						PixelSequence sequence = pixelSet.get(j);
						pixelSet.remove(j);
						pixelSet.add(j-1, sequence);
						addPixelSequences();
						invalidate();
						validate();
					}
				}
			});
			downButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (j+1 < pixelSet.size()) {
						PixelSequence sequence = pixelSet.get(j);
						pixelSet.remove(j);
						pixelSet.add(j+1, sequence);
						addPixelSequences();
						invalidate();
						validate();
					}
				}
			});
		}
		
		final PixelSequencePopupMenu addSequencePopup = new PixelSequencePopupMenu(infoSet);
		JPanel addSequencePanel = new JPanel(new BorderLayout(4,4));
		addSequencePanel.add(new JLabel("Add:"), BorderLayout.LINE_START);
		addSequencePanel.add(addSequencePopup, BorderLayout.CENTER);
		sequencePanel.add(addSequencePanel);
		
		JButton addButton = new JButton("Add");
		buttonPanel.add(addButton);
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				PixelSequence sequence = addSequencePopup.getSelectedPixelSet();
				if (sequence != null) {
					if (sequence.dependsOn(pixelSet)) {
						JOptionPane.showMessageDialog(
							PixelInterleaveEditor.this,
							"The selected sequence would create a circular dependency.",
							"Interleave",
							JOptionPane.WARNING_MESSAGE
						);
					} else {
						pixelSet.add(sequence);
						addPixelSequences();
						pack();
					}
				}
			}
		});
	}
	
	private void pack() {
		invalidate();
		validate();
		Component c = this;
		while (c != null) {
			if (c instanceof Frame) {
				c.setMinimumSize(null);
				c.setPreferredSize(null);
				c.setMaximumSize(null);
				((Frame)c).pack();
				
				int wx = c.getWidth() - this.getWidth();
				int hx = c.getHeight() - this.getHeight();
				Dimension d;
				d = this.getMinimumSize();
				d.width += wx;
				d.height += hx;
				c.setMinimumSize(d);
				d = this.getPreferredSize();
				d.width += wx;
				d.height += hx;
				c.setPreferredSize(d);
				d = this.getMaximumSize();
				d.width += wx;
				if (d.width < 0) d.width = Integer.MAX_VALUE;
				d.height += hx;
				if (d.height < 0) d.height = Integer.MAX_VALUE;
				c.setMaximumSize(d);
			}
			c = c.getParent();
		}
	}
	
	private static <C extends JComponent> C squareOff(C c, String tooltip) {
		c.putClientProperty("JButton.buttonType", "bevel");
		c.setToolTipText(tooltip);
		return c;
	}
}
