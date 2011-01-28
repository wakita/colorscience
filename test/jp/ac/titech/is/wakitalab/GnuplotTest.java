package jp.ac.titech.is.wakitalab;

import static org.junit.Assert.*;

import java.io.*;

import org.junit.*;

public class GnuplotTest {
    
    @Test
    public void initializationTest() {
        Gnuplot gnuplot = new Gnuplot();
        assertTrue(true);
        gnuplot.stop();
    }
    
    @Test
    public void plotTest1() throws IOException {
        Gnuplot gnuplot = Gnuplot.startGnuplotWithPNG();
        gnuplot.labels("X", "Y");

        gnuplot.beginPlot("sm csp t 'Y = sin(X)'");
        final int N = 60;
        for (int i = 0; i < N; i++) {
            double theta = 2 * Math.PI * i / N;
            gnuplot.plot(theta, Math.sin(theta));
        }
        gnuplot.endPlot();

        gnuplot.test();
    }
}
