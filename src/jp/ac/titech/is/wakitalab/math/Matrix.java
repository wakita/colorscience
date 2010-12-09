package jp.ac.titech.is.wakitalab.math;

/** A 3x3 cnversion matrix that transforms one color space element to
 * another for some linearly related color spaces
 *
 * $Id: Matrix.java,v 1.3 2003/12/01 03:21:52 wakita Exp $
 * @author Ken Wakita
 * @version Experimental, Nov 22, 2003
 */

import jp.ac.titech.is.wakitalab.math.M;

public class Matrix {

    /** elements of the matrix */

    public final double t11, t12, t13, t21, t22, t23, t31, t32, t33;

    public Matrix(double v11, double v12, double v13,
             double v21, double v22, double v23,
             double v31, double v32, double v33) {
        t11 = v11; t12 = v12; t13 = v13;
        t21 = v21; t22 = v22; t23 = v23;
        t31 = v31; t32 = v32; t33 = v33;
    }
    
    public String toString() {
    	return  "{ " + t11 + ", " + t12 + ", " + t13 + "\n  "
                     + t21 + ", " + t22 + ", " + t23 + ",\n  "
                     + t31 + ", " + t32 + ", " + t33 + " }";
    }
    
    private boolean equals(double x, double y) { return M.equals(x, y); }

    public boolean equals(Matrix t) {
        return equals(t11, t.t11) && equals(t12, t.t12) && equals(t13, t.t13)
            && equals(t21, t.t21) && equals(t22, t.t22) && equals(t23, t.t23)
            && equals(t31, t.t31) && equals(t32, t.t32) && equals(t33, t.t33);
    }

    /** Matrix addition T = T1 + T2 */
    
    public Matrix plus(Matrix t) {
        return new Matrix(t11 + t.t11, t12 + t.t12, t13 + t.t13,
                     t21 + t.t21, t22 + t.t22, t23 + t.t23,
                     t31 + t.t31, t32 + t.t32, t33 + t.t33);
    }
    
    /** Matrix multiplication.  T = f * T */
    
    public Matrix times(double f) {
    	return new Matrix(f * t11, f * t12, f * t13,
                     f * t21, f * t22, f * t23,
                     f * t31, f * t32, f * t33);
    }

	/** Matrix multiplication.  (r1, r2, r3) = T (v1, v2, v3) */

    public void times(double v1, double v2, double v3, double [] res) {
    	assert res.length == 3;
    	res[0] = t11 * v1 + t12 * v2 + t13 * v3;
    	res[1] = t21 * v1 + t22 * v2 + t23 * v3;
    	res[2] = t31 * v1 + t32 * v2 + t33 * v3;
    }

    public double[] times(double v1, double v2, double v3) {
    	double[] res = new double[3];
    	times(v1, v2, v3, res);
    	return res;
    }

	public double[] times(double vec[]) {
		assert vec.length == 3;
		return times(vec[0], vec[1], vec[2]);
	}
	
	public void times(double vec[], double res[]) {
		assert vec.length == 3;
		assert res.length == 3;
		times(vec[0], vec[1], vec[2], res);
	}
	
	/** Matrix maltiplication.  T = T1 * T2 */
    
    public Matrix times(Matrix t) {
    	return new Matrix(t11 * t.t11 + t12 * t.t21 + t13 * t.t31,
                     t11 * t.t12 + t12 * t.t22 + t13 * t.t32,
                     t11 * t.t13 + t12 * t.t23 + t13 * t.t33,
                     
                     t21 * t.t11 + t22 * t.t21 + t23 * t.t31,
                     t21 * t.t12 + t22 * t.t22 + t23 * t.t32,
                     t21 * t.t13 + t22 * t.t23 + t23 * t.t33,
                     
                     t31 * t.t11 + t32 * t.t21 + t33 * t.t31,
                     t31 * t.t12 + t32 * t.t22 + t33 * t.t32,
                     t31 * t.t13 + t32 * t.t23 + t33 * t.t33);
    }

    public Matrix inverse() {
        double det = t11 * t22 * t33 + t12 * t23 * t31 + t13 * t21 * t32
                   - t11 * t23 * t32 - t12 * t21 * t33 - t13 * t22 * t31;

        return new Matrix((t22 * t33 - t23 * t32) / det, (t32 * t13 - t33 * t12) / det, (t12 * t23 - t13 * t22) / det,
                     (t23 * t31 - t21 * t33) / det, (t33 * t11 - t31 * t13) / det, (t13 * t21 - t23 * t11) / det,
                     (t21 * t32 - t22 * t31) / det, (t31 * t12 - t32 * t11) / det, (t11 * t22 - t12 * t21) / det);
    }
}
