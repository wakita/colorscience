package jp.ac.titech.is.wakitalab.apps.scdraw.util;

import java.awt.Color;
import java.util.Hashtable;
import java.util.Map;

public class ColorUtil {
	private static final double MIN_COLOR_CONTRAST = 0.1;
	
	// private static Map<Integer, Color> colors = new Hashtable<Integer, Color>();
	
	private static Map<Color, Color>
	lightColors = new Hashtable<Color, Color>(),
	darkColors = new Hashtable<Color, Color>();
	
	public static Color lightColor(Color c) {
		Color lightColor = lightColors.get(c);
		if (lightColor != null) return lightColor;
		float[] hsb = new float[3];
		Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), hsb);
		double brightness = hsb[2];
		brightness =
			Math.max(Math.min(brightness + MIN_COLOR_CONTRAST, 1.0 - MIN_COLOR_CONTRAST),
				brightness);
		lightColor = Color.getHSBColor(hsb[0], hsb[1], (float)brightness);
		lightColors.put(c, lightColor);
		
		return lightColor;
	}
	
	public static Color darkColor(Color c) {
		Color darkColor = darkColors.get(c);
		if (darkColor != null) return darkColor;
		float[] hsb = new float[3];
		Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), hsb);
		double brightness = hsb[2];
		brightness = Math.max(brightness - MIN_COLOR_CONTRAST, 0);
		darkColor = Color.getHSBColor(hsb[0], hsb[1], (float)brightness);
		darkColors.put(c, darkColor);
		
		return darkColor;
	}
}
