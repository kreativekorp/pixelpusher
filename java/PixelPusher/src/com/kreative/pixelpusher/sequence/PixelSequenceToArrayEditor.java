package com.kreative.pixelpusher.sequence;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import com.kreative.pixelpusher.array.ArrayOrderingPanel;
import com.kreative.pixelpusher.array.PixelArrayEditor;
import com.kreative.pixelpusher.pixelset.PixelSetInfoSet;

public class PixelSequenceToArrayEditor extends PixelArrayEditor<PixelSequenceToArray> {
	private static final long serialVersionUID = 1L;
	
	private final PixelSetInfoSet infoSet;
	
	public PixelSequenceToArrayEditor(PixelSetInfoSet infoSet, PixelSequenceToArray array) {
		super(array);
		this.infoSet = infoSet;
		setLayout(new BorderLayout());
		add(makeMainPanel(), BorderLayout.CENTER);
	}
	
	@Override
	public void setPixelSet(PixelSequenceToArray array) {
		super.setPixelSet(array);
		removeAll();
		add(makeMainPanel(), BorderLayout.CENTER);
		revalidate();
	}
	
	private JPanel makeMainPanel() {
		JLabel sequenceLabel = new JLabel("Sequence:");
		final PixelSequencePopupMenu sequencePopup = new PixelSequencePopupMenu(infoSet);
		sequencePopup.setSelectedPixelSet(pixelSet.getSequence());
		JPanel sequencePanel = makeLine(4, sequenceLabel, sequencePopup);
		
		JLabel rowsLabel = new JLabel("Rows:");
		final SpinnerNumberModel rowsModel = new SpinnerNumberModel(pixelSet.getRowCount(), 1, 500, 1);
		JSpinner rowsSpinner = new JSpinner(rowsModel);
		JLabel columnsLabel = new JLabel(" Columns:");
		final SpinnerNumberModel columnsModel = new SpinnerNumberModel(pixelSet.getColumnCount(), 1, 500, 1);
		JSpinner columnsSpinner = new JSpinner(columnsModel);
		JPanel rowsColumnsPanel = makeLine(4, rowsLabel, rowsSpinner, columnsLabel, columnsSpinner);
		
		final ArrayOrderingPanel orderingPanel = new ArrayOrderingPanel();
		orderingPanel.setSelectedValue(pixelSet.getOrdering());
		
		JPanel mainPanel = makePage(8, sequencePanel, rowsColumnsPanel, orderingPanel);
		
		sequencePopup.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() != ItemEvent.SELECTED) return;
				PixelSequence oldSequence = pixelSet.getSequence();
				PixelSequence newSequence = sequencePopup.getSelectedPixelSet();
				if (newSequence != null) {
					if (newSequence.dependsOn(pixelSet)) {
						JOptionPane.showMessageDialog(
							PixelSequenceToArrayEditor.this,
							"The selected sequence would create a circular dependency.",
							"Convert Sequence to Array",
							JOptionPane.WARNING_MESSAGE
						);
						sequencePopup.setSelectedPixelSet(oldSequence);
					} else {
						pixelSet.setSequence(newSequence);
					}
				}
			}
		});
		rowsModel.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				pixelSet.setRowCount(rowsModel.getNumber().intValue());
			}
		});
		columnsModel.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				pixelSet.setColumnCount(columnsModel.getNumber().intValue());
			}
		});
		orderingPanel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pixelSet.setOrdering(orderingPanel.getSelectedValue());
			}
		});
		
		return mainPanel;
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
}
