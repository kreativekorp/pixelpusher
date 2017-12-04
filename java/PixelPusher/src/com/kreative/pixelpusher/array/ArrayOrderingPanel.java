package com.kreative.pixelpusher.array;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import com.kreative.pixelpusher.resources.Resources;

public class ArrayOrderingPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private final List<ArrayOrderingButton> buttons;
	
	public ArrayOrderingPanel() {
		super(new GridLayout(2,0,4,4));
		buttons = new ArrayList<ArrayOrderingButton>();
		ButtonGroup group = new ButtonGroup();
		for (ArrayOrdering arrayOrdering : ArrayOrdering.values()) {
			ArrayOrderingButton button = new ArrayOrderingButton(arrayOrdering);
			group.add(button);
			buttons.add(button);
			this.add(button);
			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					for (ActionListener listener : getActionListeners()) {
						listener.actionPerformed(e);
					}
				}
			});
		}
	}
	
	public ActionListener[] getActionListeners() {
		return listenerList.getListeners(ActionListener.class);
	}
	
	public void addActionListener(ActionListener listener) {
		listenerList.add(ActionListener.class, listener);
	}
	
	public void removeActionListener(ActionListener listener) {
		listenerList.remove(ActionListener.class, listener);
	}
	
	public ArrayOrdering getSelectedValue() {
		for (ArrayOrderingButton button : buttons) {
			if (button.isSelected()) {
				return button.arrayOrdering;
			}
		}
		return null;
	}
	
	public void setSelectedValue(ArrayOrdering value) {
		for (ArrayOrderingButton button : buttons) {
			button.setSelected(button.arrayOrdering == value);
		}
	}
	
	private static class ArrayOrderingButton extends JToggleButton {
		private static final long serialVersionUID = 1L;
		private final ArrayOrdering arrayOrdering;
		private ArrayOrderingButton(ArrayOrdering arrayOrdering) {
			super(new ImageIcon(Resources.getImage("array_ordering", arrayOrdering.name())));
			this.setToolTipText(arrayOrdering.toString());
			this.arrayOrdering = arrayOrdering;
		}
	}
}
