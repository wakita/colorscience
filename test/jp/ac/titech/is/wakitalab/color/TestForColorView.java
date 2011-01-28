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

        /*_ Trichromat, Protan, Deutan, Tritan _*/
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
                    pixel[i][j] = srgb.getIntegerExpression();
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
                    srgb = srgb.getDichromatColor(type);
                    pixel[i][j] = srgb.getIntegerExpression();
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

}

