/* * 作成日: 2003/11/20 * */package jp.ac.titech.is.wakitalab.color.shimamura;/** * @author shimaken * */public class Lab extends Vector3D {			/*	 * 定数	 */			/*	 * コンストラクター	 */			/**	 * デフォルトコンストラクター	 */	public Lab () {		super();	}		/**	 * double をとるコンストラクター	 */	public Lab (double value1, double value2, double value3) {		super(value1, value2, value3);	}		/**	 * int をとるコンストラクター	 */	public Lab (int value1, int value2, int value3) {		super(value1, value2, value3);	}		/**	 * double 配列をとるコンストラクター	 */	public Lab (double values[]) {		super(values);	}		/**	 * 引数にもらったVector3D を持つコンストラクター	 */	public Lab (Vector3D v) {		super(v);	}			/*	 * メソッド	 */			/**	 * 同じ内容をコピー	 */	Lab copyLab() {		Lab lab = new Lab((Vector3D)this);		return lab;	}		/**	 * これの中身をlabに代入する	 * @param lab	 */	void substituteLab (Lab lab) {		lab.setValue1(getValue1());		lab.setValue2(getValue2());		lab.setValue3(getValue3());	}		/**	 * XYZに変換	 */	XYZ getXYZ() {		double l = getValue1();// * 100.0;		double a = getValue2();// * 100.0;		double b = getValue3();// * 100.0;		double fyyn = (l + 16.0) / 116.0;		double fxxn = (a / 500.0) + fyyn;		double fzzn = fyyn - (b / 200.0);		double x = fInv(fxxn)*XYZ.Xn;		double y = fInv(fyyn)*XYZ.Yn;		double z = fInv(fzzn)*XYZ.Zn;		XYZ xyz = new XYZ(x, y, z);		return xyz;	}		/**	 * XYZに変換。メモリーを与えて素早く。	 * @param xyz	 */	void getXYZ(XYZ xyz) {		double l = getValue1();// * 100.0;		double a = getValue2();// * 100.0;		double b = getValue3();// * 100.0;		double fyyn = (l + 16.0) / 116.0;		double fxxn = (a / 500.0) + fyyn;		double fzzn = fyyn - (b / 200.0);		xyz.setValue(			fInv(fxxn)*XYZ.Xn,			fInv(fyyn)*XYZ.Yn,			fInv(fzzn)*XYZ.Zn);	}		/**	 * RGB に変換	 */	RGB getRGB() {		return getXYZ().getRGB();	}		/**	 * SRGB に変換	 */	public SRGB getSRGB() {		return getXYZ().getSRGB();	}		/**	 * SRGBに変換　メモリーを与えて素早く。	 * @param xyz	 * @param srgb	 * @param tempDoubles	 * @return	 */	boolean getSRGB(SRGB srgb) {		getXYZ(XYZ.tempXYZ);		return XYZ.tempXYZ.getSRGB(srgb);	}		/**	 * LMSに変換	 * @return LMS	 */	LMS getLMS() {		return getXYZ().getLMS();	}		void getLMS(LMS returnLMS) {		getXYZ(XYZ.tempXYZ);		XYZ.tempXYZ.getLMS(returnLMS);	}		boolean getSRGBdash (SRGBdash srgbDash) {		getXYZ(XYZ.tempXYZ);		return XYZ.tempXYZ.getSRGBdash(srgbDash);	}		/**	 * Lab-XYZ 間の変換に使う関数f(q)を直線で近似したときのLMS変換	 * @param returnLMS	 */	public void getLMSfLine(LMS returnLMS) {		double l=getValue1(), a=getValue2(), b=getValue3();		returnLMS.setValue1(-0.12645209152803008 + 0.00017859191268950887*a + 		    0.00007396573737191051*b + 0.003977321856609871*l);		returnLMS.setValue2(-0.11050225602155328 - 0.0003293753389814712*a - 		    0.00024005642853319468*b + 0.0034756486252487248*l);		returnLMS.setValue3(-0.07425889239491314 + 1.4125538803863923e-6*a - 		    0.0013614502698783418*b + 0.002335679166717929*l);	}	/**	 * Lab-XYZ 間の変換に使う関数fをf(q)=q^(1/3)と近似したときのLMS変換	 * @param returnLMS	 */	public void getLMSnoIf(LMS returnLMS) {		double l=getValue1(), a=getValue2(), b=getValue3();		double x_ppoi = Power(-800. + 29.*b - 50.*l,3);		double y_ppoi = Power(16. + l,3);		double z_ppoi = Power(2000. + 29.*a + 125.*l,3);		returnLMS.setValue1(				(4.9825048148063376e-14) * x_ppoi + 			    (1.6287645464679024e-7) * y_ppoi + 			    (1.9248589338314424e-14) * z_ppoi);		returnLMS.setValue2(				(-1.6170761672769917e-13) * x_ppoi + 			    (2.1886512197705506e-7) * y_ppoi - 			    (3.549998733282441e-14) * z_ppoi);		returnLMS.setValue3(				(-9.171046981766892e-13) * x_ppoi - 			    (8.662575632370583e-10) * y_ppoi + 			    (1.5224468539664955e-16) * z_ppoi);	}	static double Power(double x, int r) {		double pow = x;		for(int i=1;i<r;i++) {			pow *= x;		}		return pow;	}	/**	 * 関数 f の逆関数 fInv	 * @param fyyn 逆関数への引数。 f(Y/Yn) の値などとなる	 * @return double 逆変換後の値。 Y/Yn などとなる	 */	static double fInv (double fyyn) {		if (fyyn > 0.206929) {			double ret = fyyn * fyyn * fyyn;			return ret;		} else {			double ret = (fyyn - 16.0/116.0) / 7.787;			return ret;		}	}		/**	 * 二つのLabの色差を求める	 * 	 * return :	 *   ΔE = [ (ΔH* /(1+0.015C*))^2 + (ΔL* /1.0)^2 + (ΔC* /(1+0.045C*))^2 ] ^(1/2)	 * 	 * deltaC :	 *   ΔC* = C*ab1 - C*ab2	 * 	 * chroma1 :	 *   C*ab1 = (a*1)^2 + (b*1)^2	 * chroma2 :	 *   C*ab2 = (a*2)^2 + (b*2)^2	 * chromaAve :	 *   C* = ( C*ab1 + C*ab2 ) / 2	 * 	 * deltaH :	 *   ΔH* = [ (ΔE*ab)^2 - (ΔL*)^2 - (ΔC*)^2 ] ^(1/2)	 *        = [ (Δa*)^2 + (Δb*)^2 - (ΔC*)^2 ] ^(1/2)	 *        = 2 (C*ab1 * C*ab2)^(1/2) sin(Δh_ab/2)	 *   ΔE*ab = [ (ΔL*)^2 + (Δa*)^2 + (Δb*)^2 ] ^(1/2)	 *   Δh_ab = h_ab1 - h_ab2	 * hue1 :	 *   h_ab1 = arctan( b*1 / a*1 )	 * hue2 :	 *   h_ab2 = arctan( b*2 / a*2 )	 * 	 * @param lab 比較されるLab	 * @return 求められた色差double値	 */	double getColorDistance94(Lab lab) {		double chroma1 = getChroma();		double chroma2 = lab.getChroma();		// double hue1 = getHue();		// double hue2 = lab.getHue();		double deltaL = getValue1() - lab.getValue1();		double deltaC = chroma1 - chroma2;		double deltaH = getHueDistance(lab, deltaC);		double chromaAve = (chroma1 + chroma2) * 0.5;		double ret =			Math.sqrt(				(deltaH/(1.0+0.015*chromaAve)) * (deltaH/(1.0+0.015*chromaAve))				+ deltaL * deltaL				+ (deltaC/(1.0+0.045*chromaAve)) * ((deltaC/1.0+0.045*chromaAve)));		return ret;	}	public double getColorDistance76(Lab lab) {		return Math.sqrt(				(getValue1()-lab.getValue1()) * (getValue1()-lab.getValue1()) +				(getValue2()-lab.getValue2()) * (getValue2()-lab.getValue2()) +				(getValue3()-lab.getValue3()) * (getValue3()-lab.getValue3()));	}		/**	 * 彩度を求める	 * @return 求められた彩度 double	 */	double getChroma() {		double ret =			Math.sqrt(getValue2() * getValue2() + getValue3() * getValue3());		return ret;	}		/**	 * 色相を求める 無彩色に対して無防備	 * @return 求められた色相角度 double (-pi/2 - pi/2)	 */	double getHue() {		double ret = Math.atan( getValue3() / getValue2() );		return ret;	}		/**	 * 彩度と色相の組から1994修正版の色相差を求める	 * @param chroma1 １つ目の彩度引数	 * @param chroma2 ２つ目の彩度引数	 * @param hue1 一つ目の色相引数	 * @param hue2 二つ目の色相引数	 * @return 計算された色相差1994CIELAB版 double値	 */	double getHueDistance(double chroma1, double chroma2, double hue1, double hue2) {		double ret = 2.0 * Math.sqrt((chroma1*chroma2)) * Math.sin((hue1-hue2)/2.0);		return ret;	}		/**	 * CIELAB1994版の色相差を求める変換式	 * root{delta_a^2 + delta_b^2 - deltaC^2}	 * @param lab 比較されるLab	 * @param deltaC 比較に使う彩度差	 * @return 色相差deltaH double値	 */	double getHueDistance(Lab lab, double deltaC) {		double ret =			Math.sqrt(				(getValue2()-lab.getValue2()) * (getValue2()-lab.getValue2())				+ (getValue3() - lab.getValue3()) * (getValue3() - lab.getValue3())				+ deltaC * deltaC);		return ret;	}		/**	 * SRGBの境界条件を使った、境界補正	 * @return 補正したかの真偽	 */	boolean boundaryReviseSRGB(XYZ xyz, SRGB srgb, double[] tempDoubles) {		boolean isRevised = false;		isRevised = getSRGB(srgb);		isRevised = isRevised | srgb.boundaryRevise();		if(isRevised) {			srgb.getLab(this);		}		return isRevised;	}		/**	 * SRGBに変換する前に（ガンマ変換せずに）境界条件を判定して補正する。	 * ガンマ変換前なので相似変換の適切性は不明。	 * 	 * @return isRevised	 */	boolean boundaryReviseSRGBdash() {		getXYZ(XYZ.tempXYZ);		boolean isRevised = XYZ.tempXYZ.boundaryReviseSRGBdash();		if(isRevised) {			XYZ.tempXYZ.getLab(this);		}		return isRevised;	}			/**	 * 二つのLabの値がほぼ等しいかどうかの真偽を返す	 * @param lab 比べるLab	 * @return ほぼ等しいかの真偽	 */	boolean match(Lab lab) {		if ((Math.abs(getValue1()-lab.getValue1()) < smallNumber*100.0)			&& (Math.abs(getValue2()-lab.getValue2()) < smallNumber*100.0)			&& (Math.abs(getValue3()-lab.getValue3()) < smallNumber*100.0))				return true;		return false;	}		/**	 * 二つのLabの値が色差半径radius以内の色かの真偽を返す	 * @param lab 比べるLab	 * @param radius 似ているとする許容色差の最大	 * @return	 */	boolean match(Lab lab, double radius) {		if (getColorDistance76(lab) > radius)			return false;		else			return true;	}	}