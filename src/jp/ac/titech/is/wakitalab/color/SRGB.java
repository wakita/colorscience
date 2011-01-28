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
    
    @Override
    public SRGB getDichromatColor(VisionType t){
        return LMS().getDichromatColor(t).SRGB();
    }
    
    /* _Vector3D@shimamuraから拝借。RGBをINTに直す_ */
    public int getIntegerExpression(){
        int v1 = (int)(R * 255.0);
        int v2 = (int)(G * 255.0);
        int v3 = (int)(B * 255.0);
        if(v1 < 0){
            v1 = 0;
        }
        else if(v1 > 255){
            v1 = 255;
        }
        if(v2 < 0){
            v2 = 0;
        }
        else if(v2 > 255){
            v2 = 255;
        }
        if(v3 < 0){
            v3 = 0;
        }
        else if(v3 > 255){
            v3 = 255;
        }
        return ( 0xff000000 | (v1 << 16) | (v2 << 8) | v3);
    }
}
