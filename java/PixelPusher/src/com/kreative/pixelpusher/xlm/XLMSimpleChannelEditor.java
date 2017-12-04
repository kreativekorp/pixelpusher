package com.kreative.pixelpusher.xlm;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import com.kreative.pixelpusher.sequence.PixelSequenceEditor;

public class XLMSimpleChannelEditor extends PixelSequenceEditor<XLMChannel> {
	private static final long serialVersionUID = 1L;
	
	public XLMSimpleChannelEditor() {
		this(new XLMChannel());
	}
	
	public XLMSimpleChannelEditor(XLMChannel channel) {
		super(channel);
		setLayout(new BorderLayout());
		add(makeMainPanel(), BorderLayout.CENTER);
	}
	
	private JPanel makeMainPanel() {
		JLabel numOnLabel = makeBig(center(new JLabel("# ON")));
		JButton numOnMore = makeBig(new JButton("+"));
		JButton numOnLess = makeBig(new JButton("\u2212"));
		JPanel numOnButtonPanel = new JPanel(new GridLayout(0,1,8,8));
		numOnButtonPanel.add(numOnMore);
		numOnButtonPanel.add(numOnLess);
		JPanel numOnPanel = new JPanel(new BorderLayout(8,8));
		numOnPanel.add(numOnLabel, BorderLayout.PAGE_START);
		numOnPanel.add(numOnButtonPanel, BorderLayout.CENTER);
		
		JLabel numOffLabel = makeBig(center(new JLabel("# OFF")));
		JButton numOffMore = makeBig(new JButton("+"));
		JButton numOffLess = makeBig(new JButton("\u2212"));
		JPanel numOffButtonPanel = new JPanel(new GridLayout(0,1,8,8));
		numOffButtonPanel.add(numOffMore);
		numOffButtonPanel.add(numOffLess);
		JPanel numOffPanel = new JPanel(new BorderLayout(8,8));
		numOffPanel.add(numOffLabel, BorderLayout.PAGE_START);
		numOffPanel.add(numOffButtonPanel, BorderLayout.CENTER);
		
		JLabel speedLabel = makeBig(center(new JLabel("SPEED")));
		JButton speedFaster = makeBig(new JButton("FASTER"));
		JButton speedSlower = makeBig(new JButton("SLOWER"));
		JPanel speedButtonPanel = new JPanel(new GridLayout(0,1,8,8));
		speedButtonPanel.add(speedFaster);
		speedButtonPanel.add(speedSlower);
		JPanel speedPanel = new JPanel(new BorderLayout(8,8));
		speedPanel.add(speedLabel, BorderLayout.PAGE_START);
		speedPanel.add(speedButtonPanel, BorderLayout.CENTER);

		JLabel reverseLabel = makeBig(center(new JLabel("REVERSE")));
		JButton reverse = makeBig2(new JButton("\u21BA"));
		JPanel reversePanel = new JPanel(new BorderLayout(8,8));
		reversePanel.add(reverseLabel, BorderLayout.PAGE_START);
		reversePanel.add(reverse, BorderLayout.CENTER);
		
		JLabel rotateLabel = makeBig(center(new JLabel("ROTATE")));
		JButton rotate = makeBig2(new JButton("\u25EF"));
		JPanel rotatePanel = new JPanel(new BorderLayout(8,8));
		rotatePanel.add(rotateLabel, BorderLayout.PAGE_START);
		rotatePanel.add(rotate, BorderLayout.CENTER);
		
		JLabel freezeLabel = makeBig(center(new JLabel("FREEZE")));
		JButton freeze = makeBig(new JButton("\u25CF"));
		JPanel freezePanel = new JPanel(new BorderLayout(8,8));
		freezePanel.add(freezeLabel, BorderLayout.PAGE_START);
		freezePanel.add(freeze, BorderLayout.CENTER);
		
		JPanel mainPanel = new JPanel(new GridLayout(1,0,8,8));
		mainPanel.add(numOnPanel);
		mainPanel.add(numOffPanel);
		mainPanel.add(speedPanel);
		mainPanel.add(reversePanel);
		mainPanel.add(rotatePanel);
		mainPanel.add(freezePanel);
		
		numOnMore.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				XLMChannel channel = getPixelSet();
				channel.setPatternCount(0, channel.getPatternCount(0) + 1);
			}
		});
		numOnLess.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				XLMChannel channel = getPixelSet();
				channel.setPatternCount(0, channel.getPatternCount(0) - 1);
			}
		});
		numOffMore.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				XLMChannel channel = getPixelSet();
				channel.setPatternCount(1, channel.getPatternCount(1) + 1);
			}
		});
		numOffLess.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				XLMChannel channel = getPixelSet();
				channel.setPatternCount(1, channel.getPatternCount(1) - 1);
			}
		});
		speedFaster.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				XLMChannel channel = getPixelSet();
				channel.setMsPerFrame(channel.getMsPerFrame() - 20);
			}
		});
		speedSlower.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				XLMChannel channel = getPixelSet();
				channel.setMsPerFrame(channel.getMsPerFrame() + 20);
			}
		});
		reverse.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				XLMChannel channel = getPixelSet();
				channel.setReversed(!channel.isReversed());
			}
		});
		rotate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				XLMChannel channel = getPixelSet();
				channel.setStopped(false);
			}
		});
		freeze.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				XLMChannel channel = getPixelSet();
				channel.setStopped(true);
			}
		});
		
		return mainPanel;
	}
	
	private static <C extends JComponent> C makeBig(C c) {
		c.setFont(c.getFont().deriveFont(18.0f));
		c.putClientProperty("JButton.buttonType", "bevel");
		return c;
	}
	
	private static <C extends JComponent> C makeBig2(C c) {
		c.setFont(c.getFont().deriveFont(36.0f));
		c.putClientProperty("JButton.buttonType", "bevel");
		return c;
	}
	
	private static JLabel center(JLabel l) {
		l.setHorizontalAlignment(JLabel.CENTER);
		l.setVerticalAlignment(JLabel.CENTER);
		return l;
	}
}
