package com.kreative.unipixelpusher.gui;

import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import com.kreative.unipixelpusher.PixelSequence;

public class SpeedAdjustPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private final PixelSequence.SpeedAdjust sequence;
	private final JSlider speedSlider;
	private final SpinnerNumberModel speedModel;
	private boolean updating;
	
	public SpeedAdjustPanel(PixelSequence.SpeedAdjust seq, boolean title) {
		this.sequence = seq;
		double speed = seq.getSpeedAdjust();
		int value = (int)Math.round(Math.log10(speed) * 50.0);
		this.speedSlider = new JSlider(-100, 100, value);
		double rspeed = Math.round(speed * 100.0) / 100.0;
		this.speedModel = new SpinnerNumberModel(rspeed, 0.01, 100.0, 0.01);
		
		JPanel spinnerPanel = new JPanel(new BorderLayout(4, 4));
		spinnerPanel.add(new JSpinner(speedModel), BorderLayout.CENTER);
		spinnerPanel.add(new JLabel("x"), BorderLayout.LINE_END);
		setLayout(new BorderLayout(8, 8));
		if (title) add(new JLabel("Speed:"), BorderLayout.LINE_START);
		add(speedSlider, BorderLayout.CENTER);
		add(spinnerPanel, BorderLayout.LINE_END);
		
		updating = false;
		speedSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (updating) return;
				updating = true;
				int value = speedSlider.getValue();
				double speed = Math.pow(10.0, value / 100.0);
				speed = Math.round(speed * 100.0) / 100.0;
				speedModel.setValue(speed);
				sequence.setSpeedAdjust((float)speed);
				updating = false;
			}
		});
		speedModel.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (updating) return;
				updating = true;
				double speed = speedModel.getNumber().doubleValue();
				int value = (int)Math.round(Math.log10(speed) * 100.0);
				speedSlider.setValue(value);
				sequence.setSpeedAdjust((float)speed);
				updating = false;
			}
		});
	}
}
