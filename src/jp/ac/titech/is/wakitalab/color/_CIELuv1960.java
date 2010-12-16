/*
 * Created on 2003/12/01
 * $Id: _CIELuv1960.java,v 1.1 2003/12/03 06:40:24 wakita Exp $
 */
package jp.ac.titech.is.wakitalab.color;

/**
 * @author Ken Wakita
 * @version Experimental, 2003/12/01
 */

abstract class _CIELuv1960 extends UniformColorSpace {

    /** L (lightness) component */
    public double L;

    /** u component */

    public double u;

    /** v component */

    public double v;

    protected void _initialize(double L, double u, double v) {
        this.L = L;
        this.u = u;
        this.v = v;
    }

    public String toString() {
        return "Luv1960(" + L + ", " + u + ", " + v + ")";
    }

    /**
	 * 
	 */

    protected void convertFrom(XYZ c, XYZ w) {
        double base = c.X + 15 * c.Y + 3 * c.Z;
        _initialize(f(c.Y, w.Y), 4 * c.X / base, 9 * c.Y / base);
    }

    public final void convertTo(XYZ c, XYZ w) {
        // Need implementation
    }

    public CIELuv1960 CIELuv1960() {
        return (CIELuv1960) this;
    }
}
