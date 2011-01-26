package jp.ac.titech.is.wakitalab.color;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import jp.ac.titech.is.wakitalab.math.*;
import static jp.ac.titech.is.wakitalab.color.VisionType.*;

public class TestForColorView {

    public static void main(String[] args) {

        JFrame frame = new JFrame("Dichromat View");
        frame.getContentPane().setLayout(new GridLayout(2,2));

        int w = 600, h = 400;
        int wh = w*h;

        /*_ Trichromat, Protanope, Deuteranope, Tritanope。順番はセーブされないみたい _*/
        for(VisionType type : VisionType.values()){

            BitmapCanvas canvas = new BitmapCanvas();
            int[][] pixel = new int[w][h];
            int[] pix = new int[wh];

            for (int i=0;i<w;i++) {
                for (int j=0;j<h;j++) {
                    SRGB srgb;
                    /* _(1.0, 0, 0) は赤なので、背景の赤いグリッドを描写している。setInt(0)は黒。setValue(0,0,0)でもよい _ */
                    if(i%100==0 || j%100==0){
                        srgb = new SRGB(0.5, 0.5, 0.5);
                    }else{
                        srgb = new SRGB(1.0, 1.0, 1.0);
                        //                          srgb.setInt(0);
                    }
                    pixel[i][j] = getInt(srgb.R,srgb.G,srgb.B);
                }
            }

            //上に何か書いてみる
            for (int i = 0; i < w/2; i++) {
                for (int j = 0; j < h/2; j++) {
                    /* _レインボーにしてみる_ */
                    SRGB srgb = new SRGB(
                            ((double)i)/(w/2),
                            ((double)j)/(h/2),
                            1 - ((double)i)/w - ((double)j)/h ); //B成分が0〜1になるように気をつける

                    LMS lms = srgb.LMS();
                    LMS lmsConverted = convertToDichromatColor(lms,type);
                    srgb = lmsConverted.SRGB();
                    pixel[i][j] = getInt(srgb.R,srgb.G,srgb.B);
                }
            }


            for (int i=0;i<wh;i++)
                pix[i] = pixel[i%w][(h-1)-i/w];
            canvas.setImage(w, h, pix);
            frame.getContentPane().add(canvas);
        }

        frame.setSize(w*2+20, h*2+30);
        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
                System.exit(0);
            }
        });
    }



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
    static private LMS anchorForProtanopeAndDeuteranope = outerProduct(gray, anchor2deg[0]);
    static private LMS anchorForProtanopeAndDeuteranope2 = outerProduct(gray, anchor2deg[1]);
    static private LMS anchorForTritanope= outerProduct(gray, anchor2deg[2]);
    static private LMS anchorForTritanope2= outerProduct(gray, anchor2deg[3]);
    /* _ここまで_ */

    public static LMS convertToDichromatColor(LMS lms, VisionType type){
        LMS returnLMS;
        switch (type){
        case Trichromat:
            returnLMS = lms;
            break;
        case Protanope:
            returnLMS = convertToProtanopeColor(lms);
            break;
        case Deuteranope:
            returnLMS = convertToDeuteranopeColor(lms);
            break;
        case Tritanope:
            returnLMS = convertToTritanopeColor(lms);
            break;
        default:
            returnLMS = lms;
        break;
        }
        return returnLMS;
    }

    private static LMS convertToProtanopeColor(LMS lms){
        LMS anchor = anchorForProtanopeAndDeuteranope;
        LMS anchor2 = anchorForProtanopeAndDeuteranope2;
        double[] v = new double[3];
        LMS gray = TestForColorView.gray;
        if(lms.S * gray.M < lms.M * gray.S)
            v[0] = -(anchor2.M * lms.M + anchor2.S * lms.S) / anchor2.L;//575nm
        else
            v[0] = -(anchor.M * lms.M + anchor.S * lms.S) / anchor.L;//475nm
        v[1] = lms.M;
        v[2] = lms.S;
        return new LMS(v[0],v[1],v[2]);
    }

    private static LMS convertToDeuteranopeColor(LMS lms){
        LMS anchor = anchorForProtanopeAndDeuteranope;
        LMS anchor2 = anchorForProtanopeAndDeuteranope2;
        double[] v = new double[3];
        LMS gray = TestForColorView.gray;
        if(lms.S * gray.L < lms.L * gray.S)
            v[1] = -(anchor2.L * lms.L + anchor2.S * lms.S) / anchor2.M;//575nm
        else
            v[1] = -(anchor.L * lms.L + anchor.S * lms.S) / anchor.M;//475nm
        v[0] = lms.L;
        v[2] = lms.S;
        return new LMS(v[0],v[1],v[2]);
    }

    private static LMS convertToTritanopeColor(LMS lms){
        LMS anchor = anchorForTritanope;
        LMS anchor2 = anchorForTritanope2;
        double[] v = new double[3];
        LMS gray = TestForColorView.gray;
        if(lms.M * gray.L < lms.L * gray.M)
            v[2] = -(anchor2.M * lms.M + anchor2.L * lms.L) / anchor2.S;//660nm
        else
            v[2] = -(anchor.M * lms.M + anchor.L * lms.L) / anchor.S;//485nm
        v[0] = lms.L;
        v[1] = lms.M;
        return new LMS(v[0],v[1],v[2]);
    }


    /* _Vector3D@shimamuraから拝借。RGBをINTに直す_ */
    static int getInt(double r, double g, double b){
        int v1 = (int)(r * 255.0);
        int v2 = (int)(g * 255.0);
        int v3 = (int)(b * 255.0);
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

