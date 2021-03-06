package jp.ac.titech.is.wakitalab.color;

public final class SRGB extends _SRGB {

    /* _R, G, B must be between 0 and 1_ */

    public SRGB(double R, double G, double B) {
        _initialize(R, G, B);
    }

    /** Constructor
     *
     * Create a new SRGB instance that corresponds to the CIE XYZ
     * representation.
     *
     * @param c A color represented in CIE XYZ
     */

    public SRGB(XYZ c) {
        convertFrom(c);
    }

    /* _定義するのはここでいいのかよく分からない。_ */
    public SRGB getDichromatColor(VisionType t){
        return LMS().getDichromatColor(t).SRGB();
    }
}
