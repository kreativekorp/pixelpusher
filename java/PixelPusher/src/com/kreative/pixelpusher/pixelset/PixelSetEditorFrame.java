package com.kreative.pixelpusher.pixelset;

import java.awt.Dimension;
import javax.swing.JFrame;

public class PixelSetEditorFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	
	public PixelSetEditorFrame(String title, PixelSetEditor<? extends PixelSet> editor) {
		super(title);
		setContentPane(editor);
		pack();
		
		int wx = getWidth() - editor.getWidth();
		int hx = getHeight() - editor.getHeight();
		Dimension d;
		d = editor.getMinimumSize();
		d.width += wx;
		d.height += hx;
		setMinimumSize(d);
		d = editor.getPreferredSize();
		d.width += wx;
		d.height += hx;
		setPreferredSize(d);
		d = editor.getMaximumSize();
		d.width += wx;
		if (d.width < 0) d.width = Integer.MAX_VALUE;
		d.height += hx;
		if (d.height < 0) d.height = Integer.MAX_VALUE;
		setMaximumSize(d);
	}
}
