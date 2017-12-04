package com.kreative.pixelpusher.array;

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
import com.kreative.pixelpusher.sequence.PixelSequenceEditor;

public class PixelArrayRowEditor extends PixelSequenceEditor<PixelArrayRow> {
	private static final long serialVersionUID = 1L;
	
	private final PixelSetInfoSet infoSet;
	
	public PixelArrayRowEditor(PixelSetInfoSet infoSet, PixelArrayRow sequence) {
		super(sequence);
		this.infoSet = infoSet;
		setLayout(new BorderLayout());
		add(makeMainPanel(), BorderLayout.CENTER);
	}
	
	@Override
	public void setPixelSet(PixelArrayRow sequence) {
		super.setPixelSet(sequence);
		removeAll();
		add(makeMainPanel(), BorderLayout.CENTER);
		revalidate();
	}
	
	private JPanel makeMainPanel() {
		final PixelArrayPopupMenu arrayPopup = new PixelArrayPopupMenu(infoSet);
		arrayPopup.setSelectedPixelSet(pixelSet.getArray());
		final SpinnerNumberModel rowModel = new SpinnerNumberModel(pixelSet.getRowNumber(), 0, 255, 1);
		JSpinner rowSpinner = new JSpinner(rowModel);
		
		JPanel labelPanel = new JPanel(new GridLayout(0,1,4,4));
		labelPanel.add(new JLabel("Array:"));
		labelPanel.add(new JLabel("Row:"));
		
		JPanel componentPanel = new JPanel(new GridLayout(0,1,4,4));
		componentPanel.add(arrayPopup);
		componentPanel.add(alignLeft(rowSpinner));
		
		JPanel mainPanel = new JPanel(new BorderLayout(8,8));
		mainPanel.add(labelPanel, BorderLayout.LINE_START);
		mainPanel.add(componentPanel, BorderLayout.CENTER);
		
		arrayPopup.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() != ItemEvent.SELECTED) return;
				PixelArray oldArray = pixelSet.getArray();
				PixelArray newArray = arrayPopup.getSelectedPixelSet();
				if (newArray != null) {
					if (newArray.dependsOn(pixelSet)) {
						JOptionPane.showMessageDialog(
							PixelArrayRowEditor.this,
							"The selected array would create a circular dependency.",
							"Array Row",
							JOptionPane.WARNING_MESSAGE
						);
						arrayPopup.setSelectedPixelSet(oldArray);
					} else {
						pixelSet.setArray(newArray);
					}
				}
			}
		});
		rowModel.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				pixelSet.setRowNumber(rowModel.getNumber().intValue());
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
