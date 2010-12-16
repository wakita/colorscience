package jp.ac.titech.is.wakitalab.color;

public abstract class SimpleColorSpace extends Color {
    /**
	 * Converts color representation from the current color space to CIE XYZ.
	 * Uniform color spaces such as CIELab and CIELuv requires nominal white.
	 * 
	 * @param w
	 *            Nominal white represented in the CIE XYZ color space. A simple
	 *            color space is defined independent from human cognition and
	 *            therefore nominal white is ignored in color space conversion.
	 * @return
	 */
    public XYZ XYZ(XYZ w /* Ignored */) {
    	return XYZ();
    }
}
