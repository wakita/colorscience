package jp.ac.titech.is.wakitalab.color;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;


public class LinearRGBTest {
	Random rand = new Random();

	private boolean similarEqual(double a, double b){
		return Math.abs(a-b) < 1E-8;
	}

	private double r() { return rand.nextDouble(); }

	@Test public void testConvertToFromXYZ() {
		LinearRGB l_rgb = new LinearRGB(r(), r(), r());
		LinearRGB l_rgb2 = l_rgb.XYZ().LinearRGB();
		assertTrue(similarEqual(l_rgb.R, l_rgb2.R)
				&& similarEqual(l_rgb.G, l_rgb2.G)
				&& similarEqual(l_rgb.B, l_rgb2.B));
	}
}
