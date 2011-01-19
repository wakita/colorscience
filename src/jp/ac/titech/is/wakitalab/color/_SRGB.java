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

    //    public XYZ XYZ() {
    //        return null;
    //    }
    /* _http://www.enjoy.ne.jp/~k-ichikawa/CIEXYZ_RGB.html より_ */
    private static final Matrix toXYZ =
        new Matrix(0.4124,  0.3576, 0.1805,
                0.2126, 0.7152, 0.0722,
                0.0193, 0.1192, 0.9505);

    private static final Matrix fromXYZ = toXYZ.inverse();

    public XYZ XYZ() {
        double[] returnVector = new double[3];
        toXYZ.times(R,G,B, returnVector);
//        System.out.printf("RGB:%f, %f, %f \n",R,G,B);
//        System.out.printf("XYZ:%f, %f, %f \n",returnVector[0],returnVector[1],returnVector[2]);
        return new XYZ(returnVector[0], returnVector[1], returnVector[2]);
    }


    private static final double defaultGamma = 2.2;

    static private final double[] v = new double[3];

    private final double correct(double x, double gamma) {
        return Math.pow(x, gamma);
    }

    protected void convertFrom(XYZ xyz, double gamma) {
        _LinearRGB.fromXYZ.times(xyz.X, xyz.Y, xyz.Z, v);
        double g = gamma;
        //応急処置
//        v[0] = Math.abs(v[0]);
//        v[1] = Math.abs(v[1]);
//        v[2] = Math.abs(v[2]);
//        System.out.printf("convertFrom@_SRGB: v[0]:%f, v[1]:%f, v[2]:%f\n",v[0],v[1],v[2]);
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
