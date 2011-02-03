/*
 * Created on 2003/12/04
 * $Id: ConeFundamentalCSVTest.java,v 1.1.1.1 2005/11/15 14:06:40 wakita Exp $
 */
package jp.ac.titech.is.wakitalab.color.support;

import static org.junit.Assert.*;

import java.util.regex.*;

import jp.ac.titech.is.wakitalab.color.*;

/**
 * @author Ken Wakita
 * @version Experimental, 2003/12/04
 */

public class ConeFundamentalCSVTest {
    
    public void load(String fileName, int low, int high, int step) {
        String dirName = "/Users/wakita/project/cvrl/cones";
        String path = dirName + "/" + fileName + ".txt";
        try {
            ConeFundamentalCSV csv =
                new ConeFundamentalCSV(
                    new java.io.FileReader(path),
                    low,
                    high,
                    step);
            Fundamental fundamental = csv.fundamental();
            System.out.println(fileName + ": " + fundamental.efficiency(400).length);
        } catch (java.io.IOException e) {
            System.err.println("IOException during processing " + fileName);
        }
    }

    public void testLoad() {
        load("dpse", 400, 700, 5);
        load("dpse_1", 400, 700, 1);
        load("linss10e_1", 390, 830, 1);
        load("linss10e_5", 390, 830, 5);
//        load("linss10e_fine", 390, 830, 0.1);
//        load("s_ssfciejve", 390, 615, 5);
//        load("s_ssfciejvq", 390, 615, 5);
//        load("s_ssfsb2e", 390, 615, 5);
//        load("s_ssfsb2q", 390, 615, 5);
        load("smj10", 380, 770, 5);
        load("smj10q", 380, 770, 5);
        load("smj2", 390, 730, 5);
        load("smj2_10", 390, 730, 5);
        load("smj2_10q", 390, 730, 5);
        load("smj2q", 390, 730, 5);
        load("sp", 380, 825, 5);
        load("spq", 380, 825, 5);
        load("ss10e", 390, 830, 5);
        load("ss10e_1", 390, 830, 1);
//        load("ss10e_fine", 390, 830, 0.1);
        load("ss10q", 390, 830, 5);
        load("ss10q_1", 390, 830, 1);
//        load("ss10q_fine", 390, 830, 0.1);
        load("ss2_10e", 390, 830, 5);
        load("ss2_10e_1", 390, 830, 1);
//        load("ss2_10e_fine", 390, 830, 0.1);
        load("ss2_10q", 390, 830, 5);
        load("ss2_10q_1", 390, 830, 1);
//        load("ss2_10q_fine", 390, 830, 0.1);
        load("vew", 390, 730, 5);
//      load("vewalt", 13000, 26250, 250);
        load("vewq", 390, 730, 5);
        load("vw", 380, 825, 5);
        load("vwq", 380, 825, 5);
        
        /*
        String files[] = new String []{
            "sp", "spq",
            "ss10e", "ss10e_1", "ss10e_fine", "ss10q", "ss10q_1", "ss10q_fine",
            "ss2_10e", "ss2_10e_1", "ss2_10e_fine", "ss2_10q", "ss2_10q_1", "ss2_10q_fine",
            "vew", "vewalt", "vewq",
            "vw", "vwq"
        };
        */
    }
    
    private static final String INT = "([+-]?\\d+)";
    private static final String DBL = "([+\\-]?(?:\\d+|\\d*(?:\\d\\.|\\.\\d)\\d*)(?:[eE][+\\-]?\\d+)?)";
    
    public void testINT() {
        Pattern p = Pattern.compile(INT);
        assertTrue(p.matcher("1").matches());
        assertTrue(p.matcher("+2").matches());
        assertTrue(p.matcher("-3").matches());
        assertTrue(p.matcher("123").matches());
        assertFalse(p.matcher("").matches());
        assertFalse(p.matcher("123 ").matches());
        assertFalse(p.matcher(" 123").matches());
    }

    public void testDBL() {
        Pattern p = Pattern.compile(DBL);
        assertTrue(p.matcher("1").matches());
        assertTrue(p.matcher("+2").matches());
        assertTrue(p.matcher("-3").matches());
        assertTrue(p.matcher("123").matches());
        assertFalse(p.matcher("").matches());
        assertFalse(p.matcher("123 ").matches());
        assertFalse(p.matcher(" 123").matches());
        assertTrue(p.matcher("+1").matches());
        assertTrue(p.matcher("-2").matches());
        assertTrue(p.matcher("2.").matches());
        assertTrue(p.matcher("2.5").matches());
        assertTrue(p.matcher(".5").matches());
        assertFalse(p.matcher(".").matches());
        assertTrue(p.matcher("+1E5").matches());
        assertTrue(p.matcher("-1E5").matches());
        assertFalse(p.matcher("E5").matches());
        assertTrue(p.matcher("+1E5").matches());
        assertTrue(p.matcher("0.0000").matches());
    }
}
