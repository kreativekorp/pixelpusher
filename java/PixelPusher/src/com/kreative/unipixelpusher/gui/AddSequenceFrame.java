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
import com.kreative.unipixelpusher.PixelProgram;
import com.kreative.unipixelpusher.SequenceLoader;
import com.kreative.unipixelpusher.SequenceLoader.SequenceInfo;

public class AddSequenceFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	
	public AddSequenceFrame(final SequenceLoader loader, final PixelProgram program) {
		super("Add Sequences");
		
		final SequenceList slist = new SequenceList(loader);
		slist.setVisibleRowCount(16);
		JScrollPane spane = new JScrollPane(
			slist,
			JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
		);
		
		JButton okButton = new JButton("Add");
		JButton cancelButton = new JButton("Done");
		JPanel buttonPanel = new JPanel(new FlowLayout());
		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);
		
		JPanel mainPanel = new JPanel(new BorderLayout(8, 8));
		mainPanel.add(spane, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.PAGE_END);
		mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		
		setContentPane(mainPanel);
		SwingUtils.setDefaultButton(getRootPane(), okButton);
		SwingUtils.setCancelButton(getRootPane(), cancelButton);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setResizable(true);
		pack();
		setLocationRelativeTo(null);
		
		slist.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() > 1) {
					SequenceInfo si = slist.getSelectedSequenceInfo();
					if (si != null) program.addSequence(si.createInstance());
				}
			}
		});
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SequenceInfo si = slist.getSelectedSequenceInfo();
				if (si != null) program.addSequence(si.createInstance());
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
