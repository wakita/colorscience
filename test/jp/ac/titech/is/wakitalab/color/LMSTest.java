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
	
	@Test public void testDichromatConversion() {
		JFrame frame = new JFrame("Dichromat View");
		Container pane = frame.getContentPane();
		pane.setLayout(new GridLayout(2,2));

		final int W = 256, H = 256;

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
			pane.add(canvas);
		}

		frame.setPreferredSize(new Dimension(W * 2 + 20, H * 2 + 20));
		frame.pack();
		frame.setVisible(true);
		
		System.out.print("Your input: ");
		Scanner scan = new Scanner(System.in);
		String s = scan.nextLine();
		System.out.println(s);
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
