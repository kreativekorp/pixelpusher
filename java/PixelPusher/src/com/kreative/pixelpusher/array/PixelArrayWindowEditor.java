package com.kreative.pixelpusher.array;

import java.awt.BorderLayout;
import java.awt.Color;
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

public class PixelArrayWindowEditor extends PixelArrayEditor<PixelArrayWindow> {
	private static final long serialVersionUID = 1L;
	
	private final PixelSetInfoSet infoSet;
	
	public PixelArrayWindowEditor(PixelSetInfoSet infoSet, PixelArrayWindow array) {
		super(array);
		this.infoSet = infoSet;
		setLayout(new BorderLayout());
		add(makeMainPanel(), BorderLayout.CENTER);
	}
	
	@Override
	public void setPixelSet(PixelArrayWindow array) {
		super.setPixelSet(array);
		removeAll();
		add(makeMainPanel(), BorderLayout.CENTER);
		revalidate();
	}
	
	private PixelArrayPopupMenu arrayPopup;
	
	private JPanel makeMainPanel() {
		arrayPopup = new PixelArrayPopupMenu(infoSet);
		arrayPopup.setSelectedPixelSet(pixelSet.getArray());
		final SpinnerNumberModel locationXModel = new SpinnerNumberModel(pixelSet.getLocationX(), 0, 500, 1);
		JSpinner locationXSpinner = new JSpinner(locationXModel);
		final SpinnerNumberModel locationYModel = new SpinnerNumberModel(pixelSet.getLocationY(), 0, 500, 1);
		JSpinner locationYSpinner = new JSpinner(locationYModel);
		final SpinnerNumberModel widthModel = new SpinnerNumberModel(pixelSet.getWidth(), 0, 500, 1);
		JSpinner widthSpinner = new JSpinner(widthModel);
		final SpinnerNumberModel heightModel = new SpinnerNumberModel(pixelSet.getHeight(), 0, 500, 1);
		JSpinner heightSpinner = new JSpinner(heightModel);
		
		JPanel labelPanel = new JPanel(new GridLayout(0,1,4,4));
		labelPanel.add(new JLabel("Array:"));
		labelPanel.add(new JLabel("X Location:"));
		labelPanel.add(new JLabel("Y Location:"));
		labelPanel.add(new JLabel("Width:"));
		labelPanel.add(new JLabel("Height:"));
		
		JPanel componentPanel = new JPanel(new GridLayout(0,1,4,4));
		componentPanel.add(arrayPopup);
		componentPanel.add(alignLeft(locationXSpinner));
		componentPanel.add(alignLeft(locationYSpinner));
		componentPanel.add(alignLeft(widthSpinner));
		componentPanel.add(alignLeft(heightSpinner));
		
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
							PixelArrayWindowEditor.this,
							"The selected array would create a circular dependency.",
							"Window",
							JOptionPane.WARNING_MESSAGE
						);
						arrayPopup.setSelectedPixelSet(oldArray);
					} else {
						pixelSet.setArray(newArray);
					}
				}
			}
		});
		locationXModel.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				pixelSet.setLocationX(locationXModel.getNumber().intValue());
			}
		});
		locationYModel.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				pixelSet.setLocationY(locationYModel.getNumber().intValue());
			}
		});
		widthModel.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				pixelSet.setWidth(widthModel.getNumber().intValue());
			}
		});
		heightModel.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				pixelSet.setHeight(heightModel.getNumber().intValue());
			}
		});
		
		return alignTop(mainPanel);
	}
	
	public PixelArrayPopupMenu getArrayPopup() {
		return arrayPopup;
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
	
	public static final JPanel nullEditor(PixelSetInfoSet infoSet) {
		PixelArrayWindowEditor dummyEditor = new PixelArrayWindowEditor(infoSet, new PixelArrayWindow());
		JLabel nullEditorLabel = new JLabel("No Editor");
		nullEditorLabel.setForeground(Color.darkGray);
		nullEditorLabel.setHorizontalAlignment(JLabel.CENTER);
		nullEditorLabel.setVerticalAlignment(JLabel.CENTER);
		nullEditorLabel.setMinimumSize(dummyEditor.getPreferredSize());
		nullEditorLabel.setPreferredSize(dummyEditor.getPreferredSize());
		nullEditorLabel.setMaximumSize(dummyEditor.getMaximumSize());
		JPanel nullEditor = new JPanel(new BorderLayout());
		nullEditor.add(nullEditorLabel, BorderLayout.CENTER);
		nullEditor.setMinimumSize(dummyEditor.getPreferredSize());
		nullEditor.setPreferredSize(dummyEditor.getPreferredSize());
		nullEditor.setMaximumSize(dummyEditor.getMaximumSize());
		return nullEditor;
	}
}
