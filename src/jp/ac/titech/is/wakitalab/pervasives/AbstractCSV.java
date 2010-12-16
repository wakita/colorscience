/*
 * Created on 2003/12/03
 * $Id: AbstractCSV.java,v 1.1 2003/12/08 00:18:37 wakita Exp $
 */
package jp.ac.titech.is.wakitalab.pervasives;

/**
 * @author Ken Wakita
 * @version Experimental, 2003/12/03
 */

import java.io.BufferedReader;
import java.util.regex.*;

abstract public class AbstractCSV {
    private CSVFieldType types[];
    private int index[];
    protected int N[];
    protected double D[];
    protected String S[];
    
    protected void initialize(CSVFieldType types[]) {
        this.types = types;
        index = new int[types.length];
        int nInt = 0, nDouble = 0, nString = 0;
        for (int i = 0; i < types.length; i++) {
            if (types[i] == CSVFieldType.Integer) { index[i] = nInt++; continue; } 
            if (types[i] == CSVFieldType.Double) { index[i] = nDouble++; continue; } 
            if (types[i] == CSVFieldType.String) { index[i] = nString++; continue; }
        }
        N = new int[nInt];
        D = new double[nDouble];
        S = new String[nString];
    }
    
    protected void initialize(CSVFieldType types[], String sep) {
        initialize(types);
        this.sep = sep;
    }
    
    protected void set(int i, int n)    { N[i] = n; }
    protected void set(int i, double d) { D[i] = d; }
    protected void set(int i, String s) { S[i] = s; }
    
    protected int    getInt(int i)    { return N[i]; }
    protected double getDouble(int i) { return D[i]; }
    protected String getString(int i) { return S[i]; }
    
    abstract protected void add();
    
    protected void parse(String line, Pattern p) {
        Matcher match = p.matcher(line);
        
        if (!match.matches()) { System.err.println("Unmatched line: " + line); return; }

        for (int i = 0; i < types.length; i++) {
            CSVFieldType t = types[i];
            String token = match.group(i + 1);
            if (t == CSVFieldType.Ignore) continue;
            if (t == CSVFieldType.Integer) { set(i, Integer.parseInt(token)); continue; }
            if (t == CSVFieldType.Double) {
                double d = token.length() == 0 ? 0. : Double.parseDouble(token);
                set(i, d);
                continue;
            }
            if (t == CSVFieldType.String) { set(i, token); continue; }
        }
        add();
    }
    
    private String sep = ",\\s*";
    
    protected void parse(java.io.Reader r) throws java.io.IOException {
        StringBuffer pat = new StringBuffer("\\s*");
        for (int i = 0; i < types.length - 1; i++) pat.append(types[i].pattern() + sep);
        pat.append(types[types.length - 1].pattern());
        BufferedReader reader = new BufferedReader(r);
        String line;
        for (int lineNo = 0; (line = reader.readLine()) != null; lineNo++) {
            parse(line, Pattern.compile(pat.toString()));
        }
    }
}
