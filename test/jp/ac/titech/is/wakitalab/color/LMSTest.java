package jp.ac.titech.is.wakitalab.color;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;


public class LMSTest {
	Random rand = new Random();
	
	private double r() { return rand.nextDouble(); }
	
	@Test public void testConvertToFromXYZ() {
		LMS lms = new LMS(r(), r(), r());
		LMS lms2 = lms.XYZ().LMS();
		assertTrue(lms2.equals(lms));
	}
}
