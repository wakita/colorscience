package jp.ac.titech.is.wakitalab.color;

import jp.ac.titech.is.wakitalab.math.M;

/**
 * This is a dirty hack.  As the XYZ class cannot have a method named
 * XYZ (it is interpreted as a constructor), we create here an
 * abstract super class for XYZ so that the XYZ class can inherit XYZ
 * method from it.
 *
 * $Id: _XYZ.java,v 1.3 2003/11/26 13:00:39 wakita Exp $
 * @author Ken Wakita
 * @version Experimental, Jan 19, 2003
 */

abstract class _XYZ extends SimpleColorSpace {

    /** X component of CIE XYZ representation */
    public double X;

    /** Y component of CIE XYZ representation */
    public double Y;

    /** Z component of CIE XYZ representation */
    public double Z;
    
    public String toString() {
    	return String.format("XYZ(%1.2f, %1.2f, %1.2f)", X, Y, Z);
    }
    
    protected void _initialize(double X, double Y, double Z) {
    	assert -M.verySmall <= X && X <= 1.0 + M.verySmall;
    	assert -M.verySmall <= Y && Y <= 1.0 + M.verySmall;
    	assert -M.verySmall <= Z && Z <= 1.0 + M.verySmall;
    	this.X = X; this.Y = Y; this.Z = Z;
    }
    
    /**
     * Conversion to CIE XYZ simply returns itself as we already have
     * a color represented in CIE XYZ.
     */
    public XYZ XYZ() { return (XYZ)this; }
    

	protected void convertFrom(XYZ c) {
		_initialize(c.X, c.Y, c.Z);
	}

	protected void convertTo(XYZ c) {
		c.X = X; c.Y = Y; c.Z = Z;
	}
}
