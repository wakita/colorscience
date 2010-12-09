/*
 * Created on 2003/12/01
 * $Id: _CIELuv1976.java,v 1.1 2003/12/03 06:40:23 wakita Exp $
 */
package jp.ac.titech.is.wakitalab.color;

/**
 * @author Ken Wakita
 * @version Experimental, 2005/11/08
 */
abstract class _CIELuv1976 extends UniformColorSpace {

    /** L<sup>*</sup> (lightness) component */
    public double L;

    /** u<sup>*</sup> component */
    public double u;

    /** v<sup>*</sup> component */
    public double v;

    public String toString() {
        return "Luv1976(" + L + ", " + u + ", " + v + ")";
    }

    protected void _initialize(XYZ c, XYZ w) {
        convertFrom(c, w);
    }

    public CIELuv1976 CIELuv1976() {
        return (CIELuv1976) this;
    }

    protected void convertFrom(XYZ c, XYZ w) {
        L = f(c.Y, w.Y);

        double b = c.X + 15 * c.Y + 3 * c.Z;
        double b_w = w.X + 15 * w.Y + 3 * w.Z;
        double u = 4 * c.X / b, u_w = 4 * w.X / b_w;
        double v = 9 * c.Y / b, v_w = 4 * w.Y / b_w;

        u = 13 * L * (u - u_w);
        v = 13 * L * (v - v_w);
    }

    protected void convertFrom(XYZ c) {
        convertFrom(c, nominalWhite);
    }

    protected void convertTo(XYZ c, XYZ w) {
        double Y = Color.luminance(L, w.Y);
        double b_w = w.X + 15 * w.Y + 3 * w.Z;
        double u_w = 4 * w.X / b_w, v_w = 4 * w.Y / b_w;

        double u1 = u / (13 * L) + u_w, v1 = v / (13 * L) + v_w;
        c.X = 9 * u1 / (4 * v1) * Y;
        c.Y = Y;
        c.Z = (12 - 3 * u1 - 20 * v1) / (4 * v1) * Y;
    }

    protected void convertTo(XYZ c) {
        convertTo(c, nominalWhite);
    }
}
