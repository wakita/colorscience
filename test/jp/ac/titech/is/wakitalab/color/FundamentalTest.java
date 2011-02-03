/*
 * Created on 2003/11/28
 * $Id: FundamentalTest.java,v 1.1.1.1 2005/11/15 14:06:40 wakita Exp $
 */
package jp.ac.titech.is.wakitalab.color;

import java.io.*;

import jp.ac.titech.is.wakitalab.*;

import org.junit.*;

/**
 * @author Ken Wakita
 * @version Experimental, 2003/11/28
 */
public class FundamentalTest {

    private void display(String title, String xlabel, String ylabel, Fundamental f, boolean logscale, String message) throws IOException {
        Gnuplot gnuplot = Gnuplot.startGnuplotWithPNG();

        gnuplot.title(title);
        gnuplot.labels(xlabel, ylabel);
        gnuplot.xrange("[350:850]");
        if (!logscale) gnuplot.yrange("[0.0:1.2]");

        // if (logscale) gnuplot.out.println("set logscale y");
        
        gnuplot.beginPlot("sm csp t 'L', '-' sm csp t 'M', '-' sm csp t 'S'");
        for (int cone = 0; cone < 3; cone++) {
            for (int l = f.low; l <= f.high; l += f.step) {
                double efficiency = f.efficiency(l)[cone];
                efficiency = logscale ? Math.log10(Math.max(efficiency, 1.0E-8)) : efficiency;
                gnuplot.plot(l, efficiency);
            }
            gnuplot.endPlot();
        }
        
        gnuplot.test(message);
    }
    
    @Test
    public void condFundamentalsLogarithmic() throws IOException {
        display("Spectral sensitivities (cone fundamentals) in a logarithmic scale.",
                "Wavelength (nm)", "Relative Sensitivity",
                Fundamental.f2deg, true,
                "Malacara, \"Color Vision and Colorimetry,\" p. 143, Figure 8.8 と見比べて下さい。");
    }
    
    @Test
    public void condFundamentalsLinear() throws IOException {
        display("Spectral sensitivities (cone fundamentals) in a linear scale.",
                "Wavelength (nm)", "Relative Sensitivity",
                Fundamental.f2deg, false,
                "Malacara, \"Color Vision and Colorimetry,\" p. 143, Figure 8.9 と見比べて下さい。");
    }

    // Figure 8.10 を再現しようとしたが失敗した。再現方法が不明。
    @Test
    public void testEfficiency() throws IOException {
        double LMmax = 0;

        Fundamental f = Fundamental.f2deg;
        for (int l = f.low; l < f.high; l += f.step) {
            double[] efficiency = f.efficiency(l);
            LMmax = Math.max(LMmax, efficiency[0] + efficiency[1]);
        }
        
        Gnuplot gnuplot = Gnuplot.startGnuplotWithPNG();

        gnuplot.title("L- and M-cone sensitivity with proper scaling.");
        gnuplot.labels("Wavelength (nm)", "Relative Sensitivity");
        gnuplot.ranges("[350:850]", "[0.0:1.0]");
        
        gnuplot.beginPlot("sm csp t 'L + M', '-' sm csp t 'M', '-' sm csp t 'L'");
        
        for (int l = f.low; l <= f.high; l += f.step) {
            double[] efficiency = f.efficiency(l);
            gnuplot.plot(l, (efficiency[0] + efficiency[1]) / LMmax);
        }
        gnuplot.endPlot();
        
        for (int cone = 0; cone <= 1; cone++) {
            for (int l = f.low; l <= f.high; l += f.step) {
                double efficiency = f.efficiency(l)[cone] / LMmax;
                gnuplot.plot(l, efficiency);
            }
            gnuplot.endPlot();
        }
        
        gnuplot.test("Malacara, \"Color Vision and Colorimetry,\" p. 144, Figure 8.10 と見比べて下さい。");

    }
}
