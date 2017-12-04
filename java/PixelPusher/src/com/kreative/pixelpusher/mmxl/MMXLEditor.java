package com.kreative.pixelpusher.mmxl;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import com.kreative.pixelpusher.common.ColorPopupMenu;
import com.kreative.pixelpusher.sequence.PixelSequenceEditor;

public class MMXLEditor extends PixelSequenceEditor<MMXLSequence> {
	private static final long serialVersionUID = 1L;
	
	private MMXLParser presets;
	
	public MMXLEditor() {
		this(new MMXLSequence());
	}
	
	public MMXLEditor(MMXLSequence sequence) {
		super(sequence);
		presets = new MMXLParser();
		presets.parse();
		setLayout(new BorderLayout());
		add(makeMainPanel(), BorderLayout.CENTER);
	}
	
	@Override
	public void setPixelSet(MMXLSequence sequence) {
		super.setPixelSet(sequence);
		removeAll();
		add(makeMainPanel(), BorderLayout.CENTER);
		revalidate();
	}
	
	private JComboBox colorPatternPreset;
	private JRadioButton colorPatternRepeating;
	private JRadioButton colorPatternRandom;
	private DefaultListModel colorPatternModel;
	private JList colorPatternList;
	private JScrollPane colorPatternPane;
	private ColorPopupMenu colorPatternColor;
	private SpinnerNumberModel colorProbModel;
	private JSpinner colorProbSpinner;
	private JLabel colorProbTotal;
	private JButton colorPatternAdd;
	private JButton colorPatternRemove;
	private JButton colorPatternLeft;
	private JButton colorPatternRight;
	private JComboBox blinkPatternPreset;
	private JSlider blinkPatternSpeed;
	private JLabel blinkPatternSpeedLabel;
	private SpinnerNumberModel blinkFramesModel;
	private JSpinner blinkFramesSpinner;
	private SpinnerNumberModel blinkStrandsModel;
	private JSpinner blinkStrandsSpinner;
	private AbstractTableModel blinkPatternModel;
	private JTable blinkPatternTable;
	private JScrollPane blinkPatternPane;
	private boolean updating;
	
