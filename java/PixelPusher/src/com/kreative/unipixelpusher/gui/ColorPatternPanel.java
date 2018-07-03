package com.kreative.unipixelpusher.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import com.kreative.unipixelpusher.PixelSequence;
import com.kreative.unipixelpusher.mmxl.MMXLColorPattern;
import com.kreative.unipixelpusher.mmxl.MMXLParser;

public class ColorPatternPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private final PixelSequence.ColorPattern sequence;
	private final String[] presetNames;
	private final MMXLColorPattern[] presetColors;
	private final JComboBox presetPopup;
	private final DefaultListModel colorModel;
	private final JList colorList;
	private final ColorPopupMenu colorPopup;
	private final JButton addButton;
	private final JButton removeButton;
	private final JButton leftButton;
	private final JButton rightButton;
	private boolean updating;
	
	public ColorPatternPanel(MMXLParser mmxl, ColorListCellRenderer lcr, PixelSequence.ColorPattern seq) {
		this.sequence = seq;
		this.presetNames = mmxl.getColorPatternNames();
		this.presetColors = new MMXLColorPattern[presetNames.length];
		this.presetPopup = new JComboBox(presetNames);
		this.presetPopup.setEditable(false);
		this.presetPopup.setMaximumRowCount(Math.max(presetNames.length / 2, 16));
		this.presetPopup.setSelectedIndex(-1);
		int[] colors = seq.getColorPattern();
		for (int i = 0; i < presetNames.length; i++) {
			presetColors[i] = mmxl.getColorPattern(presetNames[i]);
			if (presetColors[i].matches(colors)) {
				presetPopup.setSelectedIndex(i);
			}
		}
		this.colorModel = new DefaultListModel();
		for (int c : colors) colorModel.addElement(c);
		this.colorList = new JList(colorModel);
		this.colorList.setLayoutOrientation(JList.VERTICAL_WRAP);
		this.colorList.setVisibleRowCount(1);
		this.colorList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.colorList.setSelectedIndex(0);
		this.colorList.setCellRenderer(lcr);
		JScrollPane colorPane = new JScrollPane(
			colorList,
			JScrollPane.VERTICAL_SCROLLBAR_NEVER,
			JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS
		);
		this.colorPopup = new ColorPopupMenu();
		this.colorPopup.setSelectedItem(colors[0]);
		this.addButton = makeButton(ADD, "+", "Add Color");
		this.removeButton = makeButton(REMOVE, "\u2212", "Remove Color");
		this.leftButton = makeButton(LEFT, "\u25C0", "Move Left");
		this.rightButton = makeButton(RIGHT, "\u25B6", "Move Right");
		
		JPanel presetPanel = new JPanel(new BorderLayout(4, 4));
		presetPanel.add(new JLabel("Preset:"), BorderLayout.LINE_START);
		presetPanel.add(presetPopup, BorderLayout.CENTER);
		JPanel colorPanel = new JPanel(new BorderLayout(4, 4));
		colorPanel.add(new JLabel("Color:"), BorderLayout.LINE_START);
		colorPanel.add(colorPopup, BorderLayout.CENTER);
		JPanel buttonPanel = new JPanel(new GridLayout(1, 0, 4, 4));
		buttonPanel.add(addButton);
		buttonPanel.add(removeButton);
		buttonPanel.add(leftButton);
		buttonPanel.add(rightButton);
		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.add(presetPanel, BorderLayout.LINE_START);
		JPanel bottomPanel = new JPanel(new BorderLayout(4, 4));
		bottomPanel.add(colorPanel, BorderLayout.LINE_START);
		bottomPanel.add(buttonPanel, BorderLayout.LINE_END);
		setLayout(new BorderLayout(8, 8));
		add(topPanel, BorderLayout.PAGE_START);
		add(colorPane, BorderLayout.CENTER);
		add(bottomPanel, BorderLayout.PAGE_END);
		
		updating = false;
		presetPopup.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (updating) return;
				updating = true;
				int i = presetPopup.getSelectedIndex();
				if (i >= 0) {
					presetColors[i].apply(sequence);
					int[] colors = sequence.getColorPattern();
					colorModel.clear();
					for (int c : colors) colorModel.addElement(c);
					colorList.setSelectedIndex(0);
					colorPopup.setSelectedItem(colors[0]);
				}
				updating = false;
			}
		});
		colorList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (updating) return;
				updating = true;
				int i = colorList.getSelectedIndex();
				if (i >= 0) {
					Object o = colorModel.get(i);
					colorPopup.setSelectedItem(o);
				}
				updating = false;
			}
		});
		colorPopup.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (updating) return;
				updating = true;
				int i = colorList.getSelectedIndex();
				Object o = colorPopup.getSelectedItem();
				if (i >= 0 && o instanceof Number) {
					colorModel.set(i, o);
					pushColors();
				}
				updating = false;
			}
		});
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (updating) return;
				updating = true;
				int i = colorList.getSelectedIndex();
				if (i >= 0) {
					colorModel.add(i, colorModel.get(i));
					colorList.setSelectedIndex(i + 1);
					pushColors();
				}
				updating = false;
			}
		});
		removeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (updating) return;
				updating = true;
				int i = colorList.getSelectedIndex();
				int n = colorModel.size();
				if (n > 1 && i >= 0) {
					colorModel.remove(i);
					if (i > 0) i--;
					colorList.setSelectedIndex(i);
					colorPopup.setSelectedItem(colorModel.get(i));
					pushColors();
				}
				updating = false;
			}
		});
		leftButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (updating) return;
				updating = true;
				int i = colorList.getSelectedIndex();
				int n = colorModel.size();
				if (n > 1 && i > 0) {
					colorModel.add(i - 1, colorModel.remove(i));
					colorList.setSelectedIndex(i - 1);
					pushColors();
				}
				updating = false;
			}
		});
		rightButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (updating) return;
				updating = true;
				int i = colorList.getSelectedIndex();
				int n = colorModel.size();
				if (n > 1 && i < (n - 1)) {
					colorModel.add(i + 1, colorModel.remove(i));
					colorList.setSelectedIndex(i + 1);
					pushColors();
				}
				updating = false;
			}
		});
	}
	
	private void pushColors() {
		Object[] objects = colorModel.toArray();
		int[] colors = new int[objects.length];
		for (int i = 0; i < objects.length; i++) {
			colors[i] = ((Number)objects[i]).intValue();
		}
		presetPopup.setSelectedIndex(-1);
		for (int i = 0; i < presetColors.length; i++) {
			if (presetColors[i].matches(colors)) {
				presetPopup.setSelectedIndex(i);
			}
		}
		sequence.setColorPattern(colors);
	}
	
	private static final Image ADD;
	private static final Image REMOVE;
	private static final Image LEFT;
	private static final Image RIGHT;
	static {
		Image add;
		Image remove;
		Image left;
		Image right;
		try {
			add = ImageIO.read(ColorPatternPanel.class.getResource("list-add.png"));
			remove = ImageIO.read(ColorPatternPanel.class.getResource("list-remove.png"));
			left = ImageIO.read(ColorPatternPanel.class.getResource("go-previous.png"));
			right = ImageIO.read(ColorPatternPanel.class.getResource("go-next.png"));
		} catch (IOException e) {
			add = null;
			remove = null;
			left = null;
			right = null;
		}
		ADD = add;
		REMOVE = remove;
		LEFT = left;
		RIGHT = right;
	}
	
	private static JButton makeButton(Image image, String altText, String titleText) {
		JButton button = (image != null)
		               ? new JButton(new ImageIcon(image))
		               : new JButton(altText);
		button.setToolTipText(titleText);
		button.putClientProperty("JButton.buttonType", "bevel");
		return button;
	}
}
