package jp.ac.titech.is.wakitalab;

import static org.junit.Assert.*;
import jp.ac.titech.is.wakitalab.math.*;

import org.junit.*;

public class JUnitBasicTest {

	@Test
	public void test() {
		assertTrue(true);
	}

	public void assertEquals(double x, double y) {
	    Assert.assertEquals(x, y, M.verySmall);
	}
	
	@Test
	public void equalsTest() {
	    assertEquals(1.0, 1.0);
	}
}
