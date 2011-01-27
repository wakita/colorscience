package jp.ac.titech.is.wakitalab.color.shimamura;

import jp.ac.titech.is.wakitalab.color.LMS;
import jp.ac.titech.is.wakitalab.color.XYZ;

public class TestForLMS {

    public static void main(String[] args){
        LMS c = new LMS(0.0448, 0.055, 0.071);
        System.out.println(c.toString());
        XYZ c2 = c.XYZ();
        System.out.println(c2.toString());
    }

}
