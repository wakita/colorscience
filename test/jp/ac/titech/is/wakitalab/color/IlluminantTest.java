package jp.ac.titech.is.wakitalab.color;

import static org.junit.Assert.*;

import java.io.*;

import jp.ac.titech.is.wakitalab.*;

import org.junit.*;

/**
 * Created on 2003/11/23
 * 
 * @author Ken Wakita
 * @version Experimental, 2003/11/23
 */
public class IlluminantTest {
    Gnuplot gnuplot;
    
    /**
     * 標準光源のデータ (A, B, C, D55, D65, D75) を検査するためのテスト
     * BUG: 等エネルギー刺激値 (EQ_ENERGY) は現在は未定なためテストを省いている。
     * @throws IOException
     */
    @Test
    public void testConstants() throws IOException {
        final Illuminant[] illuminants = { Illuminant.A, Illuminant.B, Illuminant.C, Illuminant.D55, Illuminant.D65, Illuminant.D75, Illuminant.EQ_ENERGY };

        gnuplot = Gnuplot.startGnuplotWithPNG();

        gnuplot.title("標準光源の分光放射輝度 (Spectral Radiance)に関するテスト。");
        gnuplot.labels("Wavelength (nm)", "Spectral Radiance");
        gnuplot.beginPlot("sm csp t 'A', '-' sm csp t 'B', '-' sm csp t 'C', '-' sm csp t 'D55', '-' sm csp t 'D65', '-' sm csp t 'D75', '-' sm csp t 'EQN'");

        for (Illuminant illuminant : illuminants) {
            if (illuminant != null) {
                for (int l = illuminant.low; l < illuminant.high; l += illuminant.step) {
                    gnuplot.plot(l, illuminant.radiance(l));
                }
            }
            gnuplot.endPlot();
        }

        gnuplot.flush();

        gnuplot.test("Malacara, \"Color Vision and Colorimetry,\" p. 25, Figure 2.5 & Figure 2.6と見比べて下さい。");
    }
    
    /**
     * 標準光源のLMS値に関するテスト
     * Bug: 未実装
     */
    @Test
    public void testEqEnergy() {
        assertTrue(false);
        /*
        System.out.println("Equal energy stimulus = " + Illuminant.EQ_ENERGY.LMS());
        System.out.println("Equal energy stimulus = " + Illuminant.EQ_ENERGY.LMS().RGB());
        System.out.println("D65 = " + Illuminant.D65.LMS().RGB());
        */
    }
}
