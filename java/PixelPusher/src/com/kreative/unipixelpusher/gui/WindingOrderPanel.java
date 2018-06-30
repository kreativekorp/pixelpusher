package com.kreative.unipixelpusher.gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import com.kreative.unipixelpusher.WindingOrder;

public class WindingOrderPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private final List<WindingOrderButton> buttons;
	
	public WindingOrderPanel() {
		super(new GridLayout(2, 0, 4, 4));
		buttons = new ArrayList<WindingOrderButton>();
		ButtonGroup group = new ButtonGroup();
		for (WindingOrder windingOrder : WindingOrder.values()) {
			WindingOrderButton button = new WindingOrderButton(windingOrder);
			group.add(button);
			buttons.add(button);
			this.add(button);
			button.addActionListener(new Listener());
		}
	}
	
	private class Listener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			for (ActionListener listener : getActionListeners()) {
				listener.actionPerformed(e);
			}
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
	
	public WindingOrder getSelectedValue() {
		for (WindingOrderButton button : buttons) {
			if (button.isSelected()) {
				return button.windingOrder;
			}
		}
		return null;
	}
	
	public void setSelectedValue(WindingOrder value) {
		for (WindingOrderButton button : buttons) {
			button.setSelected(button.windingOrder == value);
		}
	}
	
	private static class WindingOrderButton extends JToggleButton {
		private static final long serialVersionUID = 1L;
		private final WindingOrder windingOrder;
		private WindingOrderButton(WindingOrder windingOrder) {
			this.setIcon(new ImageIcon(Icons.getIcon(WindingOrder.class, windingOrder, 40)));
			this.setToolTipText(windingOrder.toString());
			this.windingOrder = windingOrder;
		}
	}
}
