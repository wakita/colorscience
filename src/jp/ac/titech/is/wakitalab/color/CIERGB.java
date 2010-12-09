package jp.ac.titech.is.wakitalab.color;

public final class CIERGB extends _CIERGB {

    public CIERGB(double R, double G, double B) {
        _initialize(R, G, B);
    }
    
    public CIERGB(PolychromaticStimulus stimulus) {
        double R = 0, G = 0, B = 0;
        // int wavelen = stimulus.low;
        // int step = stimulus.step;
        CMF fundamental = CMF.fundamentals2deg;

        int l = stimulus.low;
        for (Stimulus.Iterator iter = stimulus.iterator(); iter.hasNext(); ) {
            double r = iter.next();
            CIERGB rgb = fundamental.RGB(l);
            R += rgb.R * r;
            G += rgb.G * r;
            B += rgb.B * r;
            l += stimulus.step;
        }
        _initialize(R, G, B);
    }

    /** Constructor
     *
     * Create a new CIE RGB instance that corresponds to the CIE XYZ
     * representation.
     *
     * @param c A color represented in CIE XYZ
     */

    public CIERGB(XYZ c) {
        convertFrom(c);
    }
}
