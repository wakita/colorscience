package jp.ac.titech.is.wakitalab.color.masuda;

import java.awt.Color;

import jp.ac.titech.is.wakitalab.color.shimamura.Dichromat;
import jp.ac.titech.is.wakitalab.color.shimamura.LMS;
import jp.ac.titech.is.wakitalab.color.shimamura.RGB;
import jp.ac.titech.is.wakitalab.color.shimamura.SRGB;
import jp.ac.titech.is.wakitalab.color.shimamura.XYZ;




public class DichromatSimulator {
	private static final double
		L_R = Constants.L_R, L_G = Constants.L_G, L_B = Constants.L_B,
		M_R = Constants.M_R, M_G = Constants.M_G, M_B = Constants.M_B,
		S_R = Constants.S_R, S_G = Constants.S_G, S_B = Constants.S_B;
	
	private static final double
//		L_E = 0.1159786162144061,
//		M_E = 0.09482136320070213,
//		S_E = 0.05842422424151998,
		L_E = Constants.LMS_NUTRAL.l(),
		M_E = Constants.LMS_NUTRAL.m(),
		S_E = Constants.LMS_NUTRAL.s(),
		L_575 = Constants.LMS_575.l(),
		M_575 = Constants.LMS_575.m(),
		S_575 = Constants.LMS_575.s(),
		L_475 = Constants.LMS_475.l(),
		M_475 = Constants.LMS_475.m(),
		S_475 = Constants.LMS_475.s(),	
		L_660 = Constants.LMS_660.l(),
		M_660 = Constants.LMS_660.m(),
		S_660 = Constants.LMS_660.s(),
		L_485 = Constants.LMS_485.l(),
		M_485 = Constants.LMS_485.m(),
		S_485 = Constants.LMS_485.s();
//	private static final double
//		a575 = L_E*S_575 - S_E*L_575 / (M_E*S_575 - S_E*M_575),
//		b575 = M_E*L_575 - L_E*M_575 / (M_E*S_575 - S_E*M_575),
//		a475 = L_E*S_475 - S_E*L_475 / (M_E*S_475 - S_E*M_475),
//		b475 = M_E*L_475 - L_E*M_475 / (M_E*S_475 - S_E*M_475),
//		a660 = L_E*S_660 - S_E*L_660 / (M_E*S_660 - S_E*M_660),
//		b660 = M_E*L_660 - L_E*M_660 / (M_E*S_660 - S_E*M_660),
//		a485 = L_E*S_485 - S_E*L_485 / (M_E*S_485 - S_E*M_485),
//		b485 = M_E*L_485 - L_E*M_485 / (M_E*S_485 - S_E*M_485);
	private static final double
		E_L = S_E/M_E, E_M = S_E/L_E, E_S = M_E/L_E,
		P_L_R = S_R - E_L*M_R, P_L_G = S_G - E_L*M_G, P_L_B = S_B - E_L*M_B;
	
//	private static final double
//		R_R575 = 0.299707, R_G575 = 1.9233, R_B575 = 7.35183,
//		G_R575 = 0.111192, G_G575 = 0.69462, G_B575 = -1.16732,
//		B_R575 = -0.000540465, B_G575 = 0.00148434, B_B575 = 1.00567,
//		
//		R_R475 = -1.10189, R_G475 = -6.99095, R_B475 = -10.529,
//		G_R475 = 0.333737, G_G475 = 2.11002, G_B475 = 1.67179,
//		B_R475 = -0.00162218, B_G475 = -0.0053954, B_B475 = 0.991874;
	
	
//	public LRGBColor p_L(LRGBColor normal) {
//		double r = normal.lR(), g = normal.lG(), b = normal.lB();
//		double r_, g_, b_;
//		if (P_L_R*r + P_L_G*g + P_L_B*b < 0) {
//			System.out.println("575");
//			r_ = R_R575*r + R_G575*g + R_B575*b;
//			g_ = G_R575*r + G_G575*g + G_B575*b;
//			b_ = B_R575*r + B_G575*g + B_B575*b;
//		} else {
//			System.out.println("475");
//			r_ = R_R475*r + R_G475*g + R_B475*b;
//			g_ = G_R475*r + G_G475*g + G_B475*b;
//			b_ = B_R475*r + B_G475*g + B_B475*b;
//		}
//		System.out.println("lRGB: "+r_+","+g_+","+b_);
//		return new LRGBColor(r_, g_, b_);
//	}
	
