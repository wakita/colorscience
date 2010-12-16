package jp.ac.titech.is.wakitalab.color.masuda;



public class Constants {
	// XYZ->Lab�Ŏg����
	public static final double Xn = 98.072, Yn = 100, Zn = 118.225;
	public static final double sisu = 1/3.0;
	
	// sRGB<->lRGB �K���}�␳�p
	public static final double GAMMA = 2.2;
	
	public static final int NATURE = 0, PROTANOPE = 1, DEUTERANOPE = 2, TRITANOPE = 3;
	
	

/* Color system transform matrix */

/* Stiles-Burch(1955) r,g,and b 2-deg CMFs*/
//	public static final double
//	WEIGHT_L = 0.68273,
//	WEIGHT_M = 0.35235,
//	L_575 = WEIGHT_L*Math.pow(10, -0.0023),
//	M_575 = WEIGHT_M*Math.pow(10, -0.1262),
//	S_575 = Math.pow(10, -3.4584),
//	L_475 = WEIGHT_L*Math.pow(10, -0.9029),
//	M_475 = WEIGHT_M*Math.pow(10, -0.6599),
//	S_475 = Math.pow(10, -0.3084),	
//	L_660 = WEIGHT_L*Math.pow(10, -1.0304),
//	M_660 = WEIGHT_M*Math.pow(10, -2.1141),
//	S_660 = Math.pow(10, -5.7882),
//	L_485 = WEIGHT_L*Math.pow(10, -0.7770),
//	M_485 = WEIGHT_M*Math.pow(10, -0.5562),
//	S_485 = Math.pow(10, -0.5671);
//	
//	public static final double
//	//	lRGB->LMS
//	L_R = 0.214808, L_G = 0.751035, L_B = 0.045156,
//	M_R = 0.022882, M_G = 0.940534, M_B = 0.076827,
//	S_R = 0.000000, S_G = 0.016500, S_B = 0.999989,
//	//	LMS->lRGB
//	R_L = 5.088288, R_M = -4.064546, R_S = 0.08250104,
//	G_L = -0.1239587, G_M = 1.163679, G_S = -0.08380545,
//	B_L = 0.002045340, B_M = -0.01920092, B_S = 1.001394;

	
/* "Hitati" version */
//	public static final double
//	// lRGB->LMS
//	L_R = 0.1992, L_G = 0.4112, L_B = 0.0742,
//	M_R = 0.0353, M_G = 0.2226, M_B = 0.0574,
//	S_R = 0.0185, S_G = 0.1231, S_B = 1.3550,
//	// LMS->lRGB
//	R_L = 7.46452, R_M = -13.8882, R_S = 0.179569,
//	G_L = -1.18521, G_M = 6.80529, G_S = -0.223381,
//	B_L = 0.00576088, B_M = -0.428634, B_S = 0.75585;

	
public static final int FIRST = 390;
	
