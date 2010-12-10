package jp.ac.titech.is.wakitalab.color;

public abstract class UniformColorSpace extends Color {

	/**
     * Converts the color represented by this object into the CIE XYZ
     * representation.
     *
     * @param w Nominal white represented in the CIE XYZ color space.
     * @return The CIE XYZ representation for this color.
     */

    public XYZ XYZ(XYZ w) {
    	XYZ c = new XYZ();
    	convertTo(c, w);
    	return c;
    }

    /**
     * Conerts the color represented by this object in to the CIE XYZ
     * representation.  <code>XYZ.standardWhite</code> is used as the default nominal white.
     *
     * @return The CIE XYZ representation for this color.
     */

    public XYZ XYZ() {
        /* nominalWhite は Initialize クラス経由で初期化しなければならない */
        assert nominalWhite != null;
        return XYZ(nominalWhite);
    }

	abstract protected void convertTo(XYZ c, XYZ w);

	abstract protected void convertFrom(XYZ c, XYZ w);

	protected void convertFrom(XYZ c) {
		convertFrom(c, nominalWhite);
	}

	protected void convertTo(XYZ c) {
		convertTo(c, nominalWhite);
	}
}
