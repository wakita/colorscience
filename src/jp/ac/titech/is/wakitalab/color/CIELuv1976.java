package jp.ac.titech.is.wakitalab.color;

/**
 * CIE 1976 (L<sup>*</sup>u<sup>*</sup>v<sup>*</sup>) space
 *
 * $Id: CIELuv1976.java,v 1.4 2003/12/03 06:40:24 wakita Exp $
 * @author Ken Wakita
 * @version Experimental, Jan 19, 2003
 */

public class CIELuv1976 extends _CIELuv1976 {

    /** Constructor */
    public CIELuv1976(double L, double u, double v) {
        this.L = L; this.u = u; this.v = v;
    }

    /**
     * Construct an instance from CIE XYZ representation.
     *
     * @param c A color represented in the CIE XYZ color space.
     * @param w Nominal white represented in the CIE XYZ color space.
     */

    public CIELuv1976(XYZ c, XYZ w) {
    	_initialize(c, w);
    }

    /**
     * Constructor without white specification.
     * <code>Illuminant.WhiteD65</code> is used as a default nominal white.
     *
     * @param c A color represented in the CIE XYZ color space.
     */

    public CIELuv1976(XYZ c) {
        this(c, nominalWhite);
    }
}