	public static final double[][] LMS_ENERGY = {
		/*390*/ { 4.15003E-04,  3.68349E-04,  9.54729E-03 },
		/*395*/ { 1.05192E-03,  9.58658E-04,  2.38250E-02 },
		/*400*/ { 2.40836E-03,  2.26991E-03,  5.66498E-02 },
		/*405*/ { 4.83339E-03,  4.70010E-03,  1.22451E-01 },
		/*410,*/ { 8.72127E-03,  8.79369E-03,  2.33008E-01 },
		/*415,*/ { 1.33837E-02,  1.45277E-02,  3.81363E-01 },
		/*420,*/ { 1.84480E-02,  2.16649E-02,  5.43618E-01 },
		/*425,*/ { 2.29317E-02,  2.95714E-02,  6.74474E-01 },
		/*430,*/ { 2.81877E-02,  3.94566E-02,  8.02555E-01 },
		/*435,*/ { 3.41054E-02,  5.18199E-02,  9.03573E-01 },
		/*440,*/ { 4.02563E-02,  6.47782E-02,  9.91020E-01 },
		/*445,*/ { 4.49380E-02,  7.58812E-02,  9.91515E-01 },
		/*450,*/ { 4.98639E-02,  8.70524E-02,  9.55393E-01 },
		/*455,*/ { 5.53418E-02,  9.81934E-02,  8.60240E-01 },
		/*460,*/ { 6.47164E-02,  1.16272E-01,  7.86704E-01 },
		/*465,*/ { 8.06894E-02,  1.44541E-01,  7.38268E-01 },
		/*470,*/ { 9.94755E-02,  1.75893E-01,  6.46359E-01 },
		/*475,*/ { 1.18802E-01,  2.05398E-01,  5.16411E-01 },
		/*480,*/ { 1.40145E-01,  2.35754E-01,  3.90333E-01 },
		/*485,*/ { 1.63952E-01,  2.68063E-01,  2.90322E-01 },
		/*490,*/ { 1.91556E-01,  3.03630E-01,  2.11867E-01 },
		/*495,*/ { 2.32926E-01,  3.57061E-01,  1.60526E-01 },
		/*500,*/ { 2.88959E-01,  4.27764E-01,  1.22839E-01 },
		/*505,*/ { 3.59716E-01,  5.15587E-01,  8.88965E-02 },
		/*510,*/ { 4.43683E-01,  6.15520E-01,  6.08210E-02 },
		/*515,*/ { 5.36494E-01,  7.19154E-01,  4.28123E-02 },
		/*520,*/ { 6.28561E-01,  8.16610E-01,  2.92033E-02 },
		/*525,*/ { 7.04720E-01,  8.85550E-01,  1.93912E-02 },
		/*530,*/ { 7.70630E-01,  9.35687E-01,  1.26013E-02 },
		/*535,*/ { 8.25711E-01,  9.68858E-01,  8.09453E-03 },
		/*540,*/ { 8.81011E-01,  9.95217E-01,  5.08900E-03 },
		/*545,*/ { 9.19067E-01,  9.97193E-01,  3.16893E-03 },
		/*550,*/ { 9.40198E-01,  9.77193E-01,  1.95896E-03 },
		/*555,*/ { 9.65733E-01,  9.56583E-01,  1.20277E-03 },
		/*560,*/ { 9.81445E-01,  9.17750E-01,  7.40174E-04 },
		/*565,*/ { 9.94486E-01,  8.73205E-01,  4.55979E-04 },
		/*570,*/ { 9.99993E-01,  8.13509E-01,  2.81800E-04 },
		/*575,*/ { 9.92310E-01,  7.40291E-01,  1.75039E-04 },
		/*580,*/ { 9.69429E-01,  6.53274E-01,  1.09454E-04 },
		/*585,*/ { 9.55602E-01,  5.72597E-01,  6.89991E-05 },
		/*590,*/ { 9.27673E-01,  4.92599E-01,  4.39024E-05 },
		/*595,*/ { 8.85969E-01,  4.11246E-01,  2.82228E-05 },
		/*600,*/ { 8.33982E-01,  3.34429E-01,  1.83459E-05 },
		/*605,*/ { 7.75103E-01,  2.64872E-01,  1.20667E-05 },
		/*610,*/ { 7.05713E-01,  2.05273E-01,  8.03488E-06 },
		/*615,*/ { 6.30773E-01,  1.56243E-01,  5.41843E-06 },
		/*620,*/ { 5.54224E-01,  1.16641E-01,  0           },
		/*625,*/ { 4.79941E-01,  8.55872E-02,  0           },
		/*630,*/ { 4.00711E-01,  6.21120E-02,  0           },
		/*635,*/ { 3.27864E-01,  4.44879E-02,  0           },
		/*640,*/ { 2.65784E-01,  3.14282E-02,  0           },
		/*645,*/ { 2.13284E-01,  2.18037E-02,  0           },
		/*650,*/ { 1.65141E-01,  1.54480E-02,  0           },
		/*655,*/ { 1.24749E-01,  1.07120E-02,  0           },
		/*660,*/ { 9.30085E-02,  7.30255E-03,  0           },
		/*665,*/ { 6.85100E-02,  4.97179E-03,  0           },
		/*670,*/ { 4.98661E-02,  3.43667E-03,  0           },
		/*675,*/ { 3.58233E-02,  2.37617E-03,  0           },
		/*680,*/ { 2.53790E-02,  1.63734E-03,  0           },
		/*685,*/ { 1.77201E-02,  1.12128E-03,  0           },
		/*690,*/ { 1.21701E-02,  7.61051E-04,  0           },
		/*695,*/ { 8.47170E-03,  5.25457E-04,  0           },
		/*700,*/ { 5.89749E-03,  3.65317E-04,  0           },
		/*705,*/ { 4.09129E-03,  2.53417E-04,  0           },
		/*710,*/ { 2.80447E-03,  1.74402E-04,  0           },
		/*715,*/ { 1.92058E-03,  1.20608E-04,  0           },
		/*720,*/ { 1.32687E-03,  8.41716E-05,  0           },
		/*725,*/ { 9.17777E-04,  5.89349E-05,  0           },
		/*730,*/ { 6.39373E-04,  4.16049E-05,  0           },
		/*735,*/ { 4.46035E-04,  2.94354E-05,  0           },
		/*740,*/ { 3.10869E-04,  2.08860E-05,  0           },
		/*745,*/ { 2.19329E-04,  1.50458E-05,  0           },
		/*750,*/ { 1.54549E-04,  1.08200E-05,  0           },
		/*755,*/ { 1.09508E-04,  7.82271E-06,  0           },
		/*760,*/ { 7.79912E-05,  5.69093E-06,  0           },
		/*765,*/ { 5.56264E-05,  4.13998E-06,  0           },
		/*770,*/ { 3.99295E-05,  3.02683E-06,  0           },
		/*775,*/ { 2.86163E-05,  2.21100E-06,  0           },
		/*780,*/ { 2.07321E-05,  1.63433E-06,  0           },
		/*785,*/ { 1.50432E-05,  1.21054E-06,  0           },
		/*790,*/ { 1.09446E-05,  8.99170E-07,  0           },
		/*795,*/ { 7.97750E-06,  6.69594E-07,  0           },
		/*800,*/ { 5.85057E-06,  5.03187E-07,  0           },
		/*805,*/ { 4.31102E-06,  3.80046E-07,  0           },
		/*810,*/ { 3.17009E-06,  2.86329E-07,  0           },
		/*815,*/ { 2.34468E-06,  2.16878E-07,  0           },
		/*820,*/ { 1.74666E-06,  1.65158E-07,  0           },
		/*825,*/ { 1.30241E-06,  1.25508E-07,  0           },
		/*830*/ { 9.74306E-07,  9.53411E-08,  0           }
	};
	
/* "Simamura" version */
	public static final LMSColor LMS_NUTRAL;
	static {
		int length = LMS_ENERGY.length;
		double sum_l = 0, sum_m = 0, sum_s = 0;
		for (int i = 0; i < length; i++) {
			sum_l += LMS_ENERGY[i][0];
			sum_m += LMS_ENERGY[i][1];
			sum_s += LMS_ENERGY[i][2];
		}
		LMS_NUTRAL = new LMSColor(sum_l, sum_m, sum_s);
	}
	
