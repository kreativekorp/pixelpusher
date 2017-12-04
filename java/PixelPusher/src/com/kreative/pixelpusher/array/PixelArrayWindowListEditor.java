package com.kreative.pixelpusher.array;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import com.kreative.pixelpusher.pixelset.PixelSet;
import com.kreative.pixelpusher.pixelset.PixelSetInfo;
import com.kreative.pixelpusher.pixelset.PixelSetInfoSet;

public class PixelArrayWindowListEditor extends PixelArrayEditor<PixelArrayWindowList> {
	private static final long serialVersionUID = 1L;
	
	private final PixelSetInfoSet infoSet;
	
	public PixelArrayWindowListEditor(PixelSetInfoSet infoSet, PixelArrayWindowList array) {
		super(array);
		this.infoSet = infoSet;
		setLayout(new BorderLayout());
		add(makeMainPanel(), BorderLayout.CENTER);
	}
	
	@Override
	public void setPixelSet(PixelArrayWindowList array) {
		super.setPixelSet(array);
		removeAll();
		add(makeMainPanel(), BorderLayout.CENTER);
		revalidate();
	}
	
	private DefaultListModel windowListModel;
	private JList windowList;
	private boolean updating;
	
	private JPanel makeMainPanel() {
		windowListModel = new DefaultListModel();
		windowList = new JList(windowListModel);
		windowList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		final JScrollPane windowListPane = new JScrollPane(windowList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		final JButton addButton = squareOff(new JButton("+"), "Add New Window");
		final JButton removeButton = squareOff(new JButton("\u2212"), "Remove Window");
		final JButton upButton = squareOff(new JButton("\u25B2"), "Move Up");
		final JButton downButton = squareOff(new JButton("\u25BC"), "Move Down");
		final JPanel buttonPanel = new JPanel(new GridLayout(1,0,2,2));
		buttonPanel.add(addButton);
		buttonPanel.add(removeButton);
		buttonPanel.add(upButton);
		buttonPanel.add(downButton);
		final JPanel sidePanel = new JPanel(new BorderLayout(4,4));
		sidePanel.add(windowListPane, BorderLayout.CENTER);
		sidePanel.add(boxWrap(buttonPanel), BorderLayout.PAGE_END);
		final JPanel container = new JPanel(new BorderLayout());
		container.add(PixelArrayWindowEditor.nullEditor(infoSet));
		final JPanel mainPanel = new JPanel(new BorderLayout(16,16));
		mainPanel.add(fixWidth(sidePanel, buttonPanel.getPreferredSize().width), BorderLayout.LINE_START);
		mainPanel.add(container, BorderLayout.CENTER);
		
		updateListContents(-1);
		updating = false;
		windowList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (updating) return;
				updating = true;
				container.removeAll();
				int i = windowList.getSelectedIndex();
				if (i < 0) {
					container.add(PixelArrayWindowEditor.nullEditor(infoSet));
				} else {
					container.add(prepEditor(new PixelArrayWindowEditor(infoSet, pixelSet.get(i))));
				}
				updateListContents(i);
				revalidate();
				updating = false;
			}
		});
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (updating) return;
				updating = true;
				int i = windowList.getSelectedIndex() + 1;
				if (i < 1) i = pixelSet.size();
				pixelSet.add(i, new PixelArrayWindow());
				container.removeAll();
				container.add(prepEditor(new PixelArrayWindowEditor(infoSet, pixelSet.get(i))));
				updateListContents(i);
				revalidate();
				updating = false;
			}
		});
		removeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (updating) return;
				updating = true;
				int i = windowList.getSelectedIndex();
				if (i >= 0) {
					pixelSet.remove(i);
					if (i > 0) i--;
					container.removeAll();
					if (i < pixelSet.size()) {
						container.add(prepEditor(new PixelArrayWindowEditor(infoSet, pixelSet.get(i))));
					} else {
						container.add(PixelArrayWindowEditor.nullEditor(infoSet));
					}
					updateListContents(i);
					revalidate();
				}
				updating = false;
			}
		});
		upButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (updating) return;
				updating = true;
				int i = windowList.getSelectedIndex();
				if (i > 0) {
					PixelArrayWindow w = pixelSet.remove(i);
					i--;
					pixelSet.add(i, w);
					updateListContents(i);
				}
				updating = false;
			}
		});
		downButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (updating) return;
				updating = true;
				int i = windowList.getSelectedIndex();
				if (i+1 < pixelSet.size()) {
					PixelArrayWindow w = pixelSet.remove(i);
					i++;
					pixelSet.add(i, w);
					updateListContents(i);
				}
				updating = false;
			}
		});
		
		return mainPanel;
	}
	
	private void updateListContents(int selectedIndex) {
		windowListModel.removeAllElements();
		for (PixelArrayWindow window : pixelSet) {
			windowListModel.addElement(describePixelArray(window.getArray()));
		}
		if (selectedIndex < 0 || selectedIndex >= pixelSet.size()) {
			windowList.clearSelection();
		} else {
			windowList.setSelectedIndex(selectedIndex);
		}
	}
	
	private String describePixelArray(PixelArray array) {
		if (array == null) {
			return "< null >";
		} else {
			PixelSetInfo<? extends PixelSet> info = infoSet.getPixelSetInfo(array);
			if (info != null) {
				return info.getName();
			} else {
				return array.getClass().getSimpleName();
			}
		}
	}
	
	private PixelArrayWindowEditor prepEditor(final PixelArrayWindowEditor editor) {
		editor.getArrayPopup().addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				int i = windowList.getSelectedIndex();
				if (i >= 0) {
					windowListModel.set(i, describePixelArray(editor.getArrayPopup().getSelectedPixelSet()));
				}
			}
		});
		return editor;
	}
	
	private static <C extends JComponent> C squareOff(C c, String tooltip) {
		c.putClientProperty("JButton.buttonType", "bevel");
		c.setToolTipText(tooltip);
		return c;
	}
	
	private static JPanel boxWrap(Component c) {
		JPanel wrapper = new JPanel(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.VERTICAL;
		constraints.anchor = GridBagConstraints.CENTER;
		wrapper.add(c, constraints);
		return wrapper;
	}
	
	private static <C extends Component> C fixWidth(C c, int w) {
		c.setMinimumSize(new Dimension(w, c.getMinimumSize().height));
		c.setPreferredSize(new Dimension(w, c.getPreferredSize().height));
		c.setMaximumSize(new Dimension(w, c.getMaximumSize().height));
		return c;
	}
}
