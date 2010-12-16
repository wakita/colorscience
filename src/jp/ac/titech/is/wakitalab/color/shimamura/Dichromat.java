/* * 作成日: 2003/12/25 *  * 色盲変換のグレーアンカーをとりなおすべき。等エネルギー光に。 * アンカーを10deg,2deg選べる形にしたい。 * convertで必要のない外積を一部している。 * */package jp.ac.titech.is.wakitalab.color.shimamura;/** * 色盲シミュレートをするクラス * @author shimaken * */public class Dichromat {		/**	 * 正常三色覚	 */	public final static int TRICHROMAT = 0;	/**	 * 第一色盲	 */	public final static int PROTANOPE = 1;	/**	 * 第二色盲	 */	public final static int DEUTERANOPE = 2;	/**	 * 第三色盲	 */	public final static int TRITANOPE = 3;	/**	 * string for dichromat-type	 */	public final static String[] typeString = {"TRICHROMAT", "PROTANOPE", "DEUTERANOPE", "TRITANOPE"};		/**	 * anchor's spectrum	 * {anchor1 for L or M dichromat, anchor2 for L or M dichromat,	 *  anchor1 for S dichromat,      anchor2 for S dichromat}	 */	public final static int[] anchorLambda = {475, 575, 485, 660};			/**	 * 2deg anchor	 * {anchor1 for L or M dichromat, anchor2 for L or M dichromat,	 *  anchor1 for S dichromat,      anchor2 for S dichromat}	 */	public final static LMS[] anchor2deg = {			new LMS(1.1882E-01,  2.05398E-01,  5.16411E-01),			new LMS(9.92310E-01,  7.40291E-01,  1.75039E-04),			new LMS(1.63952E-01,  2.68063E-01,  2.90322E-01),			new LMS(9.30085E-02,  7.30255E-03,  0.0)};		/**	 * 10deg anchor	 * {anchor1 for L or M dichromat, anchor2 for L or M dichromat,	 *  anchor1 for S dichromat,      anchor2 for S dichromat}	 */	public final static LMS[] anchor10deg = {			new LMS(1.77116E-01,  3.10372E-01,  4.70894E-01),			new LMS(9.87057E-01,  7.00013E-01,  9.67045E-05),			new LMS(2.44046E-01,  4.05688E-01,  2.58497E-01),			new LMS(8.38077E-02,  6.50455E-03,  0.0)};		/**	 * anchor[i][j] means a 2deg anchor.	 * i = dichromat type (0:protanope, 1:deuteranope, 2:tritanope)	 * j = anchor number (0 or 1)	 */	public final static LMS[][] anchor = {			{anchor2deg[0], anchor2deg[1]},			{anchor2deg[0], anchor2deg[1]},			{anchor2deg[2], anchor2deg[3]}	};	public final static Matrix3X3 matrix_ProtanopeLongPlane =		new Matrix3X3(				0, 1.34048, -0.190457,				0, 1, 0,				0, 0, 1);	public final static Matrix3X3 matrix_ProtanopeShortPlane =		new Matrix3X3(				0, 1.43239, -0.339634,				0, 1, 0,				0, 0, 1);	public final static Matrix3X3 matrix_DeuteranopeLongPlane =		new Matrix3X3(1, 0, 0,				0.746003, 0, 0.142081,				0, 0, 1);	public final static Matrix3X3 matrix_DeuteranopeShortPlane =		new Matrix3X3(				1, 0, 0,				0.698132, 0, 0.237109,				0, 0, 1);	public final static Matrix3X3 matrix_TritanopeLongPlane =		new Matrix3X3(				1, 0, 0,				0, 1, 0,				-0.0535163, 0.681608, 0);	public final static Matrix3X3 matrix_TritanopeShortPlane =		new Matrix3X3(				1, 0, 0,				0, 1, 0,				-0.763497, 1.55, 0);		public final static Matrix3X3 matrix_toSRGBdash =		new Matrix3X3(				19.978026493260693, - 19.955552675956792, + 1.2929266405281532,				-2.797351540304871, + 7.865592602028847, - 1.3246614311458538,				-0.05352339926529847, - 0.4956579453600024, + 6.4809106125526394);		//final static LMS gray = (0.1159786162144061,0.09482136320070213,0.05842422424151998)	public final static LMS gray = EquiEnergySpectrum.EquiLMS;		// 外積による係数の計算	static private double a1LM = gray.getValue2() * anchor2deg[0].getValue3() - gray.getValue3() * anchor2deg[0].getValue2();	static private double b1LM = gray.getValue3() * anchor2deg[0].getValue1() - gray.getValue1() * anchor2deg[0].getValue3();	static private double c1LM = gray.getValue1() * anchor2deg[0].getValue2() - gray.getValue2() * anchor2deg[0].getValue1();	static private double a2LM = gray.getValue2() * anchor2deg[1].getValue3() - gray.getValue3() * anchor2deg[1].getValue2();	static private double b2LM = gray.getValue3() * anchor2deg[1].getValue1() - gray.getValue1() * anchor2deg[1].getValue3();	static private double c2LM = gray.getValue1() * anchor2deg[1].getValue2() - gray.getValue2() * anchor2deg[1].getValue1();	static private double a1S = gray.getValue2() * anchor2deg[2].getValue3() - gray.getValue3() * anchor2deg[2].getValue2();	static private double b1S = gray.getValue3() * anchor2deg[2].getValue1() - gray.getValue1() * anchor2deg[2].getValue3();	static private double c1S = gray.getValue1() * anchor2deg[2].getValue2() - gray.getValue2() * anchor2deg[2].getValue1();	static private double a2S = gray.getValue2() * anchor2deg[3].getValue3() - gray.getValue3() * anchor2deg[3].getValue2();	static private double b2S = gray.getValue3() * anchor2deg[3].getValue1() - gray.getValue1() * anchor2deg[3].getValue3();	static private double c2S = gray.getValue1() * anchor2deg[3].getValue2() - gray.getValue2() * anchor2deg[3].getValue1();	private static LMS tempLMS = new LMS(), tempDichroLMS = new LMS();		private static SRGBdash tempSrgbd = new SRGBdash();		private final static double[] tempDoubles = new double[3];		/**	 * 色と色盲タイプから色盲変換	 * 入力のLMS自体は変化させない。newされて変換されたLMSを返す	 * @param lms 色LMS型	 * @param type 色盲タイプ	 * @return 変換された色	 */	public static LMS convert(LMS lms, int type) {		LMS returnLMS = new LMS();		convert(lms, returnLMS, type);		return returnLMS;	}		/**	 * 色と色盲タイプから色盲変換	 * 入力のLab自体は変化させない。newされて変換されたLabを返す	 * @param lab Lab型の色	 * @param type 色盲タイプ	 * @return 変換された色	 */	public static Lab convert(Lab lab, int type) {		LMS lms = lab.getLMS();		convert(lms, lms, type);		return lms.getLab();	}		/**	 * 色labを第type種の色盲の色、dLabに変換	 * @param lab	 * @param dLab	 * @param type	 */	public static void convert (Lab lab, Lab dLab, int type) {		lab.getLMS(tempLMS);		Dichromat.convert(tempLMS, tempDichroLMS, type);		Dichromat.tempLMS.getLab(dLab);	}		/**	 * 色と色盲タイプから色盲変換	 * lmsは変化させない。returnLMSに変換後のLMSを入れる。	 * @param lms : Input Color	 * @param returnLMS : Output Color   If lms equals returnLMS, you can do this.	 * @param type : Dichromat Type	 */	public static void convert(LMS lms, LMS returnLMS, int type) {				returnLMS.setValue(lms.getValue1(), lms.getValue2(), lms.getValue3());				double a1, b1, c1, a2, b2, c2;		//anchorベクトルの選択		switch (type) {			case Dichromat.PROTANOPE:			case Dichromat.DEUTERANOPE:				a1 = a1LM;				b1 = b1LM;				c1 = c1LM;				a2 = a2LM;				b2 = b2LM;				c2 = c2LM;				break;			case Dichromat.TRITANOPE:				a1 = a1S;				b1 = b1S;				c1 = c1S;				a2 = a2S;				b2 = b2S;				c2 = c2S;				break;			case Dichromat.TRICHROMAT:				return;			default :				return;		}				double l=lms.getValue1(), m=lms.getValue2(), s=lms.getValue3(),				g_l=gray.getValue1(), g_m=gray.getValue2(), g_s=gray.getValue3();		// 変換部分。		if(type == Dichromat.PROTANOPE){			if(s * g_m < m * g_s)				returnLMS.setValue1(-(b2 * m + c2 * s) / a2);//575nm			else				returnLMS.setValue1(-(b1 * m + c1 * s) / a1);//475nm		}		else if(type == Dichromat.DEUTERANOPE){			if(s * g_l < l * g_s)				returnLMS.setValue2(-(a2 * l + c2 * s) / b2);//575nm			else				returnLMS.setValue2(-(a1 * l + c1 * s) / b1);//475nm		}		else if (type == Dichromat.TRITANOPE){			if(m * g_l < l * g_m)				returnLMS.setValue3(-(a2 * l + b2 * m) / c2);//660nm			else				returnLMS.setValue3(-(a1 * l + b1 * m) / c1);//485nm		}	}		/**	 * fall座標を動かして、vをanchor1とanchor2の張る平面に射影する。	 * @param fall : v [fall] will be revised.	 * @param anchor1	 * @param anchor2	 * @param v	 */	static void convertPlane(int fall, double[] anchor1, double[] anchor2, double[] v) {		assert ((anchor1.length==anchor2.length)&&(anchor2.length==v.length));		double[] abc = new double[3];		abc[0] = anchor1[1]*anchor2[2] - anchor1[2]*anchor2[1];		abc[1] = anchor1[2]*anchor2[0] - anchor1[0]*anchor2[2];		abc[2] = anchor1[0]*anchor2[1] - anchor1[1]*anchor2[0];				if(abc[fall]==0.0) {			abc[fall]=0.1E-10;		}		v[fall] = (-				(abc[(fall+1)%3]*v[(fall+1)%3] + abc[(fall+2)%3]*v[(fall+2)%3])				/ abc[fall]);	}	/**	 * Mathematica で f を直線関数にしたときの色盲変換	 * @param lab - lab convert -> dlab	 * @param dlab - return value.	 * @param type - dichromat type.	 * @return anchorBool - anchor choice	 */	static boolean convertMathLine(Lab lab, Lab dlab, int type) {		double l1=lab.getValue1(), a1=lab.getValue2(), b1=lab.getValue3();		boolean anchorBool = choiceAnchor(lab, type);		if(type==PROTANOPE) {			if(anchorBool) {				dlab.setValue(111.58067142136335 - 0.10756668212282892*a1 + 					    0.0002552437976721218*b1 + 0.9993767352349571*l1,					   0.4953461111946167 - 0.0004927228794392993*a1 + 					    0.0023724549792434016*b1 + 0.0004867697277919844*l1,					   192.3819089732953 - 0.18557677762085031*a1 + 0.9997647185471543*b1 + 					    0.00009034968353888173*l1);			} else {				dlab.setValue(112.19867309282725 - 0.1081628120385089*a1 + 					    0.0034670081011607687*b1 + 0.9993766609242418*l1,					   6.239584192658077 - 0.006033666086012371*a1 + 0.03222534967624569*b1 + 					    0.00048607902021664817*l1,193.4481011853374 - 0.18660523612316757*a1 + 					    1.0053057360644417*b1 + 0.00009022148080156667*l1);			}		} else if (type==DEUTERANOPE) {			if(anchorBool) {				dlab.setValue(61.89443716445618 + 0.043495019551523253*a1 - 					    0.00010320885381103772*b1 + 0.9993031895191836*l1,					   0.26888843072886415 + 0.00019577934013919706*a1 + 					    0.0023708212398944784*b1 + 0.0004864345244416646*l1,					   107.14087984589989 + 0.07358262749480923*a1 + 0.9991497620433492*b1 - 					    0.0000358243482472681*l1);			} else {				dlab.setValue(61.75633491509981 + 0.043400176495507176*a1 - 					    0.0013911321337333352*b1 + 0.9993033819662429*l1,					   3.441249463346452 + 0.002374428974899278*a1 + 0.03195583991459925*b1 + 					    0.0004820138031665089*l1,106.90724559362539 + 0.07342217691142615*a1 + 					    0.99697091996153*b1 - 0.00003549877625255715*l1);			}		} else if (type==TRITANOPE) {			if (anchorBool) {				dlab.setValue(74.49279858544679 + 0.0018228955737232164*a1 - 					    0.008436104838597884*b1 + 0.9993244354044535*l1,					   -216.54925196804996 + 0.8799760427603672*a1 + 0.5523272847030914*b1 - 					    4.579959144573875e-6*l1,-46.79217405345961 + 0.19014753746400523*a1 + 					    0.11934825273785123*b1 + 7.296859590860683e-6*l1);			} else {				dlab.setValue(73.79535739335884 + 0.004656763483775826*a1 - 					    0.006657379376690319*b1 + 0.9993228529694809*l1,					   -170.8864956087953 + 0.6944375035988827*a1 + 0.43587086039306117*b1 + 					    0.00009902496616882167*l1,-119.54276224574869 + 0.485750320400265*a1 + 					    0.3048883743343084*b1 - 0.00015776806108237784*l1);			}		}		return anchorBool;	}	/**	 * 色盲変換に使うanchorベクトルの選択判断メソッド	 * @param lab	 * @param type	 * @return boolean	 */	public static boolean choiceAnchor(Lab lab, int type) {		switch(type) {		case Dichromat.PROTANOPE://ture:575,false:475			return 			6.118616348537592e7 - 59020.71816880175*lab.getValue2() + 			   317985.44376789057*lab.getValue3() - 7.357241507753402*lab.getValue1()>0;		case Dichromat.DEUTERANOPE://ture:575,false:475			return			4.567915370665707e6 + 3137.060079729992*lab.getValue2() + 			   42599.77353022463*lab.getValue3() - 6.365442161023436*lab.getValue1()>0;		case Dichromat.TRITANOPE://true:660,false:485			return			-3.622602172560547e6 + 14719.486265175514*lab.getValue2() + 			   9238.936265586299*lab.getValue3() - 8.219377397537114*lab.getValue1()>0;		default:			assert false;			return true;		}	}	/**	 * LMS のうち、色盲の人に見える２成分から、色盲平面を選択	 * @param lms	 * @param type	 * @return	 */	public static boolean choiceAnchor(LMS lms, int type) {		double l=lms.getValue1(), m=lms.getValue2(), s=lms.getValue3();		double g_l = EquiEnergySpectrum.EquiLMS.getValue1(),				g_m = EquiEnergySpectrum.EquiLMS.getValue2(),				g_s = EquiEnergySpectrum.EquiLMS.getValue3();		switch(type) {		case Dichromat.PROTANOPE:			return g_s*m - g_m*s > 0;		case Dichromat.DEUTERANOPE:			return g_s*l - g_l*s > 0;		case Dichromat.TRITANOPE:			return g_m*l -	g_l*m > 0;		default:			assert false;			return false;		}	}	/**	 * anchor ２本と gray anchor で作られる、二つの平面に射影し、色盲変換をする。	 * @param originLMS 元の色	 * @param dichroLMS 色盲変換された色	 * @param type 色盲タイプ	 */	public static void convertPlane(LMS originLMS, LMS dichroLMS, boolean isLong,int type) {		originLMS.getDoubles(tempDoubles);		if (isLong) {			switch(type) {			case Dichromat.PROTANOPE:				matrix_ProtanopeLongPlane.convertDoubles(tempDoubles);				break;			case Dichromat.DEUTERANOPE:				matrix_DeuteranopeLongPlane.convertDoubles(tempDoubles);				break;			case Dichromat.TRITANOPE:				matrix_TritanopeLongPlane.convertDoubles(tempDoubles);				break;			}		} else {			switch(type) {			case Dichromat.PROTANOPE:				matrix_ProtanopeShortPlane.convertDoubles(tempDoubles);				break;			case Dichromat.DEUTERANOPE:				matrix_DeuteranopeShortPlane.convertDoubles(tempDoubles);				break;			case Dichromat.TRITANOPE:				matrix_TritanopeShortPlane.convertDoubles(tempDoubles);				break;			}		}		for(int i=0;i<3;i++) dichroLMS.setValue(i, tempDoubles[i]);	}	/**	 * SRGBdash が境界を越えた場合、指定された色盲平面の中心方向へ補正する。	 * @param srgbd	 * @param isLong	 * @param type	 */	private static boolean boundaryReviseDICHROSRGBD(SRGBdash srgbd, boolean isLong, int type) {		switch(type) {		case Dichromat.PROTANOPE:			if (isLong)				return srgbd.boundaryRevise(DichromatPlaneCenter.PROLONG_CENTERSRGBD);			else				return srgbd.boundaryRevise(DichromatPlaneCenter.PROSHORT_CENTERSRGBD);		case Dichromat.DEUTERANOPE:			if(isLong)				return srgbd.boundaryRevise(DichromatPlaneCenter.DEULONG_CENTERSRGBD);			else				return srgbd.boundaryRevise(DichromatPlaneCenter.DEUSHORT_CENTERSRGBD);		case Dichromat.TRITANOPE:			if(isLong)				return srgbd.boundaryRevise(DichromatPlaneCenter.TRILONG_CENTERSRGBD);			else				return srgbd.boundaryRevise(DichromatPlaneCenter.TRISHORT_CENTERSRGBD);		default:			assert true;			return false;		}	}	/**	 * ニュートン法で境界外とされる色を境界内に修正しながら、色盲変換する。	 * @param lms LMSの３成分のうち２つが正しい色。	 * @param type	 * @return	 */	public static boolean boundaryInConvertNewtonLMS(LMS lms, int type) {		if (type==Dichromat.TRICHROMAT) {			lms.getSRGBdash(tempSrgbd);			boolean isRevised = tempSrgbd.boundaryRevise();			tempSrgbd.getLMS(lms);			return isRevised;		}		boolean isLong = choiceAnchor(lms, type);		Dichromat.convertPlane(lms, tempLMS, isLong, type);		if(!CompoundDesire.boundaryCheck(tempLMS, isLong, type)){			tempLMS.getSRGBdash(tempSrgbd);			boolean isRevised = boundaryReviseDICHROSRGBD(tempSrgbd, isLong, type);			assert isRevised;			tempSrgbd.getLMS(lms);			return isRevised;		} else {			lms.setVector3d(tempLMS);			return false;		}	}	public static String typeString(int type) {		return typeString[type];	}}