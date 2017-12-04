package com.kreative.pixelpusher.pixelset;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class PixelSetLauncher {
	private PixelSetLauncher() {}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void main(String[] args) {
		List<String> arga = Arrays.asList(args);
		ServiceLoader<PixelSetFactory> loader = ServiceLoader.load(PixelSetFactory.class);
		Iterator<PixelSetFactory> allFactories = loader.iterator();
		List<PixelSetFactory<PixelSet>> selectedFactories = new ArrayList<PixelSetFactory<PixelSet>>();
		while (allFactories.hasNext()) {
			PixelSetFactory factory = allFactories.next();
			if (
				arga.contains(factory.getNameForMachines()) ||
				arga.contains(factory.getNameForHumans()) ||
				arga.contains(factory.getClass().getSimpleName()) ||
				arga.contains(factory.getPixelSetClass().getSimpleName())
			) {
				selectedFactories.add(factory);
			}
		}
		start(selectedFactories);
	}
	
	public static <T extends PixelSet> void start(final List<PixelSetFactory<T>> factories) {
		try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				PixelSetInfoSet infoSet = new PixelSetInfoSet();
				for (PixelSetFactory<T> factory : factories) {
					T pixelSet = infoSet.createPixelSetInfo(factory).getPixelSet();
					final PixelSetVisualEditorFrame frame = factory.createVisualEditorFrame(infoSet, pixelSet);
					frame.pack();
					frame.setLocationRelativeTo(null);
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					frame.setVisible(true);
					frame.start();
					frame.addWindowListener(new WindowAdapter() {
						@Override
						public void windowClosing(WindowEvent e) {
							frame.stop();
						}
					});
				}
			}
		});
	}
}