	public static final LMSColor
	LMS_575 = new LMSColor(LMS_ENERGY[(575-FIRST)/5]),
	LMS_475 = new LMSColor(LMS_ENERGY[(475-FIRST)/5]),	
	LMS_660 = new LMSColor(LMS_ENERGY[(660-FIRST)/5]),
	LMS_485 = new LMSColor(LMS_ENERGY[(485-FIRST)/5]);
	
//	public static final double WEIGHT_L = 0.68273, WEIGHT_M = 0.35235,
//	SUM_L, SUM_M, SUM_S, SUM_LplusM;
//	public static final LMSColor LMS_NUTRAL;
//	static {
//		int length = LMS_ENERGY.length;
//		double sum_l = 0, sum_m = 0, sum_s = 0;
//		for (int i = 0; i < length; i++) {
//			sum_l += LMS_ENERGY[i][0];
//			sum_m += LMS_ENERGY[i][1];
//			sum_s += LMS_ENERGY[i][2];
//		}
//		sum_l *= WEIGHT_L; sum_m *= WEIGHT_M;
//		SUM_L = sum_l; SUM_M = sum_m; SUM_S = sum_s;
//		SUM_LplusM = SUM_L + SUM_M;
//		LMS_NUTRAL = new LMSColor(sum_l/(sum_l+sum_m), sum_m/(sum_l+sum_m), sum_s/sum_s);
//	}
//	
//	public static final LMSColor
//	LMS_575 = new LMSColor(LMS_ENERGY[(575-FIRST)/5][0]*WEIGHT_L/(SUM_LplusM),
//							LMS_ENERGY[(575-FIRST)/5][1]*WEIGHT_M/(SUM_LplusM),
//							LMS_ENERGY[(575-FIRST)/5][2]/SUM_S),
//	LMS_475 = new LMSColor(LMS_ENERGY[(475-FIRST)/5][0]*WEIGHT_L/(SUM_LplusM),
//							LMS_ENERGY[(475-FIRST)/5][1]*WEIGHT_M/(SUM_LplusM),
//							LMS_ENERGY[(475-FIRST)/5][2]/SUM_S),	
//	LMS_660 = new LMSColor(LMS_ENERGY[(660-FIRST)/5][0]*WEIGHT_L/(SUM_LplusM),
//							LMS_ENERGY[(660-FIRST)/5][1]*WEIGHT_M/(SUM_LplusM),
//							LMS_ENERGY[(660-FIRST)/5][2]/SUM_S),
//	LMS_485 = new LMSColor(LMS_ENERGY[(485-FIRST)/5][0]*WEIGHT_L/(SUM_LplusM),
//							LMS_ENERGY[(485-FIRST)/5][1]*WEIGHT_M/(SUM_LplusM),
//							LMS_ENERGY[(485-FIRST)/5][2]/SUM_S);
//	

