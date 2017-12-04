package com.kreative.pixelpusher.device;

import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import com.heroicrobot.dropbit.devices.pixelpusher.PixelPusher;
import com.heroicrobot.dropbit.devices.pixelpusher.Strip;
import com.kreative.pixelpusher.pixelset.PixelSetInfoSet;

public class StripInfoFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private StripInfoEditor editor;
	
	public StripInfoFrame(PixelSetInfoSet infoSet, PixelPusher pusher, Strip strip, StripInfo info) {
		super("Strip Configuration");
		this.editor = new StripInfoEditor(infoSet, pusher, strip, info);
		JPanel main = new JPanel(new BorderLayout());
		main.add(this.editor, BorderLayout.CENTER);
		main.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		setContentPane(main);
		pack();
		setResizable(false);
	}
}
