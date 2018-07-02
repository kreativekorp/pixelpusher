package com.kreative.unipixelpusher.gui;

import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.SwingUtilities;
import com.kreative.unipixelpusher.DeviceLoader;
import com.kreative.unipixelpusher.DeviceLoader.DeviceInfo;
import com.kreative.unipixelpusher.DeviceType;
import com.kreative.unipixelpusher.PixelDevice;
import com.kreative.unipixelpusher.PixelDeviceListener;

public class DeviceList extends JList {
	private static final long serialVersionUID = 1L;
	
	public DeviceList(DeviceLoader loader) {
		super(loader.getDevices().toArray());
		this.setCellRenderer(new Renderer());
		loader.addPixelDeviceListener(new Listener(loader));
	}
	
	public DeviceInfo getSelectedDeviceInfo() {
		return (DeviceInfo)getSelectedValue();
	}
	
	public PixelDevice getSelectedDevice() {
		return getSelectedDeviceInfo().device();
	}
	
	private class Renderer extends DefaultListCellRenderer {
		private static final long serialVersionUID = 1L;
		@Override
		public Component getListCellRendererComponent(JList c, Object v, int i, boolean s, boolean f) {
			DeviceInfo di = (DeviceInfo)v;
			JLabel l = (JLabel)super.getListCellRendererComponent(c, di.device().name(), i, s, f);
			l.setIcon(new ImageIcon(Icons.getIcon(DeviceType.class, di.device().type(), 16)));
			l.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
			return l;
		}
	}
	
	private class Listener implements PixelDeviceListener {
		private final DeviceLoader loader;
		private Listener(DeviceLoader loader) {
			this.loader = loader;
		}
		@Override
		public void pixelDeviceAppeared(PixelDevice dev) {
			SwingUtilities.invokeLater(new Updater(loader));
		}
		@Override
		public void pixelDeviceChanged(PixelDevice dev) {
			SwingUtilities.invokeLater(new Updater(loader));
		}
		@Override
		public void pixelDeviceDisappeared(PixelDevice dev) {
			SwingUtilities.invokeLater(new Updater(loader));
		}
	}
	
	private class Updater implements Runnable {
		private final DeviceLoader loader;
		private Updater(DeviceLoader loader) {
			this.loader = loader;
		}
		@Override
		public void run() {
			setListData(loader.getDevices().toArray());
			repaint();
		}
	}
}
