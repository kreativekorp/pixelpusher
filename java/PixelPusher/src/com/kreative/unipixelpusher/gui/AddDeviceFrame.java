package com.kreative.unipixelpusher.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import com.kreative.unipixelpusher.DeviceLoader;
import com.kreative.unipixelpusher.DeviceLoader.DeviceInfo;
import com.kreative.unipixelpusher.PixelProgram;

public class AddDeviceFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	
	public AddDeviceFrame(final DeviceLoader loader, final PixelProgram program) {
		super("Add Devices");
		
		final DeviceList dlist = new DeviceList(loader);
		dlist.setVisibleRowCount(16);
		JScrollPane dpane = new JScrollPane(
			dlist,
			JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
		);
		
		JButton okButton = new JButton("Add");
		JButton updateButton = new JButton("Update");
		JButton cancelButton = new JButton("Done");
		JPanel buttonPanel = new JPanel(new FlowLayout());
		buttonPanel.add(okButton);
		buttonPanel.add(updateButton);
		buttonPanel.add(cancelButton);
		
		JPanel mainPanel = new JPanel(new BorderLayout(8, 8));
		mainPanel.add(dpane, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.PAGE_END);
		mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		
		setContentPane(mainPanel);
		SwingUtils.setDefaultButton(getRootPane(), okButton);
		SwingUtils.setCancelButton(getRootPane(), cancelButton);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setResizable(true);
		pack();
		setLocationRelativeTo(null);
		
		dlist.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() > 1) {
					DeviceInfo di = dlist.getSelectedDeviceInfo();
					if (di != null) program.addDevice(di.device());
				}
			}
		});
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DeviceInfo di = dlist.getSelectedDeviceInfo();
				if (di != null) program.addDevice(di.device());
			}
		});
		updateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				loader.update();
			}
		});
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
	}
}
