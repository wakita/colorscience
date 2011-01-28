package jp.ac.titech.is.wakitalab.color;
import jp.ac.titech.is.wakitalab.math.*;


abstract class _SRGB extends SimpleColorSpace {

    /** Red component */

    public /* final */ double R;

    /** Green component */

    public /* final */ double G;

    /** Blue component */

    public /* final */ double B;
    protected void _initialize(double R, double G, double B) {
        this.R = R;
        this.G = G;
        this.B = B;
    }

    public String toString() {
        return String.format("SRGB(%1.2f, %1.2f, %1.2f)", R, G, B);
    }

    public XYZ XYZ() {
        XYZ xyz = new XYZ();
        convertTo(xyz);
        return xyz;
    }

    private static final double defaultGamma = 2.4;

    protected void convertFrom(XYZ xyz, double gamma) {
        double[] v = new double[3];
        _LinearRGB.fromXYZ.times(xyz.X, xyz.Y, xyz.Z, v);
        double g = gamma;
        for(int i=0; i<v.length; i++){
            v[i] = v[i] > 0.0031308?
                    Math.pow(v[i],1/g) * 1.055 - 0.055 : v[i] * 12.92;
        }
        _initialize(v[0],v[1], v[2]);
    }

    protected void convertFrom(XYZ xyz) {
        convertFrom(xyz, defaultGamma);
    }

    protected void convertTo(XYZ xyz, double gamma) {
        double g = gamma;
        double[] rgb = new double[3];
        rgb[0] = R; rgb[1] = G; rgb[2] = B;
        double[] v = new double[3];
        for(int i=0; i<rgb.length; i++){
            rgb[i] = rgb[i] > 0.04045 ? Math.pow((rgb[i]+0.055)/1.055, g) : rgb[i]/12.92;
        }
        _LinearRGB.toXYZ.times(rgb[0],rgb[1],rgb[2], v);

        xyz.X = v[0]; xyz.Y = v[1]; xyz.Z = v[2];
    }

    protected void convertTo(XYZ xyz) {
        convertTo(xyz, defaultGamma);
    }
}
