package com.kreative.pixelpusher.mmxl;

import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.OverlayLayout;
import com.kreative.pixelpusher.sequence.PixelSequenceVisualEditor;

public class MMXLVisualEditor extends PixelSequenceVisualEditor<MMXLSequence> {
	private static final long serialVersionUID = 1L;
	
	public MMXLVisualEditor() {
		this(new MMXLSequence());
	}
	
	public MMXLVisualEditor(MMXLSequence sequence) {
		super(sequence, new MMXLVisualizer(sequence), new MMXLEditor(sequence));
		JPanel editorPanel = new JPanel(new BorderLayout());
		editorPanel.add(this.editor, BorderLayout.CENTER);
		editorPanel.setBorder(BorderFactory.createEmptyBorder(48,48,48,48));
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new OverlayLayout(mainPanel));
		mainPanel.add(this.visualizer);
		mainPanel.add(editorPanel);
		setLayout(new BorderLayout());
		add(mainPanel, BorderLayout.CENTER);
	}
}
