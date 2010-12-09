package jp.ac.titech.is.wakitalab.color;

public class Initialize {
    public static boolean initialized = false; 
    public static void initialize() { 
        if (!initialized) {
            Color.initialize(); // depends on Illuminant
            System.err.println("Color.initialize()");
            XYZ.initialize(); // depends on Illuminant
            System.err.println("XYZ.initialize()");
            
            System.err.flush();
            
            initialized = true;
        }
    }
}
