package jp.ac.titech.is.wakitalab.color;

public class TestForLMS {

    public static void main(String[] args){
        /* _ Initialize 経由で、Color クラスの nominalWhite を初期化する _ */
        Initialize.initialize();
        LMS c = new LMS(0.0448, 0.055, 0.071);
        System.out.println(c.toString());
        XYZ c2 = c.XYZ();
        System.out.println(c2.toString());
    }

}
