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
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import com.kreative.pixelpusher.pixelset.PixelSetInfoSet;

public class PixelConcatenateEditor extends PixelSequenceEditor<PixelConcatenate> {
	private static final long serialVersionUID = 1L;
	
	private final PixelSetInfoSet infoSet;
	
	public PixelConcatenateEditor(PixelSetInfoSet infoSet, PixelConcatenate sequence) {
		super(sequence);
		this.infoSet = infoSet;
		setLayout(new BorderLayout());
		add(makeMainPanel(), BorderLayout.CENTER);
	}
	
	@Override
	public void setPixelSet(PixelConcatenate sequence) {
		super.setPixelSet(sequence);
		removeAll();
		add(makeMainPanel(), BorderLayout.CENTER);
		pack();
	}
	
	private JPanel sequencePanel;
	private JPanel lengthPanel;
	private JPanel buttonPanel;
	
	private JPanel makeMainPanel() {
		sequencePanel = new JPanel(new GridLayout(0,1,2,2));
		lengthPanel = new JPanel(new GridLayout(0,1,2,2));
		buttonPanel = new JPanel(new GridLayout(0,1,2,2));
		
		JPanel sequencePanel2 = new JPanel(new BorderLayout(2,2));
		sequencePanel2.add(new JLabel("Sequence:"), BorderLayout.PAGE_START);
		sequencePanel2.add(sequencePanel, BorderLayout.CENTER);
		
		JPanel lengthPanel2 = new JPanel(new BorderLayout(2,2));
		lengthPanel2.add(new JLabel("Length:"), BorderLayout.PAGE_START);
		lengthPanel2.add(lengthPanel, BorderLayout.CENTER);
		
		JPanel buttonPanel2 = new JPanel(new BorderLayout(2,2));
		buttonPanel2.add(new JLabel(" "), BorderLayout.PAGE_START);
		buttonPanel2.add(buttonPanel, BorderLayout.CENTER);
		
		JPanel sidePanel = new JPanel(new BorderLayout(8,8));
		sidePanel.add(buttonPanel2, BorderLayout.LINE_END);
		sidePanel.add(lengthPanel2, BorderLayout.CENTER);
		
		JPanel centerPanel = new JPanel(new BorderLayout(8,8));
		centerPanel.add(sidePanel, BorderLayout.LINE_END);
		centerPanel.add(sequencePanel2, BorderLayout.CENTER);
		
		addPixelSequences();
		return centerPanel;
	}
	
	private void addPixelSequences() {
		sequencePanel.removeAll();
		lengthPanel.removeAll();
		buttonPanel.removeAll();
		
		for (int i = 0; i < pixelSet.size(); i++) {
			final int j = i;
			
			final PixelSequencePopupMenu sequencePopup = new PixelSequencePopupMenu(infoSet);
			sequencePopup.setSelectedPixelSet(pixelSet.getSequence(j));
			sequencePanel.add(sequencePopup);
			sequencePopup.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() != ItemEvent.SELECTED) return;
					PixelSequence oldSequence = pixelSet.getSequence(j);
					PixelSequence newSequence = sequencePopup.getSelectedPixelSet();
					if (newSequence != null) {
						if (newSequence.dependsOn(pixelSet)) {
							JOptionPane.showMessageDialog(
								PixelConcatenateEditor.this,
								"The selected sequence would create a circular dependency.",
								"Concatenate",
								JOptionPane.WARNING_MESSAGE
							);
							sequencePopup.setSelectedPixelSet(oldSequence);
						} else {
							pixelSet.setSequence(j, newSequence);
						}
					}
				}
			});
			
			final SpinnerNumberModel lengthModel = new SpinnerNumberModel(pixelSet.getLength(j), 1, 500, 1);
			lengthPanel.add(new JSpinner(lengthModel));
			lengthModel.addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent e) {
					pixelSet.setLength(j, lengthModel.getNumber().intValue());
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
						PixelSequence sequence = pixelSet.getSequence(j);
						int length = pixelSet.getLength(j);
						pixelSet.remove(j);
						pixelSet.add(j-1, sequence, length);
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
						PixelSequence sequence = pixelSet.getSequence(j);
						int length = pixelSet.getLength(j);
						pixelSet.remove(j);
						pixelSet.add(j+1, sequence, length);
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
		
		final SpinnerNumberModel addLengthModel = new SpinnerNumberModel(8, 1, 500, 1);
		JSpinner addLengthSpinner = new JSpinner(addLengthModel);
		lengthPanel.add(addLengthSpinner);
		
		JButton addButton = new JButton("Add");
		buttonPanel.add(addButton);
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				PixelSequence sequence = addSequencePopup.getSelectedPixelSet();
				int length = addLengthModel.getNumber().intValue();
				if (sequence != null && length > 0) {
					if (sequence.dependsOn(pixelSet)) {
						JOptionPane.showMessageDialog(
							PixelConcatenateEditor.this,
							"The selected sequence would create a circular dependency.",
							"Concatenate",
							JOptionPane.WARNING_MESSAGE
						);
					} else {
						pixelSet.add(sequence, length);
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
