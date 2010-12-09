/*
 * Created on 2003/12/03
 * $Id: ConeFundamentalCSV.java,v 1.1 2003/12/08 00:18:37 wakita Exp $
 */
package jp.ac.titech.is.wakitalab.color.support;

/**
 * @author Ken Wakita
 * @version Experimental, 2003/12/03
 */

import jp.ac.titech.is.wakitalab.color.Fundamental;
import jp.ac.titech.is.wakitalab.pervasives.AbstractCSV;
import jp.ac.titech.is.wakitalab.pervasives.CSVFieldType;
import jp.ac.titech.is.wakitalab.pervasives.IllFormedCSVException;

public class ConeFundamentalCSV extends AbstractCSV {
    private int low, high, step;
    private double lms[][];

    private int l = Integer.MIN_VALUE;
    
    public ConeFundamentalCSV(java.io.Reader reader, int low, int high, int step) {
        CSVFieldType DF = CSVFieldType.Double;
        initialize(new CSVFieldType[] { DF, DF, DF, DF });
        this.low = low; this.high = high; this.step = step;
        lms = new double[(high - low) / step + 1][];
        l = low;
        try { parse(reader); } catch (java.io.IOException e) {}
    }
    
    protected void set(int i, double d) {
        assert i >= 0 && i <= 3;
        if (i == 0 && d != l) throw new IllFormedCSVException(l + "/" + i + "/" + d);
        super.set(i, d);
    }
    
    private int n = 0;
    
    protected void add() {
        if (l > high + step) throw new IllFormedCSVException(l + "/" + high + "/" + step);
        lms[n++] = new double[]{ D[1], D[2], D[3] };
        l += step;
    }
    
    public Fundamental fundamental() { return new Fundamental(low, high, step, lms); }
}
