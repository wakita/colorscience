package jp.ac.titech.is.wakitalab.color;

/**
 * $Id: CIELuv1960.java,v 1.4 2003/12/03 06:40:23 wakita Exp $
 * Created on 2003/09/25
 * @author Ken Wakita
 */

public class CIELuv1960 extends _CIELuv1960 {

	/**
	 * Construct an instance from an XYZ representation.
	 *
	 * @param c Color represented in the CIE XYZ color space.
	 * @param w Background white color represented in the CIE XYZ color space.
	 */

	public CIELuv1960(XYZ c, XYZ w) {
		convertFrom(c, getNominalWhite());
	}

	/**
	 * Construct an instance from an XYZ representation.
	 *
	 * @param c Color represented in the CIE XYZ color space.
	 */

	public CIELuv1960(XYZ c) {
		convertFrom(c, getNominalWhite());
	}
}
