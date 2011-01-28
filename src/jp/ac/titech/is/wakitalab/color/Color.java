package jp.ac.titech.is.wakitalab.color;

import jp.ac.titech.is.wakitalab.math.*;

/**
 * <p>Abstract representation of a color expressed in various color
 * space.  Subclasses of this abstract class implements color
 * representation of a color spaces.</p>
 *
 *  <p>Color conversion between different color spaces is offered by
 * methods whose name is the same as the target color space.  For
 * instance, suppose you have a color "<code>c</code>" represented in
 * RGB color space.  You have its CIE
 * L<sup>*</sup>A<sup>*</sup>B<sup>*</sup> color space representation
 * by "<code>c.CIELAB()</code>"</p>
 *
 * <p>This conversion is done by default via the CIE XYZ color space.
 * Therefore each color space is supposed to offer conversion to/from
 * CIE XYZ.</p>
 *
 * $Id: Color.java,v 1.5 2003/12/03 06:40:24 wakita Exp $
 * @author Ken Wakita
 * @version Experimental, Jan 17, 2003
 */

public abstract class Color {

	protected static XYZ nominalWhite;

    // Execution of Colo::initialize depends on execution of Illuminant.initialization
    static void initialize() {
        nominalWhite = Illuminant.WhiteD65.XYZ();
        assert nominalWhite != null;
    }

	/**
	 * @param nominalWhite
	 */
	public static void setNominalWhite(XYZ nominalWhite) {
		Color.nominalWhite = nominalWhite;
	}

	/**
	 * @return nominalWhite
	 */
	public static XYZ getNominalWhite() {
		return nominalWhite;
	}

	/**
	 * Converts color representation from the current color space to CIE XYZ.
	 */

    abstract public XYZ XYZ();

    /**
	 * Converts color representation from the current color space to CIE XYZ.
	 * Uniform color spaces such as CIELab and CIELuv requires nominal white.
	 *
	 * @param w
	 *            Nominal white represented in the CIE XYZ color space.
	 * @return
	 */
    public XYZ XYZ(XYZ w) {
    	return XYZ();
    }

    /**
	 * Method to convert a color represented in current color space to CIE RGB.
	 * Subclasses can override this method to offer more efficient conversion,
	 * bypassing conversion to CIE XYZ.
	 *
	 * @return XYZ
	 */

    public CIERGB RGB() {
        return CIERGB();
    }

    public CIERGB CIERGB() {
        return new CIERGB(XYZ());
    }

    public LinearRGB LinearRGB() {
        return new LinearRGB(XYZ());
    }

    public SRGB SRGB() {
        return new SRGB(XYZ());
    }

    public CIELab CIELab(XYZ nominalWhite) {
    	return new CIELab(XYZ(), nominalWhite);
    }

    /**
	 * Method to convert a color represented in current color space to CIE L<sup>*</sup>a<sup>*</sup>b<sup>*</sup>.
	 * Subclasses can override this method to offer more efficient conversion,
	 * bypassing conversion to CIE XYZ.
	 *
	 * @return RGB
	 */

    public CIELab CIELab() {
    	return new CIELab(nominalWhite);
    }

    public CIELuv1960 CIELuv1960() {
		return new CIELuv1960(XYZ());
	}

	public CIELuv1976 CIELuv1976() {
		return new CIELuv1976(XYZ());
	}

    /**
     * Method to convert color represented in current color space to LMS.
     * Subclasses can override this method to offer more efficient conversion,
     * bypassing convertion to CIE XYZ.
     *
     * @return LMS
     */
    public LMS LMS() {
        return new LMS(XYZ());
    }

    abstract protected void convertFrom(XYZ xyz);
    abstract protected void convertTo(XYZ xyz);

    /**
	 * Translate this color to other color space which specified by the type of
	 * "c". This is an imperative API for the functional style color conversion
	 * API.
	 *
	 * RGB c1 = new RGB(r, g, b); CIELab c2 = new CIELab(); c1.convert(c2);
	 *
	 * Caution: This method is NOT thread safe. Simultaneous conversion of two
	 * colors by independent threads can be dangerous.
	 */

    static final XYZ xyz = new XYZ(0, 0, 0);
    public void convert(Color c) {
    	this.convertTo(xyz);
    	c.convertFrom(xyz);
    }

    private static final double saturation_threshold = 0.008856;

    /**
     * Method to convert luminance into perceived lightness.
     *
     * @param Y Luminance of the subject.
     * @param Yn Luminance of the nominal white color (such as D65).
     * @return Lightness.
     */

    public static final double lightness(double Y, double Yn) {
        double s = Y / Yn;
        assert s >= 0 && s <= 1.0;
        if (s > saturation_threshold) return M.cubert(s);
        else return 7.787 * s + 16.0 / 116.0;
    }

    public static final double f(double Y, double Yn) {
    	return lightness(Y, Yn);
    }

    /**
     * An approximation of the "lightness" method.
     *
     * @param Y Luminance of the subject.
     * @param Yn Luminance of the nominal white color (such as D65).
     * @return Lightness.
     */

    public static final double lightnessLight(double Y, double Yn) {
        return M.cubert(Y / Yn);
    }

    public static final double fLight(double Y, double Yn) {
		return lightnessLight(Y, Yn);
	}

    /**
	 * Method to convert lightness into luminance; inverse of [f].
	 *
	 * @param L
	 *            Lightness of the subject.
	 * @param Yn
	 *            Luminance of the nominal white color (such as D65).
	 * @return Luminance.
	 */

    static double luminance(double L, double Yn) {
        double f = (L + 16.0) / 116.0;
        if (f > M.cubert(saturation_threshold))
            return Yn * Math.pow(f, 3);
        else
            return Yn * (f - 16.0 / 116.0) / 7.787;
    }

    /**
     * An approximation of "luminance" method.
     *
     * @param L
     * @param Yn
     * @return
     */

    static double luminanceLight(double L, double Yn) {
        return M.cubert(saturation_threshold);
    }

    static double finv(double L, double Yn) {
		return luminance(L, Yn);
	}

    static double finvLight(double L, double Yn) {
    	return luminanceLight(L, Yn);
    }
    
    /*
     * Any color space can be converted to a dichromat color space
     * via LMS color space.
     * @param t VisionType
     * @return Color which can be substituded to the name of an inherited class.
     */
    abstract Color getDichromatColor(VisionType t);
}
