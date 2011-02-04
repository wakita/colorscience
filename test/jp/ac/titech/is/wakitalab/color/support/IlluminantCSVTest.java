/*
 * Created on 2003/12/05
 * $Id: IlluminantCSVTest.java,v 1.1.1.1 2005/11/15 14:06:40 wakita Exp $
 */
package jp.ac.titech.is.wakitalab.color.support;

import jp.ac.titech.is.wakitalab.color.*;

/**
 * @author Ken Wakita
 * @version Experimental, 2003/12/05
 */

public class IlluminantCSVTest {
    
    private static final String dirName = "/Users/wakita/project/ColorScience/data";
    public void load(String fileName, int low, int high, int step) {
        String path = dirName + "/" + fileName + ".txt";
        try {
            IlluminantCSV csv =
                new IlluminantCSV(
                    new java.io.FileReader(path),
                    low, high, step);
            Illuminant illuminant = csv.illuminant();
            System.out.println(illuminant);
        } catch (java.io.IOException e) {
            System.err.println("IOException during processing " + fileName);
        }
    }

    public void testIlluminantCSV() {
        load("s0", 300, 830, 5);
        load("s1", 300, 830, 5);
        load("s2", 300, 830, 5);
    }
}
