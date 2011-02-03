package jp.ac.titech.is.wakitalab.color;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;

public class ColorTest {
    
    static {
        Color.initialize();
    }
	
	static final double delta = 1.0e-5;
	static final Random rand = new Random();
	public double r() { return rand.nextDouble(); }
	static final XYZ nominalWhite = Color.getNominalWhite();
	
	public XYZ newXYZ() { return new XYZ(r(), r(), r()); }
	
	@Test
	public void testLMSconv() {
		XYZ c = newXYZ();
        assertTrue(c.equals(c.LMS().XYZ()));
	}

	@Test
	public void testCIERGBconv() {
		XYZ c = newXYZ();
		assertTrue(c.equals(c.CIERGB().XYZ()));
	}

	@Test
	public void testCIELabconv1() {
		XYZ c = new XYZ();
		System.out.printf("nominal white: %s\n", nominalWhite);
		System.out.printf("XYZ: %s\nCIELab: %s\nXYZ': %s\n", c, c.CIELab(nominalWhite), c.CIELab(nominalWhite).XYZ(nominalWhite));
		assertTrue(c.equals(c.CIELab(nominalWhite).XYZ(nominalWhite)));
	}

    @Test
    public void testCIELabconv2() {
        XYZ c = newXYZ();
        System.out.printf("nominal white: %s\n", nominalWhite);
        System.out.printf("XYZ: %s\nCIELab: %s\nXYZ': %s\n", c, c.CIELab(nominalWhite), c.CIELab(nominalWhite).XYZ(nominalWhite));
        assertTrue(c.equals(c.CIELab(nominalWhite).XYZ(nominalWhite)));
    }
	
/*	public void testLuminanceToLigitness() {
		
		assertEquals(Color.lightness(Color.luminance(1.0, 1.0)));
	}
*/
}
