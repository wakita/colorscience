package jp.ac.titech.is.wakitalab.color.smartcolor;

/**
 * $Id: Gray.java,v 1.6 2003/12/01 03:21:52 wakita Exp $
 * Created on 2003/11/21
 * @author wakita
 */

import jp.ac.titech.is.wakitalab.color.CIELab;

public class Gray extends CIELab {
    private static final java.util.Random random = new java.util.Random();

    public Gray() {
        this((maxLightness - minLightness) * random.nextDouble() + minLightness, 0, 0);
        assert haveMinLightness;
    }

    public Gray(double L, double a, double b) {
        super(L, 0, 0);
    }

    public Gray(CIELab c) {
        this(c.L, c.a, c.b);
    }

    void set(double g) {
        assert haveMinLightness;
        L = Math.min(Math.max(L, minLightness), maxLightness);
    }

    double distance(Gray g) {
        return Math.abs(L - g.L);
    }
}
