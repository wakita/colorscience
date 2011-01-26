/*
 * Created on 2003/12/01
 * $Id: _LMS.java,v 1.1 2003/12/03 06:40:24 wakita Exp $
 */
package jp.ac.titech.is.wakitalab.color;

import jp.ac.titech.is.wakitalab.math.Matrix;

/**
 * @author Ken Wakita
 * @version Experimental, 2003/12/01
 */
abstract class _LMS extends SimpleColorSpace {

    public double L, M, S;

    public LMS LMS() { return (LMS)this; }

    /* _今記載されてるのは、RGBからXYZへの変換行列！！_ */
    /* _
     * 恐らく正しくは
     //        Matrix(0.155, 0.543, -0.0031,
    //              -0.155, 0.457, 0.0329,
    //              0.00000, 0.00000, 0.016);
     * の逆行列である。（上はXYZからLMSへの変換行列。toXYZにはLMSからXYZへの変換行列をセットする。）_
     *
     * Color Vision and Colorimetry, P146 (8.6)
     *     - Daniel Malacara
     *     fromXYZ = (0.236157, 0.826427, -0.045710
     *                -0.431117, 1.206922, 0.0090020
     *                0.040557, -0.019683, 0.486195);
     */

    // ORIGINAL
    	private static final Matrix toXYZ =
    		new Matrix(2.76888, 1.75175, 1.13016,
    			  1.00000, 4.59070, 0.06010,
    		  	  0.00000, 0.05651, 5.59427);

    	private static final Matrix fromXYZ = toXYZ.inverse();

/*
    private static final Matrix fromXYZ = new Matrix(0.236157, 0.826427, -0.045710,
            -0.431117, 1.206922, 0.0090020,
            0.040557, -0.019683, 0.486195);
    private static final Matrix toXYZ = fromXYZ.inverse();

*/
    private static final Matrix toRGB =
        new Matrix(0.214808, 0.751035, 0.045156,
                0.022882, 0.940534, 0.076827,
                0.000000, 0.016500, 0.999989);

    static private final double[] v = new double[3];

    protected void convertFrom(XYZ xyz) {
        fromXYZ.times(
                xyz.X,
                xyz.Y,
                xyz.Z,
                v);
        L = v[0]; M = v[1]; S = v[2];
    }

    protected void convertTo(XYZ xyz) {
        toXYZ.times(L, M, S, v);
        xyz.X = v[0]; xyz.Y = v[1]; xyz.Z = v[2];
    }

    public CIERGB RGB() {
        double r[] = toRGB.times(L, M, S);
        double max = Math.max(Math.max(r[0], r[1]), r[2]);
        CIERGB ciergb;
        if (max <= 1.0) ciergb =  new CIERGB(r[0], r[1], r[2]);
        ciergb = new CIERGB(r[0] / max, r[1] / max, r[2] / max);
        return ciergb;
    }



    /********************* _DichromatPlaneへの写像_ *********************/
    /* _@shimamura から持ってきた謎の変数たち_ */
    final static LMS gray = new LMS(0.1159786162144061,0.09482136320070213,0.05842422424151998);
    public final static LMS[] anchor2deg = {
        new LMS(1.1882E-01,  2.05398E-01,  5.16411E-01),
        new LMS(9.92310E-01,  7.40291E-01,  1.75039E-04),
        new LMS(1.63952E-01,  2.68063E-01,  2.90322E-01),
        new LMS(9.30085E-02,  7.30255E-03,  0.0)};
    private static LMS outerProduct(LMS a, LMS b){
        return new LMS(
                a.M * b.S - a.S * b.M,
                a.S * b.L - a.L * b.S,
                a.L * b.M - a.M * b.L);
    }
    static private LMS anchorForProtanAndDeutan = outerProduct(gray, anchor2deg[0]);
    static private LMS anchorForProtanAndDeutan2 = outerProduct(gray, anchor2deg[1]);
    static private LMS anchorForTritan= outerProduct(gray, anchor2deg[2]);
    static private LMS anchorForTritan2= outerProduct(gray, anchor2deg[3]);
    /* _ここまで_ */

    public LMS getDichromatColor(VisionType type){
        LMS returnLMS;
        switch (type){
        case Trichromat:
            returnLMS = this.LMS();
            break;
        case Protanope:
            returnLMS = convertToProtanopeColor();
            break;
        case Deuteranope:
            returnLMS = convertToDeuteranopeColor();
            break;
        case Tritanope:
            returnLMS = convertToTritanopeColor();
            break;
        default:
            returnLMS = this.LMS();
        break;
        }
        return returnLMS;
    }

    private LMS convertToProtanopeColor(){
        LMS anchor = anchorForProtanAndDeutan;
        LMS anchor2 = anchorForProtanAndDeutan2;
        double[] v = new double[3];
        LMS gray = _LMS.gray;
        if(S * gray.M < M * gray.S)
            v[0] = -(anchor2.M * M + anchor2.S * S) / anchor2.L;//575nm
        else
            v[0] = -(anchor.M * M + anchor.S * S) / anchor.L;//475nm
        v[1] = M;
        v[2] = S;
        return new LMS(v[0],v[1],v[2]);
    }

    private LMS convertToDeuteranopeColor(){
        LMS anchor = anchorForProtanAndDeutan;
        LMS anchor2 = anchorForProtanAndDeutan2;
        double[] v = new double[3];
        LMS gray = _LMS.gray;
        if(S * gray.L < L * gray.S)
            v[1] = -(anchor2.L * L + anchor2.S * S) / anchor2.M;//575nm
        else
            v[1] = -(anchor.L * L + anchor.S * S) / anchor.M;//475nm
        v[0] = L;
        v[2] = S;
        return new LMS(v[0],v[1],v[2]);
    }

    private LMS convertToTritanopeColor(){
        LMS anchor = anchorForTritan;
        LMS anchor2 = anchorForTritan2;
        double[] v = new double[3];
        LMS gray = _LMS.gray;
        if(M * gray.L < L * gray.M)
            v[2] = -(anchor2.M * M + anchor2.L * L) / anchor2.S;//660nm
        else
            v[2] = -(anchor.M * M + anchor.L * L) / anchor.S;//485nm
        v[0] = L;
        v[1] = M;
        return new LMS(v[0],v[1],v[2]);
    }




}
