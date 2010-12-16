package jp.ac.titech.is.wakitalab.color.smartcolor;

/**
 * $Id: DesiredContrast.java,v 1.6 2003/12/01 03:21:52 wakita Exp $
 * Created on 2003/08/24
 * @author Ken Wakita
 */

import jp.ac.titech.is.wakitalab.color.CIELab;
import jp.ac.titech.is.wakitalab.color.Color;

public class DesiredContrast implements SimpleDesire {
    int n;
    double strength[][], contrast[][];

    DesiredContrast(double strength[][], double contrast[][]) {
        assert strength.length == contrast.length;
        assert strength[0].length == contrast[0].length;
        n = strength.length;
        this.strength = strength;
        this.contrast = contrast;
    }
    
    DesiredContrast(double strength[][], CIELab colors[]) {
    	assert strength.length == colors.length;
    	assert strength[0].length == colors.length;
    	n = strength.length;
    	this.strength = strength;
    	setContrast(colors);
    }

    void setContrast(CIELab colors[]) {
		contrast = new double[n][n];
		for (int i = 0; i < n; i++) {
			CIELab c_i = colors[i];
			for (int j = 0; j < n; j++) {
				CIELab c_j = colors[j];
				contrast[i][j] = c_i.distance(c_j);
			}
		}
    }
    
    DesiredContrast(double strength[][], Color colors[]) {
    	CIELab cielabs[] = new CIELab[colors.length];
    	for (int i = 0; i < colors.length; i++)
    	    cielabs[i] = colors[i].CIELab();
    	setContrast(cielabs);
    }

    /**
     * @see jp.ac.titech.is.wakitalab.color.smartcolor.DifferentialFunction#base(jp.ac.titech.is.wakitalab.color.smartcolor.Gray)
     */
    public int dimension() { return n; }

    /**
     * @see jp.ac.titech.is.wakitalab.color.smartcolor.DifferentialFunction#base(jp.ac.titech.is.wakitalab.color.smartcolor.Gray)
     */
    public double base(GrayCoordination g) {
        double result = 0;
        for (int i = 0; i < n; i++) {
            Gray g_i = g.get(i);
            for (int j = 0; j < i; j++) {
                Gray g_j = g.get(j);
                result += strength[i][j] * g_i.distance(g_j);
            }
        }
        return result;
    }

    /**
     * @see jp.ac.titech.is.wakitalab.color.smartcolor.DifferentialFunction#derivative(jp.ac.titech.is.wakitalab.color.smartcolor.Gray, int)
     */
    public double derivative(GrayCoordination g, int i) {
        double result = 0;
        double g_i = g.lightness(i);
        for (int j = 0; j < i; j++) {
            double g_j = g.lightness(j);
            double dd_dg = g_i > g_j ? 1 : -1;
            result += 2 * (Math.abs(g_i - g_j) - contrast[i][j]) * dd_dg;
        }
        return result;
    }

    public double derivative2(GrayCoordination g, int i) {
        return 0;
    }

    public Desire plus(Desire desire) {
		if (desire == null) return this;
    	
        assert dimension() == desire.dimension();
        if (desire instanceof DesiredContrast)
            return plus((DesiredContrast)desire);
        else
            return new CompoundDesire(this, desire);
    }

    public DesiredContrast plus(DesiredContrast C) {
        assert dimension() == C.dimension();
        double strength[][] = new double[dimension()][dimension()];
        for (int i = 0; i < dimension(); i++)
            for (int j = 0; j < dimension(); j++)
                strength[i][j] = this.strength[i][j] + C.strength[i][j];
        return new DesiredContrast(strength, contrast);
    }

    public Desire times(double factor) {
        double strength[][] = new double[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                strength[i][j] = this.strength[i][j] * factor;
        return new DesiredContrast(strength, contrast);
    }
}
