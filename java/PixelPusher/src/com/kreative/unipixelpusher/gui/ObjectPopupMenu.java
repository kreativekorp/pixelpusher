package com.kreative.unipixelpusher.gui;

import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;

public class ObjectPopupMenu extends JComboBox {
	private static final long serialVersionUID = 1L;
	
	public ObjectPopupMenu(Object[] values) {
		super(values);
		this.setEditable(false);
		this.setMaximumRowCount(Math.max(values.length / 2, 16));
		this.setSelectedIndex(0);
		this.setRenderer(new Renderer());
	}
	
	private class Renderer extends DefaultListCellRenderer {
		private static final long serialVersionUID = 1L;
		@Override
		public Component getListCellRendererComponent(JList c, Object v, int i, boolean s, boolean f) {
			JLabel l = (JLabel)super.getListCellRendererComponent(c, v, i, s, f);
			l.setIcon(new ImageIcon(Icons.getIcon(v.getClass(), 16)));
			l.setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));
			return l;
		}
	}
}
