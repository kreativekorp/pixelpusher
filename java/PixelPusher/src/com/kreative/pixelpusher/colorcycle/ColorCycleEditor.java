package com.kreative.pixelpusher.colorcycle;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import com.kreative.pixelpusher.common.ColorPopupMenu;
import com.kreative.pixelpusher.sequence.PixelSequenceEditor;

public class ColorCycleEditor<T extends ColorCycleSequence> extends PixelSequenceEditor<T> {
	private static final long serialVersionUID = 1L;
	
	public ColorCycleEditor(T sequence) {
		super(sequence);
		setLayout(new BorderLayout());
		add(makeMainPanel(), BorderLayout.CENTER);
		fixHeight(this);
	}
	
	@Override
	public void setPixelSet(T sequence) {
		super.setPixelSet(sequence);
		removeAll();
		add(makeMainPanel(), BorderLayout.CENTER);
		revalidate();
	}
	
	private SpinnerNumberModel speedModel;
	private JSpinner speedSpinner;
	private DefaultListModel colorModel;
	private JList colorList;
	private JScrollPane colorPane;
	private ColorPopupMenu colorColor;
	private JButton colorAdd;
	private JButton colorRemove;
	private JButton colorLeft;
	private JButton colorRight;
	private boolean updating;
	
	private JPanel makeMainPanel() {
		JLabel speedLabel1 = new JLabel("Speed:");
		JLabel speedLabel2 = new JLabel("ms");
		speedModel = new SpinnerNumberModel(pixelSet.getMsPerFrame(), 20, 2000, 10);
		speedSpinner = new JSpinner(speedModel);
		JPanel speedPanel = new JPanel(new BorderLayout(4,4));
		speedPanel.add(speedLabel1, BorderLayout.LINE_START);
		speedPanel.add(speedSpinner, BorderLayout.CENTER);
		speedPanel.add(speedLabel2, BorderLayout.LINE_END);
		
		JLabel colorLabel = new JLabel("Colors:");
		colorModel = new DefaultListModel();
		while (colorModel.size() < pixelSet.getColorCount()) {
			colorModel.addElement(colorModel.size());
		}
		colorList = new JList(colorModel);
		colorList.setLayoutOrientation(JList.VERTICAL_WRAP);
		colorList.setVisibleRowCount(1);
		colorList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		colorList.setSelectedIndex(0);
		colorList.setCellRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 1L;
			@Override
			public Component getListCellRendererComponent(
				JList list, Object value, int index,
				boolean selected, boolean focused
			) {
				JLabel l = (JLabel)super.getListCellRendererComponent(list, null, index, selected, focused);
				BufferedImage i = new BufferedImage(20, 20, BufferedImage.TYPE_INT_ARGB);
				Graphics2D g = i.createGraphics();
				g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g.setColor(new Color(pixelSet.getColor(index), true));
				g.fillOval(4, 4, 12, 12);
				g.dispose();
				l.setIcon(new ImageIcon(i));
				return l;
			}
		});
		colorPane = new JScrollPane(colorList, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		colorColor = new ColorPopupMenu();
		colorColor.setSelectedItem(pixelSet.getColor(0));
		colorAdd = squareOff(new JButton("+"), "Add Color");
		colorRemove = squareOff(new JButton("\u2212"), "Remove Color");
		colorLeft = squareOff(new JButton("\u25C0"), "Move Left");
		colorRight = squareOff(new JButton("\u25B6"), "Move Right");
		JPanel colorButtonPanel = new JPanel(new GridLayout(1,0,4,4));
		colorButtonPanel.add(colorAdd);
		colorButtonPanel.add(colorRemove);
		colorButtonPanel.add(colorLeft);
		colorButtonPanel.add(colorRight);
		JPanel colorEditPanel = new JPanel(new BorderLayout(4,4));
		colorEditPanel.add(colorColor, BorderLayout.CENTER);
		colorEditPanel.add(colorButtonPanel, BorderLayout.LINE_END);
		JPanel colorPanel = new JPanel(new BorderLayout(4,4));
		colorPanel.add(colorLabel, BorderLayout.LINE_START);
		colorPanel.add(colorPane, BorderLayout.CENTER);
		colorPanel.add(valignMiddle(colorEditPanel), BorderLayout.LINE_END);
		
		JPanel main = new JPanel(new BorderLayout(8,8));
		main.add(valignMiddle(speedPanel), BorderLayout.LINE_START);
		main.add(colorPanel, BorderLayout.CENTER);
		updating = false;
		
		speedModel.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (!updating) {
					updating = true;
					pixelSet.setMsPerFrame(speedModel.getNumber().intValue());
					updating = false;
				}
			}
		});
		
		colorList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!updating) {
					updating = true;
					if (colorList.getSelectedIndex() >= 0) {
						int i = colorList.getSelectedIndex();
						colorColor.setSelectedItem(pixelSet.getColor(i));
					}
					updating = false;
				}
			}
		});
		colorColor.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (!updating) {
					updating = true;
					if (colorList.getSelectedIndex() >= 0 && colorColor.getSelectedItem() != null) {
						int i = colorList.getSelectedIndex();
						int color = (Integer)colorColor.getSelectedItem();
						pixelSet.setColor(i, color);
						colorModel.set(i, i);
					}
					updating = false;
				}
			}
		});
		colorAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!updating) {
					updating = true;
					if (colorList.getSelectedIndex() >= 0) {
						int i = colorList.getSelectedIndex();
						int n = pixelSet.getColorCount();
						pixelSet.setColorCount(n + 1);
						for (int j = n; j > i; j--) {
							pixelSet.setColor(j, pixelSet.getColor(j - 1));
						}
						colorModel.addElement(colorModel.size());
						colorList.setSelectedIndex(i + 1);
					}
					updating = false;
				}
			}
		});
		colorRemove.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!updating) {
					updating = true;
					if (colorList.getSelectedIndex() >= 0 && pixelSet.getColorCount() > 1) {
						int i = colorList.getSelectedIndex();
						int n = pixelSet.getColorCount() - 1;
						for (int j = i; j < n; j++) {
							pixelSet.setColor(j, pixelSet.getColor(j + 1));
						}
						pixelSet.setColorCount(n);
						colorModel.remove(n);
						i--; if (i < 0) i = 0;
						colorList.setSelectedIndex(i);
						colorColor.setSelectedItem(pixelSet.getColor(i));
					}
					updating = false;
				}
			}
		});
		colorLeft.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!updating) {
					updating = true;
					if (colorList.getSelectedIndex() > 0) {
						int i = colorList.getSelectedIndex();
						int c1 = pixelSet.getColor(i - 1);
						int c2 = pixelSet.getColor(i);
						pixelSet.setColor(i - 1, c2);
						pixelSet.setColor(i, c1);
						colorModel.set(0, 0);
						colorList.setSelectedIndex(i - 1);
					}
					updating = false;
				}
			}
		});
		colorRight.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!updating) {
					updating = true;
					if (colorList.getSelectedIndex() >= 0 && colorList.getSelectedIndex() < pixelSet.getColorCount() - 1) {
						int i = colorList.getSelectedIndex();
						int c1 = pixelSet.getColor(i);
						int c2 = pixelSet.getColor(i + 1);
						pixelSet.setColor(i, c2);
						pixelSet.setColor(i + 1, c1);
						colorModel.set(0, 0);
						colorList.setSelectedIndex(i + 1);
					}
					updating = false;
				}
			}
		});
		
		return main;
	}
	
	private static <C extends JComponent> C squareOff(C c, String tooltip) {
		c.putClientProperty("JButton.buttonType", "bevel");
		c.setToolTipText(tooltip);
		return c;
	}
	
	private static <C extends Component> C fixHeight(C c) {
		int h = c.getPreferredSize().height;
		c.setMinimumSize(new Dimension(c.getMinimumSize().width, h));
		c.setPreferredSize(new Dimension(c.getPreferredSize().width, h));
		c.setMaximumSize(new Dimension(c.getMaximumSize().width, h));
		return c;
	}
	
	private static JPanel valignMiddle(Component c) {
		fixHeight(c);
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.PAGE_AXIS));
		p.add(Box.createVerticalGlue());
		p.add(c);
		p.add(Box.createVerticalGlue());
		return p;
	}
}
