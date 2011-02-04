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
public class XYZTest {

    static {
        XYZ.initialize();
    }
    
    private void cmf(String title, CMF cmf, String message) throws IOException {
        Gnuplot gnuplot = Gnuplot.startGnuplotWithPNG();
		gnuplot.title(title);
		gnuplot.labels("Wavelength (nm)", "Color Matching Functions");
		gnuplot.xrange("[350:800]");
		
		gnuplot.beginPlot("sm csp t 'x', '-' sm csp t 'y', '-' sm csp t 'z'");
		for (int l = cmf.low; l <= cmf.high; l += cmf.step)
			System.out.println("l = " + l + ", " + cmf.RGB(l) + ", " + cmf.RGB(l).XYZ());
		for (int l = cmf.low; l <= cmf.high; l += cmf.step) gnuplot.plot(l, cmf.RGB(l).XYZ().X);
		gnuplot.endPlot();
		for (int l = cmf.low; l <= cmf.high; l += cmf.step) gnuplot.plot(l, cmf.RGB(l).XYZ().Y);
		gnuplot.endPlot();
		for (int l = cmf.low; l <= cmf.high; l += cmf.step) gnuplot.plot(l, cmf.RGB(l).XYZ().Z);
		gnuplot.endPlot();
		
		gnuplot.test(message);
    	
    }
    
    // @Test
    public void testCMF2deg() throws IOException {
        this.cmf("Color matching functions x(l), y(l), z(l) for a 2-deg field",
                XYZ.chromaticityCoordinate2deg,
                "???");
    }
    
    // @Test
	public void testCMF10deg() throws IOException {
        this.cmf("Color matching functions x(l), y(l), z(l) for a 2-deg field",
                XYZ.chromaticityCoordinate10deg,
                "Malacara, \"Color Vision and Colorimetry,\" p. 55, Figure 4.2 と見比べて下さい。");
	}
    
    @Test
    public void colorMatching2deg() throws IOException {
        cmf("Color-matching functions x(l), y(l), z(l) for a 2-deg field (CIE 1931).",
            CMF.fundamentals2deg,
            "Malacara, \"Color Vision and Colorimetry,\" p. 52, Figure 4.1 と見比べて下さい。");
    }

/*
    public void colorMatching10deg(int x) {
		cmf("Color-matching functions x(l), y(l), z(l) for a 2-deg field (CIE 1931).",
			CMF.fundamentals10deg);
	}
*/
    
    @Test
    public void chromaticityCoordinates2deg() throws IOException {
        cmf("Chromaticity coordinates x-y-z for spectrally pure colors for a 2-deg field (CIE 1931).",
                XYZ.chromaticityCoordinate2deg,
                "Malacara, \"Color Vision and Colorimetry,\" p. 59, Figure 4.4 と見比べて下さい。");
    }
	
	public void testChromaticityDiagram2deg() throws IOException {
        Gnuplot gnuplot = Gnuplot.startGnuplotWithPNG();
        
		gnuplot.title("Chromacity coordinates x(l), y(l), z(l) for spectrally pure colors for a 2-deg field (CIE 1931).");
		gnuplot.beginPlot("sm csp t 'chromaticity'");
		gnuplot.beginPlot();
		CMF cmf = CMF.fundamentals2deg;
		for (int l = cmf.low; l <= cmf.high; l += cmf.step) {
			XYZ c = cmf.RGB(l).XYZ();
			double b = c.X + c.Y + c.Z, x = c.X / b, y = c.Y / b;
			
			// b = 0 for \lambda = 780 and produces NaN for (x, y) values
			if (b > M.verySmall) gnuplot.plot(x, y);
			else System.out.println("l = " + l + ", " + c);
			
            // (X, Y, Z) for \lambda = 380 being too small and exhibits considerable cumulative error as you observe in the middle of the graph
			if (x < 0.4 && x > 0.3 && y > 0.3 && y < 0.4)
				System.out.println("l = " + l + ", " + c);
		}
		gnuplot.endPlot();
		
		gnuplot.test();
	}
}
