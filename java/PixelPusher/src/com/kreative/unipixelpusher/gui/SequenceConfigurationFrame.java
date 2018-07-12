package com.kreative.unipixelpusher.gui;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import com.kreative.unipixelpusher.PixelSequence;

public class SequenceConfigurationFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private static final Map<PixelSequence,SequenceConfigurationFrame> windowMap =
	             new HashMap<PixelSequence,SequenceConfigurationFrame>();
	
	public static SequenceConfigurationFrame open(final PixelSequence sequence) {
		if (windowMap.containsKey(sequence)) {
			SequenceConfigurationFrame f = windowMap.get(sequence);
			f.setVisible(true);
			return f;
		} else if (sequence.hasConfigurationPanel()) {
			SequenceConfigurationFrame f = new SequenceConfigurationFrame(sequence);
			windowMap.put(sequence, f);
			f.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosed(WindowEvent e) {
					windowMap.remove(sequence);
				}
			});
			f.setVisible(true);
			return f;
		} else {
			JOptionPane.showMessageDialog(
				null, "This sequence has no configuration options.",
				sequence.toString(), JOptionPane.INFORMATION_MESSAGE
			);
			return null;
		}
	}
	
	public SequenceConfigurationFrame(PixelSequence sequence) {
		super(sequence.toString());
		JPanel content = new JPanel(new BorderLayout());
		content.add(sequence.createConfigurationPanel(), BorderLayout.CENTER);
		content.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		setContentPane(content);
		setResizable(false);
		pack();
		setLocationRelativeTo(null);
	}
}
