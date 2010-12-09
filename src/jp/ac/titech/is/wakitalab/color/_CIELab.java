/*
 * Created on 2003/12/01
 * $Id: _CIELab.java,v 1.1 2003/12/03 06:40:23 wakita Exp $
 */
package jp.ac.titech.is.wakitalab.color;

/**
 * @author Ken Wakita
 * @version Experimental, 2003/12/01
 */
abstract class _CIELab extends UniformColorSpace {

	/** Maximum lightness */

	public static final double maxLightness = 100;

	public static final double maxDiff = 600;

	/* Minimum lightness */

    protected static boolean haveMinLightness = false;
	protected static double minLightness;
    
    public static double getMinLightness() {
        if (!haveMinLightness) {
            minLightness = new CIERGB(0, 0, 0).CIELab().L;
            haveMinLightness = true;
        }
        return minLightness;
    }

	// static final double minLightness = f(0);

	/** L<sup>*</sup> (lightness) component */
	public double L;

	/** a<sup>*</sup> component */
	public double a;

	/** b<sup>*</sup> component */
	public double b;
	
	void _initialize(double L, double a, double b) {
		this.L = L; this.a = a; this.b = b;
	}

	public String toString() {
		return "Lab(" + L + ", " + a + ", " + b + ")";
	}
	
	protected void convertTo(XYZ c, XYZ w) {
		c.Y = luminance(L, w.Y);
		double fY = f(c.Y, w.Y);
		c.X = finv(a / 500 + fY, w.X);
		c.Z = finv(fY - b / 200, w.Z);
	}
	
	protected void convertTo(XYZ c) {
		convertTo(c, getNominalWhite());
	}
	
	protected void convertFrom(XYZ c, XYZ w) {
		double L = 116 * f(c.Y, w.Y) - 16;
		double a = 500 * (f(c.X, w.X) - f(c.Y, w.Y));
		double b = 200 * (f(c.Y, w.Y) - f(c.Z, w.Z));
		_initialize(L, a, b);
	}

	/**
	 * Color difference between the color represented by this object and the given color.
	 *
	 * @param c A color.
	 * @return Color distance (Percepted color difference).
	 */

	public double distance(CIELab c) {
		double dL = L - c.L, da = a - c.a, db = b - c.b;
		return Math.sqrt(dL * dL + da * da + db * db);
	}
	
    public CIELab CIELab() { return (CIELab)this; }
    public CIELabLight CIELabLight() { return (CIELabLight)this; }
}
