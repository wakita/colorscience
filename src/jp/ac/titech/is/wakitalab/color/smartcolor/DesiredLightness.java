package jp.ac.titech.is.wakitalab.color.smartcolor;

/**
 * $Id: DesiredLightness.java,v 1.2 2003/12/01 03:21:52 wakita Exp $
 * Created on 2003/08/24
 * @author Ken Wakita
 */

import jp.ac.titech.is.wakitalab.color.*;

public class DesiredLightness implements SimpleDesire {
    int n;
    double strength[], lightness[];
    
    private void initialize(double strength[], double lightness[]) {
    	assert strength.length == lightness.length;
    	this.n = strength.length;
    	this.strength = strength;
    	this.lightness = lightness;
    }

    DesiredLightness(double strength[], double lightness[]) {
        assert strength.length == lightness.length;
        initialize(strength, lightness);
    }
    
    DesiredLightness(double strength[], CIELab colors[]) {
		assert strength.length == lightness.length;
		double lightness[] = new double[colors.length];
		for (int i = 0; i < colors.length; i++)
		    lightness[i] = colors[i].L;
		initialize(strength, lightness);
    }
    
    DesiredLightness(double strength[], Color colors[]) {
		assert strength.length == lightness.length;
		this.strength = strength;
		double lightness[] = new double[colors.length];
		for (int i = 0; i < lightness.length; i++)
			lightness[i] = colors[i].CIELab().L;
		initialize(strength, lightness);
    }

    /**
     * @see jp.ac.titech.is.wakitalab.color.smartcolor.DifferentialFunction#dimension()
     */
    public int dimension() { return n; }

    /**
     * @see jp.ac.titech.is.wakitalab.color.smartcolor.DifferentialFunction#base(jp.ac.titech.is.wakitalab.color.smartcolor.Gray)
     */
    public double base(GrayCoordination g) {
        assert g.size() == n;
        double result = 0.0;
        for (int i = 0; i < n; i++) {
            double diff = g.get(i).L - lightness[i];
            result += strength[i] * diff * diff;
        }
        return result;
    }

    /**
     * @see jp.ac.titech.is.wakitalab.color.smartcolor.DifferentialFunction#derivative(jp.ac.titech.is.wakitalab.color.smartcolor.Gray, int)
     */
    public double derivative(GrayCoordination g, int i) {
        return 2.0 * strength[i] * (g.get(i).L - lightness[i]);
    }

    public double derivative2(GrayCoordination g, int i) {
        return 2.0 * strength[i];
    }

    public Desire plus(Desire desire) {
		if (desire == null) return this;
    	
        assert dimension() == desire.dimension();
        if (desire instanceof DesiredLightness)
            return plus((DesiredLightness)desire);
        else return new CompoundDesire(this, desire);
    }

    public DesiredLightness plus(DesiredLightness B) {
        assert dimension() == B.dimension();
        int n = dimension();
        DesiredLightness desired = (DesiredLightness)B;
        double strength[] = new double[n], lightness[] = new double[n];
        for (int i = 0; i < n; i++) {
            strength[i] = this.strength[i] + desired.strength[i];
            lightness[i] = this.lightness[i] + desired.strength[i];
        }
        return new DesiredLightness(strength, lightness);
    }
    
    public Desire times(double factor) {
    	double strength[] = new double[dimension()];
    	for (int i = 0; i < dimension(); i++) strength[i] = this.strength[i] * factor;
    	return new DesiredLightness(strength, lightness);
    }
}
