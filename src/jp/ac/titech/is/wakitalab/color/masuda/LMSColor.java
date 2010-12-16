package jp.ac.titech.is.wakitalab.color.masuda;




public class LMSColor extends MyColor {
	private final double l, m, s;
	public LMSColor(double l, double m, double s) {
		this.l = l; this.m = m; this.s = s;
	}
	public LMSColor(double[] lms) {
		assert lms.length == 3;
		l = lms[0]; m =lms[1]; s = lms[2];
	}
	@Override
	public String toString() {
		return "("+l()+", "+m()+", "+s()+")";
	}
	
	public double l() {
		return l;
	}
	public double m() {
		return m;
	}
	public double s() {
		return s;
	}
	
	public RGBColor getRGB() {
		double r = Constants.R_L*l + Constants.R_M*m + Constants.R_S*s;
		double g = Constants.G_L*l + Constants.G_M*m + Constants.G_S*s;
		double b = Constants.B_L*l + Constants.B_M*m + Constants.B_S*s;
//		System.out.println("lRGB: " + lR+", "+lG+", "+lB);
		
//		if ( lR<0 || lG<0 || lB<0 || lR>1 || lG>1 || lB>1 ) {
//			double[] projected_lRGB = project(new double[] {lR, lG, lB});
////			lR = projected_lRGB[0];
////			lG = projected_lRGB[1];
////			lB = projected_lRGB[2];
//			lR = 0; lG = 0; lB = 0;
////			System.out.println("lRGB after: " + lR+", "+lG+", "+lB);
//		}
		
		return new RGBColor(r, g, b);
	}
	@Override
	public XYZColor getXYZ() {
		return getRGB().getXYZ();
	}
	public SRGBColor getSRGB() {
		return getXYZ().getSRGB();
	}
	
	
	private static final int R0 = 1, R1 = 2, G0 = 3, G1 = 4, B0 = 5, B1 = 6;
	// Projection of imaginary color to the surface of human gamut.
	private double[] project(double[] rgb) {
		double r = rgb[0], g = rgb[1], b = rgb[2];
		
		assert r<0 || r>1 || g<0 || g>1 || b<0 || b>1;
		
		int out = 0;
		double outMax = 0;
		if (r<0) {
			out = R0; outMax = -r;
		} else if (r>1) {
			out = R1; outMax = r-1;
		}
		if (g<0 && -g>outMax) {
			out = G0; outMax = -g;
		} else if (g>1 && g-1>outMax) {
			out = G1; outMax = g-1;
		}
		if (b<0 && -b>outMax) {
			out = B0; outMax = -b;
		} else if (b>1 && b-1>outMax) {
			out = B1; outMax = b-1;
		}
		
		double k;
		switch (out) {
		case R0: k = -0.5/(r-0.5);
		         r = 0;
		         g = 0.5 + k*(g - 0.5);
		         b = 0.5 + k*(b - 0.5);
		         break;
		case R1: k = 0.5/(r-0.5);
		         r = 1;
		         g = 0.5 + k*(g - 0.5);
		         b = 0.5 + k*(b - 0.5);
		         break;
		case G0: k = -0.5/(g-0.5);
		         r = 0.5 + k*(r - 0.5);
		         g = 0;
		         b = 0.5 + k*(b - 0.5);
		         break;
		case G1: k = 0.5/(g-0.5);
		         r = 0.5 + k*(r - 0.5);
		         g = 1;
		         b = 0.5 + k*(b - 0.5);
		         break;
		case B0: k = -0.5/(b-0.5);
                 r = 0.5 + k*(r - 0.5);
                 g = 0.5 + k*(g - 0.5);
                 b = 0;
                 break;
		case B1: k = 0.5/(b-0.5);
                 r = 0.5 + k*(r - 0.5);
                 g = 0.5 + k*(g - 0.5);
                 b = 1;
		}
		
		return new double[] {r, g, b};
	}
	
}