	public static final double
	// lRGB->LMS   (�Q�ƁF��������̃v���O�����A�������̂�CVRL�ɂ�����)
	ir = 72.0942, ig = 1.3791, ib = 1.0,
	L_R = ir*5.89749E-3, L_G = ig*0.924341, L_B = ib*0.0351131,
	M_R = ir*3.65317E-4, M_G = ig*0.993742, M_B = ib*0.0539344,
	S_R = ir*0.0, S_G = ig*2.85277E-3, S_B = ib*0.919756,
	// LMS->lRGB	
	R_L, R_M, R_S,
	G_L, G_M, G_S,
	B_L, B_M, B_S,
	
	// XYZ->linearRGB   (�Q�ƁF��������̘_�� p20)
	lR_X = 3.5064, lR_Y =-1.7400, lR_Z =-0.5441,
	lG_X =-1.0690, lG_Y = 1.9777, lG_Z = 0.0352,
	lB_X = 0.0563, lB_Y =-0.1970, lB_Z = 1.0511,
	
	X_lR, X_lG, X_lB,
	Y_lR, Y_lG, Y_lB,
	Z_lR, Z_lG, Z_lB,
	
	// CIERGB->XYZ   (�Q�ƁF�V�ҐF�ʉȊw�n���h�u�b�N�y��2�Łzp105)
	X_R = 2.7689, X_G = 1.7517, X_B = 1.1302,
	Y_R = 1.0000, Y_G = 4.5907, Y_B = 0.0601,
	Z_R = 0.0000, Z_G = 0.0565, Z_B = 5.5943,
	
	R_X, R_Y, R_Z,
	G_X, G_Y, G_Z,
	B_X, B_Y, B_Z;
	
	static {
		double[] matLMStoRGB = new double[9];
		inverse(new double[] {L_R,L_G,L_B, M_R,M_G,M_B, S_R,S_G,S_B}, matLMStoRGB);
		R_L = matLMStoRGB[0]; R_M = matLMStoRGB[1]; R_S = matLMStoRGB[2];
		G_L = matLMStoRGB[3]; G_M = matLMStoRGB[4]; G_S = matLMStoRGB[5];
		B_L = matLMStoRGB[6]; B_M = matLMStoRGB[7]; B_S = matLMStoRGB[8];
		
		double[] matLRGBtoXYZ = new double[9];
		inverse(new double[] {lR_X,lR_Y,lR_Z, lG_X,lG_Y,lG_Z, lB_X,lB_Y,lB_Z}, matLRGBtoXYZ);
		X_lR = matLRGBtoXYZ[0]; X_lG = matLRGBtoXYZ[1]; X_lB = matLRGBtoXYZ[2];
		Y_lR = matLRGBtoXYZ[3]; Y_lG = matLRGBtoXYZ[4]; Y_lB = matLRGBtoXYZ[5];
		Z_lR = matLRGBtoXYZ[6]; Z_lG = matLRGBtoXYZ[7]; Z_lB = matLRGBtoXYZ[8];
		
		double[] matXYZtoRGB = new double[9];
		inverse(new double[] {X_R,X_G,X_B, Y_R,Y_G,Y_B, Z_R,Z_G,Z_B}, matXYZtoRGB);
		R_X = matXYZtoRGB[0]; R_Y = matXYZtoRGB[1]; R_Z = matXYZtoRGB[2];
		G_X = matXYZtoRGB[3]; G_Y = matXYZtoRGB[4]; G_Z = matXYZtoRGB[5];
		B_X = matXYZtoRGB[6]; B_Y = matXYZtoRGB[7]; B_Z = matXYZtoRGB[8];
	}
	
