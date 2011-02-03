package jp.ac.titech.is.wakitalab.color;

import java.io.*;

import jp.ac.titech.is.wakitalab.*;

import org.junit.*;

/**
 * Created on 2003/11/22
 * @author Ken Wakita
 * @version Experimental, 2003/11/22
 * $Id: CMFTest.java,v 1.1.1.1 2005/11/15 14:06:40 wakita Exp $
 */
public class CMFTest {

    private void display(String title, String xlabel, String ylabel, CMF cmf, String message) throws IOException {
        Gnuplot gnuplot = Gnuplot.startGnuplotWithPNG();

        gnuplot.title(title);
        gnuplot.labels(xlabel, ylabel);
        
    	gnuplot.beginPlot("sm csp t 'Red', '-' sm csp t 'Green', '-' sm csp t 'Blue'");
        for (int l = cmf.low; l <= cmf.high; l += cmf.step) gnuplot.plot(l, cmf.RGB(l).R);
        gnuplot.endPlot();

        for (int l = cmf.low; l <= cmf.high; l += cmf.step) gnuplot.plot(l, cmf.RGB(l).G);
		gnuplot.endPlot();

        for (int l = cmf.low; l <= cmf.high; l += cmf.step) gnuplot.plot(l, cmf.RGB(l).B);
		gnuplot.endPlot();
		
		gnuplot.test(message);
    }

    @Test
    public void colorMatchingFunctions2deg() throws IOException {
        display("Color-matching functions r-g-b for a 2-deg field (CIE, 1931).",
                "Wavelength (nm)", "Color Matching Functions",
                CMF.fundamentals2deg, "Malacara, \"Color Vision and Colorimetry,\" p. 37, Figure 3.4 と見比べて下さい。");
    }
    
    @Test
    public void chromaticityCoordinates2deg() throws IOException {
        display("Chromaticity coordinates r-g-b for spectrally pure colors for a 2-deg field (CIE, 1931)",
                "Wavelength (nm)", "Chromaticity Coordinates",
                CMF.chromaticity2deg, "Malacara, \"Color Vision and Colorimetry,\" p. 41, Figure 3.7 と見比べて下さい。");
    }
    
    @Test
    public void chromaticityDiagram2deg() throws IOException {
        Gnuplot gnuplot = Gnuplot.startGnuplotWithPNG();
        
        gnuplot.title("A chromaticity diagram for r vs. g.");
        gnuplot.labels("r", "g");
        gnuplot.ranges("[-2.0:1.5]", "[-0.5:3.0]");
        gnuplot.out.println("set size square");
        
        gnuplot.beginPlot();
        CMF cmf = CMF.chromaticity2deg;
        for (int l = cmf.low; l <= cmf.high; l += cmf.step) {
            CIERGB rgb = cmf.RGB(l);
            gnuplot.plot(rgb.R, rgb.G);
        }
        gnuplot.endPlot();
        gnuplot.test("Malacara, \"Color Vision and Colorimetry,\" p.43, Figure 3.8 と見比べて下さい。");
    }
}
