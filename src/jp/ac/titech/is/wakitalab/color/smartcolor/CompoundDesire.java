package jp.ac.titech.is.wakitalab.color.smartcolor;

/**
 * $Id: CompoundDesire.java,v 1.5 2003/11/26 15:16:23 wakita Exp $
 * Created on 2003/11/21
 * @author Ken Wakita
 */

public class CompoundDesire implements Desire {
	int dimension = -1;
        
	DesiredLightness desiredBrightness;
	DesiredContrast desiredContrast;
	DesiredDistinguishability desiredDistinguishability;
        
	private void add(SimpleDesire desire) {
		if (desire == null) return;
		
		assert dimension == -1 || dimension == desire.dimension();
		dimension = desire.dimension();
		
		if (desire instanceof DesiredLightness) {
			DesiredLightness B = (DesiredLightness)desire;
			if (desiredBrightness == null) desiredBrightness = B;
			else desiredBrightness = desiredBrightness.plus(B);
		}
		if (desire instanceof DesiredContrast) {
			DesiredContrast C = (DesiredContrast)desire;
			if (desiredContrast == null) desiredContrast = C;
			else desiredContrast = desiredContrast.plus(C);
		}
		if (desire instanceof DesiredDistinguishability) {
			DesiredDistinguishability D = (DesiredDistinguishability)desire;
			if (desiredDistinguishability == null) desiredDistinguishability = D;
			else desiredDistinguishability = desiredDistinguishability.plus(D);
		}
	}
	
	private void add(CompoundDesire desire) {
		add(desire.desiredBrightness);
		add(desire.desiredContrast);
		add(desire.desiredDistinguishability);
	}
	
	private void add(Desire desire) {
		assert desire instanceof SimpleDesire || desire instanceof CompoundDesire;
		if (desire instanceof SimpleDesire) add((SimpleDesire)desire);
		else if (desire instanceof CompoundDesire) add((CompoundDesire)desire);
	}
	
	CompoundDesire() {}
        
	CompoundDesire(Desire d1, Desire d2) {
        add(d1);
        add(d2);
	}
        
	CompoundDesire(Desire B, Desire C, Desire D) {
		add(B); add(C); add(D);
	}

	CompoundDesire(Desire desire[]) {
		for (int i = 0; i < desire.length; i++) add(desire[i]);
	}

	public int dimension() {
		return dimension;
	}

	public double base(GrayCoordination g) {
		double result = 0;
		if (desiredBrightness != null) result += desiredBrightness.base(g);
		if (desiredContrast != null) result += desiredContrast.base(g);
		if (desiredDistinguishability != null) result += desiredDistinguishability.base(g);
		return result;
	}

	public double derivative(GrayCoordination g, int i) {
		double result = 0;
		if (desiredBrightness != null) result += desiredBrightness.derivative(g, i);
		if (desiredContrast != null) result += desiredContrast.derivative(g, i);
		if (desiredDistinguishability != null) result += desiredDistinguishability.derivative(g, i);
		return result;
	}
        
	public double derivative2(GrayCoordination g, int i) {
		double result = 0;
		if (desiredBrightness != null) result += desiredBrightness.derivative2(g, i);
		if (desiredContrast != null) result += desiredContrast.derivative2(g, i);
		if (desiredDistinguishability != null) result += desiredDistinguishability.derivative2(g, i);
		return result;
	}
        
	public Desire plus(Desire desire) {
		return new CompoundDesire(this, desire);
	}
        
	public Desire times(double factor) {
		Desire B = desiredBrightness;
		if (B != null) B = B.times(factor);
		Desire C = desiredContrast;
		if (C != null) C = C.times(factor);
		Desire D = desiredDistinguishability;
		if (D != null) D = D.times(factor);

		return new CompoundDesire(B, C, D);
	}
}
