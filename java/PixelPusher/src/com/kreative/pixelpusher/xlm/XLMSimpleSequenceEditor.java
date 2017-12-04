package com.kreative.pixelpusher.xlm;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import com.kreative.pixelpusher.sequence.PixelSequenceEditor;

public class XLMSimpleSequenceEditor extends PixelSequenceEditor<XLMSequence> {
	private static final long serialVersionUID = 1L;
	
	public XLMSimpleSequenceEditor() {
		this(new XLMSequence());
	}
	
	public XLMSimpleSequenceEditor(XLMSequence sequence) {
		super(sequence);
		setLayout(new BorderLayout());
		add(makeMainPanel(), BorderLayout.CENTER);
	}
	
	@Override
	public void setPixelSet(XLMSequence sequence) {
		super.setPixelSet(sequence);
		removeAll();
		add(makeMainPanel(), BorderLayout.CENTER);
		revalidate();
	}
	
	private JPanel makeMainPanel() {
		JPanel labelPanel = new JPanel(new GridLayout(0,1,32,32));
		JPanel mainPanel = new JPanel(new GridLayout(0,1,32,32));
		for (int i = 0; i < pixelSet.getChannelCount(); i++) {
			labelPanel.add(makeBig(center(new JLabel("CH " + (i+1)))));
			mainPanel.add(new XLMSimpleChannelEditor(pixelSet.getChannel(i)));
		}
		JPanel main = new JPanel(new BorderLayout(32,32));
		main.add(labelPanel, BorderLayout.LINE_START);
		main.add(mainPanel, BorderLayout.CENTER);
		return main;
	}
	
	private static <C extends JComponent> C makeBig(C c) {
		c.setFont(c.getFont().deriveFont(18.0f));
		c.putClientProperty("JButton.buttonType", "bevel");
		return c;
	}
	
	private static JLabel center(JLabel l) {
		l.setHorizontalAlignment(JLabel.CENTER);
		l.setVerticalAlignment(JLabel.CENTER);
		return l;
	}
}
