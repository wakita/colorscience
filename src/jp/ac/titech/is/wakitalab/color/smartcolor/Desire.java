package jp.ac.titech.is.wakitalab.color.smartcolor;

/**
 * $Id: Desire.java,v 1.5 2003/11/26 13:00:39 wakita Exp $
 * Created on 2003/08/24
 * @author Ken Wakita
 */

public interface Desire {
    public int dimension();
    public double base(GrayCoordination g);
    public double derivative(GrayCoordination g, int i);
    public double derivative2(GrayCoordination g, int i);
    
    public Desire plus(Desire desirability);
    public Desire times(double factor);

/*
    public class Series implements Desire {
        int dimension;
        Desire fs[];
        Series(Desire fs[]) {
            this.fs = fs;
            dimension = fs[0].dimension();
        }

        public int dimension() {
            return dimension;
        }

        public double base(GrayCoordination g) {
            double r = 0.0;
            for (int i = 0; i < fs.length; i++) {
                r += fs[i].base(g);
            }
            return r;
        }

        public double derivative(GrayCoordination g, int i) {
            double r = 0.0;
            for (int j = 0; j < fs.length; j++)
                r += fs[j].derivative(g, i);
            return r;
        }
        
        public double derivative2(GrayCoordination g, int i) {
        	double r = 0.0;
        	for (int j = 0; j < fs.length; j++) r += fs[j].derivative2(g, i);
        	return r;
        }
    }
*/
}
