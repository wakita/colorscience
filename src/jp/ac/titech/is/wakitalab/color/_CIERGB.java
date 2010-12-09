package jp.ac.titech.is.wakitalab.color;

import jp.ac.titech.is.wakitalab.math.Matrix;

abstract class _CIERGB extends AbstractRGB {

    static final Matrix fromXYZ =
        new Matrix(+0.41846, -0.15860, -0.08283,
                -0.09117, +0.25243, +0.01571,
                +0.00092, -0.00255, +0.17860);
    static final Matrix toXYZ = fromXYZ.inverse();

    protected String shortName() {
        return "CIE RGB";
    }
    
    private static final Matrix toLMS =
        new Matrix(+5.088288, -4.064546, +0.082501,
              -0.123959, +1.163679, -0.083805,
              +0.002045, -0.019201, +1.001394);
    
    public LMS LMS() {
        double lms[] = toLMS.times(R, G, B);
        return new LMS(lms[0], lms[1], lms[2]);
    }

    protected void convertFrom(XYZ xyz) {
        convertFrom(fromXYZ, xyz);
    }

    protected void convertTo(XYZ xyz) {
        convertTo(toXYZ, xyz);
    }
}
