package jp.ac.titech.is.wakitalab.color.masuda;

import java.awt.Color;


public class ColorList {
	private static final DichromatSimulator simulator = new DichromatSimulator();
	public final int r, g, b;
	public final Color nature, protanope, deutanope, tritanope;
	private static final double gamma = Constants.GAMMA;
	public ColorList(int r, int g, int b) {
		this.r = r; this.g = g; this.b = b;
		double sR = r/255.0, sG = g/255.0, sB = b/255.0;
		nature = new Color(r, g, b);
		RGBColor lRGB = new RGBColor(Math.pow(sR, gamma), Math.pow(sG, gamma), Math.pow(sB, gamma));
		RGBColor p = simulator.p_L(lRGB);
		this.protanope = new Color((float)Math.pow(p.r(), 1/gamma), (float)Math.pow(p.g(), 1/gamma), (float)Math.pow(p.b(), 1/gamma));
		RGBColor d = simulator.p_M(lRGB);
		this.deutanope = new Color((float)Math.pow(d.r(), 1/gamma), (float)Math.pow(d.g(), 1/gamma), (float)Math.pow(d.b(), 1/gamma));
		RGBColor t = simulator.p_S(lRGB);
		this.tritanope = new Color((float)Math.pow(t.r(), 1/gamma), (float)Math.pow(t.g(), 1/gamma), (float)Math.pow(t.b(), 1/gamma));
		
	}
}
