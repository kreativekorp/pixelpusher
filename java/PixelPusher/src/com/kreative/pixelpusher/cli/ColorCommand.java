package com.kreative.pixelpusher.cli;

import java.io.PrintStream;
import java.util.Scanner;
import com.heroicrobot.dropbit.devices.pixelpusher.Pixel;
import com.heroicrobot.dropbit.devices.pixelpusher.PixelPusher;
import com.heroicrobot.dropbit.devices.pixelpusher.Strip;
import com.kreative.pixelpusher.common.ColorUtilities;
import com.kreative.pixelpusher.device.GammaCorrector;
import com.kreative.pixelpusher.device.PixelPusherController;
import com.kreative.pixelpusher.device.StripInfo;

public class ColorCommand extends CLICommand {
	public ColorCommand() {
		super(
			new String[]{"co", "col", "color"},
			"Display a color on a connected device.",
			"color [<color> [<device> [<strip> [<pixel>]]]]"
		);
	}
	
	@Override
	public void main(Scanner in, PrintStream out, PixelPusherController ctrl, String[] args) {
		try {
			int color = (args.length > 1) ? ColorUtilities.parseColor(args[1]) : -1;
			StringSearchPattern dsp = (args.length > 2) ? new StringSearchPattern(args[2]) : null;
			IntegerSearchPattern ssp = (args.length > 3) ? new IntegerSearchPattern(args[3]) : null;
			IntegerSearchPattern psp = (args.length > 4) ? new IntegerSearchPattern(args[4]) : null;
			StripSearchResults devices = StripSearchResults.forPattern(ctrl, dsp, ssp);
			int count = 0;
			for (PixelPusher pusher : devices.getPushers()) {
				for (Strip strip : devices.getStrips(pusher)) {
					StripInfo info = devices.getStripInfo(strip);
					if (info != null) info.setPixelSet(null);
					Pixel pixel = new Pixel();
					GammaCorrector gamma = (info != null) ? info.getGammaCorrector() : GammaCorrector.DEFAULT;
					gamma.setPixelColor(pixel, color);
					for (int i = 0; i < strip.getLength(); i++) {
						if (psp == null || psp.matches(i)) {
							strip.setPixel(pixel, i);
						}
					}
					count++;
				}
			}
			printCount(out, "Changed color of", count, "device", "devices");
		} catch (NumberFormatException e) {
			printError(out, "Invalid color string: " + args[1]);
		} catch (GeneralParseException e) {
			printError(out, e.getMessage());
		}
	}
}
