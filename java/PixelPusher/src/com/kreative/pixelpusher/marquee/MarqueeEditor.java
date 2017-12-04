package com.kreative.pixelpusher.marquee;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import com.kreative.pixelpusher.array.PixelArrayEditor;

public class MarqueeEditor extends PixelArrayEditor<MarqueeArray> {
	private static final long serialVersionUID = 1L;
	private static final int DIVISOR = 5;
	
	private MarqueeParser presets;
	
	public MarqueeEditor() {
		this(new MarqueeArray());
	}
	
	public MarqueeEditor(MarqueeArray array) {
		super(array);
		presets = new MarqueeParser();
		presets.parse();
		setLayout(new BorderLayout());
		add(makeMainPanel(), BorderLayout.CENTER);
	}
	
	@Override
	public void setPixelSet(MarqueeArray array) {
		super.setPixelSet(array);
		removeAll();
		add(makeMainPanel(), BorderLayout.CENTER);
		revalidate();
	}
	
	private JComboBox messagePreset;
	private JSlider scrollSpeed;
	private JLabel scrollSpeedLabel;
	
	private JPanel makeMainPanel() {
		JLabel messagePresetLabel = new JLabel("Message:");
		messagePreset = new JComboBox(presets.getMessageNames());
		messagePreset.setEditable(false);
		messagePreset.setMaximumRowCount(messagePreset.getItemCount());
		messagePreset.setSelectedItem(pixelSet.messageName());
		JLabel scrollSpeedLabel2 = new JLabel("Speed:");
		scrollSpeed = new JSlider(20 / DIVISOR, 1000 / DIVISOR, pixelSet.getScrollingMsPerFrame() / DIVISOR);
		scrollSpeedLabel = new JLabel(Integer.toString(pixelSet.getScrollingMsPerFrame()));
		JLabel scrollSpeedLabel3 = new JLabel(" ms");
		
		JPanel labelPanel = new JPanel(new GridLayout(0,1,4,4));
		labelPanel.add(messagePresetLabel);
		labelPanel.add(scrollSpeedLabel2);
		JPanel scrollSpeedInnerPanel = new JPanel(new BorderLayout());
		scrollSpeedInnerPanel.add(scrollSpeedLabel, BorderLayout.CENTER);
		scrollSpeedInnerPanel.add(scrollSpeedLabel3, BorderLayout.LINE_END);
		JPanel scrollSpeedPanel = new JPanel(new BorderLayout(4,4));
		scrollSpeedPanel.add(scrollSpeed, BorderLayout.CENTER);
		scrollSpeedPanel.add(scrollSpeedInnerPanel, BorderLayout.LINE_END);
		JPanel controlPanel = new JPanel(new GridLayout(0,1,4,4));
		controlPanel.add(messagePreset);
		controlPanel.add(scrollSpeedPanel);
		JPanel mainPanel = new JPanel(new BorderLayout(4,4));
		mainPanel.add(labelPanel, BorderLayout.LINE_START);
		mainPanel.add(controlPanel, BorderLayout.CENTER);
		
		messagePreset.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (messagePreset.getSelectedItem() != null) {
					String name = messagePreset.getSelectedItem().toString();
					MarqueeString str = presets.getMessage(name);
					pixelSet.setMarqueeString(name, str);
				}
			}
		});
		scrollSpeed.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				pixelSet.setScrollingMsPerFrame(scrollSpeed.getValue() * DIVISOR);
				scrollSpeedLabel.setText(Integer.toString(pixelSet.getScrollingMsPerFrame()));
			}
		});
		
		return mainPanel;
	}
}
