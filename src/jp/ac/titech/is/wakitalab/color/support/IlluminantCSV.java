/*
 * Created on 2003/12/05
 * $Id: IlluminantCSV.java,v 1.1 2003/12/08 00:18:37 wakita Exp $
 */
package jp.ac.titech.is.wakitalab.color.support;

import jp.ac.titech.is.wakitalab.color.Illuminant;
import jp.ac.titech.is.wakitalab.pervasives.*;

/**
 * @author Ken Wakita
 * @version Experimental, 2003/12/05
 */

public class IlluminantCSV extends AbstractCSV {
    private int low, high, step;
    private double radiance[];
    private int n = 0;

    public IlluminantCSV(java.io.Reader reader, int low, int high, int step) {
        initialize(new CSVFieldType[] { CSVFieldType.Double });
        this.low = low; this.high = high; this.step = step;
        radiance = new double[(high - low) / step + 1];
        
        try { parse(reader); } catch (java.io.IOException e) {}
    }
    
    protected void add() { radiance[n++] = D[0]; }
    
    public Illuminant illuminant() {
        assert n == high + step;
        return new Illuminant(low, high, step, radiance);
    }
}
