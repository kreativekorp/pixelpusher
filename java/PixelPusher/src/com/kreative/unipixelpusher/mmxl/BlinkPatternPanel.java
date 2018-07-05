package com.kreative.unipixelpusher.mmxl;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.AbstractTableModel;
import com.kreative.unipixelpusher.mmxl.MMXLBlinkPattern;
import com.kreative.unipixelpusher.mmxl.MMXLParser;
import com.kreative.unipixelpusher.mmxl.MMXLSequence;

public class BlinkPatternPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private final MMXLSequence sequence;
	private final String[] presetNames;
	private final MMXLBlinkPattern[] presetPatterns;
	private final JComboBox presetPopup;
	private final SpinnerNumberModel channelsModel;
	private final SpinnerNumberModel framesModel;
	private final SpinnerNumberModel durationModel;
	private final BlinkPatternTableModel patternModel;
	private final JTable patternTable;
	private boolean updating;
	
	public BlinkPatternPanel(MMXLParser mmxl, MMXLSequence seq, boolean title) {
		this.sequence = seq;
		this.presetNames = mmxl.getBlinkPatternNames();
		this.presetPatterns = new MMXLBlinkPattern[presetNames.length];
		this.presetPopup = new JComboBox(presetNames);
		this.presetPopup.setEditable(false);
		this.presetPopup.setMaximumRowCount(Math.max(presetNames.length / 2, 16));
		this.presetPopup.setSelectedIndex(-1);
		int duration = (int)seq.getFrameDuration();
		int[][] levels = seq.getLevels();
		for (int i = 0; i < presetNames.length; i++) {
			presetPatterns[i] = mmxl.getBlinkPattern(presetNames[i]);
			if (presetPatterns[i].matches(duration, levels)) {
				presetPopup.setSelectedIndex(i);
			}
		}
		this.channelsModel = new SpinnerNumberModel(levels[0].length, 1, 999, 1);
		this.framesModel = new SpinnerNumberModel(levels.length, 1, 999, 1);
		this.durationModel = new SpinnerNumberModel(duration, 1, 99999, 1);
		this.patternModel = new BlinkPatternTableModel();
		this.patternTable = new JTable(patternModel);
		this.patternTable.setPreferredScrollableViewportSize(new Dimension(200, 100));
		JScrollPane patternPane = new JScrollPane(
			patternTable,
			JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS
		);
		
		JPanel presetPanel = new JPanel(new BorderLayout(4, 4));
		presetPanel.add(new JLabel("Preset:"), BorderLayout.LINE_START);
		presetPanel.add(presetPopup, BorderLayout.CENTER);
		JPanel dimensionPanel = new JPanel();
		dimensionPanel.setLayout(new BoxLayout(dimensionPanel, BoxLayout.LINE_AXIS));
		dimensionPanel.add(new JLabel("Channels:"));
		dimensionPanel.add(Box.createHorizontalStrut(4));
		dimensionPanel.add(new JSpinner(channelsModel));
		dimensionPanel.add(Box.createHorizontalStrut(8));
		dimensionPanel.add(new JLabel("Frames:"));
		dimensionPanel.add(Box.createHorizontalStrut(4));
		dimensionPanel.add(new JSpinner(framesModel));
		dimensionPanel.add(Box.createHorizontalStrut(4));
		dimensionPanel.add(new JLabel("@"));
		dimensionPanel.add(Box.createHorizontalStrut(4));
		dimensionPanel.add(new JSpinner(durationModel));
		dimensionPanel.add(Box.createHorizontalStrut(4));
		dimensionPanel.add(new JLabel("ms"));
		JPanel topPanel = new JPanel(new BorderLayout(4, 4));
		topPanel.add(presetPanel, BorderLayout.LINE_START);
		topPanel.add(dimensionPanel, BorderLayout.LINE_END);
		JPanel mainPanel = new JPanel(new BorderLayout(8, 8));
		mainPanel.add(topPanel, BorderLayout.PAGE_START);
		mainPanel.add(patternPane, BorderLayout.CENTER);
		setLayout(new BorderLayout(8, 8));
		if (title) add(new JLabel("Blink Pattern:"), BorderLayout.PAGE_START);
		add(mainPanel, BorderLayout.CENTER);
		
		updating = false;
		presetPopup.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (updating) return;
				updating = true;
				int i = presetPopup.getSelectedIndex();
				if (i >= 0) {
					presetPatterns[i].apply(sequence);
					patternModel.pull();
					channelsModel.setValue(patternModel.columnCount);
					framesModel.setValue(patternModel.rowCount);
					durationModel.setValue((int)sequence.getFrameDuration());
				}
				updating = false;
			}
		});
		channelsModel.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (updating) return;
				int n = channelsModel.getNumber().intValue();
				patternModel.setColumnCount(n);
			}
		});
		framesModel.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (updating) return;
				int n = framesModel.getNumber().intValue();
				patternModel.setRowCount(n);
			}
		});
		durationModel.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (updating) return;
				updating = true;
				presetPopup.setSelectedIndex(-1);
				int duration = durationModel.getNumber().intValue();
				for (int i = 0; i < presetPatterns.length; i++) {
					if (presetPatterns[i].matches(duration, patternModel.levels)) {
						presetPopup.setSelectedIndex(i);
					}
				}
				sequence.setFrameDuration(duration);
				updating = false;
			}
		});
	}
	
	private class BlinkPatternTableModel extends AbstractTableModel {
		private static final long serialVersionUID = 1L;
		
		private int rowCount;
		private int columnCount;
		private int[][] levels;
		
		public BlinkPatternTableModel() {
			pull();
		}
		
		@Override
		public int getRowCount() {
			return this.rowCount;
		}
		
		public void setRowCount(int rowCount) {
			if (rowCount < 1) rowCount = 1;
			int[][] levels = new int[rowCount][columnCount];
			for (int y = 0; y < rowCount && y < this.levels.length; y++) {
				for (int x = 0; x < columnCount && x < this.levels[y].length; x++) {
					levels[y][x] = this.levels[y][x];
				}
			}
			this.rowCount = rowCount;
			this.levels = levels;
			fireTableStructureChanged();
			push();
		}
		
		@Override
		public int getColumnCount() {
			return this.columnCount;
		}
		
		public void setColumnCount(int columnCount) {
			if (columnCount < 1) columnCount = 1;
			int[][] levels = new int[rowCount][columnCount];
			for (int y = 0; y < rowCount && y < this.levels.length; y++) {
				for (int x = 0; x < columnCount && x < this.levels[y].length; x++) {
					levels[y][x] = this.levels[y][x];
				}
			}
			this.columnCount = columnCount;
			this.levels = levels;
			fireTableStructureChanged();
			push();
		}
		
		@Override
		public boolean isCellEditable(int y, int x) {
			return true;
		}
		
		@Override
		public Object getValueAt(int y, int x) {
			return this.levels[y][x];
		}
		
		@Override
		public void setValueAt(Object value, int y, int x) {
			try {
				int i = Integer.parseInt(value.toString());
				this.levels[y][x] = (i < 0) ? 0 : (i > 255) ? 255 : i;
			} catch (NumberFormatException e) {
				this.levels[y][x] = 0;
			}
			push();
		}
		
		public void pull() {
			int[][] levels = sequence.getLevels();
			this.rowCount = 1;
			if (levels.length > this.rowCount) {
				this.rowCount = levels.length;
			}
			this.columnCount = 1;
			for (int[] row : levels) {
				if (row.length > this.columnCount) {
					this.columnCount = row.length;
				}
			}
			this.levels = new int[this.rowCount][this.columnCount];
			for (int y = 0; y < this.rowCount && y < levels.length; y++) {
				for (int x = 0; x < this.columnCount && x < levels[y].length; x++) {
					this.levels[y][x] = levels[y][x];
				}
			}
			fireTableStructureChanged();
		}
		
		public void push() {
			int[][] levels = new int[rowCount][columnCount];
			for (int y = 0; y < rowCount; y++) {
				for (int x = 0; x < columnCount; x++) {
					levels[y][x] = this.levels[y][x];
				}
			}
			sequence.setLevels(levels);
			
			updating = true;
			presetPopup.setSelectedIndex(-1);
			int duration = durationModel.getNumber().intValue();
			for (int i = 0; i < presetPatterns.length; i++) {
				if (presetPatterns[i].matches(duration, levels)) {
					presetPopup.setSelectedIndex(i);
				}
			}
			updating = false;
		}
	}
}
