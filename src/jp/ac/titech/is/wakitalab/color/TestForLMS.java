package jp.ac.titech.is.wakitalab.color;

public class TestForLMS {

    public static void main(String[] args){
        /* Initialize 経由で、Color クラスの nominalWhite を初期化する */
        Initialize.initialize();
        /* LMS(1,2,3) が XYZ の何に変換されるべきかは調査中 */
        LMS c = new LMS(1,2,3);
        System.out.println(c.toString());
        XYZ w = c.XYZ();
        System.out.println(w.toString());
    }

}
