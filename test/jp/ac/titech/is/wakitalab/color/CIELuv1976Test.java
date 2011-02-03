package jp.ac.titech.is.wakitalab.color;

import jp.ac.titech.is.wakitalab.*;

import org.junit.*;

/**
 * Created on 2003/11/24
 * @author Ken Wakita
 * @version Experimental, 2003/11/24
 * $Id: CIELuv1976Test.java,v 1.1.1.1 2005/11/15 14:06:40 wakita Exp $
 */
public class CIELuv1976Test {

	private final Gnuplot gnuplot = new Gnuplot();

	@Test
	public void testLuv() {
		gnuplot.title("The L*u*v* chromaticity diagram (CIE, 1976).");
		gnuplot.xlabel("u*");
		gnuplot.ylabel("v*");
		gnuplot.beginPlot();
		CMF cmf = CMF.fundamentals2deg;
		for (int l = cmf.low; l < cmf.high; l += cmf.step) {
			CIELuv1976 c = cmf.RGB(l).CIELuv1976();
			gnuplot.plot(c.u, c.v);
		}
		gnuplot.endPlot();
		gnuplot.flush();
	}
}
