package jp.ac.titech.is.wakitalab.color;

import java.io.*;

import jp.ac.titech.is.wakitalab.*;

import org.junit.*;

/**
 * Created on 2003/11/23
 * @author Ken Wakita
 * @version Experimental, 2003/11/23
 * $Id: CIELuv1960Test.java,v 1.1.1.1 2005/11/15 14:06:40 wakita Exp $
 */
public class CIELuv1960Test {

    @Test
    public void create() {
        CIELuv1960 c = new CIELuv1960(new XYZ(0, 0, 0));
    }
    
    //@Test
    public void Luv() throws IOException {
	    Gnuplot gnuplot = Gnuplot.startGnuplotWithPNG();
        gnuplot.title("The Lu'v' chromaticity diagram (CIE, 1960).");
        gnuplot.xlabel("u'");
        gnuplot.ylabel("v'");
        gnuplot.beginPlot();
        CMF cmf = CMF.fundamentals2deg;
        for (int l = cmf.low; l < cmf.high; l += cmf.step) {
            CIELuv1960 c = cmf.RGB(l).CIELuv1960();
            gnuplot.plot(c.u, c.v);
        }
        gnuplot.endPlot();
        gnuplot.test("???");
    }
}
