package com.kreative.unipixelpusher.gui;

import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;

public class EnumPopupMenu extends JComboBox {
	private static final long serialVersionUID = 1L;
	
	public <E extends Enum<E>> EnumPopupMenu(Class<E> enumType, E[] values) {
		super(values);
		this.setEditable(false);
		this.setMaximumRowCount(Math.max(values.length / 2, 16));
		this.setSelectedIndex(0);
		this.setRenderer(new Renderer<E>(enumType));
	}
	
	private class Renderer<E extends Enum<E>> extends DefaultListCellRenderer {
		private static final long serialVersionUID = 1L;
		private final Class<E> enumType;
		private Renderer(Class<E> enumType) {
			this.enumType = enumType;
		}
		@Override
		public Component getListCellRendererComponent(JList c, Object v, int i, boolean s, boolean f) {
			JLabel l = (JLabel)super.getListCellRendererComponent(c, v, i, s, f);
			l.setIcon(new ImageIcon(Icons.getIcon(enumType, enumType.cast(v), 16)));
			l.setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));
			return l;
		}
	}
}
