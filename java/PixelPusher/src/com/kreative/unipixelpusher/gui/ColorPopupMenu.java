package com.kreative.unipixelpusher.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import com.kreative.unipixelpusher.ColorConstants;

public class ColorPopupMenu extends JComboBox {
	private static final long serialVersionUID = 1L;
	
	private static final Integer[] colors;
	private static final Map<Integer,String> colorNames;
	static {
		colors = new Integer[]{
			ColorConstants.BLACK  , ColorConstants.IRON           , ColorConstants.GRAY     , ColorConstants.SILVER     ,
			ColorConstants.WHITE  , ColorConstants.WARM_WHITE_LED , ColorConstants.FAIR     , ColorConstants.BROWN      ,
			ColorConstants.MAROON , ColorConstants.UMBER          , ColorConstants.OLIVE    , ColorConstants.PINE       ,
			ColorConstants.TEAL   , ColorConstants.NAVY           , ColorConstants.EGGPLANT , ColorConstants.PLUM       ,
			ColorConstants.RED    , ColorConstants.SCARLET        , ColorConstants.ORANGE   , ColorConstants.GOLD       ,
			ColorConstants.YELLOW , ColorConstants.CHARTREUSE     , ColorConstants.GREEN    , ColorConstants.AQUAMARINE ,
			ColorConstants.CYAN   , ColorConstants.AZURE          , ColorConstants.BLUE     , ColorConstants.INDIGO     ,
			ColorConstants.VIOLET , ColorConstants.PURPLE         , ColorConstants.MAGENTA  , ColorConstants.ROSE       ,
			ColorConstants.CORAL  , ColorConstants.CORANGE        , ColorConstants.LEMON    , ColorConstants.LIME       ,
			ColorConstants.SKY    , ColorConstants.FROST          , ColorConstants.LAVENDER , ColorConstants.PINK       ,
		};
		colorNames = new HashMap<Integer,String>();
		colorNames.put(ColorConstants.BLACK          , "Black"      );
		colorNames.put(ColorConstants.IRON           , "Iron"       );
		colorNames.put(ColorConstants.GRAY           , "Gray"       );
		colorNames.put(ColorConstants.SILVER         , "Silver"     );
		colorNames.put(ColorConstants.WHITE          , "White"      );
		colorNames.put(ColorConstants.WARM_WHITE_LED , "Warm White" );
		colorNames.put(ColorConstants.FAIR           , "Fair"       );
		colorNames.put(ColorConstants.BROWN          , "Brown"      );
		colorNames.put(ColorConstants.MAROON         , "Maroon"     );
		colorNames.put(ColorConstants.UMBER          , "Umber"      );
		colorNames.put(ColorConstants.OLIVE          , "Olive"      );
		colorNames.put(ColorConstants.PINE           , "Pine"       );
		colorNames.put(ColorConstants.TEAL           , "Teal"       );
		colorNames.put(ColorConstants.NAVY           , "Navy"       );
		colorNames.put(ColorConstants.EGGPLANT       , "Eggplant"   );
		colorNames.put(ColorConstants.PLUM           , "Plum"       );
		colorNames.put(ColorConstants.RED            , "Red"        );
		colorNames.put(ColorConstants.SCARLET        , "Scarlet"    );
		colorNames.put(ColorConstants.ORANGE         , "Orange"     );
		colorNames.put(ColorConstants.GOLD           , "Gold"       );
		colorNames.put(ColorConstants.YELLOW         , "Yellow"     );
		colorNames.put(ColorConstants.CHARTREUSE     , "Chartreuse" );
		colorNames.put(ColorConstants.GREEN          , "Green"      );
		colorNames.put(ColorConstants.AQUAMARINE     , "Aquamarine" );
		colorNames.put(ColorConstants.CYAN           , "Cyan"       );
		colorNames.put(ColorConstants.AZURE          , "Azure"      );
		colorNames.put(ColorConstants.BLUE           , "Blue"       );
		colorNames.put(ColorConstants.INDIGO         , "Indigo"     );
		colorNames.put(ColorConstants.VIOLET         , "Violet"     );
		colorNames.put(ColorConstants.PURPLE         , "Purple"     );
		colorNames.put(ColorConstants.MAGENTA        , "Magenta"    );
		colorNames.put(ColorConstants.ROSE           , "Rose"       );
		colorNames.put(ColorConstants.CORAL          , "Coral"      );
		colorNames.put(ColorConstants.CORANGE        , "Corange"    );
		colorNames.put(ColorConstants.LEMON          , "Lemon"      );
		colorNames.put(ColorConstants.LIME           , "Lime"       );
		colorNames.put(ColorConstants.SKY            , "Sky"        );
		colorNames.put(ColorConstants.FROST          , "Frost"      );
		colorNames.put(ColorConstants.LAVENDER       , "Lavender"   );
		colorNames.put(ColorConstants.PINK           , "Pink"       );
	}
	
	public ColorPopupMenu() {
		super(colors);
		this.setEditable(false);
		this.setMaximumRowCount(colors.length / 2);
		this.setSelectedIndex(0);
		this.setRenderer(new Renderer());
	}
	
	private class Renderer extends DefaultListCellRenderer {
		private static final long serialVersionUID = 1L;
		@Override
		public Component getListCellRendererComponent(JList c, Object v, int i, boolean s, boolean f) {
			JLabel l = (JLabel)super.getListCellRendererComponent(c, colorNames.get(v), i, s, f);
			BufferedImage img = new BufferedImage(32, 16, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = img.createGraphics();
			Color clr = new Color(((Number)v).intValue());
			g.setColor(clr);
			g.fillRect(0, 0, 32, 16);
			g.setColor(clr.darker());
			g.drawRect(0, 0, 31, 15);
			g.dispose();
			l.setIcon(new ImageIcon(img));
			l.setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));
			return l;
		}
	}
}
