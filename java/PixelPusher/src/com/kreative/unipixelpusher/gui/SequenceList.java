package com.kreative.unipixelpusher.gui;

import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import com.kreative.unipixelpusher.SequenceLoader;
import com.kreative.unipixelpusher.SequenceLoader.SequenceInfo;

public class SequenceList extends JList {
	private static final long serialVersionUID = 1L;
	
	public SequenceList(SequenceLoader loader) {
		super(loader.getSequences().toArray());
		this.setCellRenderer(new Renderer());
	}
	
	public SequenceInfo getSelectedSequenceInfo() {
		return (SequenceInfo)getSelectedValue();
	}
	
	private class Renderer extends DefaultListCellRenderer {
		private static final long serialVersionUID = 1L;
		@Override
		public Component getListCellRendererComponent(JList c, Object v, int i, boolean s, boolean f) {
			SequenceInfo si = (SequenceInfo)v;
			JLabel l = (JLabel)super.getListCellRendererComponent(c, si.getSequenceName(), i, s, f);
			l.setIcon(new ImageIcon(Icons.getIcon(si.getSequenceClass(), 16)));
			l.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
			return l;
		}
	}
}
