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
import com.kreative.unipixelpusher.PixelDevice;

public class DeviceConfigurationFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private static final Map<PixelDevice,DeviceConfigurationFrame> windowMap =
	             new HashMap<PixelDevice,DeviceConfigurationFrame>();
	
	public static DeviceConfigurationFrame open(final PixelDevice device) {
		if (windowMap.containsKey(device)) {
			DeviceConfigurationFrame f = windowMap.get(device);
			f.setVisible(true);
			return f;
		} else if (device.hasConfigurationPanel()) {
			DeviceConfigurationFrame f = new DeviceConfigurationFrame(device);
			windowMap.put(device, f);
			f.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosed(WindowEvent e) {
					windowMap.remove(device);
				}
			});
			f.setVisible(true);
			return f;
		} else {
			JOptionPane.showMessageDialog(
				null, "This device has no configuration options.",
				device.name(), JOptionPane.INFORMATION_MESSAGE
			);
			return null;
		}
	}
	
	public DeviceConfigurationFrame(PixelDevice device) {
		super(device.name());
		JPanel content = new JPanel(new BorderLayout());
		content.add(device.createConfigurationPanel(), BorderLayout.CENTER);
		content.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		setContentPane(content);
		setResizable(false);
		pack();
		setLocationRelativeTo(null);
	}
}
