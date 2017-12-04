package com.kreative.pixelpusher.audiospectrum;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import com.kreative.pixelpusher.array.PixelArrayEditor;

public class AudioSpectrumEditor extends PixelArrayEditor<AudioSpectrumArray> {
	private static final long serialVersionUID = 1L;
	
	public AudioSpectrumEditor() {
		this(new AudioSpectrumArray());
	}
	
	public AudioSpectrumEditor(AudioSpectrumArray array) {
		super(array);
		setLayout(new BorderLayout());
		add(makeMainPanel(), BorderLayout.CENTER);
	}
	
	@Override
	public void setPixelSet(AudioSpectrumArray array) {
		super.setPixelSet(array);
		removeAll();
		add(makeMainPanel(), BorderLayout.CENTER);
		revalidate();
	}
	
	private SpinnerNumberModel widthModel;
	private JSpinner widthSpinner;
	private SpinnerNumberModel heightModel;
	private JSpinner heightSpinner;
	
	private JPanel makeMainPanel() {
		JLabel widthLabel = new JLabel("Width:");
		widthModel = new SpinnerNumberModel(pixelSet.getWidth(), 1, 250, 1);
		widthSpinner = new JSpinner(widthModel);
		JLabel heightLabel = new JLabel("Height:");
		heightModel = new SpinnerNumberModel(pixelSet.getHeight(), 1, 250, 1);
		heightSpinner = new JSpinner(heightModel);
		JPanel main = new JPanel(new FlowLayout());
		main.add(widthLabel);
		main.add(widthSpinner);
		main.add(heightLabel);
		main.add(heightSpinner);
		
		widthSpinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				pixelSet.setWidth(widthModel.getNumber().intValue());
			}
		});
		heightSpinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				pixelSet.setHeight(heightModel.getNumber().intValue());
			}
		});
		
		return main;
	}
}
