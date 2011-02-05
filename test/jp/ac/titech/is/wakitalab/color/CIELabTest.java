package jp.ac.titech.is.wakitalab.color;

import jp.ac.titech.is.wakitalab.*;
import jp.ac.titech.is.wakitalab.math.*;

import org.junit.*;

/**
 * Created on 2003/11/22
 * @author Ken Wakita
 * @version Experimental, 2003/11/22
 * $Id: CIELabTest.java,v 1.2 2005/11/16 11:17:31 wakita Exp $
 */
public class CIELabTest {

	private final Gnuplot gnuplot = new Gnuplot();

	@Test
    public void testF() {
        for (double l = 0; l < 0.04; l += 0.001) {}
        gnuplot.title(
            "Function f(s) for CIE L'u'v' and CIE L^*a^*b^* color space.");
        gnuplot.xlabel("Saturation");
        gnuplot.ranges("[0:0.04]", "[0.0:0.4]");
        gnuplot.beginPlot("sm csp t 'Cubert', '-' sm csp t 'f'");
        for (double s = 0; s <= 0.04; s += 0.001)
            gnuplot.plot(s, M.cubert(s));
        gnuplot.endPlot();
        for (double s = 0; s <= 0.04; s += 0.001)
            gnuplot.plot(s, Color.f(s, 1.0));
        gnuplot.endPlot();
        gnuplot.flush();
    }

    public void test(int f) {
        gnuplot.title("Chromacity diagram for the CIE L^*a^*b^* color space");
        gnuplot.println("set xrange [-400:400]");
        gnuplot.println("set yrange [-400:400]");
        gnuplot.println("set zrange [0:110]");
        gnuplot.println("splot '-'");
        CMF cmf = CMF.fundamentals2deg;
        for (int l = cmf.low; l <= cmf.high; l += cmf.step) {
            CIELab c = cmf.RGB(l).CIELab();
            gnuplot.println(c.L + ", " + c.a + ", " + c.b);
        }
        gnuplot.endPlot();
        gnuplot.flush();
    }
    
    public void showColor(String name, Color c) {
    	System.out.println(name + ": " + c.RGB() + ", " + c.XYZ());
    }
    
    @Test
    public void test2() {
		showColor("Black", new CIERGB(0, 0, 0));
		showColor("White", new CIERGB(1, 1, 1));
		showColor("Red", new CIERGB(1, 0, 0));
		showColor("Green", new CIERGB(0, 1, 0));
		showColor("Blue", new CIERGB(0, 0, 1));
		// BUG: The following should be tested!
		/*
		showColor("D55", Illuminant.D55.LMS().CIERGB());
		showColor("D65", Illuminant.D65.LMS().CIERGB());
		showColor("D75", Illuminant.D75.LMS().CIERGB());
		Color X65 = Illuminant.D65.LMS().CIERGB();
		System.out.println("X65: " + X65);
		System.out.println("X65: " + X65.XYZ());
		*/

    	gnuplot.title("RGB surface projected on the CIEL^*a^*b^* color space.");
    	gnuplot.println("splot '-'");
    	for (int i = 0; i <= 10; i += 1) {
    		double x = 0.1 * i;
    		for (int j = 0; j <= 10; j += 1) {
    			double y = 0.1 * j;
    			CIELab c = new CIERGB(x, y, 0).CIELab();
				gnuplot.println(c.L + ", " + c.a + ", " + c.b);
//				c = new RGB(x, y, 1).CIELab();
//				gnuplot.println(c.L + ", " + c.a + ", " + c.b);
				
				c = new CIERGB(0, x, y).CIELab();
				gnuplot.println(c.L + ", " + c.a + ", " + c.b);
//				c = new RGB(1, x, y).CIELab();
//				gnuplot.println(c.L + ", " + c.a + ", " + c.b);
				
				c = new CIERGB(y, 0, x).CIELab();
				gnuplot.println(c.L + ", " + c.a + ", " + c.b);
//				c = new RGB(y, 1, x).CIELab();
//				gnuplot.println(c.L + ", " + c.a + ", " + c.b);
    		}
    	}
    	gnuplot.endPlot();
    	gnuplot.flush();
    }
}