	public RGBColor p_L(RGBColor normal) {
		LMSColor lms = normal.getLMS();
		double m = lms.m(), s = lms.s();
		
		double a,b,c;
		if (E_L*m - s > 0) {
			System.out.println("575");
			a = M_E*S_575-S_E*M_575;
			b = S_E*L_575-L_E*S_575;
			c = L_E*M_575-M_E*L_575;
		} else {
			System.out.println("475");
			a = M_E*S_475-S_E*M_475;
			b = S_E*L_475-L_E*S_475;
			c = L_E*M_475-M_E*L_475;
		}
		double l = -(b*m + c*s)/a;
		lms = new LMSColor(l, m, s);
		return lms.getRGB();
	}
	
	public RGBColor p_M(RGBColor normal) {
		LMSColor lms = normal.getLMS();
		double l = lms.l(), s = lms.s();
//		System.out.println(l+","+lms.m()+","+s);
		double a,b,c;
		if (E_M*l - s > 0) {
			System.out.println("575");
			a = M_E*S_575-S_E*M_575;
			b = S_E*L_575-L_E*S_575;
			c = L_E*M_575-M_E*L_575;
		} else {
			System.out.println("475");
			a = M_E*S_475-S_E*M_475;
			b = S_E*L_475-L_E*S_475;
			c = L_E*M_475-M_E*L_475;
		}
		double m = -(a*l + c*s)/b;
		lms = new LMSColor(l, m, s);
		return lms.getRGB();
	}
	
	public RGBColor p_S(RGBColor normal) {
		LMSColor lms = normal.getLMS();
		double l = lms.l(), m = lms.m();
		
		double a,b,c;
		if (E_S*l - m > 0) {
			System.out.println("660");
			a = M_E*S_660-S_E*M_660;
			b = S_E*L_660-L_E*S_660;
			c = L_E*M_660-M_E*L_660;
		} else {
			System.out.println("485");
			a = M_E*S_485-S_E*M_485;
			b = S_E*L_485-L_E*S_485;
			c = L_E*M_485-M_E*L_485;
		}
		double s = -(a*l + b*m)/c;
		lms = new LMSColor(l, m, s);
		return lms.getRGB();
	}
	
	public static void main(String[] args) {		
		new DichromatSimulator().test();
	}
	void test() {
		double r = 0.0, g = 0.0, b = 0.7;
		
		Color c = new Color((float)r, (float)g, (float)b);
		SRGBColor s = new SRGBColor(c);
		System.out.println(s.r()+","+s.g()+","+s.b());
		SRGB s_ = new SRGB(c);
		System.out.println(s_.getValue1()+","+s_.getValue2()+","+s_.getValue3());
		
		XYZColor xyz = s.getXYZ();
		System.out.println("XYZ");
		System.out.println(xyz.x()+","+xyz.y()+","+xyz.z());
		XYZ xyz_ = s_.getXYZ();
		System.out.println(xyz_.getValue1()+","+xyz_.getValue2()+","+xyz_.getValue3());
		
		RGBColor rgb = s.getRGB();
		System.out.println("RGB");
		System.out.println(rgb.r()+","+rgb.g()+","+rgb.b());
		RGB rgb_ = s_.getRGB();
		System.out.println(rgb_.getValue1()+","+rgb_.getValue2()+","+rgb_.getValue3());
		
		LMSColor lms = s.getLMS();
		System.out.println("LMS");
		System.out.println(lms.l()+","+lms.m()+","+lms.s());
		LMS lms_ = s_.getLMS();
		System.out.println(lms_.getValue1()+","+lms_.getValue2()+","+lms_.getValue3());
		
		rgb = p_L(rgb);
		System.out.println("simulated RGB");
		System.out.println(rgb.r()+","+rgb.g()+","+rgb.b());
		lms_ = Dichromat.convert(lms_, Dichromat.PROTANOPE);
		rgb_ = lms_.getRGB();
		System.out.println(rgb_.getValue1()+","+rgb_.getValue2()+","+rgb_.getValue3());
		
		xyz = rgb.getXYZ();
		System.out.println("simulated XYZ");
		System.out.println(xyz.x()+","+xyz.y()+","+xyz.z());
		//xyz_ = rgb_.getXYZ();
		System.out.println(xyz_.getValue1()+","+xyz_.getValue2()+","+xyz_.getValue3());
		
		s = rgb.getSRGB();
		System.out.println("simulated SRGB");
		System.err.println(s.r()+","+s.g()+","+s.b());
		s_ = lms_.getSRGB();
		System.err.println(s_.getValue1()+","+s_.getValue2()+","+s_.getValue3());
	}
}
