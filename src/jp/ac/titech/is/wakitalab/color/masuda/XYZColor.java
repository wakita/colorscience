package jp.ac.titech.is.wakitalab.color.masuda;


public class XYZColor extends MyColor {
	private final double x, y, z;
	
	public XYZColor(double x, double y, double z) {
		this.x = x; this.y = y; this.z = z;
	}
	@Override
	public String toString() {
		return "("+x()+", "+y()+", "+z()+")";
	}
	
	public double x() {
		return x;
	}
	public double y() {
		return y;
	}
	public double z() {
		return z;
	}
	
	public XYZColor getXYZ() {
		return this;
	}
	public RGBColor getRGB() {
		double r = Constants.R_X*x + Constants.R_Y*y + Constants.R_Z*z;
		double g = Constants.G_X*x + Constants.G_Y*y + Constants.G_Z*z;
		double b = Constants.B_X*x + Constants.B_Y*y + Constants.B_Z*z;
		return new RGBColor(r, g, b);
	}
	public LMSColor getLMS() {
		return getRGB().getLMS();
	}
	public SRGBColor getSRGB() {
		double r = Constants.lR_X*x + Constants.lR_Y*y + Constants.lR_Z*z;
		double g = Constants.lG_X*x + Constants.lG_Y*y + Constants.lG_Z*z;
		double b = Constants.lB_X*x + Constants.lB_Y*y + Constants.lB_Z*z;
		
		if ( r<0 || g<0 || b<0 || r>1 || g>1 || b>1 ) {
			double[] projected_lRGB = new double[3];
			project2(new double[] {r, g, b}, projected_lRGB);
			r = projected_lRGB[0];
			g = projected_lRGB[1];
			b = projected_lRGB[2];
//			r = 0; g = 0; b = 0;
//			System.out.println("lRGB after: " + r+", "+g+", "+b);
		}
		
		return new SRGBColor(gamma_correction_to_sRGB(r),
				gamma_correction_to_sRGB(g), gamma_correction_to_sRGB(b));
	}
	private double gamma_correction_to_sRGB(double d) {
		// TODO NAN�̏���
		return Math.pow(d, 1/Constants.GAMMA);
	}
	
	private static final double
		Xn = Constants.Xn, Yn = Constants.Yn, Zn = Constants.Zn,
		sisu = Constants.sisu;
	public LabColor getLab() {
		double Yn_ = Math.pow(y/Yn, sisu);
		double L = 116*Yn_ - 16;
		double a = 500 * (Math.pow(x/Xn, sisu) - Yn_);
		double b = 200 * (Yn_ - Math.pow(z/Zn, sisu));
		return new LabColor(L, a, b);
	}
	
	
	
	
	private static final int R0 = 1, R1 = 2, G0 = 3, G1 = 4, B0 = 5, B1 = 6;
	/**
	 * ���S�֌�悤�ɁA���U�ʑ̖̂ʂ֎ˉe����B
	 * Projection of imaginary color to the surface of human gamut.
	 */ 
	private void project1(double[] rgb, double[] p) {
		double r = rgb[0], g = rgb[1], b = rgb[2];
		
		assert r<0 || r>1 || g<0 || g>1 || b<0 || b>1;
		
		int out = 0;
		double outMax = 0;
		if (r<0) {
			System.out.println("    r -");
			out = R0; outMax = -r;
		} else if (r>1) {
			System.out.println("    r over");
			out = R1; outMax = r-1;
		}
		if (g<0 && -g>outMax) {
			System.out.println("    g -");
			out = G0; outMax = -g;
		} else if (g>1 && g-1>outMax) {
			System.out.println("    g over");
			out = G1; outMax = g-1;
		}
		if (b<0 && -b>outMax) {
			System.out.println("    b -");
			out = G0; outMax = -b;
		} else if (b>1 && b-1>outMax) {
			System.out.println("    b over");
			out = G1; outMax = b-1;
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
		p[0] = r; p[1] = g; p[2] = b;
		
	}
	
	private void project2(double[] rgb, double[] p) {
		double r = rgb[0], g = rgb[1], b = rgb[2];
		System.out.println(r+","+g+","+b);
		if(r < 0) r = 0;
		if(g < 0) g = 0;
		if(b < 0) b = 0;
		
		int out = 0; double outMax = 0;
		if (r > 1) {
			out = R1; outMax = r-1;
			System.out.println("R1");
		}
		if (g > 1 && g-1-outMax>0) {
			out = G1; outMax = g-1;
			System.out.println("G1");
		}
		if (b > 1 && b-1-outMax>0) {
			out = B1; outMax = b-1;
			System.out.println("B1");
		}
		
		switch (out) {
		case R1: g /= r;
		         b /= r;
		         r = 1;
		         break;
		case G1: r /= g;
		         b /= g;
		         g = 1;
		         break;
		case B1: r /= b;
                 g /= b;
                 b = 1;
                 break;
		default:
		}
		p[0] = r; p[1] = g; p[2] = b;
		System.out.println("after"+r+","+g+","+b);
	}
	
	private void project3(double[] rgb, double[] p) {
		double r = rgb[0], g = rgb[1], b = rgb[2];
		
		if (r < 0) r = 0;
		else if (r > 1) r = 1;
		if (g < 0) g = 0;
		else if (g > 1) g = 1;
		if (b < 0) b = 0;
		else if (b > 1) b = 1;
		
		p[0] = r; p[1] = g; p[2] = b;
	}
	
}
