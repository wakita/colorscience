package jp.ac.titech.is.wakitalab.color.masuda;

import java.awt.Color;


public class SRGBColor extends MyColor {
	private final double r, g, b;
	private static final double MAX = 255.0;
	
	public SRGBColor(double r, double g, double b) {
		this.r = r; this.g = g; this.b = b;
	}
	public SRGBColor(int r, int g, int b) {
		this.r = r/MAX; this.g = g/MAX; this.b = b/MAX;
	}
	public SRGBColor(Color c) {
		this.r = c.getRed()/MAX;
		this.g = c.getGreen()/MAX;
		this.b = c.getBlue()/MAX;
	}
	@Override
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
	
	private double gamma_correction_to_lRGB(double x) {
		// TODO NAN�̏���
		return Math.pow(x, Constants.GAMMA);
	}
	public XYZColor getXYZ() {
		double x = Constants.X_lR*gamma_correction_to_lRGB(r()) + Constants.X_lG*gamma_correction_to_lRGB(g()) + Constants.X_lB*gamma_correction_to_lRGB(b());
		double y = Constants.Y_lR*gamma_correction_to_lRGB(r()) + Constants.Y_lG*gamma_correction_to_lRGB(g()) + Constants.Y_lB*gamma_correction_to_lRGB(b());
		double z = Constants.Z_lR*gamma_correction_to_lRGB(r()) + Constants.Z_lG*gamma_correction_to_lRGB(g()) + Constants.Z_lB*gamma_correction_to_lRGB(b());
		return new XYZColor(x, y, z);
	}
	public RGBColor getRGB() {
		return getXYZ().getRGB();
	}
	public LMSColor getLMS() {
		return getRGB().getLMS();
	}
	
	public Color getColor() {
		assert r>=0&&r<=1&&g>=0&&g<=1&&b>=0&&b<=1;
		System.out.println(this.toString());
		return new Color((float)r, (float)g, (float)b);
	}
}
