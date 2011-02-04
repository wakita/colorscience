package jp.ac.titech.is.wakitalab.color;

/**
 * LMS.java
 * Created on Sep. 24, 2003
 * @author Ken Wakita
 * @version Jan. 27, 2011
 */
public class LMS extends _LMS {
    /********************* _DichromatPlaneへの写像_ *********************/
    /* _@shimamura から持ってきた謎の変数たち_ */
    static final LMS gray = new LMS(0.1159786162144061, 0.09482136320070213, 0.05842422424151998);

    // おそらく Brettel の論文に記載されていると思います。
    public final static LMS[] anchor2deg = {
        new LMS(1.1882E-01,  2.05398E-01,  5.16411E-01),
        new LMS(9.92310E-01,  7.40291E-01,  1.75039E-04),
        new LMS(1.63952E-01,  2.68063E-01,  2.90322E-01),
        new LMS(9.30085E-02,  7.30255E-03,  0.0)};
    
	/* _ここまで_ */

    private static LMS outerProduct(LMS a, LMS b){
        return new LMS(
                a.M * b.S - a.S * b.M,
                a.S * b.L - a.L * b.S,
                a.L * b.M - a.M * b.L);
    }
    
    enum Anchors {
    	PROTANOPE_DEUTERANOPE_X, PROTANOPE_DEUTERANOPE_Y,
    	TRITANOPE_X, TRITANOPE_Y
    }
    
    static private final LMS[] anchors = new LMS[4];
    static {
    		for (int i = 0; i < anchors.length; i++)
    			anchors[i] = outerProduct(gray, anchor2deg[i]);
    }

	/**
	 * Construct a color represented in LMS color system.
	 * 
	 * @param l The L element of a color represented in the LMS color system.
	 * @param m The M element of a color represented in the LMS color system.
	 * @param s The S element of a color represented in the LMS color system.
	 */
	public LMS(double l, double m, double s) {
		L = l; M = m; S = s;
	}

	/**
	 * Construct a color represented in LMS color system that is represented in terms of chromatic stimulus function
	 * 
	 * @param stimulus A chromatic stimulus function.
	 */
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

	/**
	 * Equality test.  An LMS color is regarded equals to another LMS color if and only if all its elements are nearly equal.
	 * 
	 * @param c The color being compare to this color.
	 * @return
	 */
	public boolean equals(LMS c) {
	    return _eq(L, c.L) && _eq(M, c.M) && _eq(S, c.S);
	}

	public String toString() {
		return "LMS(" + L + ", " + M + ", " + S + ")";
	}

	/**
	 * Converts a color represented in CIE XYZ color space to one represented in LMS.
	 * @param c A CIE XYZ color.
	 */
	public LMS(XYZ c) {
		convertFrom(c);
	}

	public XYZ XYZ() {
	    XYZ c = new XYZ();
	    convertTo(c);
	    return c;
	}
	
	/**
	 * @param type
	 * @return
	 */
	public LMS getDichromatColor(VisionType type){
	    switch (type){
	    case Trichromat: return this.LMS();
	    case Protanope: return convertToProtanopeColor();
	    case Deuteranope: return convertToDeuteranopeColor();
	    case Tritanope: return convertToTritanopeColor();
	    default: return this.LMS();
	    }
	}

	private LMS convertToProtanopeColor() {
		Anchors an = S * gray.M < M * gray.S ? Anchors.PROTANOPE_DEUTERANOPE_X : Anchors.PROTANOPE_DEUTERANOPE_Y;
		LMS anchor = anchors[an.ordinal()];
		double l = - (anchor.M * M + anchor.S * S) / anchor.L;
		return new LMS(l, M, S);
	}

	private LMS convertToDeuteranopeColor() {
		Anchors an = S * gray.L < L * gray.S ? Anchors.PROTANOPE_DEUTERANOPE_X : Anchors.PROTANOPE_DEUTERANOPE_Y;
		LMS anchor = anchors[an.ordinal()];
		double m = -(anchor.L * L + anchor.S * S) / anchor.M;
		return new LMS(L, m, S);
	}

	private LMS convertToTritanopeColor() {
		Anchors an = M * gray.L > L * gray.M ? Anchors.TRITANOPE_X : Anchors.TRITANOPE_Y;
	    LMS anchor = anchors[an.ordinal()];
	    double s = -(anchor.M * M + anchor.L * L) / anchor.S;
	    return new LMS(L, M, s);
	}
	
}
