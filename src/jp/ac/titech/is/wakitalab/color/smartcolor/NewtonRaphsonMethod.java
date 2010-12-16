package jp.ac.titech.is.wakitalab.color.smartcolor;

/**
 * $Id: NewtonRaphsonMethod.java,v 1.4 2003/11/26 13:00:39 wakita Exp $
 * Created on 2003/08/24
 * @author Ken Wakita
 */

public class NewtonRaphsonMethod {
	GrayCoordination solve(int loop, Desire f, double e) {
		int n = f.dimension();
		
		GrayCoordination gray = new GrayCoordination(n);
		
		double delta;
		do {
			delta = 0.0;
			for (int i = 0; i < n; i++) {
				double g = gray.get(i).L - f.base(gray) / f.derivative(gray, i);
				g = Math.min(Math.max(g, 0.0), 100.0);
				delta += Math.abs(g - gray.get(i).L);
				gray.set(i, g);
			}
		} while (loop-- > 0 && delta > e);
		
		if (delta < e) return gray;
		else return null;
	}
}
