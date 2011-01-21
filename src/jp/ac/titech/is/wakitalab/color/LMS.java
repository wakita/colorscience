package jp.ac.titech.is.wakitalab.color;

import static jp.ac.titech.is.wakitalab.math.M.*;

/**
 * $Id: LMS.java,v 1.4 2003/12/03 06:40:23 wakita Exp $
 * Created on 2003/09/24
 * @author Ken Wakita
 * @version Experimental, Nov 22, 2003
 */

public class LMS extends _LMS {

	public LMS(double l, double m, double s) {
		L = l; M = m; S = s;
	}

	public LMS(PolychromaticStimulus stimulus) {
		L = M = S = 0;
		int wavelen = stimulus.low, step = stimulus.step;

		for (Stimulus.Iterator iter = stimulus.iterator(); iter.hasNext(); ) {
			double r = iter.next();
			double efficiency[] = Fundamental.f2deg.efficiency(wavelen);
			L += efficiency[0] * r;
			M += efficiency[1] * r;
			S += efficiency[2] * r;
			wavelen += step;
		}
	}
	
	private final boolean _eq(double x, double y) {
	    return jp.ac.titech.is.wakitalab.math.M.equals(x, y);
	}
	
	public boolean equals(LMS c) {
	    return _eq(L, c.L) && _eq(M, c.M) && _eq(S, c.S);
	}

	public String toString() {
		return "LMS(" + L + ", " + M + ", " + S + ")";
	}

	public LMS(XYZ c) {
		convertFrom(c);
	}

	/* _ Color.javaでabstractになっているメソッドをここで実装している。Color <- SimpleColorSpace <- _LMS <- LMS _ */
	public XYZ XYZ() {
		// return RGB().XYZ();
	    XYZ c = new XYZ();
	    convertTo(c);
	    return c;
	}
}
