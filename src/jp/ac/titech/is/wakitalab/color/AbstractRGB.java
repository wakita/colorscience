package jp.ac.titech.is.wakitalab.color;

import jp.ac.titech.is.wakitalab.math.Matrix;

public abstract class AbstractRGB extends SimpleColorSpace {
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

    protected abstract String shortName();
    
    public String toString() {
        return String.format("%s(%1.2f, %1.2f, %1.2f)", shortName(), R, G, B);
    }
    
    static private final double[] v = new double[3];
    
    protected void convertFrom(Matrix fromXYZ, XYZ xyz) {
        fromXYZ.times(xyz.X, xyz.Y, xyz.Z, v);
        _initialize(v[0], v[1], v[2]);
    }

    protected void convertTo(Matrix toXYZ, XYZ xyz) {
        assert toXYZ != null;
        toXYZ.times(R, G, B, v);
        xyz.X = v[0]; xyz.Y = v[1]; xyz.Z = v[2];
    }

    /** Converts to the CIE XYZ color space.
     *
     * @return A new XYZ object that is a representation of the object
     * in the CIE XYZ color space.
     */

    public XYZ XYZ() {
        XYZ c = new XYZ();
        convertTo(c);
        return c;
    }
}
