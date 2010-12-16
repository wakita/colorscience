package jp.ac.titech.is.wakitalab.color;

/**
 * CIE L<sup>*</sup>a<sup>*</sup>b<sup>*</sup> color space
 *
 * $Id: CIELab.java,v 1.2 2003/12/03 06:40:23 wakita Exp $
 * @author Ken Wakita
 * @version Experimental, Jan 19, 2003
 */

public class CIELab extends _CIELab {

	/** Construct an instance from given L<sup>*</sup>, a<sup>*</sup>, and b<sup>*</sup> compnents.
	 * 
	 * @param L L<sup>*</sup> component
	 * @param a a <sup>*</sup> component
	 * @param b b <sup>*</sup> component
	 * 
	 */
	public CIELab(double L, double a, double b) {
		_initialize(L, a, b);
	}

	/**
	 * Construct an instance from CIE XYZ representation.
	 *
	 * @param c A color represented in the CIE XYZ color space.
	 * @param w The color of a nominal white
	 */

	public CIELab(XYZ c, XYZ w) {
		convertFrom(c, w);
	}

	/**
	 * Constructor without white specification.
	 * <code>Illuminant.WhiteD65</code> is used as a default nominal white.
	 *
	 * @param c A color represented in the CIE XYZ color space.
	 */

	public CIELab(XYZ c) {
		this(c, getNominalWhite());
	}
	
	public CIELabLight CIELabLight() {
		return new CIELabLight(L, a, b);
	}
}
