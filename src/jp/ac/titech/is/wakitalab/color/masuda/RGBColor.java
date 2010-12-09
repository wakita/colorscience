package jp.ac.titech.is.wakitalab.color.masuda;

import java.awt.Color;



public class RGBColor extends MyColor {
	private final double r, g, b;
//	private static final double gamma = Constants.GAMMA;
	
	public RGBColor(double r, double g, double b) {
		this.r = r; this.g = g; this.b = b;
	}
	public String toString() {
		return "("+r()+", "+g()+", "+b()+")";
	}
	
	public double r() {
		return r;
	}
	public double g() {
		return g;
	}
	public double b() {
		return b;
	}
	
//	public Color getColor() {
//		return new Color((float)Math.pow(r, 1/gamma), (float)Math.pow(g, 1/gamma), (float)Math.pow(b, 1/gamma));
//	}
	
	public LMSColor getLMS() {
		double l = Constants.L_R*r() + Constants.L_G*g() + Constants.L_B*b();
		double m = Constants.M_R*r() + Constants.M_G*g() + Constants.M_B*b();
		double s = Constants.S_R*r() + Constants.S_G*g() + Constants.S_B*b();
		
		return new LMSColor(l,m,s);
	}
	public XYZColor getXYZ() {
		double x = Constants.X_R*r() + Constants.X_G*g() + Constants.X_B*b();
		double y = Constants.Y_R*r() + Constants.Y_G*g() + Constants.Y_B*b();
		double z = Constants.Z_R*r() + Constants.Z_G*g() + Constants.Z_B*b();
		
		return new XYZColor(x, y, z);
	}
	public SRGBColor getSRGB() {
		return getXYZ().getSRGB();
	}
	public Color getColor() {
		return getSRGB().getColor();
	}
}
