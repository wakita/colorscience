package jp.ac.titech.is.wakitalab.color;

import java.io.*;

import jp.ac.titech.is.wakitalab.*;
import jp.ac.titech.is.wakitalab.math.*;

import org.junit.*;

/**
 * Created on 2003/11/22
 * @author Ken Wakita
 * @version Experimental, 2003/11/22
 */
public class CIERGBTest {

	@Test
	public void colorMatchingFunction() throws IOException {
	    Gnuplot gnuplot = Gnuplot.startGnuplotWithPNG();
        gnuplot.println("set grid");
		gnuplot.title("Color-matching functions r-g-b for a 2-deg field (CIE, 1931).");
		gnuplot.xlabel("Wavelength (nm)");
		gnuplot.beginPlot("sm csp t 'Red', '-' sm csp t 'Green', '-' sm csp t 'Blue'");
		CMF cmf = CMF.fundamentals2deg;
		for (int l = cmf.low; l <= cmf.high; l += cmf.step) gnuplot.plot(l, cmf.RGB(l).R);
		gnuplot.endPlot();
		for (int l = cmf.low; l <= cmf.high; l += cmf.step) gnuplot.plot(l, cmf.RGB(l).G);
		gnuplot.endPlot();
		for (int l = cmf.low; l <= cmf.high; l += cmf.step) gnuplot.plot(l, cmf.RGB(l).B);
		gnuplot.endPlot();
		
		gnuplot.test("Malacara, \"Color Vision and Colorimetry,\" p. 37, Figure 3.4 と見比べて下さい。");
	}

	@Test
	public void chromaticityCoordinates() throws IOException {
        Gnuplot gnuplot = Gnuplot.startGnuplotWithPNG();
        gnuplot.println("set grid");
		gnuplot.title("Chromacity coordinates r-g-b for spectrally pure colors for a 2-deg field (CIE, 1931)");
		gnuplot.xlabel("Wavelength (nm)");
		gnuplot.beginPlot("sm csp t 'Red', '-' sm csp t 'Green', '-' sm csp t 'Blue'");
		CMF cmf = CMF.fundamentals2deg;
		for (int l = cmf.low; l <= cmf.high; l += cmf.step) {
			CIERGB c = cmf.RGB(l);
			double s = c.R + c.G + c.B;
			if (s > M.verySmall) gnuplot.plot(l, c.R / s);
			else System.out.println("l = " + l + ", " + c);
		} 
		gnuplot.endPlot();
		for (int l = cmf.low; l <= cmf.high; l += cmf.step) {
			CIERGB c = cmf.RGB(l);
			double s = c.R + c.G + c.B;
			if (s > M.verySmall) gnuplot.plot(l, c.G / s);
		}
		gnuplot.endPlot();
		for (int l = cmf.low; l <= cmf.high; l += cmf.step) {
			CIERGB c = cmf.RGB(l);
			double s = c.R + c.G + c.B;
			if (s > M.verySmall) gnuplot.plot(l, c.B / s);
		}
		gnuplot.endPlot();
		
		gnuplot.test("Malacara, \"Color Vision and Colorimetry,\" p. 41, Figure 3.7 と見比べて下さい。");
	}
	
	@Test
	public void chromaticityDiagram() throws IOException {
	    Gnuplot gnuplot = Gnuplot.startGnuplotWithPNG();
	    gnuplot.println("set grid");

		gnuplot.title("A chromaticity diagram for r vs. g.");
		gnuplot.xlabel("r");
		gnuplot.ylabel("g");
		gnuplot.println("xrange [-2.0:1.5]");
		gnuplot.beginPlot();
		CMF cmf = CMF.fundamentals2deg;
		for (int l = cmf.low; l <= cmf.high; l += cmf.step) {
			CIERGB c = cmf.RGB(l);
			double s = c.R + c.G + c.B;
			if (s > M.verySmall) gnuplot.plot(c.R / s, c.G / s);
			else System.out.println("l = " + l + ", " + c);
		} 
		gnuplot.endPlot();
		gnuplot.test("Malacara, \"Color Vision and Colorimetry,\" p. 43, Figure 3.8 と見比べて下さい。");
	}
	
	@Test
	public void cube() throws IOException {
	    Gnuplot gnuplot = Gnuplot.startGnuplotWithPNG();
	    gnuplot.println("set grid");

		gnuplot.title("RGB surface.");
		gnuplot.println("splot '-'");
		for (int i = 0; i <= 10; i += 1) {
			double x = 0.1 * i;
			for (int j = 0; j <= 10; j += 1) {
				double y = 0.1 * j;
				CIERGB c = new CIERGB(x, y, 0);
				gnuplot.println(c.R + ", " + c.G + ", " + c.B);
//				c = new RGB(x, y, 1);
//				gnuplot.println(c.R + ", " + c.G + ", " + c.B);
				
				c = new CIERGB(0, x, y);
				gnuplot.println(c.R + ", " + c.G + ", " + c.B);
//				c = new RGB(1, x, y);
//				gnuplot.println(c.R + ", " + c.G + ", " + c.B);
				
				c = new CIERGB(y, 0, x);
				gnuplot.println(c.R + ", " + c.G + ", " + c.B);
//				c = new RGB(y, 1, x);
//				gnuplot.println(c.R + ", " + c.G + ", " + c.B);
			}
		}
		gnuplot.endPlot();
		gnuplot.test("???");
	}
}
