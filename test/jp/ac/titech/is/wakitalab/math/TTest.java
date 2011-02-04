package jp.ac.titech.is.wakitalab.math;

import static org.junit.Assert.*;

import org.junit.*;

/**
 * $Id: TTest.java,v 1.2 2005/11/15 14:32:42 wakita Exp $
 * Created on 2003/11/22
 * @author Ken Wakita
 * @version Experimental, Nov 22, 2003
 */

public class TTest {
	
	private static final java.util.Random random = new java.util.Random();
    private static double r() {
        return random.nextDouble();
    }

    private double x = r(), y = r(), z = r();
    
    private Matrix Id = Matrix.I, N = new Matrix(1, 2, 3, 4, 5, 6, 7, 8, 10), trand = new Matrix(r(), r(), r(), r(), r(), r(), r(), r(), r());
	
	static private boolean equals(double x, double y) { return M.equals(x, y); }

    @Test
    public void testtimes() {
        double r[] = Id.times(x, y, z);
        assertTrue(equals(r[0], x));
        assertTrue(equals(r[1], y));
        assertTrue(equals(r[2], z));

        r = N.times(x, y, z);
        assertTrue(equals(r[0], N.t11 * x + N.t12 * y + N.t13 * z));
        assertTrue(equals(r[1], N.t21 * x + N.t22 * y + N.t23 * z));
        assertTrue(equals(r[2], N.t31 * x + N.t32 * y + N.t33 * z));
    }

    @Test
    public void testPlus() {
        Matrix test = N;
        assertTrue(test.plus(test).equals(test.times(2))); // T + T = 2 * T
    }
    
    @Test
    public void testTimes() {
    	// T times(double)
    	Matrix test = new Matrix(1, 2, 3, 4, 5, 6, 7, 8, 9);
    	assertTrue(test.times(2).equals(new Matrix(2, 4, 6, 8, 10, 12, 14, 16, 18)));
    	
    	// void times(double, double, double)
		double r[] = Id.times(x, y, z);
		assertTrue(equals(r[0], x));
		assertTrue(equals(r[1], y));
		assertTrue(equals(r[2], z));
		
		r = N.times(x, y, z);
		assertTrue(equals(r[0], N.t11 * x + N.t12 * y + N.t13 * z));
		assertTrue(equals(r[1], N.t21 * x + N.t22 * y + N.t23 * z));
		assertTrue(equals(r[2], N.t31 * x + N.t32 * y + N.t33 * z));
    	
    	// T times(T)
    	test = trand;
    	assertTrue(test.equals(Id.times(test)));            // T = I * T
    	assertTrue(test.equals(test.times(Id)));            // T = T * I
    	assertTrue(Id.equals(test.times(test.inverse())));  // I = T * T^{-1}
    	assertTrue(Id.equals(test.inverse().times(test)));  // I = T^{-1} * T
    }

    @Test
    public void testInverse() {
    	assertTrue(Id.equals(Id.inverse()));
    	
    	Matrix test = N;
    	Matrix inv = test.inverse();
    	
    	// double a, b, c;
    	double r[] = inv.times(test.t11, test.t21, test.t31);
    	assertTrue(equals(r[0], 1));
		assertTrue(equals(r[1], 0));
		assertTrue(equals(r[2], 0));
    	
		r = test.times(x, y, z);
    	r = inv.times(r[0], r[1], r[2]);
		assertTrue(equals(r[0], x));
		assertTrue(equals(r[1], y));
		assertTrue(equals(r[2], z));
    	
    	assertTrue(test.equals(inv.inverse()));  // T = (T^{-1})^{-1}
    }
}
