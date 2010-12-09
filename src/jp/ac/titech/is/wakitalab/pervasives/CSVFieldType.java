/*
 * Created on 2003/12/03
 * $Id: CSVFieldType.java,v 1.2 2003/12/08 00:18:37 wakita Exp $
 */
package jp.ac.titech.is.wakitalab.pervasives;

/**
 * @author Ken Wakita
 * @version Experimental, 2003/12/03
 */
public class CSVFieldType {
    private String pattern;
    
    private static final String IGN = "[^,]*";
    private static final String INT = "([+\\-]?\\d+)";
    private static final String DBL = "([+\\-]?\\d*\\.?\\d*(?:[eE][+\\-]?\\d+)?)";
    private static final String STR = "[^,]*";
    
    public static final CSVFieldType Ignore = new CSVFieldType(IGN);
    public static final CSVFieldType Integer = new CSVFieldType(INT);
    public static final CSVFieldType Double = new CSVFieldType(DBL);
    public static final CSVFieldType String = new CSVFieldType(STR);

    public CSVFieldType(String pattern) { this.pattern = pattern; }
    public String pattern() { return pattern; }
}
