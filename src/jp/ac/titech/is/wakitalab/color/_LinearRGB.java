package jp.ac.titech.is.wakitalab.color;

import jp.ac.titech.is.wakitalab.math.Matrix;

abstract class _LinearRGB extends AbstractRGB {
    //
    // 先生の行列。
    // 少し違うのでhttp://www.motorwarp.com/koizumi/srgb.htmlに合わせるためコメントアウト
//        static final Matrix fromXYZ =
//            new Matrix(
//                    3.5064, -1.0690, 0.0563,
//                    -1.7400, 1.9777, -0.1970,
//                    -0.5441, 0.0352, 1.0511);
//        static final Matrix toXYZ = fromXYZ.inverse();
    static final Matrix toXYZ =
        new Matrix(0.4124,  0.3576, 0.1805,
                0.2126, 0.7152, 0.0722,
                0.0193, 0.1192, 0.9505);
    static final Matrix fromXYZ = toXYZ.inverse();

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
