/*
 * Created on 2003/12/01
 * $Id: _LMS.java,v 1.1 2003/12/03 06:40:24 wakita Exp $
 */
package jp.ac.titech.is.wakitalab.color;

import jp.ac.titech.is.wakitalab.math.Matrix;

/**
 * @author Ken Wakita
 * @version Experimental, 2003/12/01
 */
abstract class _LMS extends SimpleColorSpace {

	public double L, M, S;

    public LMS LMS() { return (LMS)this; }
	
	private static final Matrix toXYZ =
		new Matrix(2.76888, 1.75175, 1.13016,
			  1.00000, 4.59070, 0.06010,
		  	  0.00000, 0.05651, 5.59427);
		  
	private static final Matrix fromXYZ = toXYZ.inverse();
    
	private static final Matrix toRGB =
		new Matrix(0.214808, 0.751035, 0.045156,
			  0.022882, 0.940534, 0.076827,
			  0.000000, 0.016500, 0.999989);
	
	static private final double[] v = new double[3];

	protected void convertFrom(XYZ xyz) {
		fromXYZ.times(xyz.X, xyz.Y, xyz.Z, v);
		L = v[0]; M = v[1]; S = v[2];
	}

	protected void convertTo(XYZ xyz) {
		toXYZ.times(L, M, S, v);
		xyz.X = v[0]; xyz.Y = v[1]; xyz.Z = v[2];
	}

	public CIERGB RGB() {
		double r[] = toRGB.times(L, M, S);
		double max = Math.max(Math.max(r[0], r[1]), r[2]);
		if (max <= 1.0) return new CIERGB(r[0], r[1], r[2]);
		return new CIERGB(r[0] / max, r[1] / max, r[2] / max);
	}
}
