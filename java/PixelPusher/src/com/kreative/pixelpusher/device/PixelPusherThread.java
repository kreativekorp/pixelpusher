package com.kreative.pixelpusher.device;

import com.heroicrobot.dropbit.devices.pixelpusher.PixelPusher;
import com.heroicrobot.dropbit.devices.pixelpusher.Strip;
import com.heroicrobot.dropbit.registry.DeviceRegistry;

public class PixelPusherThread extends Thread {
	private final DeviceRegistry registry;
	private final StripInfoSet infoSet;
	
	public PixelPusherThread(DeviceRegistry registry, StripInfoSet infoSet) {
		this.registry = registry;
		this.infoSet = infoSet;
	}
	
	@Override
	public void run() {
		while (!Thread.interrupted()) {
			long tick = System.currentTimeMillis();
			for (PixelPusher pusher : registry.getPushers()) {
				for (Strip strip : pusher.getStrips()) {
					StripInfo info = infoSet.getStripInfo(pusher, strip);
					if (info != null) info.push(strip, tick);
				}
			}
			try {
				Thread.sleep(20);
			} catch (InterruptedException ie) {
				ie.printStackTrace();
				return;
			}
		}
	}
}
