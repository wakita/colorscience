package jp.ac.titech.is.wakitalab.color;

import jp.ac.titech.is.wakitalab.math.Matrix;

abstract class _LinearRGB extends AbstractRGB {

    static final Matrix fromXYZ =
        new Matrix(
                3.5064, -1.0690, 0.0563,
                -1.7400, 1.9777, -0.1970,
                -0.5441, 0.0352, 1.0511);
    static final Matrix toXYZ = fromXYZ.inverse();

    protected String shortName() {
        return "L'RGB";
    }
    
    protected void convertFrom(XYZ xyz) {
        convertFrom(fromXYZ, xyz);
    }

    protected void convertTo(XYZ xyz) {
        convertTo(toXYZ, xyz);
    }
}
