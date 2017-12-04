package com.kreative.pixelpusher.array;

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
import com.kreative.pixelpusher.pixelset.PixelSetInfoSet;
import com.kreative.pixelpusher.sequence.PixelSequenceEditor;

public class PixelArrayToSequenceEditor extends PixelSequenceEditor<PixelArrayToSequence> {
	private static final long serialVersionUID = 1L;
	
	private final PixelSetInfoSet infoSet;
	
	public PixelArrayToSequenceEditor(PixelSetInfoSet infoSet, PixelArrayToSequence sequence) {
		super(sequence);
		this.infoSet = infoSet;
		setLayout(new BorderLayout());
		add(makeMainPanel(), BorderLayout.CENTER);
	}
	
	@Override
	public void setPixelSet(PixelArrayToSequence sequence) {
		super.setPixelSet(sequence);
		removeAll();
		add(makeMainPanel(), BorderLayout.CENTER);
		revalidate();
	}
	
	private JPanel makeMainPanel() {
		JLabel arrayLabel = new JLabel("Array:");
		final PixelArrayPopupMenu arrayPopup = new PixelArrayPopupMenu(infoSet);
		arrayPopup.setSelectedPixelSet(pixelSet.getArray());
		JPanel arrayPanel = makeLine(4, arrayLabel, arrayPopup);
		
		JLabel rowsLabel = new JLabel("Rows:");
		final SpinnerNumberModel rowsModel = new SpinnerNumberModel(pixelSet.getRowCount(), 1, 500, 1);
		JSpinner rowsSpinner = new JSpinner(rowsModel);
		JLabel columnsLabel = new JLabel(" Columns:");
		final SpinnerNumberModel columnsModel = new SpinnerNumberModel(pixelSet.getColumnCount(), 1, 500, 1);
		JSpinner columnsSpinner = new JSpinner(columnsModel);
		JPanel rowsColumnsPanel = makeLine(4, rowsLabel, rowsSpinner, columnsLabel, columnsSpinner);
		
		final ArrayOrderingPanel orderingPanel = new ArrayOrderingPanel();
		orderingPanel.setSelectedValue(pixelSet.getOrdering());
		
		JPanel mainPanel = makePage(8, arrayPanel, rowsColumnsPanel, orderingPanel);
		
		arrayPopup.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() != ItemEvent.SELECTED) return;
				PixelArray oldArray = pixelSet.getArray();
				PixelArray newArray = arrayPopup.getSelectedPixelSet();
				if (newArray != null) {
					if (newArray.dependsOn(pixelSet)) {
						JOptionPane.showMessageDialog(
							PixelArrayToSequenceEditor.this,
							"The selected array would create a circular dependency.",
							"Convert Array to Sequence",
							JOptionPane.WARNING_MESSAGE
						);
						arrayPopup.setSelectedPixelSet(oldArray);
					} else {
						pixelSet.setArray(newArray);
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