	private static void inverse(double[] A, double[] invA) {
		assert A.length == 9 && invA.length == 9;
		double a11 = A[0], a12 = A[1], a13 = A[2],
				a21 = A[3], a22 = A[4], a23 = A[5],
				a31 = A[6], a32 = A[7], a33 = A[8];
		double detA = a11*a22*a33 + a21*a32*a13 + a31*a12*a23
		             - a11*a32*a23 - a31*a22*a13 - a21*a12*a33;
		invA[0] = (a22*a33-a23*a32)/detA; invA[1] = (a13*a32-a12*a33)/detA; invA[2] = (a12*a23-a13*a22)/detA;
		invA[3] = (a23*a31-a21*a33)/detA; invA[4] = (a11*a33-a13*a31)/detA; invA[5] = (a13*a21-a11*a23)/detA;
		invA[6] = (a21*a32-a22*a31)/detA; invA[7] = (a12*a31-a11*a32)/detA; invA[8] = (a11*a22-a12*a21)/detA;
	}
	
	
	private static void product(double[] a, double[] b, double[] c) {
		c[0] = a[0]*b[0] + a[1]*b[3] + a[2]*b[6];
		c[1] = a[0]*b[1] + a[1]*b[4] + a[2]*b[7];
		c[2] = a[0]*b[2] + a[1]*b[5] + a[2]*b[8];
		c[3] = a[3]*b[0] + a[4]*b[3] + a[5]*b[6];
		c[4] = a[3]*b[1] + a[4]*b[4] + a[5]*b[7];
		c[5] = a[3]*b[2] + a[4]*b[5] + a[5]*b[8];
		c[6] = a[6]*b[0] + a[7]*b[3] + a[8]*b[6];
		c[7] = a[6]*b[1] + a[7]*b[4] + a[8]*b[7];
		c[8] = a[6]*b[2] + a[7]*b[5] + a[8]*b[8];
	}
	
	
	
	public static void main(String[] args) {
		double[] e = new double[9];
//		product(new double[]{L_R,L_G,L_B, M_R,M_G,M_B, S_R,S_G,S_B},
//				new double[]{R_L, R_M, R_S,
//				G_L, G_M, G_S,
//				B_L, B_M, B_S}, e);
		product(new double[]{lR_X, lR_Y, lR_Z,
				lG_X, lG_Y, lG_Z,
				lB_X, lB_Y, lB_Z},
				new double[]{0.3933, 0.3651, 0.1903,
				   0.2123, 0.7010, 0.0858,
				   0.0182, 0.1117, 0.9570/*X_lR, X_lG, X_lB,
				Y_lR, Y_lG, Y_lB,
				Z_lR, Z_lG, Z_lB*/}, e);
//		product(new double[]{X_R, X_G, X_B,
//				Y_R, Y_G, Y_B,
//				Z_R, Z_G, Z_B},
//				new double[]{R_X, R_Y, R_Z,
//				G_X, G_Y, G_Z,
//				B_X, B_Y, B_Z}, e);
		System.out.print(e[0]+" ");
		System.out.print(e[1]+" ");
		System.out.println(e[2]);
		System.out.print(e[3]+" ");
		System.out.print(e[4]+" ");
		System.out.println(e[5]);
		System.out.print(e[6]+" ");
		System.out.print(e[7]+" ");
		System.out.println(e[8]);
		
		System.out.println(X_lR);
		System.out.println(X_lG);
		System.out.println(X_lB);
		System.out.println(Y_lR);
		System.out.println(Y_lG);
		System.out.println(Y_lB);
		System.out.println(Z_lR);
		System.out.println(Z_lG);
		System.out.println(Z_lB);
		
		System.out.println(LMS_NUTRAL.toString());
		System.out.println(new LMSColor(0.1159786162144061, 0.09482136320070213, 0.05842422424151998).getRGB().toString());
		System.out.println("575: "+LMS_575.toString());
		System.out.println("475: "+LMS_475.toString());
		System.out.println("660: "+LMS_660.toString());
		System.out.println("485: "+LMS_485.toString());
		
	}
}