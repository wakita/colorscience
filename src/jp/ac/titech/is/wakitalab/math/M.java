package jp.ac.titech.is.wakitalab.math;

/**
 * Useful mathematics
 *
 * $Id: M.java,v 1.2 2003/12/01 03:21:52 wakita Exp $
 * @author Ken Wakita
 * @version Experimental, Jan 19, 2003
 */

public class M {


	public static boolean equals(double x, double y) {
		return Math.abs(x - y) < M.verySmall;
	}

    /** Returns the cubic root of the argument value.  Unlike
     * java.lang.Math.pow, this implementation supports negative
     * values by preserving its sign.
     *
     * @param x Double value
     * @return Cubic root of <code>x</code>
     */

    public static double cubert(double x) {
        double v = Math.pow(Math.abs(x), 1.0 / 3);
        return x < 0 ? -v : v;
    }
    
    public static final double verySmall = 1.0e-4;
    
    public static double interpolate(double v[], int low, int high, int step, int x) {
    	assert low <= x;
    	assert x <= high;
    	int offset = x - low;
    	if (offset % step == 0) return v[offset / step];
    	double v1 = v[offset / step], v2 = v[offset / step + 1];
    	int diff = offset % step;
    	return (v1 * (step - diff) + v2 * diff) / step;
    }
    
    public static double interpolate(double v[], int low, int high, int step, int x, boolean zeroForOB) {
    	if (zeroForOB && (x < low || high < x)) return 0;
    	return interpolate(v, low, high, step, x);
    }
}
