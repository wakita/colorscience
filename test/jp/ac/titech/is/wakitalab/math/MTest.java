/*
 * Created on 2003/11/28
 * $Id: MTest.java,v 1.2 2005/11/15 14:32:42 wakita Exp $
 */
package jp.ac.titech.is.wakitalab.math;

import static org.junit.Assert.*;

import org.junit.*;

public class MTest {
    
    /*
     * Test for boolean equals(double, double)
     */
	
	@Test public void testEqualsdoubledouble() {}

    public void testCubert() {}
    
	private double v[] = new double[]{ 1, 2, 3, 4 };
	private double d = M.verySmall;

    /*
     * Test for double interpolate(double[], int, int, int, int)
     */
	@Test public void testInterpolatedoubleArrayintintintint() {
    	assertEquals(M.interpolate(v, 0, 3, 1, 0), 1, d);
		assertEquals(M.interpolate(v, 0, 3, 1, 3), 4, d);
  		assertEquals(M.interpolate(v, 10, 19, 3, 10), 1, d);
  		assertEquals(M.interpolate(v, 10, 19, 3, 16), 3, d);
  		assertEquals(M.interpolate(v, 10, 19, 3, 13), 2, d);
  		assertEquals(M.interpolate(v, 10, 19, 3, 11), 4.0/3, d);
	}
	
	@Test(expected = AssertionError.class)
	public void testInterpolatedoubleArrayintintintint1() {
	    M.interpolate(v, 10, 19, 3, 8);
	}

    /*
     * Test for double interpolate(double[], int, int, int, int, boolean)
     */
    @Test public void testInterpolatedoubleArrayintintintintboolean() {
		assertEquals(M.interpolate(v, 0, 3, 1, 0, true), 1, d);
		assertEquals(M.interpolate(v, 0, 3, 1, 3, true), 4, d);
		assertEquals(M.interpolate(v, 10, 19, 3, 10, true), 1, d);
		assertEquals(M.interpolate(v, 10, 19, 3, 16, true), 3, d);
		assertEquals(M.interpolate(v, 10, 19, 3, 13, true), 2, d);
		assertEquals(M.interpolate(v, 10, 19, 3, 11, true), 4.0/3, d);
		assertEquals(M.interpolate(v, 10, 19, 3, 8, true), 0, d);
    }
    
    @Test(expected = AssertionError.class)
    public void testInterpolatedoubleArrayintintintintboolean1() {
        assertEquals(M.interpolate(v, 10, 19, 3, 8, false), 1.0/3, d);
    }
}
