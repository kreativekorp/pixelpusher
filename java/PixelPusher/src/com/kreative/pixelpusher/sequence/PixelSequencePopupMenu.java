package com.kreative.pixelpusher.sequence;

import com.kreative.pixelpusher.pixelset.PixelSetInfoSet;
import com.kreative.pixelpusher.pixelset.PixelSetPopupMenu;

public class PixelSequencePopupMenu extends PixelSetPopupMenu<PixelSequence> {
	private static final long serialVersionUID = 1L;
	
	public PixelSequencePopupMenu(PixelSetInfoSet infoSet) {
		super(infoSet, PixelSequence.class);
	}
}
