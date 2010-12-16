package jp.ac.titech.is.wakitalab.color.smartcolor;

/**
 * $Id: DesiredDistinguishability.java,v 1.6 2003/12/01 03:21:52 wakita Exp $
 * Created on 2003/08/24
 * @author Ken Wakita
 */

import jp.ac.titech.is.wakitalab.color.*;

public class DesiredDistinguishability implements SimpleDesire {
	
	int n;
	double strength[][];
	
	public DesiredDistinguishability(double strength[][]) {
		assert strength.length > 0;
		assert strength.length == strength[0].length;
		n = strength.length;
		this.strength = strength;
	}

	/**
	 * @see jp.ac.titech.is.wakitalab.color.smartcolor.DifferentialFunction#dimension()
	 */
	public int dimension() {
		return n;
	}
	
	/**
	 * The maximum color difference in the CIELAB color space.
	 */
	
	static final double maxDiff = 600;

	/**
	 * @see jp.ac.titech.is.wakitalab.color.smartcolor.DifferentialFunction#base(jp.ac.titech.is.wakitalab.color.smartcolor.Gray)
	 */
	public double base(GrayCoordination g) {
		double result = 0;
		
		for (int i = 0; i < n; i++) {
			Gray g_i = g.get(i);
			for (int j = 0; j < i; j++) {
				Gray g_j = g.get(j);
				double diff = g_i.distance(g_j);
				
				result += strength[i][j] * (maxDiff - diff) * (maxDiff - diff);
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
			double max = (g_i > g_j) ? CIELab.maxDiff : -CIELab.maxDiff;
			result += 2 * strength[i][j] * (max + g_i - g_j);
		}
		return 0;
	}
	
	public double derivative2(GrayCoordination g, int i) {
		double result = 0;
		for (int j = 0; j < i; j++) result += 2 * strength[i][j];
		return result;
	}
	
	public Desire plus(Desire desire) {
		if (desire == null) return this;
		
		assert dimension() == desire.dimension();
		if (desire instanceof DesiredContrast)
			return plus((DesiredContrast)desire);
		else
			return new CompoundDesire(this, desire);
	}

	public DesiredDistinguishability plus(DesiredDistinguishability C) {
		assert dimension() == C.dimension();
		double strength[][] = new double[dimension()][dimension()];
		for (int i = 0; i < dimension(); i++)
			for (int j = 0; j < dimension(); j++)
				strength[i][j] = this.strength[i][j] + C.strength[i][j];
		return null;
	}

	public Desire times(double factor) {
		return null;
	}

}
