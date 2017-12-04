package com.kreative.pixelpusher.xlm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import com.kreative.pixelpusher.common.ColorPopupMenu;
import com.kreative.pixelpusher.sequence.PixelSequenceEditor;

public class XLMAdvancedChannelEditor extends PixelSequenceEditor<XLMChannel> {
	private static final long serialVersionUID = 1L;
	
	public XLMAdvancedChannelEditor() {
		this(new XLMChannel());
	}
	
	public XLMAdvancedChannelEditor(XLMChannel channel) {
		super(channel);
		setLayout(new BorderLayout());
		add(makeMainPanel(), BorderLayout.CENTER);
	}
	
	@Override
	public void setPixelSet(XLMChannel channel) {
		super.setPixelSet(channel);
		removeAll();
		add(makeMainPanel(), BorderLayout.CENTER);
		revalidate();
	}
	
	private JPanel makeMainPanel() {
		final XLMAdvancedPatternEditor plEditor = new XLMAdvancedPatternEditor();
		final SpinnerNumberModel msSpinnerModel = new SpinnerNumberModel(getPixelSet().getMsPerFrame(), 20, 60000, 20);
		final JSpinner msSpinner = new JSpinner(msSpinnerModel);
		final JButton backwards = squareOff(new JButton("\u25C0"), "Backwards");
		final JButton forwards = squareOff(new JButton("\u25B6"), "Forwards");
		final JButton reverse = squareOff(new JButton("\u21BA"), "Reverse");
		final JButton rotate = squareOff(new JButton("\u25EF"), "Rotate");
		final JButton freeze = squareOff(new JButton("\u25CF"), "Freeze");
		
		JPanel spinnerPanel = new JPanel(new BorderLayout(4,4));
		spinnerPanel.add(msSpinner, BorderLayout.CENTER);
		spinnerPanel.add(new JLabel("ms"), BorderLayout.LINE_END);
		
		JPanel buttonPanel = new JPanel(new GridLayout(1,0,2,2));
		buttonPanel.add(backwards);
		buttonPanel.add(forwards);
		buttonPanel.add(reverse);
		buttonPanel.add(rotate);
		buttonPanel.add(freeze);
		
		JPanel panel1 = new JPanel(new BorderLayout(8,8));
		panel1.add(spinnerPanel, BorderLayout.CENTER);
		panel1.add(buttonPanel, BorderLayout.LINE_END);
		Dimension d = panel1.getPreferredSize();
		panel1.setMinimumSize(d);
		panel1.setPreferredSize(d);
		panel1.setMaximumSize(d);
		
		JPanel panel2 = new JPanel();
		panel2.setLayout(new BoxLayout(panel2, BoxLayout.PAGE_AXIS));
		panel2.add(Box.createVerticalGlue());
		panel2.add(panel1);
		panel2.add(Box.createVerticalGlue());
		
		JPanel mainPanel = new JPanel(new BorderLayout(8,8));
		mainPanel.add(plEditor, BorderLayout.CENTER);
		mainPanel.add(panel2, BorderLayout.LINE_END);
		mainPanel.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.gray),
			BorderFactory.createEmptyBorder(8, 8, 8, 8)
		));
		
		msSpinnerModel.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				getPixelSet().setMsPerFrame(msSpinnerModel.getNumber().intValue());
			}
		});
		backwards.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				XLMChannel channel = getPixelSet();
				channel.setReversed(true);
				channel.setStopped(false);
			}
		});
		forwards.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				XLMChannel channel = getPixelSet();
				channel.setReversed(false);
				channel.setStopped(false);
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
	
	private class XLMAdvancedPatternEditor extends JPanel {
		private static final long serialVersionUID = 1L;
		private final JPanel colorList;
		public XLMAdvancedPatternEditor() {
			this.colorList = new JPanel(new GridLayout(0,1,2,2));
			for (int i = 0; i < getPixelSet().getPatternLength(); i++) {
				this.colorList.add(new XLMAdvancedPatternColorEditor(i));
			}
			JPanel colorListWrapper = new JPanel(new BorderLayout());
			colorListWrapper.add(this.colorList, BorderLayout.PAGE_START);
			JButton add = makeSmall(squareOff(new JButton("+"), "Add Color"));
			JButton remove = makeSmall(squareOff(new JButton("\u2212"), "Remove Color"));
			JPanel buttonPanel1 = new JPanel(new GridLayout(0,1,2,2));
			buttonPanel1.add(add);
			buttonPanel1.add(remove);
			Dimension d = buttonPanel1.getPreferredSize();
			buttonPanel1.setMinimumSize(d);
			buttonPanel1.setPreferredSize(d);
			buttonPanel1.setMaximumSize(d);
			JPanel buttonPanel2 = new JPanel();
			buttonPanel2.setLayout(new BoxLayout(buttonPanel2, BoxLayout.PAGE_AXIS));
			buttonPanel2.add(Box.createVerticalGlue());
			buttonPanel2.add(buttonPanel1);
			buttonPanel2.add(Box.createVerticalGlue());
			setLayout(new BorderLayout(4,4));
			add(colorListWrapper, BorderLayout.CENTER);
			add(buttonPanel2, BorderLayout.LINE_END);
			add.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					int l = getPixelSet().getPatternLength() + 1;
					getPixelSet().setPatternLength(l);
					colorList.removeAll();
					for (int i = 0; i < l; i++) {
						colorList.add(new XLMAdvancedPatternColorEditor(i));
					}
					XLMAdvancedChannelEditor.this.revalidate();
				}
			});
			remove.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					int l = getPixelSet().getPatternLength() - 1;
					if (l < 1) return;
					getPixelSet().setPatternLength(l);
					colorList.removeAll();
					for (int i = 0; i < l; i++) {
						colorList.add(new XLMAdvancedPatternColorEditor(i));
					}
					XLMAdvancedChannelEditor.this.revalidate();
				}
			});
		}
	}
	
	private class XLMAdvancedPatternColorEditor extends JPanel {
		private static final long serialVersionUID = 1L;
		private final int index;
		private final ColorPopupMenu colorPopup;
		private final SpinnerNumberModel countSpinnerModel;
		private final JSpinner countSpinner;
		public XLMAdvancedPatternColorEditor(int index) {
			this.index = index;
			this.colorPopup = new ColorPopupMenu();
			this.colorPopup.setSelectedItem(getPixelSet().getPatternColor(index));
			this.countSpinnerModel = new SpinnerNumberModel(getPixelSet().getPatternCount(index), 1, 250, 1);
			this.countSpinner = new JSpinner(this.countSpinnerModel);
			setLayout(new BorderLayout(4,4));
			add(this.colorPopup, BorderLayout.CENTER);
			add(this.countSpinner, BorderLayout.LINE_END);
			this.colorPopup.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					getPixelSet().setPatternColor(
						XLMAdvancedPatternColorEditor.this.index,
						(Integer)colorPopup.getSelectedItem()
					);
				}
			});
			this.countSpinnerModel.addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent e) {
					getPixelSet().setPatternCount(
						XLMAdvancedPatternColorEditor.this.index,
						countSpinnerModel.getNumber().intValue()
					);
				}
			});
		}
	}
	
	private static <C extends JComponent> C squareOff(C c, String tooltip) {
		c.putClientProperty("JButton.buttonType", "bevel");
		c.setToolTipText(tooltip);
		return c;
	}
	
	private static <C extends JComponent> C makeSmall(C c) {
		c.setFont(c.getFont().deriveFont(9.0f));
		return c;
	}
}
