package jp.ac.titech.is.wakitalab.color;

public final class CIELabLight extends _CIELab {
	/** Construct an instance from given L<sup>*</sup>, a<sup>*</sup>, and b<sup>*</sup> compnents.
	 * 
	 * @param L L<sup>*</sup> component
	 * @param a a <sup>*</sup> component
	 * @param b b <sup>*</sup> component
	 * 
	 */
	public CIELabLight(double L, double a, double b) {
		_initialize(L, a, b);
	}

	/**
	 * Construct an instance from CIE XYZ representation.
	 *
	 * @param c A color represented in the CIE XYZ color space.
	 * @param w The color of a nominal white
	 */

	public CIELabLight(XYZ c, XYZ w) {
		convertFrom(c, w);
	}

	/**
	 * Constructor without white specification.
	 * <code>Illuminant.WhiteD65</code> is used as a default nominal white.
	 *
	 * @param c A color represented in the CIE XYZ color space.
	 */

	public CIELabLight(XYZ c) {
		this(c, nominalWhite);
	}
	
	protected void convertTo(XYZ c, XYZ w) {
		c.Y = luminanceLight(L, w.Y);
		double fY = fLight(c.Y, w.Y);
		c.X = finvLight(a / 500 + fY, w.X);
		c.Z = finvLight(fY - b / 200, w.Z);
	}
	
	protected void convertTo(XYZ c) {
		convertTo(c, getNominalWhite());
	}

	/**
	 * Converts the color represented by this object into the CIE XYZ
	 * representation.
	 *
	 * @param w Nominal white represented in the CIE XYZ color space.
	 * @return The CIE XYZ representation for this color.
	 */

	public final XYZ XYZ(XYZ w) {
		XYZ c = new XYZ();
		convertTo(c, w);
		return c;
	}

	protected void convertFrom(XYZ c, XYZ w) {
		double L = 116 * fLight(c.Y, w.Y) - 16;
		double a = 500 * (fLight(c.X, w.X) - fLight(c.Y, w.Y));
		double b = 200 * (fLight(c.Y, w.Y) - fLight(c.Z, w.Z));
		_initialize(L, a, b);
	}
	
	public CIELab CIELab() {
		return new CIELab(L, a, b);
	}
}
