package jp.ac.titech.is.wakitalab.color;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.*;
import jp.ac.titech.is.wakitalab.math.*;
import static jp.ac.titech.is.wakitalab.color.VisionType.*;

public class TestForConvertedImages {

    private static SRGB getSRGB_FromPixel(int pixel) {
        return new SRGB (
                (double)((pixel >> 16) & 0x000000ff) / 255.0,
                (double)((pixel >> 8) & 0x000000ff) / 255.0,
                (double)(pixel & 0x000000ff) / 255.0);
    }
    
    public static void run(URL filename){
        
        JFrame frame = new JFrame("Dichromat View");
        frame.getContentPane().setLayout(new GridLayout(1,1));

        int[] rgbArray;
        int imageWidth, imageHeight;
        try{
            BufferedImage image = ImageIO.read(filename);
            imageWidth = image.getWidth();
            imageHeight = image.getHeight();
            rgbArray = new int[imageWidth*imageHeight];
            for (int i=0; i<imageWidth; i++) {
                for (int j=0; j<imageHeight; j++) {
                    rgbArray[i+j*imageWidth]= image.getRGB(i,j);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        int[] converted_rgbArray = new int[imageWidth*imageHeight];
        for (int i=0; i<imageWidth*imageHeight; i++){
                SRGB tmp = getSRGB_FromPixel(rgbArray[i]).getDichromatColor(VisionType.Protanope);
                converted_rgbArray[i] = tmp.getIntegerExpression();
        }

        BitmapCanvas canvas = new BitmapCanvas();
        canvas.setImage(imageWidth, imageHeight, converted_rgbArray);
        frame.getContentPane().add(canvas);

        frame.setSize(imageWidth, imageHeight);
        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
                System.exit(0);
            }
        });
    }
    
    public static void main(String args[]){
        // object.getClass().getResource() が普通
        run(TestForConvertedImages.class.getResource("./ColorImages/ishihara.tiff"));
        run(TestForConvertedImages.class.getResource("./ColorImages/pictureFromBrettel.png"));
    }
}

