package jp.ac.titech.is.wakitalab.color;

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
	
	public String toString() {
		return "LMS(" + L + ", " + M + ", " + S + ")";
	}
	
	public LMS(XYZ c) {
		convertFrom(c);
	}

	public XYZ XYZ() {
		return RGB().XYZ();
	}
}
