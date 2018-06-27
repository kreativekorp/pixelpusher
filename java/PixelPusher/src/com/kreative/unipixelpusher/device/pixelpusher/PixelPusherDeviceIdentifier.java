package com.kreative.unipixelpusher.device.pixelpusher;

import com.heroicrobot.dropbit.devices.pixelpusher.PixelPusher;

public enum PixelPusherDeviceIdentifier {
	MAC_ADDRESS {
		@Override
		public String getId(PixelPusher pusher) {
			return pusher.getMacAddress();
		}
		@Override
		public String getName(PixelPusher pusher) {
			return pusher.getMacAddress();
		}
	},
	IP_ADDRESS {
		@Override
		public String getId(PixelPusher pusher) {
			return pusher.getIp().getHostAddress() + ":" + pusher.getPort();
		}
		@Override
		public String getName(PixelPusher pusher) {
			return pusher.getIp().getHostAddress() + ":" + pusher.getPort();
		}
	},
	GROUP_CONTROLLER {
		@Override
		public String getId(PixelPusher pusher) {
			return "g" + pusher.getGroupOrdinal() + "/c" + pusher.getControllerOrdinal();
		}
		@Override
		public String getName(PixelPusher pusher) {
			return "Group " + pusher.getGroupOrdinal() + ", Controller " + pusher.getControllerOrdinal();
		}
	},
	ARTNET_ADDRESS {
		@Override
		public String getId(PixelPusher pusher) {
			return "u" + pusher.getArtnetUniverse() + "/c" + pusher.getArtnetChannel();
		}
		@Override
		public String getName(PixelPusher pusher) {
			return "Universe " + pusher.getArtnetUniverse() + ", Channel " + pusher.getArtnetChannel();
		}
	};
	
	public abstract String getId(PixelPusher pusher);
	public abstract String getName(PixelPusher pusher);
}
