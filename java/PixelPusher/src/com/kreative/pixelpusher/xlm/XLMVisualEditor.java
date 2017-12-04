package com.kreative.pixelpusher.xlm;

import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import com.kreative.pixelpusher.sequence.PixelSequenceVisualEditor;

public class XLMVisualEditor extends PixelSequenceVisualEditor<XLMSequence> {
	private static final long serialVersionUID = 1L;
	
	public XLMVisualEditor() {
		this(new XLMSequence());
	}
	
	public XLMVisualEditor(XLMSequence sequence) {
		super(sequence, new XLMVisualizer(sequence), new XLMEditor(sequence));
		JPanel main = new JPanel(new BorderLayout(32,32));
		main.add(this.visualizer, BorderLayout.CENTER);
		main.add(this.editor, BorderLayout.LINE_END);
		main.setBorder(BorderFactory.createEmptyBorder(32, 32, 32, 32));
		setLayout(new BorderLayout());
		add(main, BorderLayout.CENTER);
	}
}
