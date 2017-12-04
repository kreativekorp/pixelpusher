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

public class PixelSubArrayEditor extends PixelArrayEditor<PixelSubArray> {
	private static final long serialVersionUID = 1L;
	
	private final PixelSetInfoSet infoSet;
	
	public PixelSubArrayEditor(PixelSetInfoSet infoSet, PixelSubArray array) {
		super(array);
		this.infoSet = infoSet;
		setLayout(new BorderLayout());
		add(makeMainPanel(), BorderLayout.CENTER);
	}
	
	@Override
	public void setPixelSet(PixelSubArray array) {
		super.setPixelSet(array);
		removeAll();
		add(makeMainPanel(), BorderLayout.CENTER);
		revalidate();
	}
	
	private JPanel makeMainPanel() {
		final PixelArrayPopupMenu arrayPopup = new PixelArrayPopupMenu(infoSet);
		arrayPopup.setSelectedPixelSet(pixelSet.getArray());
		final SpinnerNumberModel offsetXModel = new SpinnerNumberModel(pixelSet.getOffsetX(), 0, 500, 1);
		JSpinner offsetXSpinner = new JSpinner(offsetXModel);
		final SpinnerNumberModel offsetYModel = new SpinnerNumberModel(pixelSet.getOffsetY(), 0, 500, 1);
		JSpinner offsetYSpinner = new JSpinner(offsetYModel);
		final SpinnerNumberModel widthModel = new SpinnerNumberModel(pixelSet.getWidth(), 0, 500, 1);
		JSpinner widthSpinner = new JSpinner(widthModel);
		final SpinnerNumberModel heightModel = new SpinnerNumberModel(pixelSet.getHeight(), 0, 500, 1);
		JSpinner heightSpinner = new JSpinner(heightModel);
		final SpinnerNumberModel stepXModel = new SpinnerNumberModel(pixelSet.getStepX(), 0, 500, 1);
		JSpinner stepXSpinner = new JSpinner(stepXModel);
		final SpinnerNumberModel stepYModel = new SpinnerNumberModel(pixelSet.getStepY(), 0, 500, 1);
		JSpinner stepYSpinner = new JSpinner(stepYModel);
		
		JPanel labelPanel = new JPanel(new GridLayout(0,1,4,4));
		labelPanel.add(new JLabel("Array:"));
		labelPanel.add(new JLabel("X Offset:"));
		labelPanel.add(new JLabel("Y Offset:"));
		labelPanel.add(new JLabel("Width:"));
		labelPanel.add(new JLabel("Height:"));
		labelPanel.add(new JLabel("X Step:"));
		labelPanel.add(new JLabel("Y Step:"));
		
		JPanel componentPanel = new JPanel(new GridLayout(0,1,4,4));
		componentPanel.add(arrayPopup);
		componentPanel.add(alignLeft(offsetXSpinner));
		componentPanel.add(alignLeft(offsetYSpinner));
		componentPanel.add(alignLeft(widthSpinner));
		componentPanel.add(alignLeft(heightSpinner));
		componentPanel.add(alignLeft(stepXSpinner));
		componentPanel.add(alignLeft(stepYSpinner));
		
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
							PixelSubArrayEditor.this,
							"The selected array would create a circular dependency.",
							"Subarray",
							JOptionPane.WARNING_MESSAGE
						);
						arrayPopup.setSelectedPixelSet(oldArray);
					} else {
						pixelSet.setArray(newArray);
					}
				}
			}
		});
		offsetXModel.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				pixelSet.setOffsetX(offsetXModel.getNumber().intValue());
			}
		});
		offsetYModel.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				pixelSet.setOffsetY(offsetYModel.getNumber().intValue());
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
		stepXModel.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				pixelSet.setStepX(stepXModel.getNumber().intValue());
			}
		});
		stepYModel.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				pixelSet.setStepY(stepYModel.getNumber().intValue());
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