	private JPanel makeMainPanel() {
		JLabel colorPatternLabel = new JLabel("Color Pattern:");
		JLabel colorPatternPresetLabel = new JLabel("Preset:");
		colorPatternPreset = new JComboBox(presets.getColorPatternNames());
		colorPatternPreset.setEditable(false);
		colorPatternPreset.setMaximumRowCount(colorPatternPreset.getItemCount());
		colorPatternPreset.setSelectedItem(null);
		int[] colorsToMatch = pixelSet.getPatternColors();
		int[] probsToMatch = pixelSet.getPatternProbabilities();
		boolean randomToMatch = pixelSet.isColorPatternRandom();
		for (String name : presets.getColorPatternNames()) {
			MMXLColorPattern pattern = presets.getColorPattern(name);
			if (pattern.matches(colorsToMatch, probsToMatch, randomToMatch)) {
				colorPatternPreset.setSelectedItem(name);
				break;
			}
		}
		colorPatternRepeating = new JRadioButton("Repeating");
		colorPatternRepeating.setSelected(!pixelSet.isColorPatternRandom());
		colorPatternRandom = new JRadioButton("Random");
		colorPatternRandom.setSelected(pixelSet.isColorPatternRandom());
		ButtonGroup colorPatternRandomGroup = new ButtonGroup();
		colorPatternRandomGroup.add(colorPatternRepeating);
		colorPatternRandomGroup.add(colorPatternRandom);
		colorPatternModel = new DefaultListModel();
		while (colorPatternModel.size() < pixelSet.getColorPatternLength()) {
			colorPatternModel.addElement(colorPatternModel.size());
		}
		colorPatternList = new JList(colorPatternModel);
		colorPatternList.setLayoutOrientation(JList.VERTICAL_WRAP);
		colorPatternList.setVisibleRowCount(1);
		colorPatternList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		colorPatternList.setSelectedIndex(0);
		colorPatternList.setCellRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 1L;
			@Override
			public Component getListCellRendererComponent(
				JList list, Object value, int index,
				boolean selected, boolean focused
			) {
				JLabel l = (JLabel)super.getListCellRendererComponent(list, null, index, selected, focused);
				BufferedImage i = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
				Graphics g = i.createGraphics();
				int c = pixelSet.getColorPatternColor(index);
				MMXLUtilities.drawBulb(g, 0, 0, SwingConstants.SOUTH, c);
				g.dispose();
				l.setIcon(new ImageIcon(i));
				return l;
			}
		});
		colorPatternPane = new JScrollPane(colorPatternList, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		JLabel colorPatternColorLabel = new JLabel("Color:");
		colorPatternColor = new ColorPopupMenu();
		colorPatternColor.setSelectedItem(pixelSet.getColorPatternColor(0));
		JLabel colorProbLabel = new JLabel("Probability:");
		colorProbModel = new SpinnerNumberModel(pixelSet.getColorPatternProbability(0), 1, 250, 1);
		colorProbSpinner = new JSpinner(colorProbModel);
		JLabel colorProbLabel2 = new JLabel(" in ");
		colorProbTotal = new JLabel(Integer.toString(pixelSet.getColorPatternProbabilityTotal()));
		colorPatternAdd = squareOff(new JButton("+"), "Add Color");
		colorPatternRemove = squareOff(new JButton("\u2212"), "Remove Color");
		colorPatternLeft = squareOff(new JButton("\u25C0"), "Move Left");
		colorPatternRight = squareOff(new JButton("\u25B6"), "Move Right");
		
		JLabel blinkPatternLabel = new JLabel("Blink Pattern:");
		JLabel blinkPatternPresetLabel = new JLabel("Preset:");
		blinkPatternPreset = new JComboBox(presets.getBlinkPatternNames());
		blinkPatternPreset.setEditable(false);
		blinkPatternPreset.setMaximumRowCount(blinkPatternPreset.getItemCount());
		blinkPatternPreset.setSelectedItem(null);
		int[][] levelsToMatch = pixelSet.getBlinkPatternLevels();
		int mspfToMatch = pixelSet.getMsPerFrame();
		for (String name : presets.getBlinkPatternNames()) {
			MMXLBlinkPattern pattern = presets.getBlinkPattern(name);
			if (pattern.matches(levelsToMatch, mspfToMatch)) {
				blinkPatternPreset.setSelectedItem(name);
				break;
			}
		}
		JLabel blinkPatternSpeedLabel2 = new JLabel("Speed:");
		blinkPatternSpeed = new JSlider(2, 200, pixelSet.getMsPerFrame() / 10);
		blinkPatternSpeedLabel = new JLabel(Integer.toString(pixelSet.getMsPerFrame()));
		JLabel blinkPatternSpeedLabel3 = new JLabel(" ms");
		JLabel blinkFramesLabel = new JLabel("Frames:");
		blinkFramesModel = new SpinnerNumberModel(pixelSet.getBlinkPatternFrames(), 1, 250, 1);
		blinkFramesSpinner = new JSpinner(blinkFramesModel);
		JLabel blinkStrandsLabel = new JLabel("Strands:");
		blinkStrandsModel = new SpinnerNumberModel(pixelSet.getBlinkPatternStrands(), 1, 250, 1);
		blinkStrandsSpinner = new JSpinner(blinkStrandsModel);
		blinkPatternModel = new AbstractTableModel() {
			private static final long serialVersionUID = 1L;
			@Override
			public int getRowCount() {
				return pixelSet.getBlinkPatternFrames();
			}
			@Override
			public int getColumnCount() {
				return pixelSet.getBlinkPatternStrands();
			}
			@Override
			public boolean isCellEditable(int row, int col) {
				return true;
			}
			@Override
			public Object getValueAt(int row, int col) {
				return pixelSet.getBlinkPatternLevel(row, col);
			}
			@Override
			public void setValueAt(Object value, int row, int col) {
				int i;
				try {
					i = Integer.parseInt(value.toString());
				} catch (NumberFormatException nfe) {
					i = 0;
				}
				pixelSet.setBlinkPatternLevel(row, col, i);
			}
		};
		blinkPatternTable = new JTable(blinkPatternModel);
		blinkPatternTable.setPreferredScrollableViewportSize(new Dimension(200, 100));
		blinkPatternPane = new JScrollPane(blinkPatternTable, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		updating = false;
		
		JPanel colorPatternPresetPanel = new JPanel(new BorderLayout(4,4));
		colorPatternPresetPanel.add(colorPatternPresetLabel, BorderLayout.LINE_START);
		colorPatternPresetPanel.add(colorPatternPreset, BorderLayout.CENTER);
		JPanel colorPatternRandomInnerPanel = new JPanel(new GridLayout(1,0,4,4));
		colorPatternRandomInnerPanel.add(colorPatternRepeating);
		colorPatternRandomInnerPanel.add(colorPatternRandom);
		JPanel colorPatternRandomPanel = new JPanel(new BorderLayout());
		colorPatternRandomPanel.add(colorPatternRandomInnerPanel, BorderLayout.LINE_START);
		JPanel colorPatternColorPanel = new JPanel(new BorderLayout(4,4));
		colorPatternColorPanel.add(colorPatternColorLabel, BorderLayout.LINE_START);
		colorPatternColorPanel.add(colorPatternColor, BorderLayout.CENTER);
		JPanel colorProbInnerPanel = new JPanel(new BorderLayout());
		colorProbInnerPanel.add(colorProbLabel2, BorderLayout.LINE_START);
		colorProbInnerPanel.add(colorProbTotal, BorderLayout.CENTER);
		JPanel colorProbPanel = new JPanel(new BorderLayout(4,4));
		colorProbPanel.add(colorProbLabel, BorderLayout.LINE_START);
		colorProbPanel.add(colorProbSpinner, BorderLayout.CENTER);
		colorProbPanel.add(colorProbInnerPanel, BorderLayout.LINE_END);
		JPanel colorButtonInnerPanel = new JPanel(new GridLayout(1,0,4,4));
		colorButtonInnerPanel.add(colorPatternAdd);
		colorButtonInnerPanel.add(colorPatternRemove);
		colorButtonInnerPanel.add(colorPatternLeft);
		colorButtonInnerPanel.add(colorPatternRight);
		JPanel colorButtonPanel = new JPanel(new BorderLayout());
		colorButtonPanel.add(colorButtonInnerPanel, BorderLayout.LINE_END);
		JPanel blinkPatternPresetPanel = new JPanel(new BorderLayout(4,4));
		blinkPatternPresetPanel.add(blinkPatternPresetLabel, BorderLayout.LINE_START);
		blinkPatternPresetPanel.add(blinkPatternPreset, BorderLayout.CENTER);
		JPanel blinkPatternSpeedInnerPanel = new JPanel(new BorderLayout());
		blinkPatternSpeedInnerPanel.add(blinkPatternSpeedLabel, BorderLayout.CENTER);
		blinkPatternSpeedInnerPanel.add(blinkPatternSpeedLabel3, BorderLayout.LINE_END);
		JPanel blinkPatternSpeedPanel = new JPanel(new BorderLayout(4,4));
		blinkPatternSpeedPanel.add(blinkPatternSpeedLabel2, BorderLayout.LINE_START);
		blinkPatternSpeedPanel.add(blinkPatternSpeed, BorderLayout.CENTER);
		blinkPatternSpeedPanel.add(blinkPatternSpeedInnerPanel, BorderLayout.LINE_END);
		JPanel blinkFSInnerPanel1 = new JPanel(new GridLayout(0,1,4,4));
		blinkFSInnerPanel1.add(blinkFramesLabel);
		blinkFSInnerPanel1.add(blinkStrandsLabel);
		JPanel blinkFSInnerPanel2 = new JPanel(new GridLayout(0,1,4,4));
		blinkFSInnerPanel2.add(blinkFramesSpinner);
		blinkFSInnerPanel2.add(blinkStrandsSpinner);
		JPanel blinkFSInnerPanel3 = new JPanel(new BorderLayout(4,4));
		blinkFSInnerPanel3.add(blinkFSInnerPanel1, BorderLayout.LINE_START);
		blinkFSInnerPanel3.add(blinkFSInnerPanel2, BorderLayout.CENTER);
		JPanel blinkFSPanel = new JPanel(new BorderLayout());
		blinkFSPanel.add(blinkFSInnerPanel3, BorderLayout.PAGE_START);
		
		JPanel colorPatternInnerPanel1 = new JPanel(new BorderLayout(8,8));
		colorPatternInnerPanel1.add(colorPatternPresetPanel, BorderLayout.LINE_START);
		colorPatternInnerPanel1.add(colorPatternRandomPanel, BorderLayout.CENTER);
		JPanel colorPatternInnerPanel2 = new JPanel(new BorderLayout(8,8));
		colorPatternInnerPanel2.add(colorPatternLabel, BorderLayout.PAGE_START);
		colorPatternInnerPanel2.add(colorPatternInnerPanel1, BorderLayout.CENTER);
		JPanel colorPatternInnerPanel3 = new JPanel(new BorderLayout(8,8));
		colorPatternInnerPanel3.add(colorPatternColorPanel, BorderLayout.LINE_START);
		colorPatternInnerPanel3.add(colorProbPanel, BorderLayout.CENTER);
		JPanel colorPatternInnerPanel4 = new JPanel(new BorderLayout(8,8));
		colorPatternInnerPanel4.add(colorPatternInnerPanel3, BorderLayout.LINE_START);
		colorPatternInnerPanel4.add(colorButtonPanel, BorderLayout.CENTER);
		JPanel colorPatternPanel = new JPanel(new BorderLayout(8,8));
		colorPatternPanel.add(colorPatternInnerPanel2, BorderLayout.PAGE_START);
		colorPatternPanel.add(colorPatternPane, BorderLayout.CENTER);
		colorPatternPanel.add(colorPatternInnerPanel4, BorderLayout.PAGE_END);
		JPanel blinkPatternInnerPanel1 = new JPanel(new BorderLayout(8,8));
		blinkPatternInnerPanel1.add(blinkPatternPresetPanel, BorderLayout.LINE_START);
		blinkPatternInnerPanel1.add(blinkPatternSpeedPanel, BorderLayout.CENTER);
		JPanel blinkPatternInnerPanel2 = new JPanel(new BorderLayout(8,8));
		blinkPatternInnerPanel2.add(blinkPatternLabel, BorderLayout.PAGE_START);
		blinkPatternInnerPanel2.add(blinkPatternInnerPanel1, BorderLayout.CENTER);
		JPanel blinkPatternInnerPanel3 = new JPanel(new BorderLayout(8,8));
		blinkPatternInnerPanel3.add(blinkFSPanel, BorderLayout.LINE_START);
		blinkPatternInnerPanel3.add(blinkPatternPane, BorderLayout.CENTER);
		JPanel blinkPatternPanel = new JPanel(new BorderLayout(8,8));
		blinkPatternPanel.add(blinkPatternInnerPanel2, BorderLayout.PAGE_START);
		blinkPatternPanel.add(blinkPatternInnerPanel3, BorderLayout.CENTER);
		
		JPanel mainInnerPanel = new JPanel(new BorderLayout(8,8));
		mainInnerPanel.add(colorPatternPanel, BorderLayout.PAGE_START);
		mainInnerPanel.add(new JSeparator(), BorderLayout.CENTER);
		JPanel mainPanel = new JPanel(new BorderLayout(8,8));
		mainPanel.add(mainInnerPanel, BorderLayout.PAGE_START);
		mainPanel.add(blinkPatternPanel, BorderLayout.CENTER);
		
		Dimension d1 = colorPatternPreset.getPreferredSize();
		Dimension d2 = blinkPatternPreset.getPreferredSize();
		Dimension d = new Dimension(Math.max(d1.width, d2.width), Math.max(d1.height, d2.height));
		colorPatternPreset.setMinimumSize(d);
		colorPatternPreset.setPreferredSize(d);
		blinkPatternPreset.setMinimumSize(d);
		blinkPatternPreset.setPreferredSize(d);
		
		colorPatternPreset.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (!updating) {
					updating = true;
					if (colorPatternPreset.getSelectedItem() != null) {
						presets.getColorPattern(colorPatternPreset.getSelectedItem().toString()).apply(pixelSet);
						colorPatternRepeating.setSelected(!pixelSet.isColorPatternRandom());
						colorPatternRandom.setSelected(pixelSet.isColorPatternRandom());
						colorPatternModel.clear();
						while (colorPatternModel.size() < pixelSet.getColorPatternLength()) {
							colorPatternModel.addElement(colorPatternModel.size());
						}
						colorPatternList.setSelectedIndex(0);
						colorPatternColor.setSelectedItem(pixelSet.getColorPatternColor(0));
						colorProbModel.setValue(pixelSet.getColorPatternProbability(0));
						colorProbTotal.setText(Integer.toString(pixelSet.getColorPatternProbabilityTotal()));
					}
					updating = false;
				}
			}
		});
		colorPatternRepeating.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!updating) {
					updating = true;
					pixelSet.setColorPatternRandom(false);
					updating = false;
				}
			}
		});
		colorPatternRandom.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!updating) {
					updating = true;
					pixelSet.setColorPatternRandom(true);
					updating = false;
				}
			}
		});
		colorPatternList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!updating) {
					updating = true;
					if (colorPatternList.getSelectedIndex() >= 0) {
						int i = colorPatternList.getSelectedIndex();
						colorPatternColor.setSelectedItem(pixelSet.getColorPatternColor(i));
						colorProbModel.setValue(pixelSet.getColorPatternProbability(i));
					}
					updating = false;
				}
			}
		});
		colorPatternColor.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (!updating) {
					updating = true;
					if (colorPatternList.getSelectedIndex() >= 0 && colorPatternColor.getSelectedItem() != null) {
						int i = colorPatternList.getSelectedIndex();
						int color = (Integer)colorPatternColor.getSelectedItem();
						pixelSet.setColorPatternColor(i, color);
					}
					updating = false;
				}
			}
		});
		colorProbModel.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (!updating) {
					updating = true;
					if (colorPatternList.getSelectedIndex() >= 0) {
						int i = colorPatternList.getSelectedIndex();
						int p = colorProbModel.getNumber().intValue();
						pixelSet.setColorPatternProbability(i, p);
						colorProbTotal.setText(Integer.toString(pixelSet.getColorPatternProbabilityTotal()));
					}
					updating = false;
				}
			}
		});
		colorPatternAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!updating) {
					updating = true;
					if (colorPatternList.getSelectedIndex() >= 0) {
						int i = colorPatternList.getSelectedIndex();
						int n = pixelSet.getColorPatternLength();
						pixelSet.setColorPatternLength(n + 1);
						for (int j = n; j > i; j--) {
							pixelSet.setColorPatternColor(j, pixelSet.getColorPatternColor(j - 1));
							pixelSet.setColorPatternProbability(j, pixelSet.getColorPatternProbability(j - 1));
						}
						colorPatternModel.addElement(colorPatternModel.size());
						colorPatternList.setSelectedIndex(i + 1);
						colorProbTotal.setText(Integer.toString(pixelSet.getColorPatternProbabilityTotal()));
					}
					updating = false;
				}
			}
		});
		colorPatternRemove.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!updating) {
					updating = true;
					if (colorPatternList.getSelectedIndex() >= 0 && pixelSet.getColorPatternLength() > 1) {
						int i = colorPatternList.getSelectedIndex();
						int n = pixelSet.getColorPatternLength() - 1;
						for (int j = i; j < n; j++) {
							pixelSet.setColorPatternColor(j, pixelSet.getColorPatternColor(j + 1));
							pixelSet.setColorPatternProbability(j, pixelSet.getColorPatternProbability(j + 1));
						}
						pixelSet.setColorPatternLength(n);
						colorPatternModel.remove(n);
						i--; if (i < 0) i = 0;
						colorPatternList.setSelectedIndex(i);
						colorPatternColor.setSelectedItem(pixelSet.getColorPatternColor(i));
						colorProbModel.setValue(pixelSet.getColorPatternProbability(i));
						colorProbTotal.setText(Integer.toString(pixelSet.getColorPatternProbabilityTotal()));
					}
					updating = false;
				}
			}
		});
		colorPatternLeft.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!updating) {
					updating = true;
					if (colorPatternList.getSelectedIndex() > 0) {
						int i = colorPatternList.getSelectedIndex();
						int c1 = pixelSet.getColorPatternColor(i - 1);
						int p1 = pixelSet.getColorPatternProbability(i - 1);
						int c2 = pixelSet.getColorPatternColor(i);
						int p2 = pixelSet.getColorPatternProbability(i);
						pixelSet.setColorPatternColor(i - 1, c2);
						pixelSet.setColorPatternProbability(i - 1, p2);
						pixelSet.setColorPatternColor(i, c1);
						pixelSet.setColorPatternProbability(i, p1);
						colorPatternModel.set(0, 0);
						colorPatternList.setSelectedIndex(i - 1);
					}
					updating = false;
				}
			}
		});
		colorPatternRight.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!updating) {
					updating = true;
					if (colorPatternList.getSelectedIndex() >= 0 && colorPatternList.getSelectedIndex() < pixelSet.getColorPatternLength() - 1) {
						int i = colorPatternList.getSelectedIndex();
						int c1 = pixelSet.getColorPatternColor(i);
						int p1 = pixelSet.getColorPatternProbability(i);
						int c2 = pixelSet.getColorPatternColor(i + 1);
						int p2 = pixelSet.getColorPatternProbability(i + 1);
						pixelSet.setColorPatternColor(i, c2);
						pixelSet.setColorPatternProbability(i, p2);
						pixelSet.setColorPatternColor(i + 1, c1);
						pixelSet.setColorPatternProbability(i + 1, p1);
						colorPatternModel.set(0, 0);
						colorPatternList.setSelectedIndex(i + 1);
					}
					updating = false;
				}
			}
		});
		
		blinkPatternPreset.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (!updating) {
					updating = true;
					if (blinkPatternPreset.getSelectedItem() != null) {
						presets.getBlinkPattern(blinkPatternPreset.getSelectedItem().toString()).apply(pixelSet);
						blinkPatternSpeed.setValue(pixelSet.getMsPerFrame() / 10);
						blinkPatternSpeedLabel.setText(Integer.toString(pixelSet.getMsPerFrame()));
						blinkFramesModel.setValue(pixelSet.getBlinkPatternFrames());
						blinkStrandsModel.setValue(pixelSet.getBlinkPatternStrands());
						blinkPatternModel.fireTableStructureChanged();
						blinkPatternModel.fireTableDataChanged();
					}
					updating = false;
				}
			}
		});
		blinkPatternSpeed.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (!updating) {
					updating = true;
					pixelSet.setMsPerFrame(blinkPatternSpeed.getValue() * 10);
					blinkPatternSpeedLabel.setText(Integer.toString(pixelSet.getMsPerFrame()));
					updating = false;
				}
			}
		});
		blinkFramesModel.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (!updating) {
					updating = true;
					pixelSet.setBlinkPatternFrames(blinkFramesModel.getNumber().intValue());
					blinkPatternModel.fireTableStructureChanged();
					blinkPatternModel.fireTableDataChanged();
					updating = false;
				}
			}
		});
		blinkStrandsModel.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (!updating) {
					updating = true;
					pixelSet.setBlinkPatternStrands(blinkStrandsModel.getNumber().intValue());
					blinkPatternModel.fireTableStructureChanged();
					blinkPatternModel.fireTableDataChanged();
					updating = false;
				}
			}
		});
		
		return mainPanel;
	}
	
	private static <C extends JComponent> C squareOff(C c, String tooltip) {
		c.putClientProperty("JButton.buttonType", "bevel");
		c.setToolTipText(tooltip);
		return c;
	}
}
