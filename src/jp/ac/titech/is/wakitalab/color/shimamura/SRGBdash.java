/* * 作成日： 2005/10/31 */package jp.ac.titech.is.wakitalab.color.shimamura;/** * @author shinamu1 */public class SRGBdash extends Vector3D{		static SRGBdash c2t = new SRGBdash();	static double[] alpha=new double[3], beta=new double[3], gamma=new double[3];		public SRGBdash() {		super();	}		public SRGBdash(double v1, double v2, double v3) {		super(v1, v2, v3);	}	public XYZ getXYZ() {		XYZ xyz = new XYZ();		xyz.setVector3d(SRGB.matrix_toXYZ.convertVector3D((Vector3D)this));		return xyz;	}	public void getXYZ(XYZ xyz) {		SRGB.matrix_toXYZ.convertVector3D((Vector3D)this, (Vector3D)xyz);	}		public void getLab (Lab lab) {		getXYZ(XYZ.tempXYZ);		XYZ.tempXYZ.getLab(lab);	}		public SRGB getSRGB() {		SRGB srgb = new SRGB();		for (int i=0;i<3;i++) {			srgb.setValue(i, XYZ.gammaRevise(getValue(i)));		}		return srgb; 	}	public void getSRGB(SRGB srgb) {		for (int i=0;i<3;i++) {			srgb.setValue(i, XYZ.gammaRevise(getValue(i)));		}	}		public LMS getLMS() {		LMS lms = new LMS();		SRGB.matrix_toLMS.convertVector3D((Vector3D)this, (Vector3D)lms);		return lms;	}		public void getLMS(LMS lms) {		SRGB.matrix_toLMS.convertVector3D((Vector3D)this, (Vector3D)lms);	}		/**	 * この色が、SRGBdash空間で[0,1]の中に無いとき、(.5,.5,.5)の方向へ色を修正する。	 * @return	 */	public boolean boundaryRevise() {		boolean isRevised = false;		for (int i=0;i<3;i++) tempValues[i] = getValue(i);		double overMax = 0.0;		for (int i=0;i<3;i++) {			overMax = Math.max(overMax, -tempValues[i]);			overMax = Math.max(overMax, tempValues[i]-1.0);		}		if (isRevised=(overMax>0)) {			double scale = 0.5 / (overMax + 0.5);			for (int i=0;i<3;i++) tempValues[i] = (tempValues[i] - 0.5) * scale + 0.5;		}		setValue(tempValues[0], tempValues[1], tempValues[2]);		return isRevised;	}		/**	 * この色が、SRGBdash 空間で [0,1]の範囲内に無い場合、centerの方向へ色を修正する。	 * @param center	 * @return	 */	public boolean boundaryRevise(SRGBdash center) {		boolean isRevised = false;		// centerから見た座標を c2t にセット		for(int i=0;i<3;i++) {			c2t.setValue(i, getValue(i)-center.getValue(i));		}		for (int i=0;i<3;i++) {			beta[i] = Math.max(getValue(i)-0.995, Math.max(-getValue(i)+0.005, 0));//境界overした距離			gamma[i] = Math.abs(c2t.getValue(i));//中心からの距離。gamma=alpha+beta			if(gamma[i]<1.0E-10) gamma[i]=1.0E-10;//もし中心が境界付近だと誤差が激しい			alpha[i] = gamma[i]-beta[i];//境界内での中心からの距離		}		// 		double scale = Math.min(alpha[0]/gamma[0], Math.min(alpha[1]/gamma[1], alpha[2]/gamma[2]));		if (isRevised=(scale<1.0)) {			for(int i=0;i<3;i++) {				setValue(i, center.getValue(i)+c2t.getValue(i)*scale*0.999);			}		}		return isRevised;			}	}