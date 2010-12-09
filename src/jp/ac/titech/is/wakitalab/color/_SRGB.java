package jp.ac.titech.is.wakitalab.color;


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
        return null;
    }
    
    private static final double defaultGamma = 2.2;
    
    static private final double[] v = new double[3];
    
    private final double correct(double x, double gamma) {
        return Math.pow(x, gamma);
    }

    protected void convertFrom(XYZ xyz, double gamma) {
        _LinearRGB.fromXYZ.times(xyz.X, xyz.Y, xyz.Z, v);
        double g = gamma;
        _initialize(correct(v[0], 1/g), correct(v[1], 1/g), correct(v[2], 1/g));
    }
    
    protected void convertFrom(XYZ xyz) {
        convertFrom(xyz, defaultGamma);
    }
    
    protected void convertTo(XYZ xyz, double gamma) {
        double g = gamma;
        _LinearRGB.toXYZ.times(correct(R, g), correct(G, g), correct(B, g), v);
        xyz.X = v[0]; xyz.Y = v[1]; xyz.Z = v[2];
    }

    protected void convertTo(XYZ xyz) {
        convertTo(xyz, defaultGamma);
    }
}
