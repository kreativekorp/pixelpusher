package com.kreative.pixelpusher.sequence;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import com.kreative.pixelpusher.pixelset.PixelSetInfoSet;

public class PixelSubSequenceEditor extends PixelSequenceEditor<PixelSubSequence> {
	private static final long serialVersionUID = 1L;
	
	private final PixelSetInfoSet infoSet;
	
	public PixelSubSequenceEditor(PixelSetInfoSet infoSet, PixelSubSequence sequence) {
		super(sequence);
		this.infoSet = infoSet;
		setLayout(new BorderLayout());
		add(makeMainPanel(), BorderLayout.CENTER);
	}
	
	@Override
	public void setPixelSet(PixelSubSequence sequence) {
		super.setPixelSet(sequence);
		removeAll();
		add(makeMainPanel(), BorderLayout.CENTER);
		revalidate();
	}
	
	private JPanel makeMainPanel() {
		final PixelSequencePopupMenu sequencePopup = new PixelSequencePopupMenu(infoSet);
		sequencePopup.setSelectedPixelSet(pixelSet.getSequence());
		final SpinnerNumberModel offsetModel = new SpinnerNumberModel(pixelSet.getOffset(), 0, 500, 1);
		JSpinner offsetSpinner = new JSpinner(offsetModel);
		final SpinnerNumberModel lengthModel = new SpinnerNumberModel(pixelSet.getLength(), 0, 500, 1);
		JSpinner lengthSpinner = new JSpinner(lengthModel);
		final SpinnerNumberModel stepModel = new SpinnerNumberModel(pixelSet.getStep(), 0, 500, 1);
		JSpinner stepSpinner = new JSpinner(stepModel);
		
		JPanel labelPanel = new JPanel(new GridLayout(0,1,4,4));
		labelPanel.add(new JLabel("Sequence:"));
		labelPanel.add(new JLabel("Offset:"));
		labelPanel.add(new JLabel("Length:"));
		labelPanel.add(new JLabel("Step:"));
		
		JPanel componentPanel = new JPanel(new GridLayout(0,1,4,4));
		componentPanel.add(sequencePopup);
		componentPanel.add(alignLeft(offsetSpinner));
		componentPanel.add(alignLeft(lengthSpinner));
		componentPanel.add(alignLeft(stepSpinner));
		
		JPanel mainPanel = new JPanel(new BorderLayout(8,8));
		mainPanel.add(labelPanel, BorderLayout.LINE_START);
		mainPanel.add(componentPanel, BorderLayout.CENTER);
		
		sequencePopup.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() != ItemEvent.SELECTED) return;
				PixelSequence oldSequence = pixelSet.getSequence();
				PixelSequence newSequence = sequencePopup.getSelectedPixelSet();
				if (newSequence != null) {
					if (newSequence.dependsOn(pixelSet)) {
						JOptionPane.showMessageDialog(
							PixelSubSequenceEditor.this,
							"The selected sequence would create a circular dependency.",
							"Subsequence",
							JOptionPane.WARNING_MESSAGE
						);
						sequencePopup.setSelectedPixelSet(oldSequence);
					} else {
						pixelSet.setSequence(newSequence);
					}
				}
			}
		});
		offsetModel.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				pixelSet.setOffset(offsetModel.getNumber().intValue());
			}
		});
		lengthModel.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				pixelSet.setLength(lengthModel.getNumber().intValue());
			}
		});
		stepModel.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				pixelSet.setStep(stepModel.getNumber().intValue());
			}
		});
		
		return alignTop(mainPanel);
	}
	
	private static final JPanel alignLeft(Component c) {
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(c, BorderLayout.LINE_START);
		return panel;
	}
	
	private static final JPanel alignTop(Component c) {
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(c, BorderLayout.PAGE_START);
		return panel;
	}
}
