package jp.ac.titech.is.wakitalab.color;

/**
 * $Id: CIELUV.java,v 1.3 2003/11/26 13:00:39 wakita Exp $
 * @author Ken Wakita
 * @version Experimental, Jan 19, 2003
 */

public class CIELUV extends CIELuv1976 {
    public CIELUV(double L, double u, double v) { super(L, u, v); }

    public CIELUV(XYZ c, XYZ W) { super(c, W); }
}
