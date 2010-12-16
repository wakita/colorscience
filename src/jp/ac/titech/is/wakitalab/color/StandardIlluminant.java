/*
 * Created on 2003/12/01
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jp.ac.titech.is.wakitalab.color;

/**
 * @author wakita
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */


public class StandardIlluminant extends Illuminant {

    /**
     * @param low
     * @param high
     * @param step
     * @param radiance
     */
    public StandardIlluminant(int low, int high, int step, double[] radiance) {
        super(low, high, step, radiance);
    }
    
    public static void main(String args[]) {
    	double Tc = 6500;
        double xd =
            Tc < 7000
                ? -4.6070e9 / (Tc * Tc * Tc) + 2.9678e6 / (Tc * Tc) + 0.09911e3 / Tc
                : 02.0064e9 / (Tc * Tc * Tc) + 1.9018e6 / (Tc * Tc) + 0.24748e3 / Tc;
        double yd = -3.000 * xd * xd + 2.870 * xd - 0.275;
    	System.out.println("xd = " + xd + ", yd = " + yd);
    	
//    	Gnuplot gnuplot = new Gnuplot();
//		gnuplot.title("D65");
//		gnuplot.xlabel("wavelength (nm)");
//		gnuplot.ylabel("Relative spectral luminance");
//		gnuplot.beginPlot();
//		for (int l = 300; l < 850; l += 10) {
//			
//		}
//		gnuplot.endPlot();
//		gnuplot.flush();
    }
}
