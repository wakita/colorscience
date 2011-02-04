package jp.ac.titech.is.wakitalab.color;

import static org.junit.Assert.*;

import java.awt.*;
import java.util.*;

import javax.swing.*;

import org.junit.*;


public class LMSTest {
	Random rand = new Random();

	private double r() { return rand.nextDouble(); }

	@Test public void testConvertToFromXYZ() {
		LMS lms = new LMS(r(), r(), r());
		LMS lms2 = lms.XYZ().LMS();
		assertTrue(lms2.equals(lms));
	}

	JLabel makeLabel(String title, GridBagLayout bag, GridBagConstraints constr) {
	    JLabel label = new JLabel(title);
	    bag.setConstraints(label, constr);
	    return label;
	}
	
	@Test public void testDichromatConversion() {
		JFrame frame = new JFrame("Dichromat View");
		Container pane = frame.getContentPane();
		final GridBagLayout gridBag = new GridBagLayout();
        final GridBagConstraints constr = new GridBagConstraints();
		pane.setLayout(gridBag);

		constr.fill = GridBagConstraints.BOTH;
		constr.weightx = 1.0;
		pane.add(makeLabel("標準画像", gridBag, constr));
        pane.add(makeLabel("第一色盲のシミュレーション", gridBag, constr));
        pane.add(makeLabel("第二色盲のシミュレーション", gridBag, constr));
        constr.gridwidth = GridBagConstraints.REMAINDER;
        pane.add(makeLabel("第三色盲のシミュレーション", gridBag, constr));

		final int W = 256, H = 256;

		constr.gridwidth = 1;
		for (VisionType type : VisionType.values()) {
			BitmapCanvas canvas = new BitmapCanvas();
			int[] pix = new int[W * H];
			for (int i = 0; i < pix.length; i++) {
				double w = (double)i % W, h = (double)i / W;
				double r = w / W, g = (1 - r) * h / H, b = 1 - r - g;
				SRGB c = new SRGB(r, g, b).getDichromatColor(type);
				pix[i] = getInt(c.R, c.G, c.B);
			}
			canvas.setImage(W, H, pix);
			canvas.setPreferredSize(new Dimension(W, H));
			if (type == VisionType.Trichromat) constr.gridwidth = GridBagConstraints.REMAINDER;
			gridBag.setConstraints(canvas, constr);
			pane.add(canvas);
		}

		frame.pack();
		frame.setVisible(true);

		assertEquals(JOptionPane.showConfirmDialog(null, "結果は満足のいくものですか？"),
				JOptionPane.YES_OPTION);
	}
	
	static int getInt(double x) {
		int r = (int)(x * 256);
		r = r < 0 ? 0 : r;
		return r > 255 ? 255 : r;
	}
	
    static int getInt(double r, double g, double b){
        return ( 0xff000000 | (getInt(r) << 16) | (getInt(g) << 8) | getInt(b));
    }
}
