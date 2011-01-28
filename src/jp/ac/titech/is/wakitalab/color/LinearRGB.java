package jp.ac.titech.is.wakitalab.color;

public class LinearRGB extends _LinearRGB {

    public LinearRGB(double R, double G, double B) {
        _initialize(R, G, B);
    }

    /** Constructor
     *
     * Create a new RGB instance that corresponds to the CIE XYZ
     * representation.
     *
     * @param c A color represented in CIE XYZ
     */

    public LinearRGB(XYZ c) {
        convertFrom(c);
    }
    
    public LinearRGB getDichromatColor(VisionType t){
        return LMS().getDichromatColor(t).LinearRGB();
    }
    
}
