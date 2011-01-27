package jp.ac.titech.is.wakitalab.color;

import jp.ac.titech.is.wakitalab.math.*;


import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;


public class SRGBtoLMSTest {
	Random rand = new Random();

	private boolean similarEqual(double a, double b){
		return Math.abs(a-b) < 1E-8;
	}

	private double r() { return rand.nextDouble(); }

	@Test public void testConvertSRGBToLMSXYZ() {
		SRGB srgb = new SRGB(r(), r(), r());
		LMS lms = srgb.LMS();
		SRGB srgb2 = lms.SRGB();
		assertTrue(similarEqual(srgb.R,srgb2.R)
				&& similarEqual(srgb.G,srgb2.G)
				&& similarEqual(srgb.B,srgb2.B));
	}
}
